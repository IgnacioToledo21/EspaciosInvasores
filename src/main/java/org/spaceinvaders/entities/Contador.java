package org.spaceinvaders.entities;

public class Contador {
    private long startTime;
    private long endTime;
    private boolean running = false;

    public void start() {
        startTime = System.currentTimeMillis();
        running = true;
    }

    public void stop() {
        endTime = System.currentTimeMillis();
        running = false;
    }

    public long getElapsedTime() {
        return running ? System.currentTimeMillis() - startTime : endTime - startTime;
    }

    public String getFormattedElapsedTime() {
        long elapsedTime = getElapsedTime();
        long seconds = (elapsedTime / 1000) % 60;
        long minutes = (elapsedTime / (1000 * 60)) % 60;
        long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}