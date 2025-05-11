import general.Status;
import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private  TaskManager taskManager;
    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void getSubTasks() {
        Epic epic = new Epic("Test task.Epic", "Test task.Epic description");
        int idEpic = taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Test GetNewSubTask", "Test GetNewSubTask description", epic.getId(), LocalDateTime.now().plus(1, ChronoUnit.DAYS), Duration.ofDays(1));
        int idSubTask = taskManager.addSubTask(subTask);
        assertEquals(epic.getSubTasks().size(), 1, "Не найдена подзадача");
        assertEquals(taskManager.getSubTask(idSubTask).getEpic(),idEpic,"Не найден связанный эпик подзадачи");
    }

    @Test
    void testStatusNewEpic() {
        Epic epic = new Epic("Test task.Epic", "Test task.Epic description");
        int idEpic = taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("Test GetNewSubTask", "Test GetNewSubTask description", idEpic,LocalDateTime.now().plus(1, ChronoUnit.DAYS), Duration.ofDays(1));
        subTask1.setStatus(Status.NEW);
        int idSubTask1 = taskManager.addSubTask(subTask1);

        SubTask subTask2 = new SubTask("Test GetNewSubTask", "Test GetNewSubTask description", idEpic,LocalDateTime.now().plus(2, ChronoUnit.DAYS), Duration.ofDays(1));
        subTask2.setStatus(Status.NEW);
        int idSubTask2 = taskManager.addSubTask(subTask2);

        SubTask subTask3 = new SubTask("Test GetNewSubTask", "Test GetNewSubTask description", idEpic,LocalDateTime.now().plus(3, ChronoUnit.DAYS), Duration.ofDays(1));
        subTask3.setStatus(Status.NEW);
        int idSubTask3 = taskManager.addSubTask(subTask3);
        assertEquals(epic.getStatus(), Status.NEW, "Статус Эпика не NEW");
    }

    @Test
    void testStatusDoneEpic() {
        Epic epic = new Epic("Test task.Epic", "Test task.Epic description");
        int idEpic = taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("Test GetNewSubTask", "Test GetNewSubTask description", idEpic);
        subTask1.setStatus(Status.DONE);
        int idSubTask1 = taskManager.addSubTask(subTask1);

        SubTask subTask2 = new SubTask("Test GetNewSubTask", "Test GetNewSubTask description", idEpic);
        subTask2.setStatus(Status.DONE);
        int idSubTask2 = taskManager.addSubTask(subTask2);

        SubTask subTask3 = new SubTask("Test GetNewSubTask", "Test GetNewSubTask description", idEpic);
        subTask3.setStatus(Status.DONE);
        int idSubTask3 = taskManager.addSubTask(subTask3);
        assertEquals(epic.getStatus(), Status.DONE, "Статус Эпика не DONE");
    }

    @Test
    void testStatusNewDoneEpic() {
        Epic epic = new Epic("Test task.Epic", "Test task.Epic description");
        int idEpic = taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("Test GetNewSubTask", "Test GetNewSubTask description", idEpic);
        subTask1.setStatus(Status.NEW);
        int idSubTask1 = taskManager.addSubTask(subTask1);

        SubTask subTask2 = new SubTask("Test GetNewSubTask", "Test GetNewSubTask description", idEpic);
        subTask2.setStatus(Status.DONE);
        int idSubTask2 = taskManager.addSubTask(subTask2);

        SubTask subTask3 = new SubTask("Test GetNewSubTask", "Test GetNewSubTask description", idEpic);
        subTask3.setStatus(Status.NEW);
        int idSubTask3 = taskManager.addSubTask(subTask3);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Статус Эпика не IN_PROGRESS");
    }

    @Test
    void testStatusNewDoneProgressEpic() {
        Epic epic = new Epic("Test task.Epic", "Test task.Epic description");
        int idEpic = taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("Test GetNewSubTask", "Test GetNewSubTask description", idEpic);
        subTask1.setStatus(Status.NEW);
        int idSubTask1 = taskManager.addSubTask(subTask1);

        SubTask subTask2 = new SubTask("Test GetNewSubTask", "Test GetNewSubTask description", idEpic);
        subTask2.setStatus(Status.DONE);
        int idSubTask2 = taskManager.addSubTask(subTask2);

        SubTask subTask3 = new SubTask("Test GetNewSubTask", "Test GetNewSubTask description", idEpic);
        subTask3.setStatus(Status.IN_PROGRESS);
        int idSubTask3 = taskManager.addSubTask(subTask3);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "Статус Эпика не IN_PROGRESS");
    }
}