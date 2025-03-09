import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private List<Integer> subTasks;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasks, epic.subTasks);
    }

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
