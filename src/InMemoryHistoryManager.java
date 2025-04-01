import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;

    private Map<Integer,Node> mapTasks = new HashMap<>();

    public Node linkLast(Task task) {
        Node newNode = new Node(task);
        newNode.setPriorNode(tail);
        if (Objects.isNull(head)) {
            head = newNode;
        }
        if (!Objects.isNull(tail)) {
            tail.setNextNode(newNode);
        }
        tail = newNode;
        return newNode;
    }

    public void removeNode(Node node) {
        if (node == head && node == tail) {
            head = null;
            tail = null;
        } else if (node == head) {
            head = node.getNextNode();
            head.setPriorNode(null);
        } else if (node == tail) {
            tail = node.getPriorNode();
            tail.setNextNode(null);
        } else {
            node.getPriorNode().setNextNode(node.getNextNode());
            node.getNextNode().setPriorNode(node.getPriorNode());
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        if (!Objects.isNull(head)) {
            Node node = head;
            while (node != tail) {
                tasks.add(node.getTask());
                node = node.getNextNode();
            }
            tasks.add(node.getTask());
        }
        return tasks;
    }

    @Override
    public void add(Task task) {
        if (!Objects.isNull(task)) {
            if (mapTasks.containsKey(task.getId())) {
                removeNode(mapTasks.get(task.getId()));
                mapTasks.remove(task.getId());
            }
            mapTasks.put(task.getId(),linkLast(task));
        }
    }

    @Override
    public void remove(int id) {
        removeNode(mapTasks.get(id));
        mapTasks.remove(id);
    }

}
