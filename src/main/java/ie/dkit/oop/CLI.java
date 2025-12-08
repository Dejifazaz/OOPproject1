package ie.dkit.oop;

import ie.dkit.oop.comparator.AccuracyComp;
import ie.dkit.oop.model.GameScore;
import ie.dkit.oop.service.Helper;
import ie.dkit.oop.util.Loader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CLI {
    private final ArrayList<GameScore> list = new ArrayList<>();

    public static void main(String[] args) {
        new CLI().run();
    }

    private void run() {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            printMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.print("Enter CSV path (e.g., data/sample_10.csv or data/dataset_1000.csv): ");
                    String path = sc.nextLine().trim();
                    try {
                        List<GameScore> scores = Loader.load(path);
                        list.clear();
                        list.addAll(scores);
                        System.out.println("Loaded " + list.size() + " records");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "2":
                    System.out.print("Enter N: ");
                    int n = readInt(sc);
                    printTop(list, n);
                    break;
                case "3":
                    ArrayList<GameScore> byScore = new ArrayList<>(list);
                    Collections.sort(byScore);
                    printTop(byScore, 5);
                    break;
                case "4":
                    ArrayList<GameScore> byAcc = new ArrayList<>(list);
                    Collections.sort(byAcc, new AccuracyComp());
                    printTop(byAcc, 5);
                    break;
                case "5":
                    List<GameScore> unique = Helper.removeDups(list);
                    System.out.println("Unique sessions: " + unique.size() + " from " + list.size());
                    Map<String, Long> freq = Helper.playerCounts(unique);
                    List<Map.Entry<String, Long>> s = new ArrayList<>(freq.entrySet());
                    Collections.sort(s, (a, b) -> Long.compare(b.getValue(), a.getValue()));
                    for (int i = 0; i < Math.min(3, s.size()); i++) {
                        Map.Entry<String, Long> e = s.get(i);
                        System.out.println(e.getKey() + " -> " + e.getValue());
                    }
                    break;
                case "6":
                    System.out.print("Export path (e.g., data/export_top_scores.csv): ");
                    String out = sc.nextLine().trim();
                    try {
                        List<GameScore> uniqueForExport = Helper.removeDups(list);
                        List<GameScore> topForExport = Helper.topScores(uniqueForExport, 5);
                        Helper.exportCSV(topForExport, out);
                        System.out.println("Exported to " + out);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== Menu ===");
        System.out.println("1) Load CSV");
        System.out.println("2) Show first N");
        System.out.println("3) Sort by score");
        System.out.println("4) Sort by accuracy");
        System.out.println("5) Unique sessions + top 3 players");
        System.out.println("6) Export top scores to CSV");
        System.out.println("0) Exit");
        System.out.print("Choice: ");
    }

    private int readInt(Scanner sc) {
        String s = sc.nextLine().trim();
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    private void printTop(ArrayList<GameScore> scores, int n) {
        int count = Math.min(n, scores.size());
        for (int i = 0; i < count; i++) {
            GameScore s = scores.get(i);
            System.out.println((i + 1) + ". " + s.getPlayerName() + " (Score: " + s.getScore() + ", Accuracy: " + s.getAccuracy() + "%)");
        }
    }
}
