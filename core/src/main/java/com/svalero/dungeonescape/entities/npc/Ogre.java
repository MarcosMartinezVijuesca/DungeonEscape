package com.svalero.dungeonescape.entities.npc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ogre extends NPC {

    private float speed = 60f;
    private static final float DETECTION_RANGE = 250f;
    private float leftBound, rightBound;
    private boolean movingRight = true;
    private boolean chasing = false;

    public Ogre(float x, float y, float leftBound, float rightBound) {
        super(x, y, 56, 72, 300, 500);
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    public Ogre(float x, float y, float leftBound, float rightBound, float multiplier) {
        super(x, y, 56, 72, (int)(300 * multiplier), 500);
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.speed = 60f * multiplier;
    }

    @Override
    public void update(float delta, float playerX, float playerY) {
        if (!alive) return;

        float distX = Math.abs(playerX - x);

        // Si detecta al jugador lo persigue
        if (distX < DETECTION_RANGE) {
            chasing = true;
        } else {
            chasing = false;
        }

        if (chasing) {
            if (playerX > x) {
                x += speed * 1.2f * delta; // antes era 1.5f
            } else {
                x -= speed * 1.2f * delta;
            }
        } else {
            // Patrulla normal
            if (movingRight) {
                x += speed * delta;
                if (x >= rightBound) movingRight = false;
            } else {
                x -= speed * delta;
                if (x <= leftBound) movingRight = true;
            }
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (!alive) return;

        // Cuerpo del ogro (más grande)
        shapeRenderer.setColor(chasing ? Color.RED : Color.OLIVE);
        shapeRenderer.rect(x, y, width, height);

        // Barra de vida
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y + height + 4, width, 6);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x, y + height + 4, width * ((float) health / maxHealth), 6);
    }

    public boolean isChasing() { return chasing; }
}
