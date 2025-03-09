import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private final TaskManager taskManager = Managers.getDefault();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @BeforeEach
    void ClearHistory() {
        historyManager.getHistory().clear();
    }

    @Test
    void addHistory() {
        Task task = new Task("Test GetNewTask", "Test GetNewTask description");
        int id = taskManager.addTask(task);

        Task savedTask = taskManager.getTask(id);

        assertEquals(1,  historyManager.getHistory().size(), "История пуста.");
        assertNotNull(historyManager.getHistory().getLast(), "Задача в истории NULL.");
        assertEquals(task, historyManager.getHistory().getLast(), "Задачи в истории не совпадают.");
    }

    @Test
    void addHistory2() {
        Task task1 = new Task("Test GetNewTask1", "Test GetNewTask description1");
        int id = taskManager.addTask(task1);
        Task savedTask = taskManager.getTask(id);

        Task task2 = new Task("Test GetNewTask2", "Test GetNewTask description2");
        id = taskManager.addTask(task2);
        savedTask = taskManager.getTask(id);

        assertEquals(2,  historyManager.getHistory().size(), "Количество элементов истории не верно.");
        assertNotNull(historyManager.getHistory().getLast(), "Задача в истории NULL.");
        assertEquals(task2, historyManager.getHistory().getLast(), "Последняя просмотренная задача сохранена не в последнюю позицию истории.");
    }

    @Test
    void addHistory11() {
        Task task1 = new Task("Test GetNewTask1", "Test GetNewTask description1");
        int id = taskManager.addTask(task1);
        for (int i = 0; i < 20; i++) {
            Task savedTask = taskManager.getTask(id);
        }
        assertEquals(10,  historyManager.getHistory().size(), "Количество элементов истории не 10.");

        Task task2 = new Task("Test GetNewTask2", "Test GetNewTask description2");
        id = taskManager.addTask(task2);
        Task savedTask = taskManager.getTask(id);

        assertEquals(task2, historyManager.getHistory().getLast(), "Последняя просмотренная задача сохранена не в последнюю позицию истории.");
    }


}