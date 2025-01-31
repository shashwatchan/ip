import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = 
            DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Event(String description, String startTime, String endTime) 
            throws DateTimeParseException {
        super(description);
        this.startTime = LocalDateTime.parse(startTime, INPUT_FORMAT);
        this.endTime = LocalDateTime.parse(endTime, INPUT_FORMAT);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

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

    public String toFileString() {
        return String.format("E | %d | %s | %s | %s",
                isDone() ? 1 : 0,
                getDescription(),
                startTime.format(INPUT_FORMAT),
                endTime.format(INPUT_FORMAT));
    }
} 