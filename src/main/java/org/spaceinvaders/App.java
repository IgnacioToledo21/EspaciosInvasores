package org.spaceinvaders;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.spaceinvaders.controllers.RootController;

public class App extends Application {

    private static RootController rootController;

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


        rootController = new RootController();

        primaryStage.setTitle("Space Invaders");
        Scene scene = new Scene(rootController.getRoot(), 1200, 700);

        // Cargar el archivo CSS para aplicar los estilos
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
