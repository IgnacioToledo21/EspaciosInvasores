package org.spaceinvaders.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.spaceinvaders.models.ScoreManager;

import java.util.Optional;

public class SettingsController {

    @FXML
    private Button cancelButton;

    @FXML
    private Label effectLabel;

    @FXML
    private Slider effectsVolumeSlider;

    @FXML
    private Slider musicVolumeSlider;

    @FXML
    private Button saveButton;

    @FXML
    private Label soundLabel;

    @FXML
    private Label clearLabel;

    @FXML
    private Button clearButton;

    private Stage settingsStage;
    private SettingsListener settingsListener;

    public void initialize() {
    }

    @FXML
    void onCancelButtonAction(ActionEvent event) {
        if (settingsStage != null) {
            settingsStage.close();
        }
    }

    @FXML
    void onSaveButtonAction(ActionEvent event) {
        double musicVolume = musicVolumeSlider.getValue();
        double effectsVolume = effectsVolumeSlider.getValue();

        if (settingsListener != null) {
            settingsListener.onSettingsChanged(musicVolume, effectsVolume);
        }

        if (settingsStage != null) {
            settingsStage.close();
        }
    }

    @FXML
    void onResetButtonAction(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText("¿Deseas borrar todas las puntuaciones?");
        confirm.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ScoreManager.clearScores();  // Metodo en tu clase ScoreManager
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Puntuaciones eliminadas");
            success.setHeaderText(null);
            success.setContentText("Todas las puntuaciones han sido eliminadas.");
            success.showAndWait();
        }
    }


    /**
     * Establece los valores iniciales de los sliders de volumen.
     */
    public void setInitialVolumes(double initialMusicVolume, double initialEffectsVolume) {
        musicVolumeSlider.setValue(initialMusicVolume);
        effectsVolumeSlider.setValue(initialEffectsVolume);
    }

    public void setSettingsStage(Stage settingsStage) {
        this.settingsStage = settingsStage;
    }

    public void setSettingsListener(SettingsListener settingsListener) {
        this.settingsListener = settingsListener;
    }

    public interface SettingsListener {
        void onSettingsChanged(double newMusicVolume, double newEffectsVolume);
    }
}
