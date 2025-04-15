package task;

import java.util.Objects;

import general.*;

public class Task {
    protected String title;
    protected String describe;
    protected int id;
    protected Status status;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(describe, task.describe) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, describe, id, status);
    }

    @Override
    public String toString() {
        return "task.Task{" +
                "title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    public String toFileString(TypeTask typeTask) {
        return id + "," + typeTask.toString() + "," + title + ","  + status + ","  + describe;

    }

    public Task(String value) {
        String[] stringTask = value.split(",");

        this.title = stringTask[Const.TITLE_POSITION];
        this.describe = stringTask[Const.DESCRIBE_POSITION];
        this.status = Status.valueOf(stringTask[Const.STATUS_POSITION]);
        this.id = (Integer.parseInt(stringTask[Const.ID_POSITION]));
    }

    public Task(String title, String describe) {
        this.title = title;
        this.describe = describe;
        this.status = Status.NEW;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
