package manager;

import task.Task;

public class Node {
    private Node priorNode;
    private Node nextNode;
    private Task task;

    public Node getNextNode() {
        return nextNode;
    }

    public Node getPriorNode() {
        return priorNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public void setPriorNode(Node priorNode) {
        this.priorNode = priorNode;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public Node(Task task) {
        this.task = task;
    }
}
