import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;
import httphandler.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static TaskManager taskManager;// = new InMemoryTaskManager();//Managers.getDefault();
    private static HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void stop() {
        httpServer.stop(1);
    }

    public static void start() throws IOException {
        httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/subtasks", new SubTasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicsHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager.getHistoryManager()));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
        httpServer.start(); // запускаем сервер

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public Gson getGson() {
        return new BaseHttpHandler(taskManager).getGson();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Поехали!");
        taskManager = new InMemoryTaskManager();

        Task task1 = new Task("ts1", "ts11111", LocalDateTime.now(), Duration.ofHours(24));
        Task task2 = new Task("ts2", "ts22222", LocalDateTime.now().plus(1, ChronoUnit.DAYS), Duration.ofHours(24));
        Task task3 = new Task("ts3", "ts22222", LocalDateTime.now().plus(2, ChronoUnit.DAYS), Duration.ofHours(24));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        Task task4 = new Task("ts2UPD", "ts22222", task2.getStartTime(), Duration.ofHours(24));
        task4.setId(2);
        taskManager.updateTask(task4);

        Epic epic1 = new Epic("ep1", "ep11111");
        Epic epic2 = new Epic("ep2", "ep22222");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        SubTask subtask1 = new SubTask("subtask1", "subtask1111", epic1.getId(), LocalDateTime.now().plus(3, ChronoUnit.DAYS), Duration.ofDays(1));
        SubTask subtask2 = new SubTask("subtask2", "subtask22222", epic1.getId(), LocalDateTime.now().plus(5, ChronoUnit.DAYS), Duration.ofDays(1));
        //SubTask subtask3 = new SubTask("subtask3", "subtask33333", epic1.getId(), LocalDateTime.now().plus(3, ChronoUnit.DAYS), Duration.ofDays(1));
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);

        start();
    }
}
