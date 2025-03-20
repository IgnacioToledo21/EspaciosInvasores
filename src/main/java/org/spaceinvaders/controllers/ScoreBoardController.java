package org.spaceinvaders.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import org.spaceinvaders.entities.ScoreEntry;
import org.spaceinvaders.entities.ScoreManager;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

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
}