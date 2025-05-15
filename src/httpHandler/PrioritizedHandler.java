package httpHandler;

import general.Const;
import manager.TaskManager;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void get() {
        code = Const.CODE_SUCCESS;
        response = gson.toJson(taskManager.getPrioritizedTasks());
    }
}
