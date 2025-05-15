import general.NotFoundException;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {

    @BeforeEach
    void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void addTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description",LocalDateTime.now().plus(1, ChronoUnit.DAYS), Duration.ofDays(1));
        int id = taskManager.addTask(task);

        Task savedTask = taskManager.getTask(id);

        assertNotNull(savedTask, "Задача не найдена!");
        assertEquals(task, savedTask, "Задачи не совпадают!");

        assertNotNull(taskManager.getAllTask(), "Задачи не возвращаются.");
        assertEquals(1, taskManager.getAllTask().size(), "Неверное количество задач.");
        assertEquals(task, taskManager.getAllTask().get(0), "Задачи не совпадают.");
    }

    @Test
    void getTask() {
        Task task = new Task("Test GetNewTask", "Test GetNewTask description",LocalDateTime.now().plus(1, ChronoUnit.DAYS), Duration.ofDays(1));
        int id = taskManager.addTask(task);

        Task savedTask = taskManager.getTask(id);
        Task savedTask1 = taskManager.getTask(id);

        assertNotNull(savedTask, "Задача не найдена.");
        assertNotNull(savedTask1, "Задача не найдена.");
        assertEquals(savedTask, task, "Задачи не совпадают.");
        assertEquals(savedTask, savedTask1, "Задачи не совпадают.");
    }

    @Test
    void getSubTask() {
        Epic epic = new Epic("Test GetNewSubTask", "Test GetNewSubTask description");
        int idEpic = taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Test GetNewSubTask","Test GetNewSubTask description",idEpic,LocalDateTime.now().plus(1, ChronoUnit.DAYS), Duration.ofDays(1));
        int id = taskManager.addSubTask(subTask);

        SubTask savedSubTask = taskManager.getSubTask(id);
        SubTask savedSubTask1 = taskManager.getSubTask(id);

        assertNotNull(savedSubTask, "Подадача не найдена.");
        assertNotNull(savedSubTask1, "Подзадача не найдена.");
        assertEquals(savedSubTask, subTask, "Подзадачи не совпадают.");
        assertEquals(savedSubTask, savedSubTask1, "Подзадачи не совпадают.");
    }

    @Test
    void DelSubTaskTest() {
        Epic epic = new Epic("Test GetNewSubTask", "Test GetNewSubTask description");
        int idEpic = taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Test GetNewSubTask","Test GetNewSubTask description",idEpic,LocalDateTime.now().plus(1, ChronoUnit.DAYS), Duration.ofDays(1));
        int idSubTask = taskManager.addSubTask(subTask);

        taskManager.delSubTask(idSubTask);
        assertThrows(NotFoundException.class, () -> {
            SubTask subtask = taskManager.getSubTask(idSubTask);
        }, "NotFoundException was expected");
        assertEquals(epic.getSubTasks().size(), 0, "Найдена удаленная подзадача в спивке эпика");

    }
}