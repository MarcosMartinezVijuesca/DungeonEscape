package com.svalero.dungeonescape.entities.npc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.List;

public class DarkMage extends NPC {

    private float shootTimer = 0f;
    private static final float SHOOT_INTERVAL = 2.5f;
    private static final float DETECTION_RANGE = 300f;

    // Proyectiles del mago [x, y, velocityX, active(1=si, 0=no)]
    private List<float[]> projectiles = new ArrayList<>();
    private static final float PROJ_SPEED = 150f;
    private static final float PROJ_WIDTH = 10f;
    private static final float PROJ_HEIGHT = 10f;

    public DarkMage(float x, float y) {
        super(x, y, 32, 48, 80, 200);
    }

    @Override
    public void update(float delta, float playerX, float playerY) {
        if (!alive) return;

        shootTimer += delta;

        // Detecta al jugador si está cerca y dispara
        float distX = Math.abs(playerX - x);
        if (distX < DETECTION_RANGE && shootTimer >= SHOOT_INTERVAL) {
            shootTimer = 0f;
            float direction = playerX > x ? PROJ_SPEED : -PROJ_SPEED;
            projectiles.add(new float[]{x + width / 2f, y + height / 2f, direction, 1f});
        }

        // Mover proyectiles y eliminar los inactivos o fuera de pantalla
        List<float[]> toRemove = new ArrayList<>();
        for (float[] proj : projectiles) {
            if (proj[3] == 0) {
                toRemove.add(proj);
                continue;
            }
            proj[0] += proj[2] * delta;
            if (proj[0] > 2500 || proj[0] < -50) toRemove.add(proj);
        }
        projectiles.removeAll(toRemove);
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (!alive) return;

        // Cuerpo del mago
        shapeRenderer.setColor(Color.PURPLE);
        shapeRenderer.rect(x, y, width, height);

        // Barra de vida
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y + height + 4, width, 4);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x, y + height + 4, width * ((float) health / maxHealth), 4);

        // Proyectiles activos
        shapeRenderer.setColor(Color.MAGENTA);
        for (float[] proj : projectiles) {
            if (proj[3] == 1f) {
                shapeRenderer.rect(proj[0], proj[1], PROJ_WIDTH, PROJ_HEIGHT);
            }
        }
    }

    public List<float[]> getProjectiles() { return projectiles; }
}
