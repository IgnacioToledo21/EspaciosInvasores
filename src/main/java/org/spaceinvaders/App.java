package org.spaceinvaders;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.spaceinvaders.controllers.MainMenuController;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Cargar la fuente antes de aplicarla
        Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 20);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenuView.fxml"));
        MainMenuController mainMenuController = new MainMenuController();
        loader.setController(mainMenuController);
        primaryStage.setUserData(mainMenuController);

        primaryStage.setTitle("Espacios Invasores");
        // Agregar el icono a la ventana
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/enemigos/Enemigo Tanque-2.png.png")));

        // Contenedor principal
        StackPane root = new StackPane();
        BorderPane content = new BorderPane();

        // Cargar la vista principal
        Parent mainView = loader.load();
        content.setCenter(mainView);

        // Asegurar que el contenido se mantiene con su tamaño original
        content.setPrefSize(1200, 700);
        content.setMaxSize(1200, 700);

        root.getChildren().add(content);
        root.setStyle("-fx-background-color: black;"); // Fondo negro para evitar espacios vacíos

        Scene scene = new Scene(root, 1200, 700);

        // Cargar CSS
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(false); //Evita que el usuario cambie el tamaño de la ventana
        primaryStage.setFullScreen(false); // Activa la pantalla completa
        primaryStage.show();
    }
}