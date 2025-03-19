package org.spaceinvaders.controllers;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.spaceinvaders.entities.Lives;
import org.spaceinvaders.entities.Ship;
import org.spaceinvaders.entities.EnemyManager;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

public class RootController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private Canvas gameCanvas;

    private Ship ship;
    private Lives vidas;
    private EnemyManager enemyManager;
    private GraphicsContext gc;

    private boolean gameOver = false; // Nueva variable para detener el juego

    private Set<KeyCode> keysPressed = new HashSet<>();

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

        // Almacenar teclas presionadas
        gameCanvas.setOnKeyPressed(event -> keysPressed.add(event.getCode()));
        gameCanvas.setOnKeyReleased(event -> keysPressed.remove(event.getCode()));

        // Bucle de juego utilizando AnimationTimer
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameLoop.start();
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
        if (gameOver) return; // Detener el juego si se ha perdido

        // Movimiento fluido
        if (keysPressed.contains(KeyCode.LEFT)) {
            ship.moverIzquierda();
        }
        if (keysPressed.contains(KeyCode.RIGHT)) {
            ship.moverDerecha();
        }
        if (keysPressed.contains(KeyCode.SPACE)) {
            ship.fireProjectile();
        }

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
        gameOver = false;
        keysPressed.clear(); // ✅ Detener movimiento automático
        ship = new Ship();
        enemyManager = new EnemyManager();
        enemyManager.scheduleEnemyShots();
        vidas.reiniciar();
        draw();
    }
}