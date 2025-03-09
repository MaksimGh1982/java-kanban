import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getAllTask();

    ArrayList<SubTask> getAllSubTask();

    ArrayList<Epic> getAllEpic();

    int addTask(Task task);

    int addSubTask(SubTask subTask);

    int addEpic(Epic epic);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void clearTasks();

    void clearEpics();

    void clearSubTasks();

    Task getTask(Integer idTask);

    SubTask getSubTask(Integer idSubTask);

    Epic getEpic(Integer idEpic);

    List<SubTask> getSubTaskByEpic(Integer id);

    void delTask(Integer id);

    void delSubTask(Integer id);

    void delEpic(Integer id);
}
