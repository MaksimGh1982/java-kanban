import manager.TaskManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;

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
        SubTask subTask = new SubTask("Test GetNewSubTask","Test GetNewSubTask description",idEpic,LocalDateTime.now().plus(7, ChronoUnit.DAYS), Duration.ofDays(1));
        int idSubTask = taskManager.addSubTask(subTask);

        taskManager.clearEpics();
        assertEquals(0,taskManager.getAllEpic().size(),"Не все эпики удалены");
        assertEquals(0,taskManager.getAllSubTask().size(),"Не все подзадачи удалены");
    }

    @Test
    void   intervalTest() {
        Epic epic1 = new Epic("ep1","ep11111");
        taskManager.addEpic(epic1);

        SubTask subtask1 = new SubTask("subtask1","subtask1111",epic1.getId(), LocalDateTime.now().plus(4, ChronoUnit.DAYS), Duration.ofDays(1));
        SubTask subtask2 = new SubTask("subtask2","subtask22222",epic1.getId(), LocalDateTime.now().plus(5, ChronoUnit.DAYS), Duration.ofDays(1));
        SubTask subtask3 = new SubTask("subtask3","subtask33333",epic1.getId(), LocalDateTime.now().plus(6, ChronoUnit.DAYS), Duration.ofDays(1));
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        taskManager.addSubTask(subtask3);

        LocalDateTime testDate = LocalDateTime.now().minusDays(10);
        for (int i = 1; i < 12*24*40; i+=5) {
            testDate = testDate.plusMinutes(i);
            int isOccupied = 0;
            for (Task task : taskManager.getPrioritizedTasks()) {
                if (testDate.isBefore(task.getStartTime().plus(task.getDuration())) && testDate.isAfter(task.getStartTime())) {
                    isOccupied++;
                    assertTrue(isOccupied<2, "Найдено пересечение временных интервалов");
                }
            }
        }
    }

}
