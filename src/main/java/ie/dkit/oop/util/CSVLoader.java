package ie.dkit.oop.util;

import ie.dkit.oop.model.GameScore;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVLoader {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    public static List<GameScore> loadFromCSV(String filePath) throws IOException {
        List<GameScore> scores = new ArrayList<>();
        File file = new File(filePath);
        
        if (!file.exists()) {
            throw new FileNotFoundException("File does not exist: " + filePath);
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            
            if (line != null && (line.trim().isEmpty() || isHeader(line))) {
                line = reader.readLine();
            }
            
            int lineNumber = 1;
            int validCount = 0;
            int invalidCount = 0;
            
            while (line != null) {
                lineNumber++;
                
                if (line.trim().isEmpty()) {
                    System.out.println("Line " + lineNumber + ": Empty line skipped");
                    invalidCount++;
                    line = reader.readLine();
                    continue;
                }
                
                try {
                    GameScore score = parseLine(line, lineNumber);
                    scores.add(score);
                    validCount++;
                } catch (Exception e) {
                    System.out.println("Line " + lineNumber + ": Failed to parse - " + e.getMessage());
                    invalidCount++;
                }
                
                line = reader.readLine();
            }
            
            System.out.println("Loaded " + validCount + " valid records, " + invalidCount + " invalid records");
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException caught. The file " + filePath + " may not exist. " + e);
            throw e;
        }
        
        return scores;
    }
    
    private static boolean isHeader(String line) {
        String lower = line.toLowerCase().trim();
        return lower.contains("player") || lower.contains("name") || lower.contains("score");
    }
    
    private static GameScore parseLine(String line, int lineNumber) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Line is empty");
        }
        
        String[] parts = line.split(",", -1);
        
        if (parts.length != 7) {
            throw new IllegalArgumentException("Expected 7 columns, got " + parts.length);
        }
        
        String playerName = trimField(parts[0], "playerName", lineNumber);
        String playerId = trimField(parts[1], "playerId", lineNumber);
        int score = parseInteger(parts[2], "score", lineNumber);
        double accuracy = parseDouble(parts[3], "accuracy", lineNumber);
        boolean verified = parseBoolean(parts[4], "verified", lineNumber);
        LocalDate gameDate = parseDate(parts[5], "gameDate", lineNumber);
        LocalDateTime sessionTimestamp = parseDateTime(parts[6], "sessionTimestamp", lineNumber);
        
        return new GameScore(playerName, playerId, score, accuracy, verified, gameDate, sessionTimestamp);
    }
    
    private static String trimField(String field, String fieldName, int lineNumber) {
        if (field == null) {
            throw new IllegalArgumentException("Line " + lineNumber + ": " + fieldName + " is null");
        }
        String trimmed = field.trim();
        if (trimmed.isEmpty() && (fieldName.equals("playerName") || fieldName.equals("playerId"))) {
            throw new IllegalArgumentException("Line " + lineNumber + ": " + fieldName + " cannot be empty");
        }
        return trimmed;
    }
    
    private static int parseInteger(String field, String fieldName, int lineNumber) {
        try {
            return Integer.parseInt(field.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Line " + lineNumber + ": " + fieldName + " must be a valid integer (got: '" + field + "')");
        }
    }
    
    private static double parseDouble(String field, String fieldName, int lineNumber) {
        try {
            return Double.parseDouble(field.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Line " + lineNumber + ": " + fieldName + " must be a valid number (got: '" + field + "')");
        }
    }
    
    private static boolean parseBoolean(String field, String fieldName, int lineNumber) {
        String trimmed = field.trim().toLowerCase();
        if (trimmed.equals("true") || trimmed.equals("1") || trimmed.equals("yes")) {
            return true;
        }
        if (trimmed.equals("false") || trimmed.equals("0") || trimmed.equals("no") || trimmed.isEmpty()) {
            return false;
        }
        throw new IllegalArgumentException("Line " + lineNumber + ": " + fieldName + " must be true/false/1/0/yes/no (got: '" + field + "')");
    }
    
    private static LocalDate parseDate(String field, String fieldName, int lineNumber) {
        try {
            return LocalDate.parse(field.trim(), DATE_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("Line " + lineNumber + ": " + fieldName + " must be in ISO-8601 format (YYYY-MM-DD) (got: '" + field + "')");
        }
    }
    
    private static LocalDateTime parseDateTime(String field, String fieldName, int lineNumber) {
        try {
            return LocalDateTime.parse(field.trim(), DATETIME_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("Line " + lineNumber + ": " + fieldName + " must be in ISO-8601 format (YYYY-MM-DDTHH:mm:ss) (got: '" + field + "')");
        }
    }
}
