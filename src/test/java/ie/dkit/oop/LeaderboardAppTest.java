package ie.dkit.oop;

import ie.dkit.oop.model.GameScore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

class LeaderboardAppTest {

    @TempDir
    Path tempDir;

    @Test
    void testLoadScoreDataFromFile() throws IOException {
        Path csvFile = tempDir.resolve("test.csv");
        String csvContent = "Alice,ALICE001,50000,85.5,true,2024-10-15,2024-10-15T14:30:00\n" +
                           "Bob,BOB002,45000,90.0,false,2024-10-14,2024-10-14T10:15:00\n";
        Files.write(csvFile, csvContent.getBytes());

        ArrayList<GameScore> scoreList = new ArrayList<>();
        LeaderboardApp.loadScoreDataFromFile(scoreList, csvFile.toString());

        assertEquals(2, scoreList.size());
    }

    @Test
    void testDisplayAllScores() {
        ArrayList<GameScore> scoreList = new ArrayList<>();
        LocalDate date = LocalDate.of(2024, 10, 15);
        LocalDateTime dateTime = LocalDateTime.of(2024, 10, 15, 14, 30, 0);
        scoreList.add(new GameScore("Alice", "ALICE001", 50000, 85.5, true, date, dateTime));
        scoreList.add(new GameScore("Bob", "BOB002", 45000, 90.0, false, date, dateTime));

        assertDoesNotThrow(() -> LeaderboardApp.displayAllScores(scoreList));
    }

    @Test
    void testDemonstrateOrdering() {
        ArrayList<GameScore> scoreList = new ArrayList<>();
        LocalDate date = LocalDate.of(2024, 10, 15);
        LocalDateTime dateTime = LocalDateTime.of(2024, 10, 15, 14, 30, 0);
        scoreList.add(new GameScore("Alice", "ALICE001", 50000, 85.5, true, date, dateTime));
        scoreList.add(new GameScore("Bob", "BOB002", 60000, 90.0, false, date, dateTime));

        assertDoesNotThrow(() -> LeaderboardApp.demonstrateOrdering(scoreList));
    }

    @Test
    void testDemonstrateSafeRemoval() {
        ArrayList<GameScore> scoreList = new ArrayList<>();
        LocalDate date = LocalDate.of(2024, 10, 15);
        LocalDateTime dateTime = LocalDateTime.of(2024, 10, 15, 14, 30, 0);
        scoreList.add(new GameScore("Alice", "ALICE001", 50000, 85.5, true, date, dateTime));
        scoreList.add(new GameScore("Bob", "BOB002", 45000, 90.0, false, date, dateTime));

        assertDoesNotThrow(() -> LeaderboardApp.demonstrateSafeRemoval(scoreList));
    }

    @Test
    void testLoadScoreDataFromFileWithInvalidFile() {
        ArrayList<GameScore> scoreList = new ArrayList<>();
        assertDoesNotThrow(() -> LeaderboardApp.loadScoreDataFromFile(scoreList, "nonexistent.csv"));
        assertEquals(0, scoreList.size());
    }

    @Test
    void testDemonstrateStage2Features() throws IOException {
        Path csvFile = tempDir.resolve("test_stage2.csv");
        String csvContent = "Alice,ALICE001,50000,85.5,true,2024-10-15,2024-10-15T14:30:00\n" +
                           "Bob,BOB002,60000,90.0,false,2024-10-14,2024-10-14T10:15:00\n" +
                           "Alice,ALICE001,50000,85.5,true,2024-10-15,2024-10-15T14:30:00\n";
        Files.write(csvFile, csvContent.getBytes());

        ArrayList<GameScore> scoreList = new ArrayList<>();
        LeaderboardApp.loadScoreDataFromFile(scoreList, csvFile.toString());

        assertDoesNotThrow(() -> LeaderboardApp.demonstrateStage2Features(scoreList));
    }
}

