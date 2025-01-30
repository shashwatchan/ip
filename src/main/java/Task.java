public class Task {
    protected String description;
    protected boolean isDone;
    
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    
    public void mark() {
        isDone = true;
    }
    
    public void unmark() {
        isDone = false;
    }
    
    public String getDescription() {
        return description;
    }
    
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