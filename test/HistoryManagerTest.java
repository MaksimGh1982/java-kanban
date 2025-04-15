import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private final TaskManager taskManager = Managers.getDefault();
    private final HistoryManager historyManager = taskManager.getHistoryManager();

    @BeforeEach
    void clearHistory() {
        for (Task task : historyManager.getHistory()) {
            historyManager.remove(task.getId());
        }
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
        assertEquals(task2, historyManager.getHistory().getLast(), "Последняя просмотренная задача сохранена не в последнюю позицию истории.");
    }

    @Test
    void addHistory20() {
        for (int i = 0; i < 20; i++) {
            Task task = new Task("Test GetNewTask1", "Test GetNewTask description1");
            int id = taskManager.addTask(task);
            Task savedTask = taskManager.getTask(id);
        }
        assertEquals(20,  historyManager.getHistory().size(), "Количество элементов истории не 20.");
    }

    @Test
    void addHistory21() {
        Task task = new Task("Test GetNewTask1", "Test GetNewTask description1");
        int id = taskManager.addTask(task);
        for (int i = 0; i < 20; i++) {
            Task savedTask = taskManager.getTask(id);
        }
        assertEquals(1,  historyManager.getHistory().size(), "Дублирование элементов истории ");

        Task task2 = new Task("Test GetNewTask2", "Test GetNewTask description2");
        id = taskManager.addTask(task2);
        Task savedTask = taskManager.getTask(id);

        assertEquals(2,  historyManager.getHistory().size(), "Неверное количество элементов истории ");
    }

    @Test
    void removeHistory() {

        Task task = new Task("Test GetNewTask2", "Test GetNewTask description2");
        int id = taskManager.addTask(task);
        Task savedTask = taskManager.getTask(id);
        historyManager.remove(id);

        assertEquals(0, historyManager.getHistory().size(), "Не удален элемент истории");

        for (int i = 0; i < 20; i++) {
            Task task1 = new Task("Test GetNewTask1", "Test GetNewTask description1");
            int id1 = taskManager.addTask(task1);
            Task savedTask1 = taskManager.getTask(id1);
        }
        assertEquals(20,  historyManager.getHistory().size(), "Количество элементов истории не 20.");

        for (Task task2 : historyManager.getHistory()) {
            historyManager.remove(task2.getId());
        }
        assertEquals(0,  historyManager.getHistory().size(), "Количество элементов истории не 0.");
    }
}