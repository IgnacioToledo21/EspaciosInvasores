package org.spaceinvaders.controllers;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.spaceinvaders.entities.*;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

public class RootController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private Canvas gameCanvas;

    private Ship ship;
    private Lives vidas;
    private EnemyManager enemyManager;
    private GraphicsContext gc;

    private boolean gameOver = false; // Nueva variable para detener el juego

    private Set<KeyCode> keysPressed = new HashSet<>();

    private Image background;
    private double backgroundY = 0;
    private final double BACKGROUND_SPEED = 0.05; // Velocidad de desplazamiento

    private AnimationTimer gameTimer; // Variable para el GameLoop

    private long lastUpdate = 0;

    private boolean esperandoReady = true; // ✅ Al inicio, el juego espera que el jugador presione "Ready"

    private Boss boss;

    public RootController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        background = new Image(getClass().getResourceAsStream("/images/FondoJuego.jpg"));
        gc = gameCanvas.getGraphicsContext2D();
        ship = new Ship();
        vidas = new Lives();
        enemyManager = new EnemyManager(this);

        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(event -> keysPressed.add(event.getCode()));
        gameCanvas.setOnKeyReleased(event -> keysPressed.remove(event.getCode()));

        mostrarMensajeOleada("Primera Oleada"); // ✅ Mostrar mensaje al iniciar
        mostrarBotonReady(); // ✅ Mostrar botón READY al iniciar
    }


    //dibujar el fondo
    private void drawBackground(GraphicsContext gc) {
        backgroundY += BACKGROUND_SPEED; // Mueve el fondo hacia abajo

        if (backgroundY >= gameCanvas.getHeight()) {
            backgroundY = 0; // Reinicia la posición cuando llega al final
        }

        gc.drawImage(background, 0, backgroundY - gameCanvas.getHeight(), gameCanvas.getWidth(), gameCanvas.getHeight());
        gc.drawImage(background, 0, backgroundY, gameCanvas.getWidth(), gameCanvas.getHeight());
    }


    public BorderPane getRoot() {
        return root;
    }

    private void update() {
        if (gameOver || esperandoReady) return; // ✅ No actualizar nada hasta presionar "READY"

        // Movimiento fluido
        if (keysPressed.contains(KeyCode.LEFT)) {
            ship.moverIzquierda();
        }
        if (keysPressed.contains(KeyCode.RIGHT)) {
            ship.moverDerecha();
        }
        if (keysPressed.contains(KeyCode.SPACE)) {
            ship.fireProjectile();
        }

        ship.updateProjectiles();
        enemyManager.moveEnemies();
        enemyManager.updateProjectiles(ship.getProjectiles());

        if (enemyManager.checkCollisionWithShip(ship)) {
            vidas.reducirVida();
        }

        if (vidas.getVidas() <= 0) {
            gameOver();
            return;
        }

        draw();
    }



    private void draw() {
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        drawBackground(gc); //

        if (!esperandoReady) {
            ship.draw(gc);
            enemyManager.draw(gc);
            vidas.draw(gc);
        }
    }



    private void gameOver() {
        gameOver = true; // ✅ Evita que el bucle de actualización siga corriendo
        enemyManager.stopEnemyShooting(); // ✅ Detiene los disparos enemigos

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Fin del Juego");
            alert.setHeaderText("💀 Has perdido 💀");
            alert.setContentText("¿Quieres reiniciar la partida?");

            ButtonType restartButton = new ButtonType("Reiniciar");
            ButtonType exitButton = new ButtonType("Salir");

            alert.getButtonTypes().setAll(restartButton, exitButton);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == restartButton) {
                restartGame(); // Reiniciar el juego
            } else {
                System.exit(0); // Cerrar la aplicación
            }
        });
    }

    private void restartGame() {
        stopGameLoop(); // ✅ Detener completamente el bucle de animación ANTES de reiniciar
        enemyManager.stopEnemyShooting(); // ✅ Detener el Timer de disparos enemigos

        gameOver = false;
        esperandoReady = true;
        keysPressed.clear(); // ✅ Detener movimiento automático

        // ✅ Reiniciar todas las entidades
        ship = new Ship();
        vidas.reiniciar();
        enemyManager = new EnemyManager(this);

        // ✅ Reiniciar variables de velocidad
        enemyManager.resetSpeed();
        ship.resetSpeed();

        if (boss != null) {
            boss.resetSpeed();
            boss = null; // ✅ Asegurar que no quede referencia del Boss tras reiniciar
        }

        // ✅ Asegurar que no queden proyectiles en la pantalla
        ship.getProjectiles().clear();
        enemyManager.getProjectiles().clear();

        // ✅ Asegurar que el canvas se vuelve a mostrar
        root.setCenter(gameCanvas);
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight()); // ✅ Limpiar pantalla antes de redibujar

        mostrarMensajeOleada("Primera Oleada"); // ✅ Mostrar el mensaje inicial
        mostrarBotonReady(); // ✅ Mostrar el botón READY
    }


    public void bossDefeated() {
        stopGameLoop(); //Detener el GameLoop antes de mostrar el pop-up

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("¡Enhorabuena!");
            alert.setHeaderText("🎉 Has derrotado al Boss 🎉");
            alert.setContentText("¿Quieres volver a jugar o salir?");

            ButtonType restartButton = new ButtonType("Volver a jugar");
            ButtonType exitButton = new ButtonType("Salir");

            alert.getButtonTypes().setAll(restartButton, exitButton);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == restartButton) {
                restartGame(); // ✅ Reiniciar el juego
            } else {
                System.exit(0); // ✅ Salir del juego
            }
        });
    }

    public void startGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(); // ✅ Llamar a update() directamente sin limitaciones
            }
        };
        gameTimer.start();
    }



    public void stopGameLoop() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    public void mostrarMensajeOleada(String mensaje) {
        Text textoOleada = new Text(mensaje);
        textoOleada.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        textoOleada.setFill(Color.BLACK);

        // Efecto de aparición/desaparición
        FadeTransition fade = new FadeTransition(Duration.seconds(1), textoOleada);
        fade.setFromValue(1.0);
        fade.setToValue(0.3);
        fade.setCycleCount(Animation.INDEFINITE);
        fade.setAutoReverse(true);
        fade.play();

        root.setCenter(textoOleada); // ✅ Agregamos el mensaje en el centro del BorderPane
    }

    public void mostrarBotonReady() {
        Button btnReady = new Button("READY");
        btnReady.setStyle("-fx-font-size: 20px; -fx-background-color: #00FF00; -fx-text-fill: black; -fx-padding: 10px;");

        btnReady.setOnAction(e -> {
            esperandoReady = false; // ✅ Permitir que la oleada comience
            root.setBottom(null); // ✅ Eliminar el botón
            root.setCenter(gameCanvas); // ✅ Asegurar que el Canvas se muestre correctamente

            if (!enemyManager.hayBoss() && enemyManager.getEnemies().isEmpty()) {
                enemyManager.createEnemies(); // ✅ Generar enemigos si no hay ninguno
            }

            startGameLoop(); // ✅ Iniciar el juego
        });

        VBox vbox = new VBox(20, btnReady);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 20px; -fx-border-insets: 20px; -fx-background-insets: 20px;");
        root.setBottom(vbox); //Mostrar el botón en la parte inferior del BorderPane
    }

    public void mostrarBotonSiguienteOleada() {
        Platform.runLater(() -> {
            Button btnNextWave = new Button("SIGUIENTE OLEADA");
            btnNextWave.setStyle("-fx-font-size: 20px; -fx-background-color: #FF0000; -fx-text-fill: black; -fx-padding: 10px;");

            btnNextWave.setOnAction(e -> {
                root.setBottom(null); // ✅ Eliminar el botón
                root.setCenter(gameCanvas); // ✅ Asegurar que el Canvas se mantenga visible

                enemyManager.iniciarSiguienteOleada(); // ✅ Generar nuevos enemigos
                resumeGame(); // ✅ Reanudar el juego
            });

            VBox vbox = new VBox(20, btnNextWave);
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-padding: 20px; -fx-border-insets: 20px; -fx-background-insets: 20px;");
            root.setBottom(vbox); // ✅ Mostrar el botón en la parte inferior del BorderPane
        });
    }

    public void mostrarBotonFaseFinal() {
        Platform.runLater(() -> {
            Button btnFaseFinal = new Button("FASE FINAL");
            btnFaseFinal.setStyle("-fx-font-size: 20px; -fx-background-color: #FF0000; -fx-text-fill: black; -fx-padding: 10px;");

            btnFaseFinal.setOnAction(e -> {
                root.setBottom(null); // Eliminar el botón
                root.setCenter(gameCanvas); // Asegurar que el Canvas se mantenga visible

                boss = new Boss(550, 50); // Generar el jefe final
                resumeGame(); // Reanudar el juego
            });

            VBox vbox = new VBox(20, btnFaseFinal);
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-padding: 20px; -fx-border-insets: 20px; -fx-background-insets: 20px;");
            root.setBottom(vbox); // Mostrar el botón en la parte inferior del BorderPane
        });
    }


    public void stopGame() {
        if (gameTimer != null) {
            gameTimer.stop(); // ✅ Detener el bucle del juego
        }
        esperandoReady = true; // ✅ Evitar que se sigan ejecutando actualizaciones
    }

    public void resumeGame() {
        esperandoReady = false; // ✅ Permitir que el juego continúe
        startGameLoop(); // ✅ Reanudar el bucle del juego
    }










}