package org.spaceinvaders.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import org.spaceinvaders.entities.Lives;
import org.spaceinvaders.entities.Ship;
import org.spaceinvaders.entities.EnemyManager;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private Canvas gameCanvas;

    private Ship ship;
    private EnemyManager enemyManager;
    private GraphicsContext gc;

    private boolean gameOver = false; // Nueva variable para detener el juego

    private Lives vidas;


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
        vidas = new Lives();
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
        if (gameOver) return; // ✅ Evita que el juego siga ejecutándose tras perder

        ship.updateProjectiles();
        enemyManager.moveEnemies();
        enemyManager.updateProjectiles(ship.getProjectiles());

        if (enemyManager.checkCollisionWithShip(ship)) {
            vidas.reducirVida();
        }

        if (vidas.getVidas() <= 0) {
            gameOver();
            return;
        }

        draw();
    }


    private void draw() {
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        ship.draw(gc);
        enemyManager.draw(gc);
        enemyManager.drawProjectiles(gc); // Dibujar disparos enemigos

        vidas.draw(gc); // ✅ Ahora se dibujan las vidas desde la nueva clase

    }

    private void gameOver() {
        gameOver = true; // ✅ Evita que el bucle de actualización siga corriendo
        enemyManager.stopEnemyShooting(); // ✅ Detiene los disparos enemigos

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Fin del Juego");
            alert.setHeaderText("💀 Has perdido 💀");
            alert.setContentText("¿Quieres reiniciar la partida?");

            ButtonType restartButton = new ButtonType("Reiniciar");
            ButtonType exitButton = new ButtonType("Salir");

            alert.getButtonTypes().setAll(restartButton, exitButton);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == restartButton) {
                restartGame(); // Reiniciar el juego
            } else {
                System.exit(0); // Cerrar la aplicación
            }
        });
    }

    private void restartGame() {
        gameOver = false; // ✅ Reactivar el juego
        ship = new Ship(); // ✅ Crear nueva nave
        enemyManager = new EnemyManager(); // ✅ Reiniciar enemigos
        enemyManager.scheduleEnemyShots(); // ✅ Volver a iniciar los disparos
        vidas.reiniciar(); // ✅ Restaurar las 3 vidas

        draw(); // ✅ Redibujar la pantalla
    }


}
