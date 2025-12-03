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

public class App {
    
    public static void main(String[] args) {
        String file = "data/dataset_1000.csv";
        ArrayList<GameScore> list = new ArrayList<>();
        
        load(list, file);
        show(list);
        
        System.out.println("\n=== Ordering & Searching ===");
        showOrdering(list);
        
        System.out.println("\n=== Safe Removal ===");
        showRemoval(list);

        System.out.println("\n=== Stage 2 Enhancements ===");
        showStage2(list);
    }
    
    public static void load(ArrayList<GameScore> list, String file) {
        try {
            List<GameScore> scores = Loader.load(file);
            list.addAll(scores);
            System.out.println("Loaded " + list.size() + " records\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void show(ArrayList<GameScore> list) {
        System.out.println("--- First 10 scores ---");
        Iterator<GameScore> it = list.iterator();
        int count = 0;
        while (it.hasNext() && count < 10) {
            GameScore s = it.next();
            System.out.println(s);
            count++;
        }
        if (list.size() > count) {
            System.out.println("... (" + (list.size() - count) + " more)");
        }
    }
    
    public static void showOrdering(ArrayList<GameScore> list) {
        ArrayList<GameScore> sorted = new ArrayList<>(list);
        Collections.sort(sorted);
        
        System.out.println("1. Natural Order (by score, descending):");
        printTop(sorted, 5);
        
        ArrayList<GameScore> byAccuracy = new ArrayList<>(list);
        Collections.sort(byAccuracy, new AccuracyComp());
        
        System.out.println("\n2. Custom Order (by accuracy, descending):");
        printTop(byAccuracy, 5);
        
        System.out.println("\n3. Binary Search:");
        Collections.sort(sorted);
        if (!sorted.isEmpty()) {
            GameScore key = sorted.get(0);
            int idx = Collections.binarySearch(sorted, key);
            if (idx >= 0) {
                System.out.println("Found score at index " + idx + ": " + sorted.get(idx));
            } else {
                System.out.println("Score not found");
            }
        }
    }
    
    public static void showRemoval(ArrayList<GameScore> list) {
        ArrayList<GameScore> copy = new ArrayList<>(list);
        int startSize = copy.size();
        
        Iterator<GameScore> it = copy.iterator();
        int removed = 0;
        
        while (it.hasNext()) {
            GameScore s = it.next();
            if (!s.isVerified()) {
                it.remove();
                removed++;
            }
        }
        
        System.out.println("Removed " + removed + " unverified (from " + startSize + " to " + copy.size() + ")");
        System.out.println("Verified scores:");
        printTop(copy, copy.size());
    }
    
    private static void printTop(ArrayList<GameScore> scores, int n) {
        int count = Math.min(n, scores.size());
        for (int i = 0; i < count; i++) {
            GameScore s = scores.get(i);
            System.out.println((i + 1) + ". " + s.getPlayerName() + " (Score: " + s.getScore() + ", Accuracy: " + s.getAccuracy() + "%)");
        }
    }

    static void showStage2(ArrayList<GameScore> scores) {
        List<GameScore> unique = Helper.removeDups(scores);
        System.out.println("Removed duplicates: " + unique.size() + " unique from " + scores.size());

        Map<String, Long> freq = Helper.playerCounts(unique);
        System.out.println("Top 3 players:");
        List<Map.Entry<String, Long>> sorted = new ArrayList<>(freq.entrySet());
        Collections.sort(sorted, (a, b) -> Long.compare(b.getValue(), a.getValue()));
        for (int i = 0; i < Math.min(3, sorted.size()); i++) {
            Map.Entry<String, Long> e = sorted.get(i);
            System.out.println(e.getKey() + " -> " + e.getValue() + " sessions");
        }

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(60);
        List<GameScore> recent = Helper.filterDates(unique, start, end);
        System.out.println("Last 60 days: " + recent.size() + " scores");
        int show = Math.min(3, recent.size());
        printTop(new ArrayList<>(recent), show);

        List<GameScore> topAcc = Helper.topAccuracy(unique, 5);
        System.out.println("Top 5 by accuracy:");
        printTop(new ArrayList<>(topAcc), topAcc.size());

        List<GameScore> top = Helper.topScores(unique, 5);
        String path = "data/export_top_scores.csv";
        try {
            Helper.exportCSV(top, path);
            System.out.println("Exported to " + path);
        } catch (Exception e) {
            System.out.println("Export failed: " + e.getMessage());
        }
    }
}
