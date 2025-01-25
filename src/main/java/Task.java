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
    
    @Override
    public String toString() {
        return String.format("[%c][%s] %s", 
            getTypeIcon(),
            isDone ? "X" : " ",
            description);
    }
    
    protected char getTypeIcon() {
        return 'T';
    }
}