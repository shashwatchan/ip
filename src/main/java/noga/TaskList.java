public class TaskList {
    private static final int MAX_TASKS = 100;
    private final Task[] taskArray;
    private int curIndex;

    public TaskList() {
        taskArray = new Task[MAX_TASKS + 1];
        curIndex = 1;
    }

    public void addTask(Task task) {
        assert curIndex < MAX_TASKS : "Task list is full";
        taskArray[curIndex++] = task;
    }

    public Task getTask(int index) {
        validateIndex(index);
        return taskArray[index];
    }

    public void deleteTask(int index) {
        validateIndex(index);
        for (int i = index; i < curIndex - 1; i++) {
            taskArray[i] = taskArray[i + 1];
        }
        curIndex--;
    }

    private void validateIndex(int index) {
        if (index < 1 || index >= curIndex) {
            throw new IllegalArgumentException("Invalid task index: " + index);
        }
    }

    // Other task management methods...
} 