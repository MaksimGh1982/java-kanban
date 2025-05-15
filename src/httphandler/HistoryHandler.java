package httphandler;

import general.Const;
import manager.HistoryManager;

public class HistoryHandler extends BaseHttpHandler {

    private HistoryManager historyManager;

    public HistoryHandler(HistoryManager historyManager) {
        super(null);
        this.historyManager = historyManager;
    }

    @Override
    protected void get() {
        code = Const.CODE_SUCCESS;
        response = gson.toJson(historyManager.getHistory());
    }
}
