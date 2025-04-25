package org.spaceinvaders.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {

    private static MediaPlayer currentPlayer;
    private static double volume = 1.0; // Volumen global de música (0.0 - 1.0)

    public enum Track {
        MENU("/sounds/menus/my-8-bit-hero-301280.mp3"),
        GAME("/sounds/menus/i-love-my-8-bit-game-console-301272.mp3"),
        BOSS("/sounds/menus/fun-with-my-8-bit-game-301278.mp3"),
        WIN("/sounds/menus/victory-electronic-video-game-soundtrack-denouement-credits-153944.mp3"),
        GAMEOVER("/sounds/menus/kl-peach-game-over-ii-135684.mp3");

        private final String path;

        Track(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    /**
     * Reproduce la pista especificada en bucle.
     */
    public static void play(Track track) {
        stop(); // Detener cualquier reproducción anterior

        Media media = new Media(MusicManager.class.getResource(track.getPath()).toExternalForm());
        currentPlayer = new MediaPlayer(media);
        currentPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        currentPlayer.setVolume(volume);
        currentPlayer.play();
    }

    /**
     * Detiene la reproducción actual.
     */
    public static void stop() {
        if (currentPlayer != null) {
            currentPlayer.stop();
        }
    }

    /**
     * Pausa la reproducción actual.
     */
    public static void pause() {
        if (currentPlayer != null) {
            currentPlayer.pause();
        }
    }

    /**
     * Reanuda la reproducción pausada.
     */
    public static void resume() {
        if (currentPlayer != null) {
            currentPlayer.play();
        }
    }

    /**
     * Ajusta el volumen global de la música.
     */
    public static void setVolume(double newVolume) {
        volume = newVolume;
        if (currentPlayer != null) {
            currentPlayer.setVolume(volume);
        }
    }

    /**
     * Obtiene el volumen global actual.
     */
    public static double getVolume() {
        return volume;
    }
}
