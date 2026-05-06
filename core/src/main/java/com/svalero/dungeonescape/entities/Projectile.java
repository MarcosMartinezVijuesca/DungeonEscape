package com.svalero.dungeonescape.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Projectile {

    public float x, y;
    public float width = 12, height = 8;
    private float speed;
    private boolean active = true;

    private float startX; // posición inicial para calcular distancia
    private static final float MAX_RANGE = 400f; // rango máximo del proyectil
    private static final int DAMAGE = 25;

    public Projectile(float x, float y, boolean goingRight) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.speed = goingRight ? 400f : -400f;
    }

    public void update(float delta) {
        if (!active) return;
        x += speed * delta;

        // Desactivar si supera el rango máximo
        if (Math.abs(x - startX) > MAX_RANGE) {
            active = false;
        }

        // Desactivar si sale del mapa
        if (x > 2500 || x < -50) {
            active = false;
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (!active) return;
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(x, y, width, height);
    }

    public boolean isActive() { return active; }
    public void deactivate() { active = false; }
    public int getDamage() { return DAMAGE; }

    public boolean overlaps(float ox, float oy, float ow, float oh) {
        return active &&
            x < ox + ow &&
            x + width > ox &&
            y < oy + oh &&
            y + height > oy;
    }
}
