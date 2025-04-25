package org.spaceinvaders.controllers;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.spaceinvaders.models.ScoreEntry;
import org.spaceinvaders.models.ScoreManager;
import org.spaceinvaders.utils.MusicManager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Button ExitButton;

    @FXML
    private Button PlayButton;

    @FXML
    private Button ScoreButton;

    @FXML
    private BorderPane root;

    @FXML
    private Label titleGame;

    @FXML
    private Label optionGame;

    @FXML
    private Label ScoretopLabel;

    @FXML
    private TableView<ScoreEntry> LittleScoreTable;

    @FXML
    private TableColumn<ScoreEntry, Integer> ScoreColumn;

    @FXML
    private TableColumn<ScoreEntry, String> NameColumn;

    private ObservableList<ScoreEntry> scoreList = FXCollections.observableArrayList();

    private ScoreBoardController scoreBoardController;


    private ImageView backgroundImageView1;
    private ImageView backgroundImageView2;
    private double backgroundY1 = 0;
    private double backgroundY2 = -1400;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        MusicManager.play(MusicManager.Track.MENU);
        ScoreBoardController scoreBoardController = new ScoreBoardController();

        // Load the background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/images/fondos/FondoJuegoMenu1200x1400.jpg"));
        backgroundImageView1 = new ImageView(backgroundImage);
        backgroundImageView2 = new ImageView(backgroundImage);

        // Set the initial positions
        backgroundImageView1.setY(backgroundY1);
        backgroundImageView2.setY(backgroundY2);

        // Add the background images to the root pane
        root.getChildren().add(0, backgroundImageView1);
        root.getChildren().add(1, backgroundImageView2);

        // Start the animation timer
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateBackground();
            }
        };
        timer.start();

        // Apply rainbow effect to the titleGame label
        applyRainbowEffect(titleGame);
        applyRainbowEffect(ScoretopLabel);

        // Initialize the table and columns
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        ScoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        // Load scores from the ScoreManager
        List<ScoreEntry> scoresFromFile = ScoreManager.loadScores();
        scoreList.addAll(scoresFromFile);

        // Bind the table to the score list
        LittleScoreTable.setItems(scoreList);

        // Fábrica de celdas para la columna NameColumn
        NameColumn.setCellFactory(column -> new TableCell<ScoreEntry, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (getTableRow() != null && !empty) {
                    int rowIndex = getTableRow().getIndex();
                    String textColor = "";

                    switch (rowIndex) {
                        case 0:
                            textColor = "green";
                            break;
                        case 1:
                            textColor = "cyan";
                            break;
                        case 2:
                            textColor = "blue";
                            break;
                        case 3:
                            textColor = "violet";
                            break;
                        case 4:
                            textColor = "orange";
                            break;
                        case 5:
                            textColor = "lightcoral";
                            break;
                        case 6:
                            textColor = "red";
                            break;
                        case 7:
                            textColor = "darkred";
                            break;
                        case 8:
                            textColor = "pink";
                            break;
                        case 9:
                            textColor = "yellow";
                            break;
                        case 10:
                            textColor = "yellowgreen";
                            break;
                        case 11:
                            textColor = "lightgreen";
                            break;
                        case 12:
                            textColor = "mediumseagreen";
                            break;
                        case 13:
                            textColor = "turquoise";
                            break;
                        case 14:
                            textColor = "blueviolet";
                            break;
                        default:
                            textColor = "";
                            break;
                    }

                    setStyle("-fx-text-fill: " + textColor + ";");
                    setText(item);
                } else {
                    setStyle("");
                    setText("");
                }
            }
        });

// Fábrica de celdas para la columna ScoreColumn
        ScoreColumn.setCellFactory(column -> new TableCell<ScoreEntry, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (getTableRow() != null && !empty) {
                    int rowIndex = getTableRow().getIndex();
                    String textColor = "";

                    switch (rowIndex) {
                        case 0:
                            textColor = "green";
                            break;
                        case 1:
                            textColor = "cyan";
                            break;
                        case 2:
                            textColor = "blue";
                            break;
                        case 3:
                            textColor = "violet";
                            break;
                        case 4:
                            textColor = "orange";
                            break;
                        case 5:
                            textColor = "lightcoral";
                            break;
                        case 6:
                            textColor = "red";
                            break;
                        case 7:
                            textColor = "darkred";
                            break;
                        case 8:
                            textColor = "pink";
                            break;
                        case 9:
                            textColor = "yellow";
                            break;
                        case 10:
                            textColor = "yellowgreen";
                            break;
                        case 11:
                            textColor = "lightgreen";
                            break;
                        case 12:
                            textColor = "mediumseagreen";
                            break;
                        case 13:
                            textColor = "turquoise";
                            break;
                        case 14:
                            textColor = "blueviolet";
                            break;
                        default:
                            textColor = "";
                            break;
                    }

                    setStyle("-fx-text-fill: " + textColor + ";");
                    setText(item != null ? item.toString() : "");
                } else {
                    setStyle("");
                    setText("");
                }
            }
        });


    }

    private void updateBackground() {
        backgroundY1 += 0.01;
        backgroundY2 += 0.01;

        if (backgroundY1 >= 1400) {
            backgroundY1 = -1400;
        }
        if (backgroundY2 >= 1400) {
            backgroundY2 = -1400;
        }

        backgroundImageView1.setY(backgroundY1);
        backgroundImageView2.setY(backgroundY2);
    }

    // Rainbow effect
    public void applyRainbowEffect(Label label) {
        Timeline timeline = new Timeline();
        int duration = 150; // Duration in milliseconds for each color transition

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

    @FXML
    void onExitButtonAction(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onPlayButtonAction(ActionEvent event) {
        try {
            // 1) Preparar el loader con su controlador
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ShipSelectorView.fxml"));
            ShipSelectorController selectorController = new ShipSelectorController();
            loader.setController(selectorController);

            // 2) Cargar la vista de selección
            BorderPane selectorRoot = loader.load();
            Scene selectorScene = new Scene(selectorRoot, 600, 400);
            selectorScene.getStylesheets().add(
                    getClass().getResource("/css/styles.css").toExternalForm()
            );

            // 3) Crear y configurar el diálogo
            Stage selectorStage = new Stage();
            selectorStage.setTitle("Selecciona tu nave");
            selectorStage.setScene(selectorScene);
            selectorStage.initModality(Modality.APPLICATION_MODAL);

            // 4) Obtener el Stage principal desde el BorderPane inyectado
            Stage primaryStage = (Stage) root.getScene().getWindow();
            selectorStage.initOwner(primaryStage);

            // 5) Pasar ambos Stages al controlador de selección
            selectorController.setDialogStage(selectorStage);
            selectorController.setPrimaryStage(primaryStage);

            // 6) Mostrar diálogo
            selectorStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @FXML
    void onScoreButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ScoreBoardView.fxml"));
            ScoreBoardController scoreBoardController = new ScoreBoardController();
            loader.setController(scoreBoardController);

            // Configurar la propiedad showRestartButton antes de cargar la vista
            scoreBoardController.setShowRestartButton(false);
            scoreBoardController.setShowDownloadButton(false);


            // Obtener el RootController de la escena actual y pasarlo
            Stage stage = (Stage) root.getScene().getWindow();
            Object controller = stage.getUserData();
            if (controller instanceof RootController) {
                scoreBoardController.setRootController((RootController) controller);
            }

            // Cargar la nueva escena
            Scene scene = new Scene(loader.load(), 1200, 700);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
