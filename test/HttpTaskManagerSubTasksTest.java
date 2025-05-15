import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import general.Const;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import task.Epic;
import task.SubTask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerSubTasksTest {

    class SubTaskListTypeToken extends TypeToken<List<SubTask>> {
        // здесь ничего не нужно реализовывать
    }

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private final String path = "http://localhost:8080/subtasks";

    private int epicId;

    public HttpTaskManagerSubTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        manager.clearTasks();
        manager.clearSubTasks();
        manager.clearEpics();

        taskServer.start();

        Epic epic = new Epic("Test 2", "Testing Epic 2");
        String epicJson = gson.toJson(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS_FOR_CREATE_UPD, response.statusCode());

        epicId = manager.getAllEpic().get(0).getId();

    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddSubTask() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Test 2", "Testing subtask 2",epicId, LocalDateTime.now(), Duration.ofMinutes(5));
        String subtaskJson = gson.toJson(subTask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(path);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS_FOR_CREATE_UPD, response.statusCode());

        List<SubTask> subTasksFromManager = manager.getAllSubTask();

        assertNotNull(subTasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subTasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", subTasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testDelSubTask() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Test 2", "Testing subtask 2",epicId, LocalDateTime.now(), Duration.ofMinutes(5));
        String subTaskJson = gson.toJson(subTask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(path);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS_FOR_CREATE_UPD, response.statusCode());

        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS, response.statusCode());

        List<SubTask> subTasks = gson.fromJson(response.body(), new SubTaskListTypeToken().getType());
        assertNotNull(subTasks, "Задачи не возвращаются");
        assertEquals(1, subTasks.size(), "Некорректное количество задач");
        assertEquals(subTask.getTitle(), subTasks.get(0).getTitle(), "Некорректное имя задачи");
        assertEquals(manager.getEpic(epicId).getSubTasks().size(), 1, "Подзадача не была добавлена в список Эпика");
        //удаление
        url = URI.create(path + "/" + subTasks.get(0).getId());
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS, response.statusCode());
        //запрос по id после удаления
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_NOT_FOUND, response.statusCode());

        assertEquals(manager.getEpic(epicId).getSubTasks().size(), 0, "Подзадача не была удалена из списка Эпика");
    }

    @Test
    public void testUpdateSubTask() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("Test 2", "Testing subtask 2",epicId, LocalDateTime.now(), Duration.ofMinutes(5));
        String subTaskJson = gson.toJson(subTask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(path);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS_FOR_CREATE_UPD, response.statusCode());

        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS, response.statusCode());

        List<SubTask> subTasks = gson.fromJson(response.body(), new SubTaskListTypeToken().getType());
        assertNotNull(subTasks, "Задачи не возвращаются");
        assertEquals(1, subTasks.size(), "Некорректное количество задач");
        assertEquals(subTask.getTitle(), subTasks.get(0).getTitle(), "Некорректное имя задачи");
        //update
        SubTask subTaskUpd = new SubTask("Test Update", "Testing task 2", epicId, LocalDateTime.now(), Duration.ofMinutes(5));
        subTaskUpd.setId(subTasks.get(0).getId());
        subTaskJson = gson.toJson(subTaskUpd);
        url = URI.create(path + "/" + subTasks.get(0).getId());
        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS_FOR_CREATE_UPD, response.statusCode());
        //запрос после update по id
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS, response.statusCode());
        SubTask subTaskGetUpd = gson.fromJson(response.body(), SubTask.class);
        assertEquals(subTaskUpd, subTaskGetUpd, "Объекты не равны");
    }
}
