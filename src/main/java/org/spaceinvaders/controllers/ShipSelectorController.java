package org.spaceinvaders.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ShipSelectorController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Button leftButton;

    @FXML
    private Button rightButton;

    @FXML
    private ImageView shipImageView;

    private Stage dialogStage;
    private Stage primaryStage;

    private final List<String> skinPaths = List.of(
            "/images/naves/NavePrincipal.png",
            "/images/naves/NavePrincipal2.png"

    );
    private int currentIndex = 0;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updatePreview();

    }

    private void updatePreview() {
        String path = skinPaths.get(currentIndex);
        shipImageView.setImage(new Image(
                getClass().getResourceAsStream(path)
        ));
    }

    @FXML
    void onLeftButtonAction(ActionEvent event) {
        currentIndex = (currentIndex - 1 + skinPaths.size()) % skinPaths.size();
        updatePreview();
    }

    @FXML
    void onRightButtonAction(ActionEvent event) {
        currentIndex = (currentIndex + 1) % skinPaths.size();
        updatePreview();
    }

    @FXML
    void onCancelButtonAction(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    void onConfirmButtonAction(ActionEvent event) {
        try {
            // 1) Carga de la vista principal (RootView.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootView.fxml"));
            RootController rootController = new RootController();
            loader.setController(rootController);
            rootController.setSelectedSkinPath(skinPaths.get(currentIndex));

            Parent root = loader.load();

            // 2) Nueva Scene para el Stage principal
            Scene newScene = new Scene(root, 1200, 700);
            newScene.getStylesheets().add(
                    getClass().getResource("/css/styles.css").toExternalForm()
            );

            // 3) Reemplaza la Scene del Stage principal
            primaryStage.setScene(newScene);

            // 4) Inicializa lo que necesites en el controlador
            rootController.mostrarMensajeOleada("Primera Oleada");
            rootController.mostrarBotonReady();

            // 5) Cierra el diálogo de selección
            dialogStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
