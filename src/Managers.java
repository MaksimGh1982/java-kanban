public class Managers {

    private final static InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    private static InMemoryTaskManager inMemoryTaskManager;

    public static TaskManager getDefault() {
        inMemoryTaskManager = new InMemoryTaskManager();
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }
}
