package ie.dkit.oop.comparator;

import ie.dkit.oop.comparator.AccuracyComparator;
import ie.dkit.oop.model.GameScore;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

class AccuracyComparatorTest {
    
    private AccuracyComparator comparator;
    private LocalDate testDate;
    private LocalDateTime testDateTime;
    
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        comparator = new AccuracyComparator();
        testDate = LocalDate.of(2024, 10, 15);
        testDateTime = LocalDateTime.of(2024, 10, 15, 14, 30, 0);
    }
    
    @Test
    void testCompare_HigherAccuracy() {
        GameScore highAccuracy = new GameScore("Alice", "ALICE1", 50000, 95.0, true, testDate, testDateTime);
        GameScore lowAccuracy = new GameScore("Bob", "BOB001", 50000, 80.0, true, testDate, testDateTime);
        
        assertTrue(comparator.compare(highAccuracy, lowAccuracy) < 0);
    }
    
    @Test
    void testCompare_LowerAccuracy() {
        GameScore lowAccuracy = new GameScore("Alice", "ALICE1", 50000, 80.0, true, testDate, testDateTime);
        GameScore highAccuracy = new GameScore("Bob", "BOB001", 50000, 95.0, true, testDate, testDateTime);
        
        assertTrue(comparator.compare(lowAccuracy, highAccuracy) > 0);
    }
    
    @Test
    void testCompare_SameAccuracy_DifferentScore() {
        GameScore highScore = new GameScore("Alice", "ALICE1", 60000, 85.0, true, testDate, testDateTime);
        GameScore lowScore = new GameScore("Bob", "BOB001", 40000, 85.0, true, testDate, testDateTime);
        
        assertTrue(comparator.compare(highScore, lowScore) < 0);
    }
    
    @Test
    void testCompare_Null() {
        GameScore score = new GameScore("Alice", "ALICE1", 50000, 85.0, true, testDate, testDateTime);
        
        assertThrows(NullPointerException.class, () -> {
            comparator.compare(score, null);
        });
    }
}
