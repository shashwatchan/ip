import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task that needs to be done before a specific date/time.
 * Extends the basic Task class with a deadline.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");
    private LocalDateTime by;

    /**
     * Creates a new Deadline task with the given description and deadline.
     *
     * @param description the description of the deadline task
     * @param by the deadline date/time in format "yyyy-MM-dd HHmm"
     * @throws DateTimeParseException if the deadline string cannot be parsed
     */
    public Deadline(String description, String by) throws DateTimeParseException {
        super(description);
        this.by = LocalDateTime.parse(by, INPUT_FORMAT);
    }
    
    /**
     * Gets the deadline of this task.
     *
     * @return the deadline as a LocalDateTime
     */
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

    /**
     * Converts this deadline task to a string format for file storage.
     *
     * @return the task in storage format
     */
    public String toFileString() {
        return String.format("D | %d | %s | %s",
                isDone() ? 1 : 0,
                getDescription(),
                by.format(INPUT_FORMAT));
    }
} 