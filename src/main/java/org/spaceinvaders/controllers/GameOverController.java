package org.spaceinvaders.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GameOverController {

    @FXML
    private Button restartButton;

    @FXML
    private Button exitButton;

    private RootController rootController;
    private Stage stage;

    public void initialize() {
        // Asignamos los manejadores de evento en vez de en FXML
        restartButton.setOnAction(this::onRestartButtonAction);
        exitButton.setOnAction(this::onExitButtonAction);
    }

    @FXML
    private void onRestartButtonAction(ActionEvent event) {
        if (rootController != null) {
            rootController.restartGame();  // Reinicia la partida
        }
        if (stage != null) {
            stage.close();                // Cierra la ventana de Game Over
        }
    }

    @FXML
    private void onExitButtonAction(ActionEvent event) {
        System.exit(0);                   // Cierra la aplicación
    }

    // Setters para inyectar desde RootController
    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
