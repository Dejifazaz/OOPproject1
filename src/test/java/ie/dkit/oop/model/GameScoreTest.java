package ie.dkit.oop.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

class GameScoreTest {
    
    private GameScore validScore;
    private LocalDate testDate;
    private LocalDateTime testDateTime;
    
    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 10, 15);
        testDateTime = LocalDateTime.of(2024, 10, 15, 14, 30, 0);
        validScore = new GameScore("Alice", "ALICE001", 50000, 85.5, true, testDate, testDateTime);
    }
    
    @Test
    void testValidConstruction() {
        assertNotNull(validScore);
        assertEquals("Alice", validScore.getPlayerName());
        assertEquals("ALICE001", validScore.getPlayerId());
        assertEquals(50000, validScore.getScore());
        assertEquals(85.5, validScore.getAccuracy(), 0.01);
        assertTrue(validScore.isVerified());
    }
    
    @Test
    void testPlayerNameValidation_Null() {
        assertThrows(NullPointerException.class, () -> {
            new GameScore(null, "ID001", 1000, 50.0, true, testDate, testDateTime);
        });
    }
    
    @Test
    void testPlayerNameValidation_Empty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GameScore("   ", "ID001", 1000, 50.0, true, testDate, testDateTime);
        });
    }
    
    @Test
    void testPlayerNameTrimming() {
        GameScore score = new GameScore("  Bob  ", "BOB001", 1000, 50.0, true, testDate, testDateTime);
        assertEquals("Bob", score.getPlayerName());
    }
    
    @Test
    void testScoreValidation_Negative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GameScore("Alice", "ID001", -1, 50.0, true, testDate, testDateTime);
        });
    }
    
    @Test
    void testScoreValidation_TooHigh() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GameScore("Alice", "ID001", 1000001, 50.0, true, testDate, testDateTime);
        });
    }
    
    @Test
    void testAccuracyValidation_Negative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GameScore("Alice", "ID001", 1000, -0.1, true, testDate, testDateTime);
        });
    }
    
    @Test
    void testAccuracyValidation_TooHigh() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GameScore("Alice", "ID001", 1000, 100.1, true, testDate, testDateTime);
        });
    }
    
    @Test
    void testCompareTo_HigherScore() {
        GameScore highScore = new GameScore("Bob", "BOB001", 60000, 80.0, true, testDate, testDateTime);
        assertTrue(validScore.compareTo(highScore) > 0);
    }
    
    @Test
    void testCompareTo_LowerScore() {
        GameScore lowScore = new GameScore("Bob", "BOB001", 40000, 80.0, true, testDate, testDateTime);
        assertTrue(validScore.compareTo(lowScore) < 0);
    }
    
    @Test
    void testCompareTo_Null() {
        assertThrows(NullPointerException.class, () -> {
            validScore.compareTo(null);
        });
    }
    
    @Test
    void testIsTopTier_True() {
        GameScore topTier = new GameScore("Champ", "CHAMP1", 950000, 95.0, true, testDate, testDateTime);
        assertTrue(topTier.isTopTier());
    }
    
    @Test
    void testIsTopTier_False() {
        assertFalse(validScore.isTopTier());
    }
}
