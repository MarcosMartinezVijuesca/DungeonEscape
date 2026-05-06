package com.svalero.dungeonescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.svalero.dungeonescape.DungeonEscape;
import com.svalero.dungeonescape.managers.GameState;
import com.svalero.dungeonescape.managers.SoundManager;

public class ConfigScreen implements Screen {

    private final DungeonEscape game;
    private Stage stage;
    private Skin skin;

    public ConfigScreen(DungeonEscape game) {
        this.game = game;
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

        // Título
        Label titleLabel = new Label("CONFIGURACION", skin);
        table.add(titleLabel).padBottom(40).row();

        // Opción 1: Sonido activado/desactivado
        Label soundLabel = new Label("Sonido:", skin);
        CheckBox soundCheckBox = new CheckBox(" Activado", skin);
        soundCheckBox.setChecked(GameState.getInstance().isSoundEnabled());

        Table soundRow = new Table();
        soundRow.add(soundLabel).padRight(20);
        soundRow.add(soundCheckBox);
        table.add(soundRow).padBottom(25).row();

        soundCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.getInstance().toggleSound();
            }
        });

        // Opción 2: Dificultad
        Label diffLabel = new Label("Dificultad:", skin);
        SelectBox<String> diffSelect = new SelectBox<>(skin);
        diffSelect.setItems("Facil", "Normal", "Dificil");
        diffSelect.setSelected(GameState.getInstance().getDifficulty());

        Table diffRow = new Table();
        diffRow.add(diffLabel).padRight(20);
        diffRow.add(diffSelect).width(150);
        table.add(diffRow).padBottom(40).row();

        diffSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameState.getInstance().setDifficulty(diffSelect.getSelected());
            }
        });

        // Botón volver
        TextButton btnBack = new TextButton("Volver al Menu", skin);
        table.add(btnBack).width(250).height(50).row();

        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.08f, 0.08f, 0.12f, 1);
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
    public void hide() { dispose(); }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
