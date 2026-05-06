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

public class InstructionsScreen implements Screen {

    private final DungeonEscape game;
    private Stage stage;
    private Skin skin;

    public InstructionsScreen(DungeonEscape game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Tabla exterior que ocupa toda la pantalla
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();
        stage.addActor(rootTable);

        // Tabla interior con el contenido
        Table contentTable = new Table();
        contentTable.center();

        Label titleLabel = new Label("INSTRUCCIONES", skin);
        contentTable.add(titleLabel).padBottom(30).row();

        String[] instrucciones = {
            "MOVIMIENTO",
            "Mover izquierda:   A / Flecha izquierda",
            "Mover derecha:     D / Flecha derecha",
            "Saltar:            ESPACIO / Flecha arriba / W",
            "Doble salto:       Pulsa ESPACIO dos veces",
            "",
            "COMBATE",
            "Disparar:          F",
            "Pausa:             ESC",
            "",
            "OBJETIVO",
            "Derrota a todos los enemigos y escapa",
            "de las mazmorras del castillo.",
            "",
            "ENEMIGOS",
            "Esqueleto (blanco):  100 pts  - Patrulla",
            "Mago oscuro (lila):  200 pts  - Dispara",
            "Ogro (verde):        500 pts  - Persigue",
        };

        for (String linea : instrucciones) {
            Label label = new Label(linea, skin);
            contentTable.add(label).padBottom(4).row();
        }

        // ScrollPane con la tabla de contenido
        com.badlogic.gdx.scenes.scene2d.ui.ScrollPane scrollPane =
            new com.badlogic.gdx.scenes.scene2d.ui.ScrollPane(contentTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false); // solo scroll vertical

        TextButton btnBack = new TextButton("Volver al Menu", skin);

        rootTable.add(scrollPane).width(600).height(450).padBottom(20).row();
        rootTable.add(btnBack).width(250).height(50).row();

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
