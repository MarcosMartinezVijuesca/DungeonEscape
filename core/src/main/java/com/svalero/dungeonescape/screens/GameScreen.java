package com.svalero.dungeonescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.svalero.dungeonescape.DungeonEscape;
import com.svalero.dungeonescape.entities.Player;
import com.svalero.dungeonescape.entities.Projectile;
import com.svalero.dungeonescape.entities.npc.DarkMage;
import com.svalero.dungeonescape.entities.npc.Ogre;
import com.svalero.dungeonescape.entities.npc.Skeleton;
import com.svalero.dungeonescape.managers.GameState;
import com.svalero.dungeonescape.managers.SoundManager;
import com.svalero.dungeonescape.ui.HUD;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    private final DungeonEscape game;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private HUD hud;

    // Entidades
    private Player player;
    private List<Projectile> projectiles;
    private List<Skeleton> skeletons;
    private List<DarkMage> darkMages;
    private Ogre ogre;

    // Estado
    private GameState state;
    private boolean paused = false;

    // Plataformas [x, y, width, height]
    private float[][] platforms;

    public GameScreen(DungeonEscape game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Cámara fija 800x600
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, 800, 600);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        hud = new HUD();
        state = GameState.getInstance();
        projectiles = new ArrayList<>();

        SoundManager.getInstance().load();
        SoundManager.getInstance().playMusic();

        setupLevel();
    }

    private void setupLevel() {
        player = new Player(100, 50);

        platforms = new float[][]{
            {0, 20, 2400, 20},   // suelo principal largo
            {100, 140, 150, 20},   // plataforma 1
            {320, 220, 150, 20},   // plataforma 2
            {550, 170, 150, 20},   // plataforma 3
            {750, 250, 120, 20},   // plataforma 4
            {950, 180, 150, 20},   // plataforma 5
            {1150, 280, 130, 20},   // plataforma 6
            {1350, 200, 150, 20},   // plataforma 7
            {1550, 300, 120, 20},   // plataforma 8
            {1750, 220, 150, 20},   // plataforma 9
            {1950, 150, 130, 20},   // plataforma 10
            {2100, 260, 150, 20},   // plataforma 11
        };

        skeletons = new ArrayList<>();
        skeletons.add(new Skeleton(300, 40, 200, 500));
        skeletons.add(new Skeleton(700, 40, 550, 900));
        skeletons.add(new Skeleton(1200, 40, 1000, 1400));
        skeletons.add(new Skeleton(1800, 40, 1600, 2000));

        darkMages = new ArrayList<>();
        darkMages.add(new DarkMage(800, 40));
        darkMages.add(new DarkMage(1500, 40));
        darkMages.add(new DarkMage(2100, 40));

        ogre = new Ogre(2200, 40, 2000, 2350);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.08f, 0.08f, 0.12f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualizar cámara
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        if (!paused) {
            update(delta);
        }

        // Dibujar mundo
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawPlatforms();
        player.render(shapeRenderer);
        drawNPCs();
        drawProjectiles();
        shapeRenderer.end();

        // Cámara sigue al jugador
        camera.position.x = Math.max(400, Math.min(player.getX() + 16, 2400 - 400));
        camera.position.y = 300;
        camera.update();

        // HUD con cámara fija
        game.batch.setProjectionMatrix(hudCamera.combined);
        hud.render(game.batch, 600);

        // Restaurar cámara del juego para el siguiente frame
        game.batch.setProjectionMatrix(camera.combined);

        // Pausa con ESC
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new PauseScreen(game, this));
        }
    }

    private void update(float delta) {
        player.update(delta);
        handlePlayerPlatformCollisions();

        // Disparo con F
        if (Gdx.input.isKeyJustPressed(Input.Keys.F) && player.canShoot()) {
            float projX = player.isFacingRight()
                ? player.getX() + player.getWidth()
                : player.getX() - 12;
            float projY = player.getY() + player.getHeight() / 2f;
            projectiles.add(new Projectile(projX, projY, player.isFacingRight()));
            player.shoot();
        }

        for (Projectile p : projectiles) p.update(delta);
        projectiles.removeIf(p -> !p.isActive());

        for (Skeleton s : skeletons) s.update(delta, player.getX(), player.getY());
        for (DarkMage m : darkMages) m.update(delta, player.getX(), player.getY());
        if (ogre != null) ogre.update(delta, player.getX(), player.getY());

        handleProjectileNPCCollisions();
        handleNPCPlayerCollisions();
        handleMageProjectileCollisions();
        checkGameOver();
    }

    private void handlePlayerPlatformCollisions() {
        boolean onAnyPlatform = false;

        for (float[] plat : platforms) {
            float px = plat[0], py = plat[1], pw = plat[2], ph = plat[3];
            float platTop = py + ph;

            boolean overlapX = player.getX() + player.getWidth() > px &&
                player.getX() < px + pw;

            boolean fallingOnto = player.getY() <= platTop + 5 &&
                player.getY() >= platTop - 10;

            // Solo colisiona si está cayendo o quieto, nunca si está subiendo
            boolean falling = player.getVelocityY() <= 0;

            if (overlapX && fallingOnto && falling) {
                player.setPosition(player.getX(), platTop);
                player.setVelocityY(0);
                player.setOnGround(true);
                onAnyPlatform = true;
                break;
            }
        }

        if (!onAnyPlatform) {
            player.setOnGround(false);
        }
    }

    private void handleProjectileNPCCollisions() {
        for (Projectile proj : projectiles) {
            for (Skeleton s : skeletons) {
                if (s.isAlive() && proj.overlaps(s.getX(), s.getY(), s.getWidth(), s.getHeight())) {
                    s.takeDamage(proj.getDamage());
                    proj.deactivate();
                    if (!s.isAlive()) state.addScore(s.getScoreValue());
                }
            }
            for (DarkMage m : darkMages) {
                if (m.isAlive() && proj.overlaps(m.getX(), m.getY(), m.getWidth(), m.getHeight())) {
                    m.takeDamage(proj.getDamage());
                    proj.deactivate();
                    if (!m.isAlive()) state.addScore(m.getScoreValue());
                }
            }
            if (ogre != null && ogre.isAlive() &&
                proj.overlaps(ogre.getX(), ogre.getY(), ogre.getWidth(), ogre.getHeight())) {
                ogre.takeDamage(proj.getDamage());
                proj.deactivate();
                if (!ogre.isAlive()) state.addScore(ogre.getScoreValue());
            }
        }
    }

    private void handleNPCPlayerCollisions() {
        if (!player.isAlive()) return;

        for (Skeleton s : skeletons) {
            if (s.isAlive() && s.overlaps(player.getX(), player.getY(),
                player.getWidth(), player.getHeight())) {
                player.takeDamage(10);
            }
        }
        for (DarkMage m : darkMages) {
            if (m.isAlive() && m.overlaps(player.getX(), player.getY(),
                player.getWidth(), player.getHeight())) {
                player.takeDamage(15);
            }
        }
        if (ogre != null && ogre.isAlive() &&
            ogre.overlaps(player.getX(), player.getY(),
                player.getWidth(), player.getHeight())) {
            player.takeDamage(25);
        }
    }

    private void handleMageProjectileCollisions() {
        for (DarkMage m : darkMages) {
            for (float[] proj : m.getProjectiles()) {
                boolean hitsPlayer = proj[0] < player.getX() + player.getWidth() &&
                    proj[0] + 10 > player.getX() &&
                    proj[1] < player.getY() + player.getHeight() &&
                    proj[1] + 10 > player.getY();
                if (hitsPlayer) {
                    player.takeDamage(20);
                    proj[3] = 0;
                }
            }
        }
    }

    private void checkGameOver() {
        if (!player.isAlive()) {
            if (state.getLives() > 0) {
                player.setPosition(100, 50);
                player.revive();
                for (Skeleton s : skeletons) {
                    if (Math.abs(s.getX() - 100) < 150) s.x = 350;
                }
                for (DarkMage m : darkMages) {
                    if (Math.abs(m.getX() - 100) < 150) m.x = 500;
                }
                if (ogre != null && Math.abs(ogre.getX() - 100) < 150) {
                    ogre.x = 600;
                }
            } else {
                SoundManager.getInstance().stopMusic();
                game.setScreen(new GameOverScreen(game));
            }
        }
    }

    private void drawPlatforms() {
        shapeRenderer.setColor(0.3f, 0.2f, 0.1f, 1);
        for (float[] plat : platforms) {
            shapeRenderer.rect(plat[0], plat[1], plat[2], plat[3]);
        }
    }

    private void drawNPCs() {
        for (Skeleton s : skeletons) s.render(shapeRenderer);
        for (DarkMage m : darkMages) m.render(shapeRenderer);
        if (ogre != null) ogre.render(shapeRenderer);
    }

    private void drawProjectiles() {
        for (Projectile p : projectiles) p.render(shapeRenderer);
    }

    public void resumeGame() {
        paused = false;
        SoundManager.getInstance().resumeMusic();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 800, 600);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        hud.dispose();
        SoundManager.getInstance().dispose();
    }
}
