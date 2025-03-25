package org.spaceinvaders.controllers;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
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
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.*;

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
    private String playerName; //Variable para almacenar el nombre del jugador

    private Contador contador = new Contador(); //Contador
    private Label timerLabel;

    private long lastUpdate = 0;

    private boolean esperandoReady = true; // ✅ Al inicio, el juego espera que el jugador presione "Ready"

    private Boss boss;

    private ScoreBoardController scoreBoardController;

    private List<DefenseWall> defenseWalls; // Lista de muros de defensa

    public RootController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Ship getShip() {
        return ship;
    }

    public Contador getContador() {
        return contador;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scoreBoardController = new ScoreBoardController();
        scoreBoardController.setRootController(this); // Set the RootController reference


        background = new Image(getClass().getResourceAsStream("/images/FondoJuegoCasas1200x1400.jpg"));
        gc = gameCanvas.getGraphicsContext2D();
        ship = new Ship();
        vidas = new Lives();
        enemyManager = new EnemyManager(this);

        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(event -> keysPressed.add(event.getCode()));
        gameCanvas.setOnKeyReleased(event -> keysPressed.remove(event.getCode()));

        // Inicializar la lista de muros de defensa
        defenseWalls = new ArrayList<>();
        defenseWalls.add(new DefenseWall(200, 500, 50, 40, 20));
        defenseWalls.add(new DefenseWall(400, 500, 50, 40, 20));
        defenseWalls.add(new DefenseWall(600, 500, 50, 40, 20));
        defenseWalls.add(new DefenseWall(800, 500, 50, 40, 20));
        defenseWalls.add(new DefenseWall(1000, 500, 50, 40, 20));

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
        if (gameOver || esperandoReady) return;

        // Movimiento suave de la nave
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

        // Verificar colisiones con los muros de defensa
        Iterator<DefenseWall> wallIterator = defenseWalls.iterator();
        while (wallIterator.hasNext()) {
            DefenseWall wall = wallIterator.next();
            if (!wall.estaDestruido()) {
                // Verificamos colisión con las balas del jugador
                Iterator<Projectile> iter = ship.getProjectiles().iterator();
                while (iter.hasNext()) {
                    Projectile projectile = iter.next();
                    if (wall.checkCollision(projectile)) {
                        iter.remove(); // Eliminamos la bala aliada si impacta en el muro
                    }
                }

                // Verificamos colisión con las balas enemigas
                Iterator<EnemyProjectile> enemyIter = enemyManager.getProjectiles().iterator();
                while (enemyIter.hasNext()) {
                    EnemyProjectile enemyProjectile = enemyIter.next();
                    if (wall.checkCollision(enemyProjectile)) {
                        enemyIter.remove(); // Eliminamos la bala enemiga si impacta en el muro
                    }
                }

                // Verificar colisión con los enemigos
                Iterator<Enemy> enemyIterator = enemyManager.getEnemies().iterator();
                while (enemyIterator.hasNext()) {
                    Enemy enemy = enemyIterator.next();
                    if (wall.checkCollision(enemy)) {
                        // Elimina al enemigo cuando colisiona con el muro
                        enemyIterator.remove();
                    }
                }
            }
        }

        // Verificación de colisión con la nave
        for (Enemy enemy : enemyManager.getEnemies()) {
            if (enemy.getBounds().intersects(ship.getBounds())) {
                gameOver(); // Si colisiona con un enemigo, termina el juego
                return;
            }
        }

        // Verificación de colisión entre enemigos y la nave
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
        drawBackground(gc);

        if (!esperandoReady) {
            ship.draw(gc);
            enemyManager.draw(gc);
            vidas.draw(gc);

            // Dibujar los muros de defensa con sprites
            for (DefenseWall wall : defenseWalls) {
                if (!wall.estaDestruido()) {
                    gc.drawImage(wall.getCurrentImage(), wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight());
                }
            }
        }

        // Dibujar el texto del contador en el Canvas
        gc.setFont(Font.font("Press Start 2P Regular", FontWeight.NORMAL, 20));
        gc.setFill(Color.WHITE);
        gc.fillText(contador.getFormattedElapsedTime(), gameCanvas.getWidth() - 160, 30);
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

    public void restartGame() {
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

        // ✅ Reiniciar los muros de defensa
        reiniciarMuros();

        // ✅ Asegurar que el canvas se vuelve a mostrar
        root.setCenter(gameCanvas);
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight()); // ✅ Limpiar pantalla antes de redibujar

        mostrarMensajeOleada("Primera Oleada"); // ✅ Mostrar el mensaje inicial
        mostrarBotonReady(); // ✅ Mostrar el botón READY
    }

    // Metodo para reiniciar los muros de defensa
    private void reiniciarMuros() {
        defenseWalls.clear();
        defenseWalls.add(new DefenseWall(200, 500, 50, 40, 20));
        defenseWalls.add(new DefenseWall(400, 500, 50, 40, 20));
        defenseWalls.add(new DefenseWall(600, 500, 50, 40, 20));
        defenseWalls.add(new DefenseWall(800, 500, 50, 40, 20));
        defenseWalls.add(new DefenseWall(1000, 500, 50, 40, 20));
    }


    public void bossDefeated() {
        stopGameLoop();
        contador.stop();

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("¡Enhorabuena!");
            alert.setHeaderText("🎉 Has derrotado al Boss 🎉");
            alert.setContentText("¿Quieres revisar la puntuación o salir del juego?");

            ButtonType reviewButton = new ButtonType("Revisar Puntuaciones");
            ButtonType exitButton = new ButtonType("Salir del juego");

            alert.getButtonTypes().setAll(reviewButton, exitButton);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == reviewButton) {
                String playerName = promptForName(); // Metodo para pedir el nombre del jugador
                int finalScore = ship.getScore();    // Puntuación final del jugador
                int vidasRestantes = vidas.getVidas(); // Vidas restantes al derrotar al Boss

                System.out.println("🏆 Puntuación TOTAL FINAL: " + finalScore);

                // Crear el objeto ScoreEntry con la puntuación final
                ScoreEntry newScore = new ScoreEntry(playerName, finalScore, vidasRestantes);

                // Añadir el ScoreEntry al controlador de puntuaciones (ScoreBoardController)
                scoreBoardController.addScoreEntry(newScore);

                // Guardar las puntuaciones en el archivo JSON
                ScoreManager.saveScores(scoreBoardController.getScoreList());

                // Mostrar la pantalla de puntuaciones
                root.setCenter(scoreBoardController.getRoot());
            } else if (result.isPresent() && result.get() == exitButton) {
                System.exit(0);
            }
        });
    }

    //SoLicitar y almacenar el nombre del jugador
    private String promptForName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nombre del Jugador");
        dialog.setHeaderText("Introduce tu nombre para la tabla de puntuaciones:");
        dialog.setContentText("Nombre:");

        Optional<String> result = dialog.showAndWait();
        playerName = result.orElse("Jugador Anónimo"); // Almacenar el nombre del jugador
        return playerName;
    }

    public String getPlayerName() {
        return playerName;
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
        Text textoOleada = new Text();
        textoOleada.setFont(Font.font("Press Start 2P Regular", 50));
        textoOleada.setFill(Color.WHITE);

        root.setCenter(textoOleada); // ✅ Colocamos el texto en el centro del BorderPane

        Timeline timeline = new Timeline();
        for (int i = 0; i < mensaje.length(); i++) {
            final int index = i;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100 * index), e -> {
                textoOleada.setText(mensaje.substring(0, index + 1));
            }));
        }

        timeline.play();
    }

    public void mostrarBotonReady() {
        Button btnReady = new Button("READY");
        btnReady.setStyle("-fx-font-size: 20px; -fx-background-color: #00FF00; -fx-text-fill: black; -fx-padding: 10px; -fx-font-family: 'Press Start 2P Regular';");

        btnReady.setOnAction(e -> {
            esperandoReady = false; // ✅ Permitir que la oleada comience
            root.setBottom(null); // ✅ Eliminar el botón
            root.setCenter(gameCanvas); // ✅ Asegurar que el Canvas se muestre correctamente

            if (!enemyManager.hayBoss() && enemyManager.getEnemies().isEmpty()) {
                enemyManager.createEnemies(); // ✅ Generar enemigos si no hay ninguno
            }
            contador.start();
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

                reiniciarMuros(); // ✅ Reiniciar los muros de defensa
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
                root.setBottom(null); // ✅ Eliminar el botón
                root.setCenter(gameCanvas); // ✅ Asegurar que el Canvas se mantenga visible

                reiniciarMuros(); // ✅ Reiniciar los muros de defensa
                enemyManager.iniciarBossFinal(); // ✅ Llamar metodo para generar el Boss
                resumeGame(); // ✅ Reanudar el juego
            });

            VBox vbox = new VBox(20, btnFaseFinal);
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-padding: 20px; -fx-border-insets: 20px; -fx-background-insets: 20px;");
            root.setBottom(vbox); // ✅ Mostrar el botón en la parte inferior del BorderPane
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