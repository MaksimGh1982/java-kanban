import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import general.Const;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerListsTest {

    class TaskListTypeToken extends TypeToken<List<Task>> {
        // здесь ничего не нужно реализовывать
    }

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = taskServer.getGson();
    private final String path = "http://localhost:8080/tasks";

    public HttpTaskManagerListsTest () throws IOException {
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
    public void testLists() throws IOException, InterruptedException {
        Task task1 = new Task("ts1", "ts11", LocalDateTime.now().plus(2, ChronoUnit.DAYS), Duration.ofHours(24));
        Task task2 = new Task("ts2", "ts22", LocalDateTime.now(), Duration.ofHours(24));
        Task task3 = new Task("ts3", "ts33", LocalDateTime.now().plus(1, ChronoUnit.DAYS), Duration.ofHours(24));
        int id1 = manager.addTask(task1);
        int id2 = manager.addTask(task2);
        int id3 =manager.addTask(task3);

        manager.getTask(id3);
        manager.getTask(id1);
        manager.getTask(id2);
        manager.getTask(id1);

        // тест истории
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS, response.statusCode());

        List<Task> tasks = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(3, tasks.size(), "Некорректное количество задач в истории");
        assertEquals(task3, tasks.get(0), "Задачи не совпадают");
        assertEquals(task2, tasks.get(1), "Задачи не совпадают");
        assertEquals(task1, tasks.get(2), "Задачи не совпадают");

        // тест приоритетов
        url = URI.create("http://localhost:8080/prioritized");
        request = HttpRequest.newBuilder().uri(url).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(Const.CODE_SUCCESS, response.statusCode());

        tasks = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(3, tasks.size(), "Некорректное количество задач в истории");
        assertEquals(task2, tasks.get(0), "Задачи не совпадают");
        assertEquals(task3, tasks.get(1), "Задачи не совпадают");
        assertEquals(task1, tasks.get(2), "Задачи не совпадают");
    }
}
