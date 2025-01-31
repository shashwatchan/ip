package noga;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task that occurs during a specific time period.
 * Extends the basic Task class with start and end times.
 */
public class Event extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = 
            DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /**
     * Creates a new Event task with the given description and time period.
     *
     * @param description the description of the event
     * @param startTime the start time in format "yyyy-MM-dd HHmm"
     * @param endTime the end time in format "yyyy-MM-dd HHmm"
     * @throws DateTimeParseException if either time string cannot be parsed
     */
    public Event(String description, String startTime, String endTime) 
            throws DateTimeParseException {
        super(description);
        this.startTime = LocalDateTime.parse(startTime, INPUT_FORMAT);
        this.endTime = LocalDateTime.parse(endTime, INPUT_FORMAT);
    }

    /**
     * Gets the start time of this event.
     *
     * @return the start time as a LocalDateTime
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of this event.
     *
     * @return the end time as a LocalDateTime
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    protected char getTypeIcon() {
        return 'E';
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() 
                + " (from: " + startTime.format(OUTPUT_FORMAT)
                + " to: " + endTime.format(OUTPUT_FORMAT) + ")";
    }

    /**
     * Converts this event task to a string format for file storage.
     *
     * @return the task in storage format
     */
    public String toFileString() {
        return String.format("E | %d | %s | %s | %s",
                isDone() ? 1 : 0,
                getDescription(),
                startTime.format(INPUT_FORMAT),
                endTime.format(INPUT_FORMAT));
    }
} 