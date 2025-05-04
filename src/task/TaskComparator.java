package task;

import java.util.Comparator;

public class TaskComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        if (o1.getId() == o2.getId()) {
            return 0;
        } else {
            return o2.getStartTime().isBefore(o1.getStartTime())  ? 1 : -1;
        }
    }
}
