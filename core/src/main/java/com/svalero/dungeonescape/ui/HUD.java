package com.svalero.dungeonescape.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.svalero.dungeonescape.managers.GameState;

public class HUD {

    private BitmapFont font;
    private GameState state;

    public HUD() {
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.4f);
        state = GameState.getInstance();
    }

    public void render(SpriteBatch batch, int screenHeight) {
        batch.begin();

        font.setColor(Color.GOLD);
        font.draw(batch, "Puntuacion: " + state.getScore(), 10, screenHeight - 10);

        font.setColor(Color.RED);
        font.draw(batch, "Vidas: " + state.getLives(), 10, screenHeight - 35);

        font.setColor(Color.CYAN);
        font.draw(batch, "Nivel: " + state.getCurrentLevel(), 10, screenHeight - 60);

        batch.end();
    }

    public void dispose() {
        font.dispose();
    }
}
