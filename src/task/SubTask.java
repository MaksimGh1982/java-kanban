package task;

import general.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epic;

    public SubTask(String title, String describe, int epic) {
        super(title, describe);
        this.epic = epic;
    }

    public SubTask(String title, String describe, int epic, LocalDateTime startTime, Duration duration) {
        super(title, describe, startTime, duration);
        this.epic = epic;
    }

    public SubTask(String value) {
        super(value);
        String[] stringTask = value.split(",");
        this.epic = Integer.parseInt(stringTask[Const.EPIC_ID_POSITION]);
    }

    public int getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "task.SubTask{" +
                "epic=" + epic +
                ", title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    @Override
    public String toFileString(TypeTask typeTask) {
        return super.toFileString(typeTask) + "," + epic;

    }



}
