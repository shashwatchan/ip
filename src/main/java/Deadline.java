import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");
    private LocalDateTime by;

    public Deadline(String description, String by) throws DateTimeParseException {
        super(description);
        this.by = LocalDateTime.parse(by, INPUT_FORMAT);
    }
    
    public LocalDateTime getBy() {
        return by;
    }
    
    @Override
    protected char getTypeIcon() {
        return 'D';
    }
    
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }

    public String toFileString() {
        return String.format("D | %d | %s | %s",
                isDone() ? 1 : 0,
                getDescription(),
                by.format(INPUT_FORMAT));
    }
} 