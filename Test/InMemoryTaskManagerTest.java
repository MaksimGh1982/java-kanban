import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        int id = taskManager.addTask(task);

        Task savedTask = taskManager.getTask(id);

        assertNotNull(savedTask, "Задача не найдена!");
        assertEquals(task, savedTask, "Задачи не совпадают!");

        List<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getTask() {
        Task task = new Task("Test GetNewTask", "Test GetNewTask description");
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
        SubTask subTask = new SubTask("Test GetNewSubTask","Test GetNewSubTask description",epic.getId());
        int id = taskManager.addSubTask(subTask);

        SubTask savedSubTask = taskManager.getSubTask(id);
        SubTask savedSubTask1 = taskManager.getSubTask(id);

        assertNotNull(savedSubTask, "Подадача не найдена.");
        assertNotNull(savedSubTask1, "Подзадача не найдена.");
        assertEquals(savedSubTask, subTask, "Подзадачи не совпадают.");
        assertEquals(savedSubTask, savedSubTask1, "Подзадачи не совпадают.");
    }

    @Test
    void getEpic() {
        Epic epic = new Epic("Test GetNewEpic", "Test GetNewEpick description");
        int id = taskManager.addEpic(epic);

        Epic savedEpic = taskManager.getEpic(id);
        Epic savedEpic1 = taskManager.getEpic(id);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertNotNull(savedEpic1, "Эпик не найден.");
        assertEquals(savedEpic, epic, "Эпики не совпадают.");
        assertEquals(savedEpic, savedEpic1, "Эпики не совпадают.");
    }

    @Test
    void clearEpic() {
        Epic epic = new Epic("Test GetNewSubTask", "Test GetNewSubTask description");
        int idEpic = taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Test GetNewSubTask","Test GetNewSubTask description",epic.getId());
        int idSubTask = taskManager.addSubTask(subTask);

        taskManager.clearEpics();
        assertEquals(0,taskManager.getAllEpic().size(),"Не все эпики удалены");
        assertEquals(0,taskManager.getAllSubTask().size(),"Не все подзадачи удалены");
    }

    @Test
    void StatusEpicTest() {
        Epic epic = new Epic("Test GetNewSubTask", "Test GetNewSubTask description");
        int idEpic = taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Test GetNewSubTask","Test GetNewSubTask description",epic.getId());
        int idSubTask = taskManager.addSubTask(subTask);

        subTask = new SubTask("Test GetNewSubTask","Test GetNewSubTask description",epic.getId());
        int idSubTask1 = taskManager.addSubTask(subTask);

        SubTask getSubTask = taskManager.getSubTask(idSubTask);
        getSubTask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(getSubTask);

        assertEquals(taskManager.getEpic(idEpic).getStatus(),Status.IN_PROGRESS,"Неверный статус эпика");

        getSubTask = taskManager.getSubTask(idSubTask);
        getSubTask.setStatus(Status.DONE);
        taskManager.updateSubTask(getSubTask);

        getSubTask = taskManager.getSubTask(idSubTask1);
        getSubTask.setStatus(Status.DONE);
        taskManager.updateSubTask(getSubTask);

        assertEquals(taskManager.getEpic(idEpic).getStatus(),Status.DONE,"Неверный статус эпика");

        taskManager.delSubTask(idSubTask);
        taskManager.delSubTask(idSubTask1);
        assertEquals(taskManager.getEpic(idEpic).getStatus(),Status.NEW,"Неверный статус эпика");
    }

    @Test
    void DelSubTaskTest() {
        Epic epic = new Epic("Test GetNewSubTask", "Test GetNewSubTask description");
        int idEpic = taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Test GetNewSubTask","Test GetNewSubTask description",epic.getId());
        int idSubTask = taskManager.addSubTask(subTask);

        taskManager.delSubTask(idSubTask);
        assertNull(taskManager.getSubTask(idSubTask), "Найдена удаленная подзадача");
        assertEquals(epic.getSubTasks().size(), 0, "Найдена удаленная подзадача в спивке эпика");

    }
}