package manager;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTaskManager("filewriter.txt");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
