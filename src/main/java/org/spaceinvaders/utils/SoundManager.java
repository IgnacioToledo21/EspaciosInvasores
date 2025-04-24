package org.spaceinvaders.utils;

import javafx.scene.media.AudioClip;

public class SoundManager {

    // Definir las rutas de los sonidos
    public enum Sound {
        SHOT("/effects/lasergun-35817.mp3"),
        DAMAGE("/effects/bullet-hit-metal-84818.mp3"),
        BOMB("/effects/explosion-42132.mp3"),
        ENEMY_DESTROYED("/effects/death-103830.mp3"),;

        private String path;

        Sound(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    // Metodo para reproducir un sonido
    public static void play(Sound sound) {
        AudioClip soundClip = new AudioClip(SoundManager.class.getResource(sound.getPath()).toExternalForm());
        soundClip.play();
    }
}
