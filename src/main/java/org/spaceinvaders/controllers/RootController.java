package org.spaceinvaders.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.spaceinvaders.entities.Ship;
import org.spaceinvaders.entities.EnemyManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private Canvas gameCanvas;

    private Ship ship;
    private EnemyManager enemyManager;
    private GraphicsContext gc;

    public RootController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = gameCanvas.getGraphicsContext2D();
        ship = new Ship();
        enemyManager = new EnemyManager();

        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(this::handleKeyPressed);

        // Bucle de juego
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(16);  // Aproximadamente 60 FPS
                    update();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public BorderPane getRoot() {
        return root;
    }

    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT -> ship.moverIzquierda();
            case RIGHT -> ship.moverDerecha();
            case SPACE -> ship.fireProjectile();
        }
    }

    private void update() {
        ship.updateProjectiles();
        enemyManager.moveEnemies();
        enemyManager.updateProjectiles(ship.getProjectiles()); // ✅ Ahora pasamos los proyectiles de la nave
        draw();
    }



    private void draw() {
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        ship.draw(gc);
        enemyManager.draw(gc);
        enemyManager.drawProjectiles(gc); // Dibujar disparos enemigos
    }

}
