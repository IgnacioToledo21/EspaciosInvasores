package org.spaceinvaders.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.spaceinvaders.entities.ScoreEntry;
import org.spaceinvaders.entities.ScoreManager;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ScoreBoardController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private Label ScoreTitle;

    @FXML
    private TableView<ScoreEntry> ScoreTable;

    @FXML
    private TableColumn<ScoreEntry, Integer> LivesColumn;

    @FXML
    private TableColumn<ScoreEntry, String> NamesColumn;

    @FXML
    private TableColumn<ScoreEntry, Integer> ScoreColumn;

    @FXML
    private Button ReiniciarJuegoButton;

    @FXML
    private Button SalirButton;

    @FXML
    private Button DescargarDiplomaButton;

    private ObservableList<ScoreEntry> scoreList = FXCollections.observableArrayList();

    public ScoreBoardController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ScoreBoardView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Cargar puntuaciones desde el archivo
        List<ScoreEntry> scoresFromFile = ScoreManager.loadScores();
        scoreList.addAll(scoresFromFile);

        // Ordenar la lista por puntuación en orden descendente
        scoreList.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());

        // Limitar la lista a 15 entradas
        if (scoreList.size() > 15) {
            scoreList = FXCollections.observableArrayList(scoreList.subList(0, 15));
        }

        NamesColumn.setCellValueFactory(cellData -> cellData.getValue().playerNameProperty());
        ScoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());
        LivesColumn.setCellValueFactory(cellData -> cellData.getValue().livesProperty().asObject());

        ScoreTable.setItems(scoreList);

        // Efectos para la tabla
        ScoreTitle.setStyle("-fx-font-family: 'Press Start 2P Regular';");
        applyRainbowEffect(ScoreTitle);

        // Fábrica de celdas para la columna NamesColumn (primera columna)
        NamesColumn.setCellFactory(column -> new TableCell<ScoreEntry, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (getTableRow() != null && !empty) {
                    // Verificar que la fila no esté vacía y luego aplicar los colores
                    int rowIndex = getTableRow().getIndex();
                    String textColor = "";  // Color por defecto

                    // Asignar color según el índice de la fila
                    switch (rowIndex) {
                        case 0: textColor = "green"; break;
                        case 1: textColor = "cyan"; break;
                        case 2: textColor = "blue"; break;
                        case 3: textColor = "violet"; break;
                        case 4: textColor = "orange"; break;
                        case 5: textColor = "lightcoral"; break;
                        case 6: textColor = "red"; break;
                        case 7: textColor = "darkred"; break;
                        case 8: textColor = "pink"; break;
                        case 9: textColor = "yellow"; break;
                        case 10: textColor = "yellowgreen"; break;
                        case 11: textColor = "lightgreen"; break;
                        case 12: textColor = "mediumseagreen"; break;
                        case 13: textColor = "turquoise"; break;
                        case 14: textColor = "blueviolet"; break;
                        default: textColor = ""; break;
                    }

                    // Aplicar el color al texto de la celda
                    setStyle("-fx-text-fill: " + textColor + ";");

                    // Mostrar el texto en la celda
                    setText(item);
                } else {
                    setStyle("");  // Restablecer el estilo si la celda está vacía
                    setText("");    // Limpiar el texto
                }
            }
        });

        ScoreColumn.setCellFactory(column -> new TableCell<ScoreEntry, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (getTableRow() != null && !empty) {
                    switch (getTableRow().getIndex()) {
                        case 0: setStyle("-fx-text-fill: green;"); break;
                        case 1: setStyle("-fx-text-fill: cyan;"); break;
                        case 2: setStyle("-fx-text-fill: blue;"); break;
                        case 3: setStyle("-fx-text-fill: violet;"); break;
                        case 4: setStyle("-fx-text-fill: orange;"); break;
                        case 5: setStyle("-fx-text-fill: lightcoral;"); break;
                        case 6: setStyle("-fx-text-fill: red;"); break;
                        case 7: setStyle("-fx-text-fill: darkred;"); break;
                        case 8: setStyle("-fx-text-fill: pink;"); break;
                        case 9: setStyle("-fx-text-fill: yellow;"); break;
                        case 10: setStyle("-fx-text-fill: yellowgreen;"); break;
                        case 11: setStyle("-fx-text-fill: lightgreen;"); break;
                        case 12: setStyle("-fx-text-fill: mediumseagreen;"); break;
                        case 13: setStyle("-fx-text-fill: turquoise;"); break;
                        case 14: setStyle("-fx-text-fill: blueviolet;"); break;
                        default: setStyle(""); break;
                    }
                } else {
                    setStyle("");
                }
                setText(item != null ? item.toString() : "");
            }
        });

        LivesColumn.setCellFactory(column -> new TableCell<ScoreEntry, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (getTableRow() != null && !empty) {
                    switch (getTableRow().getIndex()) {
                        case 0: setStyle("-fx-text-fill: green;"); break;
                        case 1: setStyle("-fx-text-fill: cyan;"); break;
                        case 2: setStyle("-fx-text-fill: blue;"); break;
                        case 3: setStyle("-fx-text-fill: violet;"); break;
                        case 4: setStyle("-fx-text-fill: orange;"); break;
                        case 5: setStyle("-fx-text-fill: lightcoral;"); break;
                        case 6: setStyle("-fx-text-fill: red;"); break;
                        case 7: setStyle("-fx-text-fill: darkred;"); break;
                        case 8: setStyle("-fx-text-fill: pink;"); break;
                        case 9: setStyle("-fx-text-fill: yellow;"); break;
                        case 10: setStyle("-fx-text-fill: yellowgreen;"); break;
                        case 11: setStyle("-fx-text-fill: lightgreen;"); break;
                        case 12: setStyle("-fx-text-fill: mediumseagreen;"); break;
                        case 13: setStyle("-fx-text-fill: turquoise;"); break;
                        case 14: setStyle("-fx-text-fill: blueviolet;"); break;
                        default: setStyle(""); break;
                    }
                } else {
                    setStyle("");
                }
                setText(item != null ? item.toString() : "");
            }
        });
    }



    public BorderPane getRoot() {
        return root;
    }

    public void addScoreEntry(ScoreEntry entry) {
        scoreList.add(entry);
        // Ordenar la lista por puntuación en orden descendente
        scoreList.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());

        // Limitar la lista a 15 entradas
        if (scoreList.size() > 15) {
            if (scoreList.indexOf(entry) >= 15) {
                // Mostrar mensaje si el jugador no se posiciona en los 15 mejores
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Puntuación");
                alert.setHeaderText(null);
                alert.setContentText("No te has posicionado en los 15 mejores, ¡Inténtalo la próxima vez!");
                alert.showAndWait();
            }
            scoreList.remove(15);
        }
    }

    public ObservableList<ScoreEntry> getScoreList() {
        return scoreList;
    }

    @FXML
    void onDownloadButtonAction(ActionEvent event) {
        // Implement download functionality
    }

    @FXML
    void onExitButtonAction(ActionEvent event) {
        // Implement exit functionality
    }

    @FXML
    void onResetButtonAction(ActionEvent event) {
        // Implement reset functionality
    }

    //Efecto arcoiris
    public void applyRainbowEffect(Label label) {
        Timeline timeline = new Timeline();
        int duration = 100; // Duration in milliseconds for each color transition

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(0), e -> label.setTextFill(Color.RED)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration), e -> label.setTextFill(Color.ORANGE)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration * 2), e -> label.setTextFill(Color.YELLOW)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration * 3), e -> label.setTextFill(Color.GREEN)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration * 4), e -> label.setTextFill(Color.BLUE)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration * 5), e -> label.setTextFill(Color.INDIGO)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration * 6), e -> label.setTextFill(Color.VIOLET)));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}