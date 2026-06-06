package com.svalero.dungeonescape.entities.npc;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.svalero.dungeonescape.managers.SoundManager;
import com.svalero.dungeonescape.utils.AnimationHelper;

public class Ogre extends NPC {

    private float speed = 60f;
    private static final float DETECTION_RANGE = 250f;
    private float leftBound, rightBound;
    private boolean movingRight = true;
    private boolean chasing = false;
    private float detectionRange = 250f;

    // Animaciones
    private Animation<TextureRegion> walkAnim;
    private Animation<TextureRegion> attackAnim;
    private Animation<TextureRegion> hurtAnim;
    private Animation<TextureRegion> deathAnim;
    private float stateTime = 0f;

    // Estado
    private boolean dying = false;
    private float dyingTimer = 0f;
    private boolean hurting = false;
    private float hurtTimer = 0f;
    private static final float HURT_DURATION = 0.4f;
    private boolean attacking = false;
    private float attackTimer = 0f;
    private static final float ATTACK_DURATION = 0.7f;
    private float attackCooldown = 0f;
    private static final float ATTACK_RANGE = 120f;

    // Tamaño visual — grande para el boss
    private static final float DRAW_WIDTH = 200f;
    private static final float DRAW_HEIGHT = 190f;

    public Ogre(float x, float y, float leftBound, float rightBound) {
        super(x, y, 56, 72, 300, 500);
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        loadAnimations();
    }

    public Ogre(float x, float y, float leftBound, float rightBound, float multiplier) {
        super(x, y, 56, 72, (int)(300 * multiplier), 500);
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.speed = 60f * multiplier;
        loadAnimations();
    }

    public Ogre(float x, float y, float leftBound, float rightBound, float multiplier, float detectionRange) {
        super(x, y, 56, 72, (int)(300 * multiplier), 500);
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.speed = 60f * multiplier;
        this.detectionRange = detectionRange;
        loadAnimations();
    }

    private void loadAnimations() {
        walkAnim = AnimationHelper.loadAnimationMultiRowPartial(
            "sprites/ogre/ogre_walk.png", 5, 2, 9, 0.12f); // 9 frames reales
        attackAnim = AnimationHelper.loadAnimationMultiRowPartial(
            "sprites/ogre/ogre_attack.png", 5, 2, 5, 0.1f); // 5 frames reales
        hurtAnim = AnimationHelper.loadAnimationMultiRow(
            "sprites/ogre/ogre_hurt.png", 5, 1, 0.1f);
        deathAnim = AnimationHelper.loadAnimationMultiRowPartial(
            "sprites/ogre/ogre_dead.png", 5, 2, 7, 0.12f);

        walkAnim.setPlayMode(Animation.PlayMode.LOOP);
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

        if (attackCooldown > 0) attackCooldown -= delta;

        if (attacking) {
            attackTimer += delta;
            if (attackTimer >= ATTACK_DURATION) {
                attacking = false;
                attackTimer = 0f;
            }
            return;
        }

        float distX = Math.abs(playerX - x);

        // Detectar y perseguir
        float distY = Math.abs(playerY - y);
        if (distX < detectionRange && distY < 100f) {
            chasing = true;
        } else {
            chasing = false;
        }

        // Atacar si está cerca
        if (distX < ATTACK_RANGE && distY < 50f && attackCooldown <= 0) {
            attacking = true;
            attackTimer = 0f;
            attackCooldown = 1.5f;
            return;
        }

        if (chasing) {
            if (playerX > x) {
                x += speed * 1.2f * delta;
                movingRight = true;
            } else {
                x -= speed * 1.2f * delta;
                movingRight = false;
            }
        } else {
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
    public void takeDamage(int damage) {
        if (!alive) return;
        health -= damage;
        hurting = true;
        hurtTimer = 0f;
        SoundManager.getInstance().playOrcHit();
        if (health <= 0) {
            health = 0;
            alive = false;
            dying = true;
            dyingTimer = 0f;
            SoundManager.getInstance().playOrcDeath();
        }
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {}

    public void render(SpriteBatch batch) {
        if (dying) {
            TextureRegion frame = deathAnim.getKeyFrame(dyingTimer);
            batch.draw(frame, x - 80, y - 15, DRAW_WIDTH, DRAW_HEIGHT);
            return;
        }

        if (!alive) return;

        TextureRegion frame;

        if (hurting) {
            frame = hurtAnim.getKeyFrame(hurtTimer);
        } else if (attacking) {
            frame = attackAnim.getKeyFrame(attackTimer);
        } else {
            frame = walkAnim.getKeyFrame(stateTime);
        }

        if (!movingRight) {
            batch.draw(frame, x - 80, y - 15, DRAW_WIDTH, DRAW_HEIGHT);
        } else {
            batch.draw(frame, x + DRAW_WIDTH - 80, y - 15, -DRAW_WIDTH, DRAW_HEIGHT);
        }
    }

    public boolean isDying() { return dying; }
    public boolean isChasing() { return chasing; }
}
