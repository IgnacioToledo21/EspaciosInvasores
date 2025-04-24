package org.spaceinvaders.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {

    private static MediaPlayer currentPlayer;

    public enum Track {
        MENU("/sounds/menus/my-8-bit-hero-301280.mp3"),
        GAME("/sounds/menus/i-love-my-8-bit-game-console-301272.mp3"),
        BOSS("/sounds/menus/fun-with-my-8-bit-game-301278.mp3"),
        WIN("/sounds/menus/victory-electronic-video-game-soundtrack-denouement-credits-153944.mp3"),
        GAME_OVER("/sounds/menus/kl-peach-game-over-ii-135684.mp3");

        private final String path;

        Track(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public static void play(Track track) {
        stop(); // Detenemos si ya hay música sonando

        Media media = new Media(MusicManager.class.getResource(track.getPath()).toExternalForm());
        currentPlayer = new MediaPlayer(media);
        currentPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        currentPlayer.play();
    }

    public static void stop() {
        if (currentPlayer != null) {
            currentPlayer.stop();
        }
    }

    public static void pause() {
        if (currentPlayer != null) {
            currentPlayer.pause();
        }
    }

    public static void resume() {
        if (currentPlayer != null) {
            currentPlayer.play();
        }
    }

    public static void setVolume(double volume) {
        if (currentPlayer != null) {
            currentPlayer.setVolume(volume); // De 0.0 a 1.0
        }
    }
}
