package com.svalero.dungeonescape.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationHelper {

    // Carga una animación desde imágenes separadas
    public static Animation<TextureRegion> loadAnimationFromFiles(String[] paths, float frameDuration) {
        TextureRegion[] frames = new TextureRegion[paths.length];
        for (int i = 0; i < paths.length; i++) {
            Texture tex = new Texture(Gdx.files.internal(paths[i]));
            tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            frames[i] = new TextureRegion(tex);
        }
        return new Animation<>(frameDuration, frames);
    }

    public static Animation<TextureRegion> loadAnimation(String path, int frameCount, float frameDuration) {
        Texture sheet = new Texture(Gdx.files.internal(path));
        sheet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int frameWidth = sheet.getWidth() / frameCount;
        int frameHeight = sheet.getHeight();

        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new TextureRegion(sheet, i * frameWidth, 0, frameWidth, frameHeight);
        }
        return new Animation<>(frameDuration, frames);
    }

    public static Animation<TextureRegion> loadAnimationMultiRow(String path, int cols, int rows, float frameDuration) {
        Texture sheet = new Texture(Gdx.files.internal(path));
        sheet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int frameWidth = sheet.getWidth() / cols;
        int frameHeight = sheet.getHeight() / rows;

        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                frames[index++] = new TextureRegion(sheet, col * frameWidth, row * frameHeight, frameWidth, frameHeight);
            }
        }
        return new Animation<>(frameDuration, frames);
    }

    public static Animation<TextureRegion> loadAnimationFromRow(String path, int frameCount, int row, int totalRows, float frameDuration) {
        Texture sheet = new Texture(Gdx.files.internal(path));
        sheet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int frameWidth = sheet.getWidth() / frameCount;
        int frameHeight = sheet.getHeight() / totalRows;

        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = new TextureRegion(sheet, i * frameWidth, row * frameHeight, frameWidth, frameHeight);
        }
        return new Animation<>(frameDuration, frames);
    }

    public static Animation<TextureRegion> loadAnimationMultiRowPartial(
        String path, int cols, int rows, int totalFrames, float frameDuration) {
        Texture sheet = new Texture(Gdx.files.internal(path));
        sheet.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int frameWidth = sheet.getWidth() / cols;
        int frameHeight = sheet.getHeight() / rows;

        TextureRegion[] frames = new TextureRegion[totalFrames];
        int index = 0;
        outer:
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (index >= totalFrames) break outer;
                frames[index++] = new TextureRegion(sheet,
                    col * frameWidth, row * frameHeight, frameWidth, frameHeight);
            }
        }
        return new Animation<>(frameDuration, frames);
    }




}
