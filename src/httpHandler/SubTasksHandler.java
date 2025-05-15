package httpHandler;

import general.Const;
import general.NotFoundException;
import general.TaskAcrossException;
import manager.TaskManager;
import task.SubTask;

public class SubTasksHandler extends BaseHttpHandler {

    public SubTasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void post() {
        SubTask subTask = gson.fromJson(jsonBody, SubTask.class);
        try {
            if (subTask.getId() > 0) {
                taskManager.updateSubTask(subTask);
            } else {
                taskManager.addSubTask(subTask);
            }
            code = Const.CODE_SUCCESS_FOR_CREATE_UPD;
        }
        catch (TaskAcrossException e) {
            code = Const.CODE_ACCROSS_ERROR;
        }
    }

    @Override
    protected void get() {
        code = Const.CODE_SUCCESS;
        if (paramId > 0) {
            try {
                response = gson.toJson(taskManager.getSubTask(paramId));
            }
            catch (NotFoundException e) {
                code = Const.CODE_NOT_FOUND;
                response = "";
            }
        } else {
            response = gson.toJson(taskManager.getAllSubTask());
        }
    }

    @Override
    protected void delete() {
        if (paramId > 0) {
            taskManager.delSubTask(paramId);
        }
        code = Const.CODE_SUCCESS;
    }
}
