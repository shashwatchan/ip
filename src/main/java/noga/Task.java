package noga;
/**
 * Represents a task in the task list.
 * A task has a description and can be marked as done or not done.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    
    /**
     * Creates a new Task with the given description.
     * The task is initially not done.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    
    /**
     * Marks this task as done.
     */
    public void mark() {
        isDone = true;
    }
    
    /**
     * Marks this task as not done.
     */
    public void unmark() {
        isDone = false;
    }
    
    /**
     * Gets the description of this task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if this task is done.
     *
     * @return true if the task is done, false otherwise
     */
    public boolean isDone() {
        return isDone;
    }
    
    @Override
    public String toString() {
        return "[" + (isDone ? "✓" : "✗") + "] " + description;
    }
    
    protected char getTypeIcon() {
        return 'T';
    }
}