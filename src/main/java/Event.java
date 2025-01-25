public class Event extends Task {
    private String startTime;
    private String endTime;
    
    public Event(String description, String startTime, String endTime) {
        super(description);
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    @Override
    protected char getTypeIcon() {
        return 'E';
    }
    
    @Override
    public String toString() {
        return super.toString() + " (from: " + startTime + " to: " + endTime + ")";
    }
} 