package com.svalero.dungeonescape.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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

public class MainMenuScreen implements Screen {

    private final DungeonEscape game;
    private Stage stage;
    private Skin skin;

    public MainMenuScreen(DungeonEscape game) {
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
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = skin.getFont("default-font");
        titleStyle.fontColor = Color.GOLD;
        Label titleLabel = new Label("DUNGEON ESCAPE", titleStyle);
        titleLabel.setFontScale(2f);

        // Botones
        TextButton btnPlay = new TextButton("Jugar", skin);
        TextButton btnConfig = new TextButton("Configuracion", skin);
        TextButton btnInstructions = new TextButton("Instrucciones", skin);
        TextButton btnExit = new TextButton("Salir", skin);

        // Layout
        table.add(titleLabel).padBottom(60).row();
        table.add(btnPlay).width(250).height(50).padBottom(15).row();
        table.add(btnConfig).width(250).height(50).padBottom(15).row();
        table.add(btnInstructions).width(250).height(50).padBottom(15).row();
        table.add(btnExit).width(250).height(50).row();

        // Listeners
        btnPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameState.getInstance().reset();
                game.setScreen(new GameScreen(game));
            }
        });

        btnConfig.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ConfigScreen(game));
            }
        });

        btnInstructions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new InstructionsScreen(game));
            }
        });

        btnExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
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
