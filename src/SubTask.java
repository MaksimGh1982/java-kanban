public class SubTask extends Task {
    private Epic epic;

    public SubTask(String title, String describe, Epic headTask) {
        super(title, describe);
        this.epic = headTask;
    }

    public Epic getEpic() {
        return epic;
    }

    public void delete() {
        this.epic.deleteSubTask(this);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epic=" + epic.id +
                ", title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
