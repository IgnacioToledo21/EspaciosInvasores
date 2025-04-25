package org.spaceinvaders.utils;

import javafx.scene.media.AudioClip;

public class SoundManager {

    private static double volume = 1.0; // Volumen por defecto

    public enum Sound {
        SHOT("/effects/lasergun-35817.mp3"),
        DAMAGE("/effects/bullet-hit-metal-84818.mp3"),
        BOMB("/effects/explosion-42132.mp3"),
        ENEMY_DESTROYED("/effects/death-103830.mp3");

        private final String path;

        Sound(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public static void play(Sound sound) {
        AudioClip soundClip = new AudioClip(SoundManager.class.getResource(sound.getPath()).toExternalForm());
        soundClip.setVolume(volume); // Aplicar volumen actual
        soundClip.play();
    }

    public static void setVolume(double newVolume) {
        volume = newVolume;
    }

    public static double getVolume() {
        return volume;
    }
}
