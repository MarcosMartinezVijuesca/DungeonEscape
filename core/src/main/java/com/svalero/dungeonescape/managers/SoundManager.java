package com.svalero.dungeonescape.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    private static SoundManager instance;

    private Music backgroundMusic;
    private Sound jumpSound;
    private Sound shootSound;
    private Sound hitSound;
    private Sound deathSound;

    private SoundManager() {}

    public static SoundManager getInstance() {
        if (instance == null) instance = new SoundManager();
        return instance;
    }

    public void load() {
        try {
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/dungeon_music.ogg"));
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(0.5f);
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "Música no encontrada");
        }
        try {
            jumpSound = Gdx.audio.newSound(Gdx.files.internal("audio/jump.ogg"));
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "jump.ogg no encontrado");
        }
        try {
            shootSound = Gdx.audio.newSound(Gdx.files.internal("audio/shoot.ogg"));
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "shoot.ogg no encontrado");
        }
        try {
            hitSound = Gdx.audio.newSound(Gdx.files.internal("audio/hit.ogg"));
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "hit.ogg no encontrado");
        }
        try {
            deathSound = Gdx.audio.newSound(Gdx.files.internal("audio/death.ogg"));
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "death.ogg no encontrado");
        }
    }

    public void playMusic() {
        if (backgroundMusic != null && GameState.getInstance().isSoundEnabled())
            backgroundMusic.play();
    }

    public void stopMusic() {
        if (backgroundMusic != null) backgroundMusic.stop();
    }

    public void pauseMusic() {
        if (backgroundMusic != null) backgroundMusic.pause();
    }

    public void resumeMusic() {
        if (backgroundMusic != null && GameState.getInstance().isSoundEnabled())
            backgroundMusic.play();
    }

    public void toggleSound() {
        GameState state = GameState.getInstance();
        state.setSoundEnabled(!state.isSoundEnabled());
        if (state.isSoundEnabled()) {
            resumeMusic();
        } else {
            pauseMusic();
        }
    }

    public void playJump() {
        if (jumpSound != null && GameState.getInstance().isSoundEnabled())
            jumpSound.play(0.8f);
    }

    public void playShoot() {
        if (shootSound != null && GameState.getInstance().isSoundEnabled())
            shootSound.play(0.7f);
    }

    public void playHit() {
        if (hitSound != null && GameState.getInstance().isSoundEnabled())
            hitSound.play(1f);
    }

    public void playDeath() {
        if (deathSound != null && GameState.getInstance().isSoundEnabled())
            deathSound.play(1f);
    }

    public void dispose() {
        if (backgroundMusic != null) backgroundMusic.dispose();
        if (jumpSound != null) jumpSound.dispose();
        if (shootSound != null) shootSound.dispose();
        if (hitSound != null) hitSound.dispose();
        if (deathSound != null) deathSound.dispose();
    }
}
