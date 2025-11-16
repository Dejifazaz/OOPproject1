package ie.dkit.oop;

import ie.dkit.oop.comparator.AccuracyComparator;
import ie.dkit.oop.model.GameScore;
import ie.dkit.oop.util.CSVLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LeaderboardApp {
    
    public LeaderboardApp() {
    }
    
    public static void main(String[] args) {
        String fileName = "data/sample_10.csv";
        ArrayList<GameScore> scoreList = new ArrayList<>();
        
        loadScoreDataFromFile(scoreList, fileName);
        displayAllScores(scoreList);
        
        System.out.println("\n=== Ordering & Searching ===");
        demonstrateOrdering(scoreList);
        
        System.out.println("\n=== Safe Removal ===");
        demonstrateSafeRemoval(scoreList);
    }
    
    public static void loadScoreDataFromFile(ArrayList<GameScore> scoreList, String fileName) {
        try {
            List<GameScore> scores = CSVLoader.loadFromCSV(fileName);
            scoreList.addAll(scores);
            System.out.println("Go...");
            System.out.println("Successfully loaded " + scoreList.size() + " valid score records.\n");
        } catch (Exception e) {
            System.out.println("Error loading CSV: " + e.getMessage());
        }
    }
    
    public static void displayAllScores(ArrayList<GameScore> scoreList) {
        System.out.println("--- All Scores ---");
        Iterator<GameScore> iterator = scoreList.iterator();
        
        while (iterator.hasNext()) {
            GameScore score = iterator.next();
            System.out.println(score);
        }
    }
    
    public static void demonstrateOrdering(ArrayList<GameScore> scoreList) {
        ArrayList<GameScore> sortedByScore = new ArrayList<>(scoreList);
        Collections.sort(sortedByScore);
        
        System.out.println("1. Natural Order (by score, descending):");
        printTopN(sortedByScore, 5);
        
        ArrayList<GameScore> sortedByAccuracy = new ArrayList<>(scoreList);
        Collections.sort(sortedByAccuracy, new AccuracyComparator());
        
        System.out.println("\n2. Custom Order (by accuracy, descending):");
        printTopN(sortedByAccuracy, 5);
        
        System.out.println("\n3. Binary Search:");
        Collections.sort(sortedByScore);
        if (!sortedByScore.isEmpty()) {
            GameScore searchKey = sortedByScore.get(0);
            int index = Collections.binarySearch(sortedByScore, searchKey);
            if (index >= 0) {
                System.out.println("Found score at index " + index + ": " + sortedByScore.get(index));
            } else {
                System.out.println("Score not found");
            }
        }
    }
    
    public static void demonstrateSafeRemoval(ArrayList<GameScore> scoreList) {
        ArrayList<GameScore> copy = new ArrayList<>(scoreList);
        int initialSize = copy.size();
        
        Iterator<GameScore> iterator = copy.iterator();
        int removedCount = 0;
        
        while (iterator.hasNext()) {
            GameScore score = iterator.next();
            if (!score.isVerified()) {
                iterator.remove();
                removedCount++;
            }
        }
        
        System.out.println("Removed " + removedCount + " unverified scores (from " + initialSize + " to " + copy.size() + ").");
        System.out.println("Remaining verified scores:");
        printTopN(copy, copy.size());
    }
    
    private static void printTopN(ArrayList<GameScore> scores, int n) {
        int count = Math.min(n, scores.size());
        for (int i = 0; i < count; i++) {
            GameScore score = scores.get(i);
            System.out.println((i + 1) + ". " + score.getPlayerName() + " (Score: " + score.getScore() + ", Accuracy: " + score.getAccuracy() + "%)");
        }
    }
}
