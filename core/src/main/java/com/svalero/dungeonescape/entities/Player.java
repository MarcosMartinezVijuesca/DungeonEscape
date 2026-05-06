package com.svalero.dungeonescape.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.svalero.dungeonescape.managers.GameState;
import com.svalero.dungeonescape.managers.SoundManager;

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
    private float invincibleTimer = 0f; // tiempo de invencibilidad tras recibir daño

    // Disparo
    private float shootCooldown = 0f;
    private static final float SHOOT_DELAY = 0.4f;
    private boolean facingRight = true;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update(float delta) {
        if (!alive) return;

        if (shootCooldown > 0) shootCooldown -= delta;
        if (invincibleTimer > 0) invincibleTimer -= delta;

        // Movimiento horizontal
        velocityX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocityX = -SPEED;
            facingRight = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocityX = SPEED;
            facingRight = true;
        }

        // Gravedad ANTES del salto para no cancelarlo
        velocityY += GRAVITY * delta;

        // Salto
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
            Gdx.input.isKeyJustPressed(Input.Keys.UP) ||
            Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            if (jumpsLeft > 0) {
                velocityY = JUMP_FORCE;
                onGround = false;
                jumpsLeft--;
                SoundManager.getInstance().playJump();
            }
        }

        // Aplicar movimiento
        x += velocityX * delta;
        y += velocityY * delta;


        //Suelo
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

    public boolean canShoot() {
        return shootCooldown <= 0 && alive;
    }

    public void shoot() {
        shootCooldown = SHOOT_DELAY;
        SoundManager.getInstance().playShoot();
    }

    public void takeDamage(int damage) {
        if (invincibleTimer > 0) return;
        health -= damage;

        // Menos invencibilidad en dificultad alta
        float invTime = 1.5f / GameState.getInstance().getDifficultyMultiplier();
        invincibleTimer = invTime;

        SoundManager.getInstance().playHit();

        if (health <= 0) {
            health = 0;
            alive = false;
            SoundManager.getInstance().playDeath();
            GameState.getInstance().loseLife();
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (!alive) return;

        // Parpadeo cuando es invencible
        if (invincibleTimer > 0 && (int)(invincibleTimer * 10) % 2 == 0) return;

        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.rect(x, y, width, height);
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
    public void revive() {
        health = 100;
        alive = true;
        invincibleTimer = 3f;
    }
}
