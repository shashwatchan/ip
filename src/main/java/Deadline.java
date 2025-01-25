public class Deadline extends Task {
    private String by;
    
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }
    
    @Override
    protected char getTypeIcon() {
        return 'D';
    }
    
    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
} 