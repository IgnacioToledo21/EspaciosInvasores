package org.spaceinvaders.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.spaceinvaders.models.ScoreEntry;
import org.spaceinvaders.models.ScoreManager;
import org.spaceinvaders.models.Ship;
import org.spaceinvaders.models.Lives;

import java.util.List;
import java.util.Optional;

public class VictoryController {

    @FXML private Button exitButton;
    @FXML private Button reviewButton;
    @FXML private BorderPane root;

    private Ship ship;
    private Lives vidas;
    private ScoreBoardController scoreBoardController;
    private Stage stage;
    private Runnable onScoreViewRequested;

    public void initialize() {
        reviewButton.setOnAction(this::onReviewButtonAction);
        exitButton.setOnAction(e -> System.exit(0));
    }

    @FXML
    void onReviewButtonAction(ActionEvent event) {
        // 1) Pedir nombre al jugador
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Introduce tu nombre");
        dialog.setHeaderText("¡Enhorabuena! Ingresa tu nombre para el ranking:");
        dialog.setContentText("Nombre:");

        Optional<String> nameOpt = dialog.showAndWait().filter(s -> !s.isBlank());
        if (nameOpt.isEmpty()) {
            // Si no ingresa nombre, salimos sin hacer nada
            return;
        }
        String playerName = nameOpt.get();

        // 2) Crear y guardar la nueva puntuación
        int finalScore = ship.getScore();
        int vidasRestantes = vidas.getVidas();
        ScoreEntry newScore = new ScoreEntry(playerName, finalScore, vidasRestantes);
        List<ScoreEntry> list = scoreBoardController.getScoreList();
        list.add(newScore);
        ScoreManager.saveScores(list);

        // 3) Ordenar y limitar al top 15
        list.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        if (list.size() > 15) {
            list.subList(15, list.size()).clear();
        }

        // 4) Si no está dentro del top 15, avisar
        int index = list.indexOf(newScore);
        if (index < 0 || index >= 15) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("No te has posicionado");
            a.setHeaderText(null);
            a.setContentText("No entraste en el top 15. ¡Inténtalo la próxima vez!");
            a.showAndWait();
        }

        scoreBoardController.setCurrentPlayerName(playerName);


        // 5) Mostrar la tabla de puntuaciones
        if (onScoreViewRequested != null) {
            onScoreViewRequested.run();
        }

        // 6) Cerrar la ventana de victoria
        stage.close();
    }

    public void onExitButtonAction (ActionEvent event){
        System.exit(0);
     }

    // Setters para inyectar dependencias desde RootController
    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void setVidas(Lives vidas) {
        this.vidas = vidas;
    }

    public void setScoreBoardController(ScoreBoardController scoreBoardController) {
        this.scoreBoardController = scoreBoardController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOnScoreViewRequested(Runnable onScoreViewRequested) {
        this.onScoreViewRequested = onScoreViewRequested;
    }
}
