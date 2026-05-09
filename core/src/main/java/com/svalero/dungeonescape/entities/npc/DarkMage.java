package com.svalero.dungeonescape.entities.npc;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.svalero.dungeonescape.entities.EnemyProjectile;
import com.svalero.dungeonescape.managers.GameState;
import com.svalero.dungeonescape.utils.AnimationHelper;

import java.util.ArrayList;
import java.util.List;

public class DarkMage extends NPC {

    private float shootTimer = 0f;
    private float shootInterval = 2.5f;
    private static final float DETECTION_RANGE = 300f;

    // Proyectiles [x, y, velocityX, active]
    private boolean facingRight = true;

    private List<EnemyProjectile> projectiles = new ArrayList<>();
    private static final float PROJ_SPEED = 150f;
    private static final float PROJ_WIDTH = 10f;
    private static final float PROJ_HEIGHT = 10f;

    // Animaciones
    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> runAnim;
    private Animation<TextureRegion> attackAnim;
    private Animation<TextureRegion> hurtAnim;
    private Animation<TextureRegion> deathAnim;
    private float stateTime = 0f;

    // Estado
    private boolean dying = false;
    private float dyingTimer = 0f;
    private boolean hurting = false;
    private float hurtTimer = 0f;
    private static final float HURT_DURATION = 0.3f;
    private boolean attacking = false;
    private float attackTimer = 0f;
    private static final float ATTACK_DURATION = 0.6f;

    // Tamaño visual
    private float drawWidth = 250f;
    private float drawHeight = 350f;

    public DarkMage(float x, float y) {
        super(x, y, 60, 150, 80, 200);
        loadAnimations();
    }

    public DarkMage(float x, float y, float multiplier) {
        super(x, y, 60, 150, (int)(80 * multiplier), 200);
        this.shootInterval = 2.5f / multiplier;
        loadAnimations();
    }

    private void loadAnimations() {
        idleAnim   = AnimationHelper.loadAnimation("sprites/darkmage/Idle.png",     8, 0.1f);
        runAnim    = AnimationHelper.loadAnimation("sprites/darkmage/Run.png",      8, 0.08f);
        attackAnim = AnimationHelper.loadAnimation("sprites/darkmage/Attack1.png",  8, 0.07f);
        hurtAnim   = AnimationHelper.loadAnimation("sprites/darkmage/Take hit.png", 3, 0.1f);
        deathAnim  = AnimationHelper.loadAnimation("sprites/darkmage/Death.png",    7, 0.1f);

        idleAnim.setPlayMode(Animation.PlayMode.LOOP);
        runAnim.setPlayMode(Animation.PlayMode.LOOP);
        attackAnim.setPlayMode(Animation.PlayMode.NORMAL);
        hurtAnim.setPlayMode(Animation.PlayMode.NORMAL);
        deathAnim.setPlayMode(Animation.PlayMode.NORMAL);
    }

    @Override
    public void update(float delta, float playerX, float playerY) {
        stateTime += delta;

        if (dying) {
            dyingTimer += delta;
            return;
        }

        if (hurting) {
            hurtTimer += delta;
            if (hurtTimer >= HURT_DURATION) {
                hurting = false;
                hurtTimer = 0f;
            }
        }

        if (!alive) return;

        if (attacking) {
            attackTimer += delta;
            if (attackTimer >= ATTACK_DURATION) {
                attacking = false;
                attackTimer = 0f;
            }
        }

        shootTimer += delta;

        float distX = Math.abs(playerX - x);
        float distY = Math.abs(playerY - y);
        if (distX < DETECTION_RANGE && distY < 80f && shootTimer >= shootInterval) {
            facingRight = playerX > x;
            shootTimer = 0f;
            attacking = true;
            attackTimer = 0f;
            boolean goingRight = playerX > x;
            projectiles.add(new EnemyProjectile(x + width / 2f, y + 30f, goingRight));
        }

// Mover proyectiles
        List<EnemyProjectile> toRemove = new ArrayList<>();
        for (EnemyProjectile proj : projectiles) {
            proj.update(delta);
            if (!proj.isActive()) toRemove.add(proj);
        }
        projectiles.removeAll(toRemove);
    }

    @Override
    public void takeDamage(int damage) {
        if (!alive) return;
        health -= damage;
        hurting = true;
        hurtTimer = 0f;
        if (health <= 0) {
            health = 0;
            alive = false;
            dying = true;
            dyingTimer = 0f;
            projectiles.clear();
        }
    }

    public void render(SpriteBatch batch) {
        if (dying) {
            TextureRegion frame = deathAnim.getKeyFrame(dyingTimer);
            batch.draw(frame, x + drawWidth - 30, y - 120, -drawWidth, drawHeight);
            return;
        }

        if (!alive) return;

        TextureRegion frame;

        if (hurting) {
            frame = hurtAnim.getKeyFrame(hurtTimer);
        } else if (attacking) {
            frame = attackAnim.getKeyFrame(attackTimer);
        } else {
            frame = idleAnim.getKeyFrame(stateTime);
        }

        if (facingRight) {
            batch.draw(frame, x - 30, y - 120, drawWidth, drawHeight);
        } else {
            batch.draw(frame, x + drawWidth - 30, y - 120, -drawWidth, drawHeight);
        }
    }

    public void renderProjectiles(SpriteBatch batch) {
        // Los proyectiles los dibuja GameScreen con ShapeRenderer
    }

    public boolean isDying() { return dying; }
    public List<EnemyProjectile> getProjectiles() { return projectiles; }
}
