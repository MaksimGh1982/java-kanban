package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import general.Const;
import general.TypeTask;
import general.ManagerSaveException;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private String file;
    private String fileHistory;

    public FileBackedTaskManager(String fileName, String fileHistory) {
        this.file = fileName;
        this.fileHistory = fileHistory;
        loadFromFile();
        loadHistory();
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            for (Task task : getAllTask()) {
                fileWriter.write(task.toFileString(TypeTask.TASK) + "\n");
            }
            for (Epic epic : getAllEpic()) {
                fileWriter.write(epic.toFileString(TypeTask.EPIC) + "\n");
            }
            for (SubTask subTask : getAllSubTask()) {
                fileWriter.write(subTask.toFileString(TypeTask.SUBTASK) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private void saveHistory() {
        try (Writer fileWriter = new FileWriter(fileHistory)) {
            for (Task task : historyManager.getHistory()) {
                fileWriter.write(task.getId() + "," + task.getClass().getSimpleName().toUpperCase() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private void loadHistory() {
        File f = new File(fileHistory);
        if (f.exists()) {
            try (FileReader reader = new FileReader(fileHistory); BufferedReader br = new BufferedReader(reader)) {
                while (br.ready()) {
                    String line = br.readLine();
                    String[] stringHistory = line.split(",");

                    if (TypeTask.valueOf(stringHistory[1]) == TypeTask.TASK) {
                        historyManager.add(tasks.get(Integer.parseInt(stringHistory[0])));
                    } else if (TypeTask.valueOf(stringHistory[1]) == TypeTask.SUBTASK) {
                        historyManager.add(subTasks.get(Integer.parseInt(stringHistory[0])));
                    } else if (TypeTask.valueOf(stringHistory[1]) == TypeTask.EPIC) {
                        historyManager.add(epics.get(Integer.parseInt(stringHistory[0])));
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadFromFile() {
        File f = new File(file);
        if (f.exists()) {
            try (FileReader reader = new FileReader(file); BufferedReader br = new BufferedReader(reader)) {
                while (br.ready()) {
                    String line = br.readLine();
                    String[] stringTask = line.split(",");

                    if (TypeTask.valueOf(stringTask[Const.TYPE_TASK_POSITION]) == TypeTask.TASK) {
                        Task task = new Task(line);
                        tasks.put(task.getId(), task);
                        addTaskInSortList(task);
                    } else if (TypeTask.valueOf(stringTask[Const.TYPE_TASK_POSITION]) == TypeTask.SUBTASK) {
                        SubTask subTask = new SubTask(line);
                        subTasks.put(subTask.getId(), subTask);
                        addTaskInSortList(subTask);
                        epics.get(subTask.getEpic()).getSubTasks().add(subTask.getId());
                    } else if (TypeTask.valueOf(stringTask[Const.TYPE_TASK_POSITION]) == TypeTask.EPIC) {
                        Epic epic = new Epic(line);
                        epics.put(epic.getId(), epic);
                    }
                    if (id < Integer.parseInt(stringTask[Const.ID_POSITION])) {
                        id = Integer.parseInt(stringTask[Const.ID_POSITION]) + 1;
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int addSubTask(SubTask subTask) {
        int id = super.addSubTask(subTask);
        save();
        return id;
    }

    @Override
    public int addEpic(Epic epic) {
        int id = super.addEpic(epic);
        save();
        return id;
    }

    @Override
    public int addTask(Task task) {
        int id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public void clearSubTasks() {
        super.clearSubTasks();
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void delEpic(Integer id) {
        super.delEpic(id);
        save();
    }

    @Override
    public void delSubTask(Integer id) {
        super.delSubTask(id);
        save();
    }

    @Override
    public void delTask(Integer id) {
        super.delTask(id);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public int updateSubTask(SubTask subTask) {
        int  i = super.updateSubTask(subTask);
        save();
        return i;
    }

    @Override
    public int updateTask(Task task) {
        int i = super.updateTask(task);
        save();
        return i;
    }

    @Override
    public Epic getEpic(Integer idEpic) {
        Epic epic = super.getEpic(idEpic);
        saveHistory();
        return epic;
    }

    @Override
    public SubTask getSubTask(Integer idSubTask) {
        SubTask subTask = super.getSubTask(idSubTask);
        saveHistory();
        return subTask;
    }

    @Override
    public Task getTask(Integer idTask) {
        Task task = super.getTask(idTask);
        saveHistory();
        return task;
    }
}
