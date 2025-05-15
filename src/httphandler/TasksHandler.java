package httphandler;

import general.Const;
import general.NotFoundException;
import general.TaskAcrossException;
import manager.TaskManager;
import task.Task;

public class TasksHandler extends BaseHttpHandler {

    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void post() {
        Task task = gson.fromJson(jsonBody, Task.class);
        try {
            if (task.getId() > 0) {
                taskManager.updateTask(task);
            } else {
                taskManager.addTask(task);
            }
            code = Const.CODE_SUCCESS_FOR_CREATE_UPD;
        } catch (TaskAcrossException e) {
            code = Const.CODE_ACCROSS_ERROR;
        }
    }

    @Override
    protected void get() {
        code = Const.CODE_SUCCESS;
        if (paramId > 0) {
            try {
                response = gson.toJson(taskManager.getTask(paramId));
            } catch (NotFoundException e) {
                code = Const.CODE_NOT_FOUND;
                response = "";
            }
        } else {
            response = gson.toJson(taskManager.getAllTask());
        }
    }

    @Override
    protected void delete() {
        if (paramId > 0) {
            taskManager.delTask(paramId);
        }
        code = Const.CODE_SUCCESS;
    }
}