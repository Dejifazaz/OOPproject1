package ie.dkit.oop;

import ie.dkit.oop.comparator.AccuracyComp;
import ie.dkit.oop.model.GameScore;
import ie.dkit.oop.util.Loader;
import ie.dkit.oop.service.Helper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LeaderboardApp {
    
    public LeaderboardApp() {
    }
    
    public static void main(String[] args) {
        String fileName = "data/dataset_1000.csv";
        ArrayList<GameScore> scoreList = new ArrayList<>();
        
        loadScoreDataFromFile(scoreList, fileName);
        displayAllScores(scoreList);
        
        System.out.println("\n=== Ordering & Searching ===");
        demonstrateOrdering(scoreList);
        
        System.out.println("\n=== Safe Removal ===");
        demonstrateSafeRemoval(scoreList);

        System.out.println("\n=== Stage 2 Enhancements ===");
        demonstrateStage2Features(scoreList);
    }
    
    public static void loadScoreDataFromFile(ArrayList<GameScore> scoreList, String fileName) {
        try {
            List<GameScore> scores = Loader.load(fileName);
            scoreList.addAll(scores);
            System.out.println("Go...");
            System.out.println("Successfully loaded " + scoreList.size() + " valid score records.\n");
        } catch (Exception e) {
            System.out.println("Error loading CSV: " + e.getMessage());
        }
    }
    
    public static void displayAllScores(ArrayList<GameScore> scoreList) {
        System.out.println("--- Sample of Scores (first 10) ---");
        Iterator<GameScore> iterator = scoreList.iterator();
        int printed = 0;
        while (iterator.hasNext() && printed < 10) {
            GameScore score = iterator.next();
            System.out.println(score);
            printed++;
        }
        if (scoreList.size() > printed) {
            System.out.println("... (" + (scoreList.size() - printed) + " more not displayed)");
        }
    }
    
    public static void demonstrateOrdering(ArrayList<GameScore> scoreList) {
        ArrayList<GameScore> sortedByScore = new ArrayList<>(scoreList);
        Collections.sort(sortedByScore);
        
        System.out.println("1. Natural Order (by score, descending):");
        printTopN(sortedByScore, 5);
        
        ArrayList<GameScore> sortedByAccuracy = new ArrayList<>(scoreList);
        Collections.sort(sortedByAccuracy, new AccuracyComp());
        
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

    static void demonstrateStage2Features(ArrayList<GameScore> scores) {
        List<GameScore> deduped = Helper.removeDups(scores);
        System.out.println("Deduplicated using equals/hashCode: " + deduped.size() + " unique sessions from " + scores.size());

        Map<String, Long> freqByPlayer = Helper.playerCounts(deduped);
        System.out.println("Top 3 most active players:");
        List<Map.Entry<String, Long>> sorted = new ArrayList<>(freqByPlayer.entrySet());
        Collections.sort(sorted, (a, b) -> Long.compare(b.getValue(), a.getValue()));
        for (int i = 0; i < Math.min(3, sorted.size()); i++) {
            Map.Entry<String, Long> entry = sorted.get(i);
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " sessions");
        }

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(60);
        List<GameScore> recent = Helper.filterDates(deduped, start, end);
        System.out.println("Scores played in last 60 days: " + recent.size());
        int showCount = Math.min(3, recent.size());
        printTopN(new ArrayList<>(recent), showCount);

        List<GameScore> topAccuracy = Helper.topAccuracy(deduped, 5);
        System.out.println("Top 5 by accuracy:");
        printTopN(new ArrayList<>(topAccuracy), topAccuracy.size());

        List<GameScore> topScores = Helper.topScores(deduped, 5);
        String exportPath = "data/export_top_scores.csv";
        try {
            Helper.exportCSV(topScores, exportPath);
            System.out.println("Exported top scores to " + exportPath);
        } catch (Exception e) {
            System.out.println("Failed to export CSV: " + e.getMessage());
        }
    }
}
