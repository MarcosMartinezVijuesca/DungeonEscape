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
import com.svalero.dungeonescape.managers.ScoreManager;

import java.util.List;

public class ScoreScreen implements Screen {

    private final DungeonEscape game;
    private Stage stage;
    private Skin skin;

    public ScoreScreen(DungeonEscape game) {
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

        Label titleLabel = new Label("TOP 10 PUNTUACIONES", skin);
        table.add(titleLabel).padBottom(30).row();

        // Cargar y mostrar puntuaciones
        List<ScoreManager.ScoreEntry> scores = ScoreManager.loadScores();

        if (scores.isEmpty()) {
            Label emptyLabel = new Label("No hay puntuaciones todavia", skin);
            table.add(emptyLabel).padBottom(20).row();
        } else {
            for (int i = 0; i < scores.size(); i++) {
                ScoreManager.ScoreEntry entry = scores.get(i);
                String line = (i + 1) + ".  " + entry.name + "  -  " + entry.score + " pts";
                Label scoreLabel = new Label(line, skin);
                table.add(scoreLabel).padBottom(8).row();
            }
        }

        TextButton btnBack = new TextButton("Volver", skin);
        table.add(btnBack).width(200).height(50).padTop(30).row();

        btnBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.08f, 0.08f, 0.15f, 1);
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
