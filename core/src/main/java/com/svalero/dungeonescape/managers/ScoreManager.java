package com.svalero.dungeonescape.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreManager {

    private static final String SCORES_FILE = "scores.txt";
    private static final int MAX_SCORES = 10;

    public static class ScoreEntry {
        public String name;
        public int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }

    public static List<ScoreEntry> loadScores() {
        List<ScoreEntry> scores = new ArrayList<>();
        FileHandle file = Gdx.files.local(SCORES_FILE);
        if (!file.exists()) return scores;

        String content = file.readString();
        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length == 2) {
                try {
                    scores.add(new ScoreEntry(parts[0], Integer.parseInt(parts[1])));
                } catch (NumberFormatException ignored) {}
            }
        }
        return scores;
    }

    public static void saveScore(String name, int score) {
        List<ScoreEntry> scores = loadScores();
        scores.add(new ScoreEntry(name, score));
        scores.sort(Comparator.comparingInt((ScoreEntry e) -> e.score).reversed());
        if (scores.size() > MAX_SCORES) {
            scores = scores.subList(0, MAX_SCORES);
        }

        StringBuilder sb = new StringBuilder();
        for (ScoreEntry entry : scores) {
            sb.append(entry.name).append(",").append(entry.score).append("\n");
        }
        Gdx.files.local(SCORES_FILE).writeString(sb.toString(), false);
    }
}
