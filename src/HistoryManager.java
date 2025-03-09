import java.util.List;

public interface HistoryManager {

    void AddHistory(Task task);

    List<Task> getHistory();
}
