package com.svalero.dungeonescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.svalero.dungeonescape.DungeonEscape;
import com.svalero.dungeonescape.managers.GameState;
import com.svalero.dungeonescape.managers.SoundManager;

public class PauseScreen implements Screen {

    private final DungeonEscape game;
    private final GameScreen gameScreen;
    private Stage stage;
    private Skin skin;
    private boolean initialized = false;

    public PauseScreen(DungeonEscape game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        Label titleLabel = new Label("PAUSA", skin);

        // Botón sonido dinámico según estado actual
        String soundText = GameState.getInstance().isSoundEnabled()
            ? "Desactivar Sonido"
            : "Activar Sonido";
        TextButton btnSound = new TextButton(soundText, skin);
        TextButton btnContinue = new TextButton("Continuar", skin);
        TextButton btnMenu = new TextButton("Menu Principal", skin);
        TextButton btnExit = new TextButton("Salir del juego", skin);

        table.add(titleLabel).padBottom(40).row();
        table.add(btnContinue).width(250).height(50).padBottom(15).row();
        table.add(btnSound).width(250).height(50).padBottom(15).row();
        table.add(btnMenu).width(250).height(50).padBottom(15).row();
        table.add(btnExit).width(250).height(50).row();

        // Continuar
        btnContinue.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameScreen.resumeGame();
                game.setScreen(gameScreen);
            }
        });

        // Activar/Desactivar sonido
        btnSound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.getInstance().toggleSound();
                String text = GameState.getInstance().isSoundEnabled()
                    ? "Desactivar Sonido"
                    : "Activar Sonido";
                btnSound.setText(text);
            }
        });

        // Volver al menú principal
        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.getInstance().stopMusic();
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // Salir del juego
        btnExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        // Fondo semitransparente oscuro
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
