# Game Leaderboard System - Stage 1

**Module:** COMP C8Z03 (Object-Oriented Programming)  
**Stage:** 1 - Design & Prototype  
**Authors:** [Your Names Here]  
**Student IDs:** [Your Student IDs Here]

---

## Solution Domain

The Game Leaderboard System is a Java application that manages game score entries for competitive gaming platforms. The system tracks individual game sessions where players achieve scores with accuracy metrics, verification status, and timestamps.

The main entity is GameScore which stores player information like name and ID, performance metrics like score and accuracy percentage, whether the score is verified, and when the game was played. The system can load data from CSV files, sort scores in different ways, and generate reports.

The application handles real-world data quality issues by logging errors and skipping invalid records without crashing. It demonstrates OOP principles like encapsulation, validation, and using Java Collections for data management.

---

## Design Justification

### Entity Design

The GameScore class has seven fields as required: two String fields (playerName, playerId), one int (score), one double (accuracy), one boolean (verified), one LocalDate (gameDate), and one LocalDateTime (sessionTimestamp). All fields are private with getters and setters that check the data is valid.

The playerName and playerId provide identification. The score is the main performance number from 0 to 1,000,000. The accuracy is a percentage from 0.0 to 100.0. The verified flag shows if a score has been checked. The gameDate and sessionTimestamp allow sorting by time.

Validation happens in the setters and constructor. String fields are trimmed and checked for length. Numbers are checked to be in the right range. Dates can't be in the future and the timestamp must match the date. This prevents bad data from getting into the system.

### Data Structure Choice: ArrayList

We chose ArrayList over LinkedList because:

1. The application mostly does random access (getting items by index) for sorting and searching. ArrayList gives O(1) random access while LinkedList is O(n).

2. Iterating through ArrayList is faster because the data is stored together in memory, which helps with cache performance.

3. ArrayList uses less memory per item since it doesn't need pointers like LinkedList.

4. We rarely add or remove items in the middle of the list. Most operations are adding at the end, sorting, and iterating. ArrayList handles these well.

5. The Collections.sort() and Collections.binarySearch() methods work efficiently with ArrayList.

ArrayList fits our needs better because we read data more than we modify it, and we need fast sorting and searching.

### Ordering Implementation

The GameScore class implements Comparable with descending score order as the natural ordering. Higher scores come first, and if scores are equal, player names are used as a tiebreaker. This makes sense for a leaderboard where you want the best scores at the top.

The AccuracyComparator provides a different way to sort by accuracy (descending), with score as a tiebreaker. This lets you find players who are accurate even if they don't have the highest scores.

Both implementations handle nulls properly and follow the comparator rules.

### CSV Loading Strategy

The CSVLoader class loads data from CSV files with error handling. Each row is parsed separately, so if one row is bad, it doesn't stop the rest from loading. Invalid rows are logged and skipped. The loader automatically detects and skips header rows. It uses helper methods to parse each type of field with clear error messages.

This approach lets the system work with real CSV files that might have some bad data, which is important for real applications.

### DRY Principles

Repeated code is put into helper methods. The CSVLoader uses parseInteger(), parseDouble(), parseBoolean(), parseDate(), and parseDateTime() methods to avoid copying the same parsing code. The LeaderboardApp uses printTopN() to format output consistently. Validation limits are defined once in GameScore and reused.

This makes the code easier to maintain and ensures everything works the same way.

---

## Defensive Coding Examples

### Example 1: Null Checks

**Location:** GameScore.java setters

```java
public void setPlayerName(String playerName) {
    if (playerName == null) {
        throw new NullPointerException("Player name cannot be null");
    }
    String trimmed = playerName.trim();
    // ... validation continues
}
```

This checks for null before doing anything else. It stops the program early with a clear error message if the data is null. All setters use this pattern.

### Example 2: Range Validation

**Location:** GameScore.setScore()

```java
public void setScore(int score) {
    if (score < 0 || score > 1000000) {
        throw new IllegalArgumentException("Score must be between 0 and 1000000");
    }
    this.score = score;
}
```

