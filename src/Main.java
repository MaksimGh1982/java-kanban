import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("ts1","ts11111", LocalDateTime.now(), Duration.ofDays(1));
        Task task2 = new Task("ts2","ts22222", LocalDateTime.now().plus(2, ChronoUnit.DAYS), Duration.ofDays(1));
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("ep1","ep11111");
        Epic epic2 = new Epic("ep2","ep22222");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        SubTask subtask1 = new SubTask("subtask1","subtask1111",epic1.getId(), LocalDateTime.now().plus(4, ChronoUnit.DAYS), Duration.ofDays(1));
        SubTask subtask2 = new SubTask("subtask2","subtask22222",epic1.getId(), LocalDateTime.now().plus(5, ChronoUnit.DAYS), Duration.ofDays(1));
        SubTask subtask3 = new SubTask("subtask3","subtask33333",epic1.getId(), LocalDateTime.now().plus(3, ChronoUnit.DAYS), Duration.ofDays(1));
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        taskManager.addSubTask(subtask3);

        List<SubTask> ls = taskManager.getSubTaskByEpic(epic1.getId());
    }
}
