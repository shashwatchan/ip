package noga;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class DeadlineTest {
    
    @Test
    public void testDeadlineCreationAndRetrieval() {
        String description = "Submit homework";
        String deadlineStr = "2024-03-20 1430";
        
        Deadline deadline = new Deadline(description, deadlineStr);
        LocalDateTime expectedDateTime = LocalDateTime.of(2024, 3, 20, 14, 30);
        
        assertEquals(expectedDateTime, deadline.getBy());
        assertEquals(description, deadline.getDescription());
    }
    
    @Test
    public void testInvalidDeadlineFormat() {
        assertThrows(DateTimeParseException.class, () -> {
            new Deadline("Test task", "2024/03/20 2:30 PM");
        });
    }
}
