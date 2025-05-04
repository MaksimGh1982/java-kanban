package task;

import general.Const;
import general.TypeTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private List<Integer> subTasks;
    private LocalDateTime endTime;

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasks, epic.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }

    public Epic(String title, String describe) {
        super(title, describe);
        subTasks = new ArrayList<>();
    }

    public Epic(String value) {
        super(value);
        subTasks = new ArrayList<>();
        String[] stringTask = value.split(",");
        if (stringTask[Const.ENDTIME_POSITION].equals("null")) {
            this.endTime = null;
        } else {
            this.endTime = LocalDateTime.parse(stringTask[Const.ENDTIME_POSITION], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }

    public List<Integer> getSubTasks() {
        return subTasks;
    }

    @Override
    public String toString() {
        return "task.Epic{" +
                "title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subTask=" + subTasks +
                '}';
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toFileString(TypeTask typeTask) {
        return super.toFileString(typeTask) +
                (Objects.nonNull(endTime) ? ","  + endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : ",null");
    }
}
