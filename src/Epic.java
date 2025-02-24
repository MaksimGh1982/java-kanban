import java.util.List;
import java.util.ArrayList;

public class Epic extends Task {

    private List<Integer> subTasks;

    public Epic(String title, String describe) {
        super(title, describe);
        subTasks = new ArrayList<>();
    }

    public List<Integer> getSubTasks() {
        return subTasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subTask=" + subTasks +
                '}';
    }
}
