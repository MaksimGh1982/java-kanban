package manager;

import task.Epic;
import task.SubTask;
import task.Task;
import general.*;
import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private String file;

    public FileBackedTaskManager(String fileName) {
        this.file = fileName;
        loadFromFile();
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            for (Task task : getAllTask()) {
                fileWriter.write(task.toFileString(TypeTask.TASK) + "\n");
            }
            for (Epic epic: getAllEpic()) {
                fileWriter.write(epic.toFileString(TypeTask.EPIC) + "\n");
            }
            for (SubTask subTask : getAllSubTask()) {
                fileWriter.write(subTask.toFileString(TypeTask.SUBTASK) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    private void loadFromFile()  {
        File f = new File(file);
        if (f.exists()) {
            try (FileReader reader = new FileReader(file); BufferedReader br = new BufferedReader(reader)) {
                while (br.ready()) {
                    String line = br.readLine();
                    String[] stringTask = line.split(",");

                    if (TypeTask.valueOf(stringTask[Const.TYPETASK_POSITION]) == TypeTask.TASK) {
                        Task task = new Task(line);
                        tasks.put(task.getId(), task);
                    } else if (TypeTask.valueOf(stringTask[Const.TYPETASK_POSITION]) == TypeTask.SUBTASK) {
                        SubTask subTask = new SubTask(line);
                        subTasks.put(subTask.getId(), subTask);
                        epics.get(subTask.getEpic()).getSubTasks().add(subTask.getId());
                    } else if (TypeTask.valueOf(stringTask[Const.TYPETASK_POSITION]) == TypeTask.EPIC) {
                        Epic epic = new Epic(line);
                        epics.put(epic.getId(), epic);
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
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }
}
