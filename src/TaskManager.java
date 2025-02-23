import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    private int id = 0;

    public ArrayList<Task> getAllTask() {
        ArrayList<Task> values = new ArrayList<>(tasks.values());
        return values;
    }

    public ArrayList<SubTask> getAllSubTask() {
        ArrayList<SubTask> values = new ArrayList<>(subTasks.values());
        return values;
    }

    public ArrayList<Epic> getAllEpic() {
        ArrayList<Epic> values = new ArrayList<>(epics.values());
        return values;
    }

    public void addTask(Task task) {
        task.setId(id++);
        tasks.put(task.getId(), task);
    }

    public void addSubTask(SubTask subTask) {
        subTask.setId(id++);
        subTasks.put(subTask.getId(), subTask);
        if (!Objects.isNull(subTask.getEpic())) {
            subTask.getEpic().addSubTask(subTask);
        }
    }

    public void addEpic(Epic epic) {
        epic.setId(id++);
        epics.put(epic.getId(), epic);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        if (!Objects.isNull(subTask.getEpic())) {
            subTask.getEpic().updateStatus();
        }
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void clearSubTasks() {
        for (SubTask subTask : subTasks.values()) {
            subTask.getEpic().ClearSubTask();
        }
        subTasks.clear();
    }

    public Task getTask(Integer idTask) {
        return tasks.get(idTask);
    }

    public SubTask getSubTask(Integer idTask) {
        return subTasks.get(idTask);
    }

    public Epic getEpic(Integer idTask) {
        return epics.get(idTask);
    }

    public ArrayList<SubTask> getSubTaskByEpic(Integer id) {
        if (epics.containsKey(id)) {
            return epics.get(id).getSubTasks();
        } else {
            return null;
        }
    }

    public void delTask(Integer id) {
        tasks.remove(id);
    }

    public void delSubTask(Integer id) {
        if (subTasks.containsKey(id)) {
            subTasks.get(id).delete();
            subTasks.remove(id);
        }
    }

    public void delEpic(Integer id) {
        if (epics.containsKey(id)) {
            for (SubTask subtask : epics.get(id).getSubTasks()) {
                subTasks.remove(subtask.getId());
            }
            epics.remove(id);
        }
    }
}
