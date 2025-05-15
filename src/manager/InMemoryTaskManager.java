package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import general.*;
import task.TaskComparator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {

    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, SubTask> subTasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected int id = 0;
    private TreeSet<Task> sortTasks = new TreeSet<>(new TaskComparator());

    @Override
    public List<Task> getAllTask() {
        ArrayList<Task> values = new ArrayList<>(tasks.values());
        return values;
    }

    @Override
    public List<SubTask> getAllSubTask() {
        ArrayList<SubTask> values = new ArrayList<>(subTasks.values());
        return values;
    }

    @Override
    public List<Epic> getAllEpic() {
        ArrayList<Epic> values = new ArrayList<>(epics.values());
        return values;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public int addTask(Task task) {
        if (!checkCross(task)) {
            task.setId(++id);
            tasks.put(id, task);
            addTaskInSortList(task);
            return id;
        } else {
            throw new TaskAcrossException("Пересечение задач");
        }
    }

    @Override
    public int addSubTask(SubTask subTask) {
        if (!checkCross(subTask)) {
            subTask.setId(++id);
            subTasks.put(id, subTask);
            addTaskInSortList(subTask);
            Epic epic = epics.get(subTask.getEpic());
            if (!Objects.isNull(epic)) {
                epic.getSubTasks().add(id);
                updateStatus(epic);
                updateEpicTime(epic);
            }
            return id;
        } else {
            throw new TaskAcrossException("Пересечение задач");
        }
    }

    @Override
    public int addEpic(Epic epic) {
        epic.setId(++id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public int updateTask(Task task) {
        if (!checkCross(task)) {
            tasks.put(task.getId(), task);
            sortTasks.remove(task);
            sortTasks.add(task);
            return task.getId();
        } else {
            throw new TaskAcrossException("Пересечение задач");
        }

    }

    @Override
    public int updateSubTask(SubTask subTask) {
        if (!checkCross(subTask)) {
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTask.getEpic());
            if (!Objects.isNull(epic)) {
                updateStatus(epic);
                updateEpicTime(epic);
            }
            sortTasks.remove(subTask);
            sortTasks.add(subTask);
            return subTask.getId();
        } else {
            throw new TaskAcrossException("Пересечение задач");
        }

    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void clearTasks() {
        for (Task task : tasks.values()) {
            sortTasks.remove(task);
        }
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
            updateEpicTime(epic);
        }
        for (SubTask subTask : subTasks.values()) {
            sortTasks.remove(subTask);
        }
        subTasks.clear();
    }

    @Override
    public Task getTask(Integer idTask) {
        historyManager.add(tasks.get(idTask));
        Task task = tasks.get(idTask);
        if (Objects.isNull(task)) {
            throw new NotFoundException("Task не существует");
        } else {
            return task;
        }
    }

    @Override
    public SubTask getSubTask(Integer idSubTask) {
        historyManager.add(subTasks.get(idSubTask));
        SubTask subTask = subTasks.get(idSubTask);
        if (Objects.isNull(subTask)) {
            throw new NotFoundException("SubTask не существует");
        } else {
            return subTask;
        }
    }

    @Override
    public Epic getEpic(Integer idEpic) {
        historyManager.add(epics.get(idEpic));
        Epic epic = epics.get(idEpic);
        if (Objects.isNull(epic)) {
            throw new NotFoundException("epic не существует");
        } else {
            return epic;
        }
    }

    @Override
    public List<SubTask> getSubTaskByEpic(Integer id) {
        if (epics.containsKey(id)) {
            /*List<SubTask> subTasksByEpic = new ArrayList<>();
            for (Integer idSubTask : epics.get(id).getSubTasks()) {
                subTasksByEpic.add(subTasks.get(idSubTask));
            }
            return subTasksByEpic;*/
            return epics.get(id).getSubTasks().stream()
                    .map(idSubTask -> subTasks.get(idSubTask))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @Override
    public void delTask(Integer id) {
        sortTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void delSubTask(Integer id) {
        if (subTasks.containsKey(id)) {
            Epic epic = epics.get(subTasks.get(id).getEpic());
            if (!Objects.isNull(epic)) {
                epic.getSubTasks().remove(id);
                updateStatus(epic);
                updateEpicTime(epic);
            }
            sortTasks.remove(subTasks.get(id));
            subTasks.remove(id);
        }

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
            switch (subtask.getStatus()) {
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

    private void updateEpicTime(Epic epic) {
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        for (Integer subtaskId : epic.getSubTasks()) {
            SubTask subtask = subTasks.get(subtaskId);
            if (Objects.nonNull(subtask.getStartTime()) && Objects.nonNull(subtask.getDuration())) {
                if (Objects.isNull(startTime)) {
                    startTime = subtask.getStartTime();
                } else if (subtask.getStartTime().isBefore(startTime)) {
                        startTime = subtask.getStartTime();
                }

                if (Objects.isNull(endTime)) {
                    endTime = subtask.getStartTime().plus(subtask.getDuration());
                } else if (subtask.getStartTime().plus(subtask.getDuration()).isAfter(endTime)) {
                    endTime = subtask.getStartTime().plus(subtask.getDuration());
                }
            }
        }
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            epic.setDuration(Duration.between(startTime, endTime));
        } else {
            epic.setDuration(null);
        }

    }

    public List<Task> getPrioritizedTasks() {
        return sortTasks.stream().toList();
    }

    protected void addTaskInSortList(Task task) {
        if (Objects.nonNull(task.getStartTime())) {
            sortTasks.add(task);
        }
    }

    private boolean checkCross(Task newTask) {
        List<Task> sortedTasks = getPrioritizedTasks();
        long count = sortedTasks.stream()
                .filter( task -> { return task.getId() != newTask.getId(); } )
                .filter(task -> {
                    if (ChronoUnit.SECONDS.between(newTask.getStartTime().plus(newTask.getDuration()), task.getStartTime()) >= 0 ||
                            ChronoUnit.SECONDS.between(task.getStartTime().plus(task.getDuration()), newTask.getStartTime()) >= 0) {
                        return false;
                    } else {
                        return true;
                    }
                })
                .count();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }
}
