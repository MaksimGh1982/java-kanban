package httpHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import general.Const;
import manager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        jsonWriter.value(Objects.isNull(duration) ? null : duration.toMinutes());
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        } else {
            return Duration.ofMinutes(Integer.parseInt(jsonReader.nextString()));
        }
    }
}

class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
        jsonWriter.value(Objects.isNull(localDateTime) ? null : localDateTime.format(dtf));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        } else {
            return LocalDateTime.parse(jsonReader.nextString(), dtf);
        }
    }
}

public class BaseHttpHandler implements HttpHandler {

    protected TaskManager taskManager;
    protected String response = "";
    protected int code;
    protected String path;
    protected int paramId;
    protected String method;
    protected InputStream inputStream;
    protected String jsonBody;

    protected final static Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public static Gson getGson() {
        return gson;
    }

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    protected void post() {
        code = Const.CODE_HTTP_ERROR;
    }

    protected void get() {
        code = Const.CODE_HTTP_ERROR;
    }

    protected void delete() {
        code = Const.CODE_HTTP_ERROR;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        path = httpExchange.getRequestURI().getPath();
        paramId = path.split("/").length >= 3 ? Integer.parseInt(path.split("/")[2]) : -1;

        method = httpExchange.getRequestMethod();
        inputStream = httpExchange.getRequestBody();
        jsonBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        switch (method) {
            case "POST":
                post();
                break;
            case "GET":
                get();
                break;
            case "DELETE":
                delete();
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