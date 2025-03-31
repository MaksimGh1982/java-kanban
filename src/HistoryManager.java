import java.util.List;

public interface HistoryManager {

    void Add(Task task);
    void remove(int id);
    List<Task> getHistory();
}
