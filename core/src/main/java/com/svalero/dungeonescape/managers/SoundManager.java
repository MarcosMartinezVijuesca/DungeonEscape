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
    private Sound orcHitSound;
    private Sound orcDeathSound;
    private Sound skeletonHitSound;
    private Sound skeletonDeathSound;
    private Sound playerGruntSound;
    private Sound mageHitSound;
    private Sound mageDeathSound;



    private SoundManager() {}

    public static SoundManager getInstance() {
        if (instance == null) instance = new SoundManager();
        return instance;
    }

    public void load() {
        try {
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/dungeon_music.wav"));
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(0.5f);
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "Música no encontrada");
        }
        try {
            jumpSound = Gdx.audio.newSound(Gdx.files.internal("audio/jump.wav"));
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "jump.wav no encontrado");
        }
        try {
            shootSound = Gdx.audio.newSound(Gdx.files.internal("audio/fireball.wav"));
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "fireball.wav no encontrado");
        }
        try {
            hitSound = Gdx.audio.newSound(Gdx.files.internal("audio/impact_chain2.ogg"));
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "ERROR cargando impact_chain.wav: " + e.getMessage());
        }
        try {
            deathSound = Gdx.audio.newSound(Gdx.files.internal("audio/mage_death.wav"));
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "mage_death.wav no encontrado");
        }
        try {
            orcHitSound = Gdx.audio.newSound(Gdx.files.internal("audio/orc_hit.wav"));
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "orc_hit.wav no encontrado");
        }
        try {
            orcDeathSound = Gdx.audio.newSound(Gdx.files.internal("audio/orc_death.wav"));
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "orc_death.wav no encontrado");
        }
        try {
            skeletonHitSound = Gdx.audio.newSound(Gdx.files.internal("audio/skeleton_hit.wav"));
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "skeleton_hit.wav no encontrado");
        }
        try {
            skeletonDeathSound = Gdx.audio.newSound(Gdx.files.internal("audio/skeleton_death.wav"));
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "skeleton_death.wav no encontrado");
        }
        try {
            playerGruntSound = Gdx.audio.newSound(Gdx.files.internal("audio/player_grunt.ogg"));
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "player_grunt.ogg no encontrado");
        }
        try {
            mageHitSound = Gdx.audio.newSound(Gdx.files.internal("audio/mage_hit.wav"));
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "mage_hit.wav no encontrado");
        }
        try {
            mageDeathSound = Gdx.audio.newSound(Gdx.files.internal("audio/mage_death.wav"));
        } catch (Exception e) {
            Gdx.app.log("SoundManager", "mage_death.wav no encontrado");
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
        if (orcHitSound != null) orcHitSound.dispose();
        if (orcDeathSound != null) orcDeathSound.dispose();
        if (skeletonHitSound != null) skeletonHitSound.dispose();
        if (skeletonDeathSound != null) skeletonDeathSound.dispose();
        if (playerGruntSound != null) playerGruntSound.dispose();
        if (mageHitSound != null) mageHitSound.dispose();
        if (mageDeathSound != null) mageDeathSound.dispose();
    }

    public void playOrcHit() {
        if (orcHitSound != null && GameState.getInstance().isSoundEnabled())
            orcHitSound.play(1f);
    }

    public void playOrcDeath() {
        if (orcDeathSound != null && GameState.getInstance().isSoundEnabled())
            orcDeathSound.play(1f);
    }

    public void playSkeletonHit() {
        if (skeletonHitSound != null && GameState.getInstance().isSoundEnabled())
            skeletonHitSound.play(1f);
    }

    public void playSkeletonDeath() {
        if (skeletonDeathSound != null && GameState.getInstance().isSoundEnabled())
            skeletonDeathSound.play(1f);
    }

    public void playPlayerGrunt() {
        if (playerGruntSound != null && GameState.getInstance().isSoundEnabled())
            playerGruntSound.play(1f);
    }

    public void playMageHit() {
        if (mageHitSound != null && GameState.getInstance().isSoundEnabled())
            mageHitSound.play(1f);
    }

    public void playMageDeath() {
        if (mageDeathSound != null && GameState.getInstance().isSoundEnabled())
            mageDeathSound.play(1f);
    }

}
