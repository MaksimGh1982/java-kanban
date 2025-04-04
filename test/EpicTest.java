import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private final TaskManager taskManager = Managers.getDefault();

    @Test
    void getSubTasks() {
        Epic epic = new Epic("Test Epic", "Test Epic description");
        int idEpic = taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Test GetNewSubTask", "Test GetNewSubTask description", epic.getId());
        int idSubTask = taskManager.addSubTask(subTask);
        assertEquals(epic.getSubTasks().size(), 1, "Не найдена подзадача");
    }
}