public class Managers {

    private final static InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return inMemoryHistoryManager;
    }
}