This checks the score is in the valid range before storing it. The error message says what the range should be and what value was given, which helps with debugging.

### Example 3: Date Consistency Check

**Location:** GameScore.setSessionTimestamp()

```java
public void setSessionTimestamp(LocalDateTime sessionTimestamp) {
    if (sessionTimestamp == null) {
        throw new NullPointerException("Session timestamp cannot be null");
    }
    if (!sessionTimestamp.toLocalDate().equals(gameDate)) {
        throw new IllegalArgumentException("Session timestamp date must match game date");
    }
    this.sessionTimestamp = sessionTimestamp;
}
```

This makes sure the timestamp date matches the game date. This prevents logical errors that could cause problems when querying data later.

### Example 4: CSV Error Handling

**Location:** CSVLoader.loadFromCSV()

```java
try {
    GameScore score = parseLine(line, lineNumber);
    scores.add(score);
    validCount++;
} catch (Exception e) {
    System.out.println("Line " + lineNumber + ": Failed to parse - " + e.getMessage());
    invalidCount++;
}
```

Each row is parsed separately. If one row fails, it doesn't stop the rest from loading. Errors are printed so you can see what went wrong. This is important when loading files with mixed quality data.

### Example 5: Safe Iterator Removal

**Location:** LeaderboardApp.demonstrateSafeRemoval()

```java
Iterator<GameScore> iterator = copy.iterator();
while (iterator.hasNext()) {
    GameScore score = iterator.next();
    if (!score.isVerified()) {
        iterator.remove();
        removedCount++;
    }
}
```

Using the iterator's remove() method is the safe way to remove items while iterating. Using a regular for loop would cause a ConcurrentModificationException. This shows understanding of how to modify collections during iteration.

### Example 6: Column Count Validation

**Location:** CSVLoader.parseLine()

```java
String[] parts = line.split(",", -1);
if (parts.length != 7) {
    throw new IllegalArgumentException("Expected 7 columns, got " + parts.length);
}
```

This checks the row has the right number of columns before trying to parse them. Finding problems early prevents confusing errors later. The -1 in split() keeps empty fields at the end, which helps detect missing data.

---

## Commit Contributions

| Contributor | Commits | Areas of Work |
|------------|---------|---------------|
| [Name 1] | [Count] | Entity design, validation, CSV loader |
| [Name 2] | [Count] | Tests, comparator, documentation |

---

## Reflection

### Student 1 Reflection

[100-150 words about what you learned, challenges you faced, and insights from implementing Stage 1. Talk about OOP concepts you used, design decisions, and what you'd improve.]

### Student 2 Reflection

[100-150 words about what you learned, challenges you faced, and insights from implementing Stage 1. Talk about OOP concepts you used, design decisions, and what you'd improve.]

---

## Running the Application

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Build and Run

```bash
mvn compile
mvn test
mvn exec:java -Dexec.mainClass="ie.dkit.oop.LeaderboardApp"
```

### Project Structure

```
.
├── data/
│   └── sample_10.csv
├── src/
│   ├── main/java/ie/dkit/oop/
│   │   ├── model/
│   │   │   └── GameScore.java
│   │   ├── util/
│   │   │   └── CSVLoader.java
│   │   ├── comparator/
│   │   │   └── AccuracyComparator.java
│   │   └── LeaderboardApp.java
│   └── test/java/ie/dkit/oop/
│       ├── model/
│       │   └── GameScoreTest.java
│       ├── comparator/
│       │   └── AccuracyComparatorTest.java
│       └── util/
│           └── CSVLoaderTest.java
├── pom.xml
└── README.md
```

---

## Test Coverage

Current test coverage: ≥35% (Stage 1 requirement)

Run `mvn test jacoco:report` to generate coverage report at `target/site/jacoco/index.html`

---

## Generative AI Usage Declaration

[Declare if you used any AI tools and what for. If none, state "No Generative AI tools were used."]

---

## References

[Add Harvard-style references if any. If none, state "No external references."]
