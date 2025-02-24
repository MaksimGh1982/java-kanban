import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TaskManager {

    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
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
        task.setId(id);
        tasks.put(id, task);
        id++;
    }

    public void addSubTask(SubTask subTask) {
        subTask.setId(id);
        subTasks.put(id, subTask);
        Epic epic = epics.get(subTask.getEpic());
        if (!Objects.isNull(epic)) {
            epic.getSubTasks().add(id);
            updateStatus(epic);
        }
        id++;
    }

    public void addEpic(Epic epic) {
        epic.setId(id);
        epics.put(id, epic);
        id++;
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpic());
        if (!Objects.isNull(epic)) {
            updateStatus(epic);
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
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            epic.setStatus(Status.NEW);
        }
        subTasks.clear();
    }

    public Task getTask(Integer idTask) {
        return tasks.get(idTask);
    }

    public SubTask getSubTask(Integer idSubTask) {
        return subTasks.get(idSubTask);
    }

    public Epic getEpic(Integer idEpic) {
        return epics.get(idEpic);
    }

    public List<SubTask> getSubTaskByEpic(Integer id) {
        if (epics.containsKey(id)) {
            List<SubTask> subTasksByEpic = new ArrayList<>();
            for (Integer idSubTask : epics.get(id).getSubTasks()) {
                subTasksByEpic.add(subTasks.get(idSubTask));
            }
            return subTasksByEpic;
        } else {
            return null;
        }
    }

    public void delTask(Integer id) {
        tasks.remove(id);
    }

    public void delSubTask(Integer id) {
        if (subTasks.containsKey(id)) {
            Epic epic = epics.get(subTasks.get(id).getEpic());
            if (!Objects.isNull(epic)) {
                epic.getSubTasks().remove(id);
                updateStatus(epic);
            }
        }
        subTasks.remove(id);
    }

    public void delEpic(Integer id) {
        if (epics.containsKey(id)) {
            for (Integer idSubTask : epics.get(id).getSubTasks()) {
                subTasks.remove(idSubTask);
            }
            epics.remove(id);
        }
    }

    public void updateStatus(Epic epic) {
        int countDone = 0;
        int countNew = 0;
        int countProgress = 0;
        for (Integer subtaskId : epic.getSubTasks()) {
            SubTask subtask = subTasks.get(subtaskId);
            switch (subtask.status) {
                case NEW:
                    countNew++;
                    break;
                case DONE:
                    countDone++;
                    break;
                case IN_PROGRESS:
                    countProgress++;
                    break;
            }
            if (countProgress > 0 || (countNew > 0 && countDone > 0)) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        if (epic.getSubTasks().isEmpty() || epic.getSubTasks().size() == countNew) {
            epic.setStatus(Status.NEW);
        } else if (epic.getSubTasks().size() == countDone) {
            epic.setStatus(Status.DONE);
        }
    }
}
