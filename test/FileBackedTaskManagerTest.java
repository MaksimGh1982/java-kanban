import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void beforeEach() {
        File myFile = new File("filewriter.txt");
        myFile.delete();
        taskManager = Managers.getDefault();
    }

    @Test
    void checkFileBacked() {
        int[] epicId = new int[5];
        for (int i = 0; i < 20; i++) {
            Task task = new Task("Test NewTask1", "Test NewTask description1");
            int id = taskManager.addTask(task);
        }
        for (int i = 0; i < 5; i++) {
            Epic epic = new Epic("Test NewEpic1", "Test NewEpic description1");
            int id = taskManager.addEpic(epic);
            epicId[i] = id;
        }
        for (int i = 0; i < 20; i++) {
            SubTask subTask = new SubTask("Test NewSubTask1", "Test NewSubTask description1",epicId[i/5]);
            int id = taskManager.addSubTask(subTask);
        }

        TaskManager taskManagerFromFile = Managers.getDefault();

        assertEquals(taskManager.getAllTask(), taskManagerFromFile.getAllTask(), "Восстановленные Задачи не равны исходным");
        assertEquals(taskManager.getAllSubTask(), taskManagerFromFile.getAllSubTask(), "Восстановленные Подзадачи не равны исходным");
    }
}
