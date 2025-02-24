public class SubTask extends Task {
    private int epic;

    public SubTask(String title, String describe, int epic) {
        super(title, describe);
        this.epic = epic;
    }

    public int getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epic=" + epic +
                ", title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
