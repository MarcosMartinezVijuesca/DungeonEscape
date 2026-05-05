package com.svalero.dungeonescape.managers;

public class GameState {

    private static GameState instance;

    private int score;
    private int lives;
    private int currentLevel;
    private boolean soundEnabled;
    private String playerName;

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
        lives = 3;
        currentLevel = 1;
        playerName = "";
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
}
