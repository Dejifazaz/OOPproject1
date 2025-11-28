package ie.dkit.oop.service;

import ie.dkit.oop.comparator.AccuracyComp;
import ie.dkit.oop.model.GameScore;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Helper {

    public static List<GameScore> removeDups(List<GameScore> scores) {
        if (scores == null) {
            return new ArrayList<>();
        }
        Set<GameScore> unique = new HashSet<>(scores);
        return new ArrayList<>(unique);
    }

    public static Map<String, GameScore> bestScores(List<GameScore> scores) {
        Map<String, GameScore> best = new HashMap<>();
        for (GameScore s : scores) {
            GameScore existing = best.get(s.getPlayerId());
            if (existing == null || s.getScore() > existing.getScore()) {
                best.put(s.getPlayerId(), s);
            }
        }
        return best;
    }

    public static Map<String, Long> playerCounts(List<GameScore> scores) {
        Map<String, Long> freq = new HashMap<>();
        for (GameScore s : scores) {
            String id = s.getPlayerId();
            if (freq.containsKey(id)) {
                freq.put(id, freq.get(id) + 1);
            } else {
                freq.put(id, 1L);
            }
        }
        return freq;
    }

    public static List<GameScore> filterDates(List<GameScore> scores, LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Need start and end dates");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date can't be before start date");
        }
        List<GameScore> result = new ArrayList<>();
        for (GameScore s : scores) {
            LocalDate date = s.getGameDate();
            if (!date.isBefore(start) && !date.isAfter(end)) {
                result.add(s);
            }
        }
        Collections.sort(result);
        return result;
    }

    public static List<GameScore> topAccuracy(List<GameScore> scores, int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be at least 1");
        }
        ArrayList<GameScore> copy = new ArrayList<>(scores);
        Collections.sort(copy, new AccuracyComp());
        if (copy.size() > n) {
            return new ArrayList<>(copy.subList(0, n));
        }
        return copy;
    }

    public static List<GameScore> topScores(List<GameScore> scores, int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n must be at least 1");
        }
        ArrayList<GameScore> copy = new ArrayList<>(scores);
        Collections.sort(copy);
        if (copy.size() > n) {
            return new ArrayList<>(copy.subList(0, n));
        }
        return copy;
    }

    public static void exportCSV(List<GameScore> scores, String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        try {
            for (GameScore s : scores) {
                writer.write(s.getPlayerName() + "," +
                        s.getPlayerId() + "," +
                        s.getScore() + "," +
                        s.getAccuracy() + "," +
                        s.isVerified() + "," +
                        s.getGameDate().toString() + "," +
                        s.getSessionTimestamp().toString());
                writer.newLine();
            }
        } finally {
            writer.close();
        }
    }
}
