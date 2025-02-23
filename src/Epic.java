import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<SubTask> subTasks;

    public Epic(String title, String describe) {
        super(title, describe);
        subTasks = new ArrayList<>();
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
        updateStatus();
    }

    public void updateStatus() {
        int countDone = 0;
        int countNew = 0;
        int countProgress = 0;
        for (SubTask subtask : subTasks) {
            switch (subtask.status) {
                case NEW:
                    countNew++;
                    break;
                case DONE:
                    countDone++;
                    break;
                case IN_PROGRESS:
                    countProgress++;
                    break;
            }
            if (countProgress > 0 || (countNew > 0 && countDone > 0)) {
                this.status = Status.IN_PROGRESS;
                return;
            }
        }
        if (subTasks.isEmpty() || subTasks.size() == countNew) {
            this.status = Status.NEW;
        } else if (subTasks.size() == countDone) {
            this.status = Status.DONE;
        }
    }

    public void deleteSubTask(SubTask subtask) {
        subTasks.remove(subtask);
        updateStatus();
    }

    public void ClearSubTask() {
        subTasks.clear();
        this.status = Status.NEW;
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

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }
}
