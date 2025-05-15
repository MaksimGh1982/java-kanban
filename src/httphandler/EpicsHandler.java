package httphandler;

import general.Const;
import general.NotFoundException;
import manager.TaskManager;
import task.Epic;

public class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void post() {
        Epic epic = gson.fromJson(jsonBody, Epic.class);
        if (epic.getId() > 0) {
            taskManager.updateEpic(epic);
        } else {
            taskManager.addEpic(epic);
        }
        code = Const.CODE_SUCCESS_FOR_CREATE_UPD;
    }

    @Override
    protected void get() {
        code = Const.CODE_SUCCESS;
        if (paramId > 0) {
            Epic epic;
            try {
                epic = taskManager.getEpic(paramId);
                if ((path.split("/").length >= 4 ? path.split("/")[3] : "-").equals("subtasks")) {
                    response = gson.toJson(taskManager.getSubTaskByEpic(paramId));
                } else {
                    response = gson.toJson(epic);
                }
            }
            catch (NotFoundException e) {
                code = Const.CODE_NOT_FOUND;
                response = "";
            }
        } else {
            response = gson.toJson(taskManager.getAllEpic());
        }
    }

    @Override
    protected void delete() {
        if (paramId > 0) {
            taskManager.delEpic(paramId);
        }
        code = Const.CODE_SUCCESS;
    }
}

