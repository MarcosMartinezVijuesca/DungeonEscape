package com.svalero.dungeonescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.svalero.dungeonescape.DungeonEscape;
import com.svalero.dungeonescape.entities.EnemyProjectile;
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
    private boolean initialized = false;


    // Plataformas [x, y, width, height]
    private float[][] platforms;
    private Texture[] wallTiles; // [0]=wall1 ... [8]=wall9
    private static final int TILE = 16;

    // Nivel actual
    private int currentLevel = 1;
    private static final int MAP_WIDTH_L1 = 2400;
    private static final int MAP_WIDTH_L2 = 2400;
    private static final int MAP_WIDTH_L3 = 1200;

    // Puerta de salida nivel 1 [x, y, width, height]
    private float[] exitDoor = {2330, 40, 40, 80};
    private boolean exitVisible = false;

    public GameScreen(DungeonEscape game) {
        this.game = game;
    }

    @Override
    public void show() {
        if (initialized) return;
        initialized = true;

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

        wallTiles = new Texture[9];
        for (int i = 0; i < 9; i++) {
            wallTiles[i] = new Texture(Gdx.files.internal("sprites/environment/tiles/wall" + (i + 1) + ".png"));
        }

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
            {0, 20, 2400, 20},
            {100, 140, 150, 20},
            {320, 220, 150, 20},
            {550, 170, 150, 20},
            {750, 250, 120, 20},
            {950, 180, 150, 20},
            {1150, 280, 130, 20},
            {1350, 200, 150, 20},
            {1550, 300, 120, 20},
            {1750, 220, 150, 20},
            {1950, 150, 130, 20},
            {2100, 260, 150, 20},
        };

        float m = state.getDifficultyMultiplier();

        skeletons = new ArrayList<>();
        skeletons.add(new Skeleton(300, 40, 200, 500, m));
        skeletons.add(new Skeleton(700, 40, 550, 900, m));
        skeletons.add(new Skeleton(1200, 40, 1000, 1400, m));
        skeletons.add(new Skeleton(1800, 40, 1600, 2000, m));

        darkMages = new ArrayList<>();
        darkMages.add(new DarkMage(800, 40, m));
        darkMages.add(new DarkMage(1500, 40, m));
        darkMages.add(new DarkMage(2100, 40, m));

        // Ogro temporal para pruebas — QUITAR ANTES DE ENTREGAR
        ogre = new Ogre(600, 40, 200, 1000, m * 1.8f);
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
            {0, 20, 2400, 20},   // suelo
            {50, 160, 120, 20},   // plataforma 1
            {250, 260, 100, 20},   // plataforma 2
            {420, 180, 130, 20},   // plataforma 3
            {600, 300, 110, 20},   // plataforma 4
            {780, 200, 140, 20},   // plataforma 5
            {980, 320, 120, 20},   // plataforma 6
            {1150, 240, 130, 20},   // plataforma 7
            {1350, 350, 100, 20},   // plataforma 8
            {1520, 260, 140, 20},   // plataforma 9
            {1700, 180, 120, 20},   // plataforma 10
            {1900, 300, 130, 20},   // plataforma 11
            {2100, 220, 150, 20},   // plataforma 12
        };

        float m = state.getDifficultyMultiplier();

        // Más enemigos en nivel 2
        skeletons = new ArrayList<>();
        skeletons.add(new Skeleton(250, 40, 150, 500, m));
        skeletons.add(new Skeleton(600, 40, 450, 850, m));
        skeletons.add(new Skeleton(1000, 40, 800, 1200, m));
        skeletons.add(new Skeleton(1400, 40, 1200, 1600, m));
        skeletons.add(new Skeleton(1900, 40, 1700, 2100, m));

        darkMages = new ArrayList<>();
        darkMages.add(new DarkMage(400, 40, m));
        darkMages.add(new DarkMage(900, 40, m));
        darkMages.add(new DarkMage(1600, 40, m));
        darkMages.add(new DarkMage(2000, 40, m));

        ogre = null;

    }

    // ==================== NIVEL 3 ====================
    private void setupLevel3() {
        currentLevel = 3;
        state.setCurrentLevel(3);
        exitVisible = false;
        projectiles.clear();

        player.setPosition(100, 50);
        player.revive();

        // Sala del trono — plataformas simétricas para el boss fight
        platforms = new float[][]{
            {0, 20, 1200, 20},  // suelo
            {100, 160, 100, 20},  // plataforma izquierda baja
            {200, 280, 100, 20},  // plataforma izquierda alta
            {550, 320, 100, 20},  // plataforma central alta
            {900, 280, 100, 20},  // plataforma derecha alta
            {1000, 160, 100, 20},  // plataforma derecha baja
        };

        skeletons = new ArrayList<>();
        darkMages = new ArrayList<>();

        float m = state.getDifficultyMultiplier();
        ogre = new Ogre(600, 40, 200, 1000, m * 1.8f);
    }

    // ==================== RENDER ====================
    @Override
    public void render(float delta) {
        // Color de fondo según nivel
        if (currentLevel == 1) {
            Gdx.gl.glClearColor(0.08f, 0.08f, 0.12f, 1);
        } else if (currentLevel == 2) {
            Gdx.gl.glClearColor(0.15f, 0.12f, 0.05f, 1);
        } else {
            Gdx.gl.glClearColor(0.12f, 0.05f, 0.05f, 1);
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int mapWidth = currentLevel == 1 ? MAP_WIDTH_L1 :
            currentLevel == 2 ? MAP_WIDTH_L2 : MAP_WIDTH_L3;
        camera.position.x = Math.max(400, Math.min(player.getX() + 16, mapWidth - 400));
        camera.position.y = 300;
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        game.batch.setProjectionMatrix(camera.combined);

        if (!paused) {
            update(delta);
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawExit();
        shapeRenderer.end();
        game.batch.setProjectionMatrix(camera.combined);
        drawPlatforms();

        // SpriteBatch para jugador y NPCs con cámara del juego
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.render(game.batch);
        for (Projectile p : projectiles) p.render(game.batch); // ← añadir aquí
        for (Skeleton s : skeletons) {
            if (s.isAlive() || s.isDying()) s.render(game.batch);
        }
        for (DarkMage m : darkMages) {
            if (m.isAlive() || m.isDying()) m.render(game.batch);
            for (EnemyProjectile proj : m.getProjectiles()) {
                proj.render(game.batch);
            }
        }
        if (ogre != null && (ogre.isAlive() || ogre.isDying())) {
            ogre.render(game.batch);
        }
        game.batch.end();

        // HUD con cámara fija
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
            float projY = player.getY() + player.getHeight() - 10f;
            projectiles.add(new Projectile(projX, projY, player.isFacingRight()));
            player.shoot();
        }

        for (Projectile p : projectiles) p.update(delta);
        projectiles.removeIf(p -> !p.isActive());

        for (Skeleton s : skeletons) {
            if (s.isAlive() || s.isDying()) {
                s.update(delta, player.getX(), player.getY());
            }
        }
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
        exitVisible = true;

        // Nivel 3: victoria directa al morir el ogro
        if (currentLevel == 3 && ogre != null && !ogre.isAlive()) {
            SoundManager.getInstance().stopMusic();
            game.setScreen(new WinScreen(game));
        }
    }

    private void checkLevelComplete() {
        if (!exitVisible) return;

        boolean playerAtExit = player.getX() + player.getWidth() > exitDoor[0] &&
            player.getX() < exitDoor[0] + exitDoor[2] &&
            player.getY() + player.getHeight() > exitDoor[1] &&
            player.getY() < exitDoor[1] + exitDoor[3];

        if (playerAtExit) {
            // Bonus por matar todos los enemigos antes de pasar
            if (allEnemiesDeadLevel1() && currentLevel == 1) {
                state.addScore(500);
            }
            if (allEnemiesDeadLevel2() && currentLevel == 2) {
                state.addScore(1000);
            }

            if (currentLevel == 1) {
                setupLevel2();
            } else if (currentLevel == 2) {
                setupLevel3();
            } else {
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
                if (s.isAlive() && proj.overlaps(s.getX(), s.getY() + 20, s.getWidth(), s.getHeight())) {
                    s.takeDamage(proj.getDamage());
                    proj.deactivate();
                    if (!s.isAlive()) state.addScore(s.getScoreValue());
                }
            }
            for (DarkMage m : darkMages) {
                if (m.isAlive() && proj.overlaps(m.getX() + 60, m.getY() - 20, 60, 150)) {
                    m.takeDamage(proj.getDamage());
                    proj.deactivate();
                    if (!m.isAlive()) state.addScore(m.getScoreValue());
                }
            }
            if (ogre != null && ogre.isAlive() &&
                proj.overlaps(ogre.getX(), ogre.getY() + 20, ogre.getWidth(), ogre.getHeight())) {
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
                player.takeDamage((int) (10 * m), true);
            }
        }
        if (ogre != null && ogre.isAlive() &&
            ogre.overlaps(player.getX() - 80, player.getY(),
                player.getWidth() + 150, player.getHeight())) {
            player.takeDamage((int)(25 * m), false);
        }
    }

    private void handleMageProjectileCollisions() {
        float m = state.getDifficultyMultiplier();
        for (DarkMage dm : darkMages) {
            for (EnemyProjectile proj : dm.getProjectiles()) {
                if (proj.overlaps(player.getX(), player.getY(),
                    player.getWidth(), player.getHeight())) {
                    player.takeDamage((int)(20 * m), false);
                    proj.deactivate();
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
        game.batch.begin();
        for (float[] plat : platforms) {
            drawPlatformTiled(plat[0], plat[1], plat[2], plat[3]);
        }
        game.batch.end();
    }

    private void drawPlatformTiled(float px, float py, float pw, float ph) {
        int cols = Math.max(1, Math.round(pw / TILE));
        int rows = Math.max(1, Math.round(ph / TILE));

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int tileIndex = getTileIndex(col, row, cols, rows);
                float drawX = px + col * TILE;
                float drawY = py + row * TILE;
                game.batch.draw(wallTiles[tileIndex], drawX, drawY, TILE, TILE);
            }
        }
    }

    private int getTileIndex(int col, int row, int cols, int rows) {
        boolean isTop = (row == rows - 1);
        boolean isBottom = (row == 0);
        boolean isLeft = (col == 0);
        boolean isRight = (col == cols - 1);

        if (isTop && isLeft) return 0; // wall1
        if (isTop && isRight) return 2; // wall3
        if (isTop) return 1; // wall2
        if (isBottom && isLeft) return 6; // wall7
        if (isBottom && isRight) return 8; // wall9
        if (isBottom) return 7; // wall8
        if (isLeft) return 3; // wall4
        if (isRight) return 5; // wall6
        return 4;                          // wall5 (centro)
    }

    private void drawNPCs() {
        game.batch.begin();
        for (Skeleton s : skeletons) s.render(game.batch);
        for (DarkMage m : darkMages) m.render(game.batch);
        if (ogre != null) ogre.render(game.batch);
        game.batch.end();
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

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        hud.dispose();
        SoundManager.getInstance().dispose();
        if (wallTiles != null) {
            for (Texture t : wallTiles) if (t != null) t.dispose();
        }
    }
}
