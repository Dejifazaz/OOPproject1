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

class AppTest {

    @TempDir
    Path tempDir;

    @Test
    void testLoad() throws IOException {
        Path csvFile = tempDir.resolve("test.csv");
        String csvContent = "Alice,ALICE001,50000,85.5,true,2024-10-15,2024-10-15T14:30:00\n" +
                           "Bob,BOB002,45000,90.0,false,2024-10-14,2024-10-14T10:15:00\n";
        Files.write(csvFile, csvContent.getBytes());

        ArrayList<GameScore> list = new ArrayList<>();
        App.load(list, csvFile.toString());

        assertEquals(2, list.size());
    }

    @Test
    void testShow() {
        ArrayList<GameScore> list = new ArrayList<>();
        LocalDate date = LocalDate.of(2024, 10, 15);
        LocalDateTime dateTime = LocalDateTime.of(2024, 10, 15, 14, 30, 0);
        list.add(new GameScore("Alice", "ALICE001", 50000, 85.5, true, date, dateTime));
        list.add(new GameScore("Bob", "BOB002", 45000, 90.0, false, date, dateTime));

        assertDoesNotThrow(() -> App.show(list));
    }

    @Test
    void testShowOrdering() {
        ArrayList<GameScore> list = new ArrayList<>();
        LocalDate date = LocalDate.of(2024, 10, 15);
        LocalDateTime dateTime = LocalDateTime.of(2024, 10, 15, 14, 30, 0);
        list.add(new GameScore("Alice", "ALICE001", 50000, 85.5, true, date, dateTime));
        list.add(new GameScore("Bob", "BOB002", 60000, 90.0, false, date, dateTime));

        assertDoesNotThrow(() -> App.showOrdering(list));
    }

    @Test
    void testShowRemoval() {
        ArrayList<GameScore> list = new ArrayList<>();
        LocalDate date = LocalDate.of(2024, 10, 15);
        LocalDateTime dateTime = LocalDateTime.of(2024, 10, 15, 14, 30, 0);
        list.add(new GameScore("Alice", "ALICE001", 50000, 85.5, true, date, dateTime));
        list.add(new GameScore("Bob", "BOB002", 45000, 90.0, false, date, dateTime));

        assertDoesNotThrow(() -> App.showRemoval(list));
    }

    @Test
    void testLoadWithInvalidFile() {
        ArrayList<GameScore> list = new ArrayList<>();
        assertDoesNotThrow(() -> App.load(list, "nonexistent.csv"));
        assertEquals(0, list.size());
    }

    @Test
    void testShowStage2() throws IOException {
        Path csvFile = tempDir.resolve("test_stage2.csv");
        String csvContent = "Alice,ALICE001,50000,85.5,true,2024-10-15,2024-10-15T14:30:00\n" +
                           "Bob,BOB002,60000,90.0,false,2024-10-14,2024-10-14T10:15:00\n" +
                           "Alice,ALICE001,50000,85.5,true,2024-10-15,2024-10-15T14:30:00\n";
        Files.write(csvFile, csvContent.getBytes());

        ArrayList<GameScore> list = new ArrayList<>();
        App.load(list, csvFile.toString());

        assertDoesNotThrow(() -> App.showStage2(list));
    }
}

