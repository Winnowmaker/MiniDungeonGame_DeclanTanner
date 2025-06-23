package dungeon.engine;

import java.io.*;
import java.util.*;

public class HighScoreManager
{
    private static final String HIGH_SCORES_FILE = "high_scores.dat";
    private static final int MAX_HIGH_SCORES = 5;
    private List<ScoreEntry> highScores;

    public HighScoreManager()
    {
        highScores = new ArrayList<>();
        loadHighScores();
    }

    public static class ScoreEntry implements Serializable
    {
        private static final long serialVersionUID = 1L;
        private final String playerName;
        private final int score;
        private final String date;

        public ScoreEntry(String playerName, int score)
        {
            this.playerName = playerName;
            this.score = score;
            this.date = new java.text.SimpleDateFormat("yyyy-MM-dd")
                           .format(new java.util.Date());
        }

        public String getPlayerName() { return playerName; }
        public int getScore() { return score; }
        public String getDate() { return date; }
    }

    public boolean isHighScore(int score)
    {
        return highScores.size() < MAX_HIGH_SCORES || 
               score > highScores.get(highScores.size() - 1).getScore();
    }

    public void addScore(String playerName, int score)
    {
        ScoreEntry newEntry = new ScoreEntry(playerName, score);
        highScores.add(newEntry);
        highScores.sort((a, b) -> b.getScore() - a.getScore());
        
        if (highScores.size() > MAX_HIGH_SCORES)
        {
            highScores = highScores.subList(0, MAX_HIGH_SCORES);
        }
        
        saveHighScores();
    }

    public List<ScoreEntry> getHighScores()
    {
        return new ArrayList<>(highScores);
    }

    private void loadHighScores()
    {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(HIGH_SCORES_FILE))) {
            highScores = (List<ScoreEntry>) ois.readObject();
        } catch (FileNotFoundException e) {
            highScores = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading high scores: " + e.getMessage());
            highScores = new ArrayList<>();
        }
    }

    private void saveHighScores()
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(HIGH_SCORES_FILE))) {
            oos.writeObject(highScores);
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }
}