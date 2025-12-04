package ie.dkit.oop.comparator;

import ie.dkit.oop.model.GameScore;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

class AccuracyCompTest {
    
    private AccuracyComp comp;
    private LocalDate testDate;
    private LocalDateTime testDateTime;
    
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        comp = new AccuracyComp();
        testDate = LocalDate.of(2024, 10, 15);
        testDateTime = LocalDateTime.of(2024, 10, 15, 14, 30, 0);
    }
    
    @Test
    void testCompareHigherAccuracy() {
        GameScore high = new GameScore("Alice", "ALICE1", 50000, 95.0, true, testDate, testDateTime);
        GameScore low = new GameScore("Bob", "BOB001", 50000, 80.0, true, testDate, testDateTime);
        
        assertTrue(comp.compare(high, low) < 0);
    }
    
    @Test
    void testCompareLowerAccuracy() {
        GameScore low = new GameScore("Alice", "ALICE1", 50000, 80.0, true, testDate, testDateTime);
        GameScore high = new GameScore("Bob", "BOB001", 50000, 95.0, true, testDate, testDateTime);
        
        assertTrue(comp.compare(low, high) > 0);
    }
    
    @Test
    void testCompareSameAccuracyDifferentScore() {
        GameScore highScore = new GameScore("Alice", "ALICE1", 60000, 85.0, true, testDate, testDateTime);
        GameScore lowScore = new GameScore("Bob", "BOB001", 40000, 85.0, true, testDate, testDateTime);
        
        assertTrue(comp.compare(highScore, lowScore) < 0);
    }
    
    @Test
    void testCompareNull() {
        GameScore score = new GameScore("Alice", "ALICE1", 50000, 85.0, true, testDate, testDateTime);
        
        assertThrows(NullPointerException.class, () -> {
            comp.compare(score, null);
        });
    }
}

