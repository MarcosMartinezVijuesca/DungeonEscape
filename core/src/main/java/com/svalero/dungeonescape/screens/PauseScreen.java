package com.svalero.dungeonescape.screens;

import com.badlogic.gdx.Screen;
import com.svalero.dungeonescape.DungeonEscape;

public class PauseScreen implements Screen {
    private final DungeonEscape game;
    private final GameScreen gameScreen;

    public PauseScreen(DungeonEscape game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
    }
    @Override public void show() {}
    @Override public void render(float delta) {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
