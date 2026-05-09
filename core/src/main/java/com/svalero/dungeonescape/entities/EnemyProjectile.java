package com.svalero.dungeonescape.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.svalero.dungeonescape.utils.AnimationHelper;

public class EnemyProjectile {

    public float x, y;
    private float speed;
    private boolean active = true;
    private float stateTime = 0f;
    private boolean goingRight;

    private Animation<TextureRegion> fireAnim;

    public EnemyProjectile(float x, float y, boolean goingRight) {
        this.x = x;
        this.y = y;
        this.goingRight = goingRight;
        this.speed = goingRight ? 150f : -150f;
        loadAnimation();
    }

    private void loadAnimation() {
        String[] paths = {
            "sprites/projectiles/FB001.png",
            "sprites/projectiles/FB002.png",
            "sprites/projectiles/FB003.png",
            "sprites/projectiles/FB004.png",
            "sprites/projectiles/FB005.png"
        };
        fireAnim = AnimationHelper.loadAnimationFromFiles(paths, 0.08f);
        fireAnim.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void update(float delta) {
        if (!active) return;
        stateTime += delta;
        x += speed * delta;
        if (x > 2500 || x < -50) active = false;
    }

    public void render(SpriteBatch batch) {
        if (!active) return;
        TextureRegion frame = fireAnim.getKeyFrame(stateTime);

        // Teñir de azul/morado para distinguirlo del jugador
        batch.setColor(0.5f, 0f, 1f, 1f);

        if (goingRight) {
            batch.draw(frame, x, y, 36, 22);
        } else {
            batch.draw(frame, x + 36, y, -36, 22);
        }

        // Restaurar color blanco para no afectar otros sprites
        batch.setColor(1f, 1f, 1f, 1f);
    }

    public boolean isActive() { return active; }
    public void deactivate() { active = false; }

    public boolean overlaps(float ox, float oy, float ow, float oh) {
        return active &&
            x < ox + ow &&
            x + 36 > ox &&
            y < oy + oh &&
            y + 22 > oy;
    }
}
