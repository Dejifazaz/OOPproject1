package ie.dkit.oop.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GameScore implements Comparable<GameScore> {
    
    private String playerName;
    private String playerId;
    private int score;
    private double accuracy;
    private boolean verified;
    private LocalDate gameDate;
    private LocalDateTime sessionTimestamp;
    
    public GameScore(String playerName, String playerId, int score, double accuracy,
                     boolean verified, LocalDate gameDate, LocalDateTime sessionTimestamp) {
        setPlayerName(playerName);
        setPlayerId(playerId);
        setScore(score);
        setAccuracy(accuracy);
        this.verified = verified;
        setGameDate(gameDate);
        setSessionTimestamp(sessionTimestamp);
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public String getPlayerId() {
        return playerId;
    }
    
    public int getScore() {
        return score;
    }
    
    public double getAccuracy() {
        return accuracy;
    }
    
    public boolean isVerified() {
        return verified;
    }
    
    public LocalDate getGameDate() {
        return gameDate;
    }
    
    public LocalDateTime getSessionTimestamp() {
        return sessionTimestamp;
    }
    
    public void setPlayerName(String playerName) {
        if (playerName == null) {
            throw new NullPointerException("Player name cannot be null");
        }
        String trimmed = playerName.trim();
        if (trimmed.length() < 1 || trimmed.length() > 50) {
            throw new IllegalArgumentException("Player name must be between 1 and 50 characters");
        }
        this.playerName = trimmed;
    }
    
    public void setPlayerId(String playerId) {
        if (playerId == null) {
            throw new NullPointerException("Player ID cannot be null");
        }
        String trimmed = playerId.trim();
        if (trimmed.length() < 3 || trimmed.length() > 20) {
            throw new IllegalArgumentException("Player ID must be between 3 and 20 characters");
        }
        if (!trimmed.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Player ID must contain only alphanumeric characters and underscores");
        }
        this.playerId = trimmed;
    }
    
    public void setScore(int score) {
        if (score < 0 || score > 1000000) {
            throw new IllegalArgumentException("Score must be between 0 and 1000000");
        }
        this.score = score;
    }
    
    public void setAccuracy(double accuracy) {
        if (Double.isNaN(accuracy) || Double.isInfinite(accuracy)) {
            throw new IllegalArgumentException("Accuracy cannot be NaN or infinite");
        }
        if (accuracy < 0.0 || accuracy > 100.0) {
            throw new IllegalArgumentException("Accuracy must be between 0.0 and 100.0");
        }
        this.accuracy = accuracy;
    }
    
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    
    public void setGameDate(LocalDate gameDate) {
        if (gameDate == null) {
            throw new NullPointerException("Game date cannot be null");
        }
        if (gameDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Game date cannot be in the future");
        }
        this.gameDate = gameDate;
    }
    
    public void setSessionTimestamp(LocalDateTime sessionTimestamp) {
        if (sessionTimestamp == null) {
            throw new NullPointerException("Session timestamp cannot be null");
        }
        if (!sessionTimestamp.toLocalDate().equals(gameDate)) {
            throw new IllegalArgumentException("Session timestamp date must match game date");
        }
        this.sessionTimestamp = sessionTimestamp;
    }
    
    @Override
    public int compareTo(GameScore other) {
        if (other == null) {
            throw new NullPointerException("Cannot compare to null");
        }
        int scoreCompare = Integer.compare(other.score, this.score);
        if (scoreCompare != 0) {
            return scoreCompare;
        }
        return this.playerName.compareToIgnoreCase(other.playerName);
    }
    
    public double calculatePerformanceRating() {
        double normalizedScore = (score / 1000000.0) * 100.0;
        return (normalizedScore * 0.7) + (accuracy * 0.3);
    }
    
    public boolean isTopTier() {
        return score >= 900000;
    }
    
    @Override
    public String toString() {
        return "GameScore{player='" + playerName + "', id='" + playerId + "', score=" + score + 
               ", accuracy=" + accuracy + "%, verified=" + verified + ", date=" + gameDate + "}";
    }
}
