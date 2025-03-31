public class Main {

    public static void main(String[] args) {

        System.out.println("Поехали!");
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("ts1","ts11111");
        Task task2 = new Task("ts2","ts22222");
        taskManager.addTask(task1);
        taskManager.addTask(task2);


        Epic epic1 = new Epic("ep1","ep11111");
        Epic epic2 = new Epic("ep2","ep22222");
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        SubTask subtask1 = new SubTask("subtask1","subtask1111",epic1.getId());
        SubTask subtask2 = new SubTask("subtask2","subtask22222",epic1.getId());
        SubTask subtask3 = new SubTask("subtask3","subtask33333",epic2.getId());
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        taskManager.addSubTask(subtask3);

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllSubTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getSubTaskByEpic(3));

        SubTask subtask = taskManager.getSubTask(7);
        subtask.setStatus(Status.DONE);
        taskManager.updateSubTask(subtask);

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllSubTask());
        System.out.println(taskManager.getAllEpic());

        /*taskManager.delEpic(2);
        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllSubTask());
        System.out.println(taskManager.getAllEpic());*/

        subtask = taskManager.getSubTask(5);
        subtask.setStatus(Status.DONE);
        taskManager.updateSubTask(subtask);

        subtask = taskManager.getSubTask(6);
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subtask);
        System.out.println("Hist " + Managers.getDefaultHistory().getHistory());

        subtask = taskManager.getSubTask(5);
        System.out.println("Hist " + Managers.getDefaultHistory().getHistory());
        subtask = taskManager.getSubTask(6);
        System.out.println("Hist " + Managers.getDefaultHistory().getHistory());

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllSubTask());
        System.out.println(taskManager.getAllEpic());


        taskManager.delSubTask(7);

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllSubTask());
        System.out.println(taskManager.getAllEpic());

        taskManager.clearSubTasks();

        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllSubTask());
        System.out.println(taskManager.getAllEpic());

        subtask = taskManager.getSubTask(77);
        System.out.println(Managers.getDefaultHistory().getHistory());

    }
}
