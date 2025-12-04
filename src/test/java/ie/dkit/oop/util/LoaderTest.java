package ie.dkit.oop.util;

import ie.dkit.oop.model.GameScore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class LoaderTest {
    
    @TempDir
    Path tempDir;
    
    @Test
    void testLoad() throws IOException {
        Path csvFile = tempDir.resolve("test.csv");
        String csvContent = "Alice,ALICE001,50000,85.5,true,2024-10-15,2024-10-15T14:30:00\n" +
                           "Bob,BOB002,45000,90.0,false,2024-10-14,2024-10-14T10:15:00\n";
        Files.write(csvFile, csvContent.getBytes());
        
        List<GameScore> scores = Loader.load(csvFile.toString());
        
        assertEquals(2, scores.size());
        assertEquals("Alice", scores.get(0).getPlayerName());
        assertEquals("Bob", scores.get(1).getPlayerName());
    }
    
    @Test
    void testLoadWithHeader() throws IOException {
        Path csvFile = tempDir.resolve("test_header.csv");
        String csvContent = "playerName,playerId,score,accuracy,verified,gameDate,sessionTimestamp\n" +
                           "Alice,ALICE001,50000,85.5,true,2024-10-15,2024-10-15T14:30:00\n";
        Files.write(csvFile, csvContent.getBytes());
        
        List<GameScore> scores = Loader.load(csvFile.toString());
        
        assertEquals(1, scores.size());
    }
    
    @Test
    void testLoadInvalidRow() throws IOException {
        Path csvFile = tempDir.resolve("test_invalid.csv");
        String csvContent = "Alice,ALICE001,50000,85.5,true,2024-10-15,2024-10-15T14:30:00\n" +
                           "Bob,BOB002,invalid,90.0,false,2024-10-14,2024-10-14T10:15:00\n" +
                           "Charlie,CHAR003,60000,95.0,true,2024-10-16,2024-10-16T16:45:00\n";
        Files.write(csvFile, csvContent.getBytes());
        
        List<GameScore> scores = Loader.load(csvFile.toString());
        
        assertEquals(2, scores.size());
    }
    
    @Test
    void testLoadFileNotFound() {
        assertThrows(IOException.class, () -> {
            Loader.load("nonexistent.csv");
        });
    }

    @Test
    void testLoadWithStats() throws IOException {
        Path csvFile = tempDir.resolve("stats.csv");
        String csvContent = "Alice,ALICE001,50000,85.5,true,2024-10-15,2024-10-15T14:30:00\n" +
                "InvalidRow\n";
        Files.write(csvFile, csvContent.getBytes());

        Loader.LoadResult result = Loader.loadWithStats(csvFile.toString());
        assertEquals(1, result.getValidCount());
        assertEquals(1, result.getInvalidCount());
        assertEquals(1, result.getRecords().size());
    }
}

