package com.svalero.dungeonescape.entities.npc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Skeleton extends NPC {

    private float speed = 80f;
    private float leftBound, rightBound;
    private boolean movingRight = true;

    public Skeleton(float x, float y, float leftBound, float rightBound) {
        super(x, y, 32, 48, 60, 100);
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    @Override
    public void update(float delta, float playerX, float playerY) {
        if (!alive) return;

        // Patrulla entre leftBound y rightBound
        if (movingRight) {
            x += speed * delta;
            if (x >= rightBound) movingRight = false;
        } else {
            x -= speed * delta;
            if (x <= leftBound) movingRight = true;
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (!alive) return;
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(x, y, width, height);

        // Barra de vida
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y + height + 4, width, 4);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x, y + height + 4, width * ((float) health / maxHealth), 4);
    }
}
