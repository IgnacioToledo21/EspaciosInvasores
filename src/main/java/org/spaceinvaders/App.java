package org.spaceinvaders;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.spaceinvaders.controllers.MainMenuController;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Cargar la fuente antes de aplicarla
        Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 20);
        Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 20);
        if (font == null) {
            System.out.println("⚠️ No se pudo cargar la fuente.");
        } else {
            System.out.println("✅ Fuente cargada correctamente: " + font.getName());
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenuView.fxml"));
        MainMenuController mainMenuController = new MainMenuController();
        loader.setController(mainMenuController);
        primaryStage.setUserData(mainMenuController);

        primaryStage.setTitle("Space Invaders");
        Scene scene = new Scene(loader.load(), 1200, 700);

        // Cargar el archivo CSS para aplicar los estilos
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}