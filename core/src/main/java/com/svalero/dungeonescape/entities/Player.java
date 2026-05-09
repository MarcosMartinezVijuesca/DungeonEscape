package com.svalero.dungeonescape.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.svalero.dungeonescape.managers.GameState;
import com.svalero.dungeonescape.managers.SoundManager;
import com.svalero.dungeonescape.utils.AnimationHelper;

public class Player {

    // Posición y tamaño
    public float x, y;
    public float width = 32, height = 48;

    // Física
    private float velocityX = 0;
    private float velocityY = 0;
    private static final float SPEED = 180f;
    private static final float JUMP_FORCE = 420f;
    private static final float GRAVITY = -900f;
    private boolean onGround = false;
    private int jumpsLeft = 2;

    // Estado
    private int health = 100;
    private boolean alive = true;
    private float invincibleTimer = 0f;

    // Disparo
    private float shootCooldown = 0f;
    private static final float SHOOT_DELAY = 0.4f;
    private boolean facingRight = true;

    // Animaciones
    private Animation<TextureRegion> idleAnim;
    private Animation<TextureRegion> runAnim;
    private Animation<TextureRegion> jumpAnim;
    private Animation<TextureRegion> fallAnim;
    private Animation<TextureRegion> attackAnim;
    private Animation<TextureRegion> hitAnim;
    private Animation<TextureRegion> deathAnim;
    private float stateTime = 0f;
    private boolean attacking = false;
    private float attackTimer = 0f;
    private static final float ATTACK_DURATION = 0.5f;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        loadAnimations();
    }

    private void loadAnimations() {
        idleAnim   = AnimationHelper.loadAnimation("sprites/player/Idle.png",    6, 0.12f);
        runAnim    = AnimationHelper.loadAnimation("sprites/player/Run.png",     8, 0.08f);
        jumpAnim   = AnimationHelper.loadAnimation("sprites/player/Jump.png",    2, 0.15f);
        fallAnim   = AnimationHelper.loadAnimation("sprites/player/Fall.png",    2, 0.15f);
        attackAnim = AnimationHelper.loadAnimation("sprites/player/Attack1.png", 8, 0.07f);
        hitAnim    = AnimationHelper.loadAnimation("sprites/player/Hit.png",     4, 0.1f);
        deathAnim  = AnimationHelper.loadAnimation("sprites/player/Death.png",   7, 0.1f);

        idleAnim.setPlayMode(Animation.PlayMode.LOOP);
        runAnim.setPlayMode(Animation.PlayMode.LOOP);
        jumpAnim.setPlayMode(Animation.PlayMode.NORMAL);
        fallAnim.setPlayMode(Animation.PlayMode.LOOP);
        attackAnim.setPlayMode(Animation.PlayMode.NORMAL);
        hitAnim.setPlayMode(Animation.PlayMode.NORMAL);
        deathAnim.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public void update(float delta) {
        if (!alive) {
            stateTime += delta;
            return;
        }

        stateTime += delta;

        if (shootCooldown > 0) shootCooldown -= delta;
        if (invincibleTimer > 0) invincibleTimer -= delta;
        if (attacking) {
            attackTimer += delta;
            if (attackTimer >= ATTACK_DURATION) {
                attacking = false;
                attackTimer = 0f;
            }
        }

        // Movimiento horizontal
        velocityX = 0;
        if (!attacking) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                velocityX = -SPEED;
                facingRight = false;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                velocityX = SPEED;
                facingRight = true;
            }
        }

        // Gravedad
        velocityY += GRAVITY * delta;

        // Salto
        if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
            Gdx.input.isKeyJustPressed(Input.Keys.UP) ||
            Gdx.input.isKeyJustPressed(Input.Keys.W)) && jumpsLeft > 0) {
            velocityY = JUMP_FORCE;
            onGround = false;
            jumpsLeft--;
            SoundManager.getInstance().playJump();
        }

        // Aplicar movimiento
        x += velocityX * delta;
        y += velocityY * delta;

        // Suelo de seguridad
        if (y <= 40) {
            y = 40;
            velocityY = 0;
            onGround = true;
            jumpsLeft = 2;
        }

        // Límites horizontales
        if (x < 0) x = 0;
        if (x + width > 2400) x = 2400 - width;
    }

    public void render(SpriteBatch batch) {
        if (!alive && deathAnim.isAnimationFinished(stateTime)) return;

        if (invincibleTimer > 0 && (int)(invincibleTimer * 10) % 2 == 0) return;

        TextureRegion frame = getCurrentFrame();

        float drawWidth = 120;
        float drawHeight = 160;

        float drawX = x - (drawWidth - width) / 2f;
        float drawY = y - (drawHeight - height) / 2f;

        if (facingRight) {
            batch.draw(frame, x - 30, y - 50, drawWidth, drawHeight);
        } else {
            batch.draw(frame, x + drawWidth - 30, y - 50, -drawWidth, drawHeight);
        }
    }

    private TextureRegion getCurrentFrame() {
        if (!alive) return deathAnim.getKeyFrame(stateTime);
        if (attacking) return attackAnim.getKeyFrame(attackTimer);
        if (invincibleTimer > 0) return hitAnim.getKeyFrame(stateTime);
        if (!onGround && velocityY < 0) return fallAnim.getKeyFrame(stateTime);
        if (!onGround) return jumpAnim.getKeyFrame(stateTime);
        if (velocityX != 0) return runAnim.getKeyFrame(stateTime);
        return idleAnim.getKeyFrame(stateTime);
    }

    // Render legacy con ShapeRenderer (ya no se usa pero por si acaso)
    public void render(ShapeRenderer shapeRenderer) {}

    public boolean canShoot() {
        return shootCooldown <= 0 && alive && !attacking;
    }

    public void shoot() {
        shootCooldown = SHOOT_DELAY;
        attacking = true;
        attackTimer = 0f;
        stateTime = 0f;
        SoundManager.getInstance().playShoot();
    }

    public void takeDamage(int damage) {
        if (invincibleTimer > 0) return;
        health -= damage;
        float invTime = 1.5f / GameState.getInstance().getDifficultyMultiplier();
        invincibleTimer = invTime;
        SoundManager.getInstance().playHit();

        if (health <= 0) {
            health = 0;
            alive = false;
            stateTime = 0f;
            SoundManager.getInstance().playDeath();
            GameState.getInstance().loseLife();
        }
    }

    public void revive() {
        health = 100;
        alive = true;
        invincibleTimer = 3f;
        stateTime = 0f;
        attacking = false;
        attackTimer = 0f;
    }

    // Getters
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public int getHealth() { return health; }
    public boolean isAlive() { return alive; }
    public boolean isFacingRight() { return facingRight; }
    public boolean isOnGround() { return onGround; }
    public float getVelocityY() { return velocityY; }

    // Setters
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
        if (onGround) jumpsLeft = 2;
    }
    public void setVelocityY(float vy) { this.velocityY = vy; }
    public void setPosition(float x, float y) { this.x = x; this.y = y; }
}
