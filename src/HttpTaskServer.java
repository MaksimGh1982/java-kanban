import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.google.gson.Gson;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        jsonWriter.value(Objects.isNull(duration) ? null : duration.toMinutes());
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        return Duration.ofMinutes(Integer.parseInt(jsonReader.nextString()));
    }
}

class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;//.ofPattern("dd-MM-yyyy");

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
        jsonWriter.value(Objects.isNull(localDateTime) ? null : localDateTime.format(dtf));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return  LocalDateTime.parse(jsonReader.nextString(), dtf);
    }
}

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) throws IOException {
        System.out.println("Поехали!");
        //TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("ts1","ts11111", LocalDateTime.now(), Duration.ofDays(1));
        Task task2 = new Task("ts2","ts22222", LocalDateTime.now().plus(2, ChronoUnit.DAYS), Duration.ofDays(1));
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("ep1","ep11111");
        Epic epic2 = new Epic("ep2","ep22222");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        SubTask subtask1 = new SubTask("subtask1","subtask1111",epic1.getId(), LocalDateTime.now().plus(3, ChronoUnit.DAYS), Duration.ofDays(1));
        SubTask subtask2 = new SubTask("subtask2","subtask22222",epic1.getId(), LocalDateTime.now().plus(5, ChronoUnit.DAYS), Duration.ofDays(1));
        SubTask subtask3 = new SubTask("subtask3","subtask33333",epic1.getId(), LocalDateTime.now().plus(3, ChronoUnit.DAYS), Duration.ofDays(1));
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        taskManager.addSubTask(subtask3);

        //taskManager.delTask(1);

        /*List<SubTask> ls = taskManager.getSubTaskByEpic(epic1.getId());*/
/*
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        String response = gson.toJson(taskManager.getAllTask());
        System.out.println(response);
*/
        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        //httpServer.createContext("/subtasks", new TasksHandler<SubTask>());
        httpServer.start(); // запускаем сервер

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks запроса от клиента.");

            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .setPrettyPrinting()
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();

            String path = httpExchange.getRequestURI().getPath();
            int paramId = path.split("/").length>=3 ? Integer.parseInt(path.split("/")[2]) : -1;

            String method = httpExchange.getRequestMethod();
            String response = "";
            int code = 200;
            System.out.println(method);
            Task task;
            switch(method) {
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String jsonBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    task = gson.fromJson(jsonBody, Task.class);
                    int i;
                    if (task.getId() > 0) {
                        i = taskManager.updateTask(task);
                    } else {
                        i = taskManager.addTask(task);
                    }
                    code = i == 0 ? 406 : 201;
                    break;
                case "GET":
                    if (paramId > 0) {
                        task = taskManager.getTask(paramId);
                        if (Objects.nonNull(task)) {
                            response = gson.toJson(taskManager.getTask(paramId));
                        } else {
                            code = 404;
                        }
                    } else {
                        response = gson.toJson(taskManager.getAllTask());
                    }
                    break;
                case "DELETE":
                    if (paramId > 0) {
                        taskManager.delTask(paramId);
                    }
                    break;
                default:
                    response = "Вы использовали какой-то другой метод!";
            }

            System.out.println(response);
            httpExchange.sendResponseHeaders(code, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
