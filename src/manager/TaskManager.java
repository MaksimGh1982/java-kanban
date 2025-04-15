package manager;

import task.Epic;
import task.SubTask;
import task.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTask();

    List<SubTask> getAllSubTask();

    List<Epic> getAllEpic();

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

    HistoryManager getHistoryManager();
}
