public class Task {
    String description;
    boolean marked;
    public Task(String description){
        this.description = description;
        this.marked = false;
    }
    public String toString(){
        return "[" + (marked ? "X" : " ") + "] " + description;
    }
    public void mark(){
        marked = true;
    }
    public void unmark(){
        marked = false;
    }
}