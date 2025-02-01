package noga;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class EventTest {
    
    @Test
    public void testGetStartTime() {
        Event event = new Event("Meeting", "2024-03-20 1400", "2024-03-20 1600");
        LocalDateTime expectedStart = LocalDateTime.of(2024, 3, 20, 14, 0);
        assertEquals(expectedStart, event.getStartTime());
    }
    
    @Test
    public void testGetEndTime() {
        Event event = new Event("Meeting", "2024-03-20 1400", "2024-03-20 1600");
        LocalDateTime expectedEnd = LocalDateTime.of(2024, 3, 20, 16, 0);
        assertEquals(expectedEnd, event.getEndTime());
    }
    
    @Test
    public void testInvalidDateFormat() {
        assertThrows(DateTimeParseException.class, () -> 
            new Event("Meeting", "2024/03/20 1400", "2024-03-20 1600")
        );
    }
    
    @Test
    public void testGetTypeIcon() {
        Event event = new Event("Meeting", "2024-03-20 1400", "2024-03-20 1600");
        assertEquals('E', event.getTypeIcon());
    }
}
