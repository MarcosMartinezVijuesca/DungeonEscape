package com.svalero.dungeonescape.entities.npc;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class NPC {

    public float x, y;
    public float width, height;
    protected int health;
    protected int maxHealth;
    protected boolean alive = true;
    protected int scoreValue; // puntos que da al morir

    public NPC(float x, float y, float width, float height, int health, int scoreValue) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;
        this.maxHealth = health;
        this.scoreValue = scoreValue;
    }

    public abstract void update(float delta, float playerX, float playerY);
    public abstract void render(ShapeRenderer shapeRenderer);

    public void takeDamage(int damage) {
        if (!alive) return;
        health -= damage;
        if (health <= 0) {
            health = 0;
            alive = false;
        }
    }

    public boolean overlaps(float ox, float oy, float ow, float oh) {
        return alive &&
            x < ox + ow &&
            x + width > ox &&
            y < oy + oh &&
            y + height > oy;
    }

    public boolean isAlive() { return alive; }
    public int getScoreValue() { return scoreValue; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
}
