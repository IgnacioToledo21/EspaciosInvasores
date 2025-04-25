package org.spaceinvaders.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

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
