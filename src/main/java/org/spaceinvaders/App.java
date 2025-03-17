package org.spaceinvaders;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.spaceinvaders.controllers.RootController;

public class App extends Application {

    private static RootController rootController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        rootController = new RootController();

        primaryStage.setTitle("Space Invaders");
        Scene scene = new Scene(rootController.getRoot(), 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
