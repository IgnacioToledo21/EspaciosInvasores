package org.spaceinvaders.models;

import javafx.beans.property.*;

public class ScoreEntry {
    private final StringProperty playerName;
    private final IntegerProperty score;
    private final IntegerProperty lives;

    public ScoreEntry(String playerName, int score, int lives) {
        this.playerName = new SimpleStringProperty(playerName);
        this.score = new SimpleIntegerProperty(score);
        this.lives = new SimpleIntegerProperty(lives);
    }

    public StringProperty playerNameProperty() {
        return playerName;
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public IntegerProperty livesProperty() {
        return lives;
    }

    public String getPlayerName() {
        return playerName.get();
    }

    public int getScore() {
        return score.get();
    }

    public int getLives() {
        return lives.get();
    }
}
