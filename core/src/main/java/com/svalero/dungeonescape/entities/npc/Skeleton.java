package com.svalero.dungeonescape.entities.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.svalero.dungeonescape.managers.GameState;
import com.svalero.dungeonescape.utils.AnimationHelper;

public class Skeleton extends NPC {

    private float speed = 80f;
    private float leftBound, rightBound;
    private boolean movingRight = true;

    // Animaciones
    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> moveAnim;
    private float stateTime = 0f;
    private Animation<TextureRegion> hurtAnim;
    private Animation<TextureRegion> deathAnim;
    private boolean dying = false;
    private float dyingTimer = 0f;
    public boolean isDying() { return dying; }
    private boolean hurting = false;
    private float hurtTimer = 0f;
    private static final float HURT_DURATION = 0.3f;
    private Animation<TextureRegion> attackAnim;
    private boolean attacking = false;
    private float attackTimer = 0f;
    private static final float ATTACK_DURATION = 0.5f;
    private static final float ATTACK_RANGE = 50f;
    private float attackCooldown = 0f;

    // Tamaño visual
    private static final float DRAW_WIDTH = 100f;
    private static final float DRAW_HEIGHT = 100f;

    public Skeleton(float x, float y, float leftBound, float rightBound) {
        super(x, y, 32, 48, 60, 100);
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        loadAnimations();
    }

    public Skeleton(float x, float y, float leftBound, float rightBound, float multiplier) {
        super(x, y, 32, 48, (int)(60 * multiplier), 100);
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        this.speed = 80f * multiplier;
        loadAnimations();
    }

    private void loadAnimations() {
        idleAnim = AnimationHelper.loadAnimation("sprites/skeleton/skeletonIdle-Sheet64x64.png", 9, 0.1f);
        moveAnim = AnimationHelper.loadAnimation("sprites/skeleton/skeletonMove-Sheet64x64.png", 10, 0.08f);
        hurtAnim = AnimationHelper.loadAnimation("sprites/skeleton/skeletonHurt-Sheet64x64.png", 3, 0.1f);
        deathAnim = AnimationHelper.loadAnimationMultiRow("sprites/skeleton/skeletonDie-Sheet118x64_all.png", 5, 5, 0.1f);
        attackAnim = AnimationHelper.loadAnimationMultiRow("sprites/skeleton/skeletonAttack-Sheet146x64.png", 5, 5, 0.08f);

        attackAnim.setPlayMode(Animation.PlayMode.NORMAL);
        deathAnim.setPlayMode(Animation.PlayMode.NORMAL);
        hurtAnim.setPlayMode(Animation.PlayMode.NORMAL);
        deathAnim.setPlayMode(Animation.PlayMode.NORMAL);
        idleAnim.setPlayMode(Animation.PlayMode.LOOP);
        moveAnim.setPlayMode(Animation.PlayMode.LOOP);


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

        if (movingRight) {
            x += speed * delta;
            if (x >= rightBound) movingRight = false;
        } else {
            x -= speed * delta;
            if (x <= leftBound) movingRight = true;
        }
        if (attacking) {
            attackTimer += delta;
            if (attackTimer >= ATTACK_DURATION) {
                attacking = false;
                attackTimer = 0f;
            }
            return;
        }

        if (attackCooldown > 0) attackCooldown -= delta;

        float distX = Math.abs(playerX - x);
        float distY = Math.abs(playerY - y);
        if (distX < ATTACK_RANGE && distY < 30f && attackCooldown <= 0) {
            attacking = true;
            attackTimer = 0f;
            // Cooldown igual a la invencibilidad del jugador según dificultad
            float invTime = 1.5f / GameState.getInstance().getDifficultyMultiplier();
            attackCooldown = invTime + 0.1f; // pequeño margen extra
        }
    }

    public void render(SpriteBatch batch) {
        if (dying) {
            TextureRegion frame = deathAnim.getKeyFrame(dyingTimer);
            // Tamaño proporcional al frame de muerte (más ancho)
            batch.draw(frame, x - 30, y - 10, 130f, 100f);
            return;
        }

        if (!alive) return;

        TextureRegion frame;

        if (hurting) {
            frame = hurtAnim.getKeyFrame(hurtTimer);
            if (movingRight) {
                batch.draw(frame, x + DRAW_WIDTH - 10, y - 10, -DRAW_WIDTH, DRAW_HEIGHT);
            } else {
                batch.draw(frame, x - 10, y - 10, DRAW_WIDTH, DRAW_HEIGHT);
            }
        } else if (attacking) {
            frame = attackAnim.getKeyFrame(attackTimer);
            // Frame de ataque más ancho
            if (movingRight) {
                batch.draw(frame, x + 180f - 10, y - 10, -180f, DRAW_HEIGHT);
            } else {
                batch.draw(frame, x - 10, y - 10, 180f, DRAW_HEIGHT);
            }
        } else {
            frame = moveAnim.getKeyFrame(stateTime);
            if (movingRight) {
                batch.draw(frame, x + DRAW_WIDTH - 10, y - 10, -DRAW_WIDTH, DRAW_HEIGHT);
            } else {
                batch.draw(frame, x - 10, y - 10, DRAW_WIDTH, DRAW_HEIGHT);
            }
        }
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
        }
    }
}
