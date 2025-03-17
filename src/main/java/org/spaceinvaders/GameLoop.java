package org.spaceinvaders;

import javafx.animation.AnimationTimer;
import org.spaceinvaders.entities.EnemyManager;

public class GameLoop {

    private EnemyManager enemyManager;

    public GameLoop() {
        this.enemyManager = new EnemyManager();
    }

    public void startGame() {
        // Utiliza AnimationTimer para actualizar el juego a 60 FPS
        AnimationTimer gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                enemyManager.moveEnemies();
            }
        };
        gameTimer.start();  // Inicia el bucle de animación
    }
}
