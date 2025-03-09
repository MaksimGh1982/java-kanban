import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> tasksHistory = new ArrayList<>();

    @Override
    public void AddHistory(Task task) {
        if (!Objects.isNull(task)) {
            tasksHistory.addLast(task);
            if (tasksHistory.size() > 10) {
                tasksHistory.removeFirst();
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return tasksHistory;
    }
}
