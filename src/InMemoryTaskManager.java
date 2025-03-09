import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private int id = 0;

    @Override
    public ArrayList<Task> getAllTask() {
        ArrayList<Task> values = new ArrayList<>(tasks.values());
        return values;
    }

    @Override
    public ArrayList<SubTask> getAllSubTask() {
        ArrayList<SubTask> values = new ArrayList<>(subTasks.values());
        return values;
    }

    @Override
    public ArrayList<Epic> getAllEpic() {
        ArrayList<Epic> values = new ArrayList<>(epics.values());
        return values;
    }

    @Override
    public int addTask(Task task) {
        task.setId(++id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public int addSubTask(SubTask subTask) {
        subTask.setId(++id);
        subTasks.put(id, subTask);
        Epic epic = epics.get(subTask.getEpic());
        if (!Objects.isNull(epic)) {
            epic.getSubTasks().add(id);
            updateStatus(epic);
        }
        return id;
    }

    @Override
    public int addEpic(Epic epic) {
        epic.setId(++id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpic());
        if (!Objects.isNull(epic)) {
            updateStatus(epic);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void clearSubTasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            epic.setStatus(Status.NEW);
        }
        subTasks.clear();
    }

    @Override
    public Task getTask(Integer idTask) {
        historyManager.AddHistory(tasks.get(idTask));
        return tasks.get(idTask);
    }

    @Override
    public SubTask getSubTask(Integer idSubTask) {
        historyManager.AddHistory(subTasks.get(idSubTask));
        return subTasks.get(idSubTask);
    }

    @Override
    public Epic getEpic(Integer idEpic) {
        historyManager.AddHistory(epics.get(idEpic));
        return epics.get(idEpic);
    }

    @Override
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

    @Override
    public void delTask(Integer id) {
        tasks.remove(id);
    }

    @Override
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

    @Override
    public void delEpic(Integer id) {
        if (epics.containsKey(id)) {
            for (Integer idSubTask : epics.get(id).getSubTasks()) {
                subTasks.remove(idSubTask);
            }
            epics.remove(id);
        }
    }

    private void updateStatus(Epic epic) {
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
