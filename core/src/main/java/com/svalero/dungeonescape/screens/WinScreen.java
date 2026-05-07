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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.svalero.dungeonescape.DungeonEscape;
import com.svalero.dungeonescape.managers.GameState;
import com.svalero.dungeonescape.managers.ScoreManager;

public class WinScreen implements Screen {

    private final DungeonEscape game;
    private Stage stage;
    private Skin skin;
    private TextField nameField;
    private boolean scoreSaved = false;

    public WinScreen(DungeonEscape game) {
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

        Label titleLabel = new Label("¡VICTORIA!", skin);
        Label subtitleLabel = new Label("Has escapado del castillo!", skin);
        Label scoreLabel = new Label("Puntuacion final: " + GameState.getInstance().getScore(), skin);
        Label nameLabel = new Label("Introduce tu nombre:", skin);

        nameField = new TextField("", skin);
        nameField.setMaxLength(20);
        nameField.setMessageText("Tu nombre...");

        TextButton btnSave = new TextButton("Guardar puntuacion", skin);
        TextButton btnScores = new TextButton("Ver Top 10", skin);
        TextButton btnMenu = new TextButton("Menu Principal", skin);

        table.add(titleLabel).padBottom(10).row();
        table.add(subtitleLabel).padBottom(20).row();
        table.add(scoreLabel).padBottom(30).row();
        table.add(nameLabel).padBottom(10).row();
        table.add(nameField).width(300).height(40).padBottom(15).row();
        table.add(btnSave).width(250).height(50).padBottom(15).row();
        table.add(btnScores).width(250).height(50).padBottom(15).row();
        table.add(btnMenu).width(250).height(50).row();

        btnSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String name = nameField.getText().trim();
                if (name.isEmpty()) name = "Anonimo";
                if (!scoreSaved) {
                    ScoreManager.saveScore(name, GameState.getInstance().getScore());
                    scoreSaved = true;
                    GameState.getInstance().setPlayerName(name);
                    btnSave.setText("Puntuacion guardada!");
                    btnSave.setDisabled(true);
                }
            }
        });

        btnScores.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ScoreScreen(game));
            }
        });

        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.15f, 0.05f, 1); // verde oscuro
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
