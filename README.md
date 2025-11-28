Game Leaderboard System – Stage 1

Module: COMP C8Z03 (Object-Oriented Programming)
Stage: 1 – Design & Prototype
Authors: Ayodeji
Student ID: D00276269

Solution Domain

The Game Leaderboard System is a Java program designed to manage score entries for competitive gaming platforms. It records individual game sessions containing player information, performance metrics, verification status, and timestamps.

The core entity is GameScore, which stores a player's name and ID, their score, accuracy percentage, whether the score is verified, and when the game was played. The system can load data from CSV files, sort the scores in different orders, and generate summary reports.

The program also deals with real-world data issues such as inconsistent or missing values by printing clear error messages and skipping invalid rows. The design follows OOP principles including encapsulation, validation, modularity, and the use of Java Collections for efficient data handling.

Design Justification
Entity Design

The GameScore class contains seven required fields:

Strings: playerName, playerId

int: score

double: accuracy

boolean: verified

LocalDate: gameDate

LocalDateTime: sessionTimestamp

All fields are private and accessed through getters and setters with validation.
Validation includes:

Strings are trimmed and checked for length.

Scores must be between 0 and 1,000,000.

Accuracy must be between 0.0 and 100.0.

Dates cannot be set in the future.

The session timestamp must match the game date.

This prevents invalid data from entering the object and helps maintain consistency throughout the system.

Data Structure Choice: ArrayList

ArrayList was chosen instead of LinkedList for several reasons:

The program mainly needs fast random access for sorting and searching.

ArrayList provides better cache performance due to contiguous memory storage.

It uses less memory than LinkedList because it doesn’t require node pointers.

The common operations are adding at the end, sorting, and iteration.

Java’s built-in sorting and searching algorithms run efficiently on ArrayList.

Since the system reads far more than it modifies, ArrayList suits the workload best.

Ordering Implementation

The GameScore class implements Comparable to define the natural ordering of scores in descending order. Higher scores appear first. If two scores are equal, the comparison falls back on the player’s name.

The custom AccuracyComparator allows sorting by accuracy in descending order, using score as a tiebreaker. This provides an alternate view of the leaderboard.

Both comparators follow the required rules and handle null values safely.

CSV Loading Strategy

The CSVLoader class reads data from CSV files with error handling. Each line is processed individually. Invalid lines are skipped but recorded through console messages. Header detection is included automatically.

Helper methods break down the parsing of integers, doubles, booleans, and date/time values, each providing clear feedback when values are invalid.

This approach makes the loader robust against messy or inconsistent CSV data.

DRY Principles

To avoid code duplication:

Common parsing logic is written once in helper methods inside CSVLoader.

Validation rules in GameScore are centralised and reused.

Repeated output formatting in LeaderboardApp is handled through helper methods.

This improves maintainability and ensures consistent behaviour.

Defensive Coding Examples
1. Null Checks

Location: GameScore.setPlayerName()

public void setPlayerName(String playerName) {
    if (playerName == null) {
        throw new NullPointerException("Player name cannot be null");
    }
    String trimmed = playerName.trim();
    // further validation...
}

2. Range Validation

Location: GameScore.setScore()

public void setScore(int score) {
    if (score < 0 || score > 1000000) {
        throw new IllegalArgumentException("Score must be between 0 and 1000000");
    }
    this.score = score;
}

3. Date Consistency Check

Location: GameScore.setSessionTimestamp()

public void setSessionTimestamp(LocalDateTime sessionTimestamp) {
    if (sessionTimestamp == null) {
        throw new NullPointerException("Session timestamp cannot be null");
    }
    if (!sessionTimestamp.toLocalDate().equals(gameDate)) {
        throw new IllegalArgumentException("Session timestamp date must match game date");
    }
    this.sessionTimestamp = sessionTimestamp;
}

4. CSV Error Handling

Location: CSVLoader.loadFromCSV()

try {
    GameScore score = parseLine(line, lineNumber);
    scores.add(score);
    validCount++;
} catch (Exception e) {
    System.out.println("Line " + lineNumber + ": Failed to parse - " + e.getMessage());
    invalidCount++;
}

5. Safe Iterator Removal

Location: LeaderboardApp.demonstrateSafeRemoval()

Iterator<GameScore> iterator = copy.iterator();
while (iterator.hasNext()) {
    GameScore score = iterator.next();
    if (!score.isVerified()) {
        iterator.remove();
    }
}

6. Column Count Validation

Location: CSVLoader.parseLine()

String[] parts = line.split(",", -1);
if (parts.length != 7) {
    throw new IllegalArgumentException("Expected 7 columns, got " + parts.length);
}
