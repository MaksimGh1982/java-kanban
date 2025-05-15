import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import general.Const;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import task.Epic;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerEpicsTest {

    class EpicListTypeToken extends TypeToken<List<Epic>> {
        // здесь ничего не нужно реализовывать
    }

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private final String path = "http://localhost:8080/epics";

    public HttpTaskManagerEpicsTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        manager.clearTasks();
        manager.clearSubTasks();
        manager.clearEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing Epic 2");
        String epicJson = gson.toJson(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(path);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS_FOR_CREATE_UPD, response.statusCode());

        List<Epic> epicsFromManager = manager.getAllEpic();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", epicsFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testDelEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing Epic 2");
        String epicJson = gson.toJson(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(path);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS_FOR_CREATE_UPD, response.statusCode());

        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS, response.statusCode());

        List<Epic> epics = gson.fromJson(response.body(), new EpicListTypeToken().getType());
        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(), "Некорректное количество задач");
        assertEquals(epic.getTitle(), epics.get(0).getTitle(), "Некорректное имя задачи");
        //удаление
        url = URI.create(path + "/" + epics.get(0).getId());
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS, response.statusCode());
        //запрос по id после удаления
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_NOT_FOUND, response.statusCode());
    }

    @Test
    public void testUpdateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing epic 2");
        String epicJson = gson.toJson(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(path);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS_FOR_CREATE_UPD, response.statusCode());

        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS, response.statusCode());

        List<Epic> epics = gson.fromJson(response.body(), new EpicListTypeToken().getType());
        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(), "Некорректное количество задач");
        assertEquals(epic.getTitle(), epics.get(0).getTitle(), "Некорректное имя задачи");
        //update
        Epic epicUpd = new Epic("Test Update", "Testing epic 2");
        epicUpd.setId(epics.get(0).getId());
        epicJson = gson.toJson(epicUpd);
        url = URI.create(path + "/" + epics.get(0).getId());
        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS_FOR_CREATE_UPD, response.statusCode());
        //запрос после update по id
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS, response.statusCode());
        Epic epicGetUpd = gson.fromJson(response.body(), Epic.class);
        assertEquals(epicGetUpd.getId(), epicUpd.getId(), "Некорректное id");
        assertEquals("Test Update", epicGetUpd.getTitle(), "Некорректное имя задачи");
    }
} 