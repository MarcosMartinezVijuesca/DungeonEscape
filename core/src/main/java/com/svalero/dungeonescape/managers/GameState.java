package com.svalero.dungeonescape.managers;

public class GameState {

    private static GameState instance;

    private int score;
    private int lives;
    private int currentLevel;
    private boolean soundEnabled;
    private String playerName;
    private String difficulty = "Normal";

    private GameState() {
        reset();
        soundEnabled = true;
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public void reset() {
        score = 0;
        lives = 1;
        currentLevel = 1;
        playerName = "";
    }

    public float getDifficultyMultiplier() {
        switch (difficulty) {
            case "Facil":   return 0.7f;
            case "Dificil": return 1.5f;
            default:        return 1.0f; // Normal
        }
    }



    public int getScore() { return score; }
    public void addScore(int points) { this.score += points; }

    public int getLives() { return lives; }
    public void loseLife() { this.lives--; }

    public int getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(int level) { this.currentLevel = level; }

    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean enabled) { this.soundEnabled = enabled; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String name) { this.playerName = name; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

}
