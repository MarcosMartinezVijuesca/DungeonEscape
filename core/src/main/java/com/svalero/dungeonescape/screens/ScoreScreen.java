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

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();
        stage.addActor(rootTable);

        Table contentTable = new Table();
        contentTable.center();

        Label titleLabel = new Label("TOP 10 PUNTUACIONES", skin);
        contentTable.add(titleLabel).padBottom(30).row();

        List<ScoreManager.ScoreEntry> scores = ScoreManager.loadScores();

        if (scores.isEmpty()) {
            Label emptyLabel = new Label("No hay puntuaciones todavia", skin);
            contentTable.add(emptyLabel).padBottom(20).row();
        } else {
            for (int i = 0; i < scores.size(); i++) {
                ScoreManager.ScoreEntry entry = scores.get(i);
                String line = (i + 1) + ".  " + entry.name + "  -  " + entry.score + " pts";
                Label scoreLabel = new Label(line, skin);
                contentTable.add(scoreLabel).padBottom(8).row();
            }
        }

        com.badlogic.gdx.scenes.scene2d.ui.ScrollPane scrollPane =
            new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane(contentTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        TextButton btnClear = new TextButton("Borrar puntuaciones", skin);
        TextButton btnBack = new TextButton("Menu Principal", skin);

        rootTable.add(scrollPane).width(500).height(300).padBottom(15).row();
        rootTable.add(btnClear).width(250).height(50).padBottom(10).row();
        rootTable.add(btnBack).width(200).height(50).row();

        btnClear.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.files.local("scores.txt").delete();
                game.setScreen(new ScoreScreen(game));
            }
        });

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
