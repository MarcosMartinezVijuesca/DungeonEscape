package com.svalero.dungeonescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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

    // Nivel actual
    private int currentLevel = 1;
    private static final int MAP_WIDTH_L1 = 2400;
    private static final int MAP_WIDTH_L2 = 2400;

    // Puerta de salida nivel 1 [x, y, width, height]
    private float[] exitDoor = {2330, 40, 40, 80};
    private boolean exitVisible = false;

    public GameScreen(DungeonEscape game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, 800, 600);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        hud = new HUD();
        state = GameState.getInstance();
        state.setCurrentLevel(1);
        projectiles = new ArrayList<>();

        SoundManager.getInstance().load();
        SoundManager.getInstance().playMusic();

        setupLevel1();
    }

    // ==================== NIVEL 1 ====================
    private void setupLevel1() {
        currentLevel = 1;
        state.setCurrentLevel(1);
        exitVisible = false;
        projectiles.clear();

        player = new Player(100, 50);

        platforms = new float[][]{
            {0,    20,  2400, 20},
            {100,  140, 150,  20},
            {320,  220, 150,  20},
            {550,  170, 150,  20},
            {750,  250, 120,  20},
            {950,  180, 150,  20},
            {1150, 280, 130,  20},
            {1350, 200, 150,  20},
            {1550, 300, 120,  20},
            {1750, 220, 150,  20},
            {1950, 150, 130,  20},
            {2100, 260, 150,  20},
        };

        float m = state.getDifficultyMultiplier();

        skeletons = new ArrayList<>();
        skeletons.add(new Skeleton(300,  40, 200,  500,  m));
        skeletons.add(new Skeleton(700,  40, 550,  900,  m));
        skeletons.add(new Skeleton(1200, 40, 1000, 1400, m));
        skeletons.add(new Skeleton(1800, 40, 1600, 2000, m));

        darkMages = new ArrayList<>();
        darkMages.add(new DarkMage(800,  40, m));
        darkMages.add(new DarkMage(1500, 40, m));
        darkMages.add(new DarkMage(2100, 40, m));

        ogre = null; // no hay ogro en nivel 1
    }

    // ==================== NIVEL 2 ====================
    private void setupLevel2() {
        currentLevel = 2;
        state.setCurrentLevel(2);
        exitVisible = false;
        projectiles.clear();

        player.setPosition(100, 50);
        player.revive();

        // Torres del castillo — más plataformas elevadas
        platforms = new float[][]{
            {0,    20,  2400, 20},   // suelo
            {50,   160, 120,  20},   // plataforma 1
            {250,  260, 100,  20},   // plataforma 2
            {420,  180, 130,  20},   // plataforma 3
            {600,  300, 110,  20},   // plataforma 4
            {780,  200, 140,  20},   // plataforma 5
            {980,  320, 120,  20},   // plataforma 6
            {1150, 240, 130,  20},   // plataforma 7
            {1350, 350, 100,  20},   // plataforma 8
            {1520, 260, 140,  20},   // plataforma 9
            {1700, 180, 120,  20},   // plataforma 10
            {1900, 300, 130,  20},   // plataforma 11
            {2100, 220, 150,  20},   // plataforma 12
        };

        float m = state.getDifficultyMultiplier();

        // Más enemigos en nivel 2
        skeletons = new ArrayList<>();
        skeletons.add(new Skeleton(250,  40, 150,  500,  m));
        skeletons.add(new Skeleton(600,  40, 450,  850,  m));
        skeletons.add(new Skeleton(1000, 40, 800,  1200, m));
        skeletons.add(new Skeleton(1400, 40, 1200, 1600, m));
        skeletons.add(new Skeleton(1900, 40, 1700, 2100, m));

        darkMages = new ArrayList<>();
        darkMages.add(new DarkMage(400,  40, m));
        darkMages.add(new DarkMage(900,  40, m));
        darkMages.add(new DarkMage(1600, 40, m));
        darkMages.add(new DarkMage(2000, 40, m));

        // Ogro reforzado como boss final — más vida y más rápido
        ogre = new Ogre(2200, 40, 2000, 2350, m * 1.5f);
    }

    // ==================== RENDER ====================
    @Override
    public void render(float delta) {
        // Color de fondo según nivel
        if (currentLevel == 1) {
            Gdx.gl.glClearColor(0.08f, 0.08f, 0.12f, 1); // mazmorra oscura
        } else {
            Gdx.gl.glClearColor(0.15f, 0.12f, 0.05f, 1); // torres doradas
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int mapWidth = currentLevel == 1 ? MAP_WIDTH_L1 : MAP_WIDTH_L2;
        camera.position.x = Math.max(400, Math.min(player.getX() + 16, mapWidth - 400));
        camera.position.y = 300;
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        if (!paused) {
            update(delta);
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawPlatforms();
        drawExit();
        player.render(shapeRenderer);
        drawNPCs();
        drawProjectiles();
        shapeRenderer.end();

        game.batch.setProjectionMatrix(hudCamera.combined);
        hud.render(game.batch, 600);
        game.batch.setProjectionMatrix(camera.combined);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new PauseScreen(game, this));
        }
    }

    // ==================== UPDATE ====================
    private void update(float delta) {
        player.update(delta);
        handlePlayerPlatformCollisions();

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

        checkExit();
        checkLevelComplete();
        checkGameOver();
    }

    // ==================== PUERTA DE SALIDA ====================
    private boolean allEnemiesDeadLevel1() {
        for (Skeleton s : skeletons) if (s.isAlive()) return false;
        for (DarkMage m : darkMages) if (m.isAlive()) return false;
        return true;
    }

    private boolean allEnemiesDeadLevel2() {
        for (Skeleton s : skeletons) if (s.isAlive()) return false;
        for (DarkMage m : darkMages) if (m.isAlive()) return false;
        if (ogre != null && ogre.isAlive()) return false;
        return true;
    }

    private void checkExit() {
        if (currentLevel == 1 && allEnemiesDeadLevel1()) {
            exitVisible = true;
        }
        if (currentLevel == 2 && allEnemiesDeadLevel2()) {
            exitVisible = true;
        }
    }

    private void checkLevelComplete() {
        if (!exitVisible) return;

        boolean playerAtExit = player.getX() + player.getWidth() > exitDoor[0] &&
            player.getX() < exitDoor[0] + exitDoor[2] &&
            player.getY() + player.getHeight() > exitDoor[1] &&
            player.getY() < exitDoor[1] + exitDoor[3];

        if (playerAtExit) {
            if (currentLevel == 1) {
                setupLevel2();
            } else {
                // Victoria!
                SoundManager.getInstance().stopMusic();
                game.setScreen(new WinScreen(game));
            }
        }
    }

    private void drawExit() {
        if (!exitVisible) return;
        // Puerta parpadeante en verde
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(exitDoor[0], exitDoor[1], exitDoor[2], exitDoor[3]);
        shapeRenderer.setColor(Color.LIME);
        shapeRenderer.rect(exitDoor[0] + 5, exitDoor[1] + 5, exitDoor[2] - 10, exitDoor[3] - 10);
    }

    // ==================== COLISIONES ====================
    private void handlePlayerPlatformCollisions() {
        boolean onAnyPlatform = false;

        for (float[] plat : platforms) {
            float px = plat[0], py = plat[1], pw = plat[2], ph = plat[3];
            float platTop = py + ph;

            boolean overlapX = player.getX() + player.getWidth() > px &&
                player.getX() < px + pw;
            boolean fallingOnto = player.getY() <= platTop + 5 &&
                player.getY() >= platTop - 10;
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
        float m = state.getDifficultyMultiplier();

        for (Skeleton s : skeletons) {
            if (s.isAlive() && s.overlaps(player.getX(), player.getY(),
                player.getWidth(), player.getHeight())) {
                player.takeDamage((int)(10 * m));
            }
        }
        for (DarkMage dm : darkMages) {
            if (dm.isAlive() && dm.overlaps(player.getX(), player.getY(),
                player.getWidth(), player.getHeight())) {
                player.takeDamage((int)(15 * m));
            }
        }
        if (ogre != null && ogre.isAlive() &&
            ogre.overlaps(player.getX(), player.getY(),
                player.getWidth(), player.getHeight())) {
            player.takeDamage((int)(25 * m));
        }
    }

    private void handleMageProjectileCollisions() {
        float m = state.getDifficultyMultiplier();
        for (DarkMage dm : darkMages) {
            if (!dm.isAlive()) continue; // ← ignorar proyectiles de magos muertos
            for (float[] proj : dm.getProjectiles()) {
                boolean hitsPlayer = proj[0] < player.getX() + player.getWidth() &&
                    proj[0] + 10 > player.getX() &&
                    proj[1] < player.getY() + player.getHeight() &&
                    proj[1] + 10 > player.getY();
                if (hitsPlayer) {
                    player.takeDamage((int)(20 * m));
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

    // ==================== DIBUJO ====================
    private void drawPlatforms() {
        // Color diferente según nivel
        if (currentLevel == 1) {
            shapeRenderer.setColor(0.3f, 0.2f, 0.1f, 1); // marrón mazmorra
        } else {
            shapeRenderer.setColor(0.5f, 0.4f, 0.2f, 1); // dorado torres
        }
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
        hudCamera.setToOrtho(false, 800, 600);
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
