package org.spaceinvaders.controllers;

import com.itextpdf.text.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.spaceinvaders.entities.Contador;
import org.spaceinvaders.entities.ScoreEntry;
import org.spaceinvaders.entities.ScoreManager;

import java.io.*;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

//PDF
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.pdf.BaseFont;


public class ScoreBoardController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private Label ScoreTitle;

    @FXML
    private TableView<ScoreEntry> ScoreTable;

    @FXML
    private TableColumn<ScoreEntry, Integer> LivesColumn;

    @FXML
    private TableColumn<ScoreEntry, String> NamesColumn;

    @FXML
    private TableColumn<ScoreEntry, Integer> ScoreColumn;

    @FXML
    private Button ReiniciarJuegoButton;

    @FXML
    private Button SalirButton;

    @FXML
    private Button DescargarDiplomaButton;

    private String playerName; // Nombre del jugador

    private ObservableList<ScoreEntry> scoreList = FXCollections.observableArrayList();

    private RootController rootController;

    public ScoreBoardController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ScoreBoardView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Cargar puntuaciones desde el archivo
        List<ScoreEntry> scoresFromFile = ScoreManager.loadScores();
        scoreList.addAll(scoresFromFile);

        // Ordenar la lista por puntuación en orden descendente
        scoreList.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());

        // Limitar la lista a 15 entradas
        if (scoreList.size() > 15) {
            scoreList = FXCollections.observableArrayList(scoreList.subList(0, 15));
        }

        NamesColumn.setCellValueFactory(cellData -> cellData.getValue().playerNameProperty());
        ScoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());
        LivesColumn.setCellValueFactory(cellData -> cellData.getValue().livesProperty().asObject());

        ScoreTable.setItems(scoreList);

        // Efectos para la tabla
        ScoreTitle.setStyle("-fx-font-family: 'Press Start 2P Regular';");
        applyRainbowEffect(ScoreTitle);

        // Fábrica de celdas para la columna NamesColumn (primera columna)
        NamesColumn.setCellFactory(column -> new TableCell<ScoreEntry, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (getTableRow() != null && !empty) {
                    // Verificar que la fila no esté vacía y luego aplicar los colores
                    int rowIndex = getTableRow().getIndex();
                    String textColor = "";  // Color por defecto

                    // Asignar color según el índice de la fila
                    switch (rowIndex) {
                        case 0: textColor = "green"; break;
                        case 1: textColor = "cyan"; break;
                        case 2: textColor = "blue"; break;
                        case 3: textColor = "violet"; break;
                        case 4: textColor = "orange"; break;
                        case 5: textColor = "lightcoral"; break;
                        case 6: textColor = "red"; break;
                        case 7: textColor = "darkred"; break;
                        case 8: textColor = "pink"; break;
                        case 9: textColor = "yellow"; break;
                        case 10: textColor = "yellowgreen"; break;
                        case 11: textColor = "lightgreen"; break;
                        case 12: textColor = "mediumseagreen"; break;
                        case 13: textColor = "turquoise"; break;
                        case 14: textColor = "blueviolet"; break;
                        default: textColor = ""; break;
                    }

                    // Aplicar el color al texto de la celda
                    setStyle("-fx-text-fill: " + textColor + ";");

                    // Mostrar el texto en la celda
                    setText(item);
                } else {
                    setStyle("");  // Restablecer el estilo si la celda está vacía
                    setText("");    // Limpiar el texto
                }
            }
        });

        ScoreColumn.setCellFactory(column -> new TableCell<ScoreEntry, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (getTableRow() != null && !empty) {
                    switch (getTableRow().getIndex()) {
                        case 0: setStyle("-fx-text-fill: green;"); break;
                        case 1: setStyle("-fx-text-fill: cyan;"); break;
                        case 2: setStyle("-fx-text-fill: blue;"); break;
                        case 3: setStyle("-fx-text-fill: violet;"); break;
                        case 4: setStyle("-fx-text-fill: orange;"); break;
                        case 5: setStyle("-fx-text-fill: lightcoral;"); break;
                        case 6: setStyle("-fx-text-fill: red;"); break;
                        case 7: setStyle("-fx-text-fill: darkred;"); break;
                        case 8: setStyle("-fx-text-fill: pink;"); break;
                        case 9: setStyle("-fx-text-fill: yellow;"); break;
                        case 10: setStyle("-fx-text-fill: yellowgreen;"); break;
                        case 11: setStyle("-fx-text-fill: lightgreen;"); break;
                        case 12: setStyle("-fx-text-fill: mediumseagreen;"); break;
                        case 13: setStyle("-fx-text-fill: turquoise;"); break;
                        case 14: setStyle("-fx-text-fill: blueviolet;"); break;
                        default: setStyle(""); break;
                    }
                } else {
                    setStyle("");
                }
                setText(item != null ? item.toString() : "");
            }
        });

        LivesColumn.setCellFactory(column -> new TableCell<ScoreEntry, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (getTableRow() != null && !empty) {
                    switch (getTableRow().getIndex()) {
                        case 0: setStyle("-fx-text-fill: green;"); break;
                        case 1: setStyle("-fx-text-fill: cyan;"); break;
                        case 2: setStyle("-fx-text-fill: blue;"); break;
                        case 3: setStyle("-fx-text-fill: violet;"); break;
                        case 4: setStyle("-fx-text-fill: orange;"); break;
                        case 5: setStyle("-fx-text-fill: lightcoral;"); break;
                        case 6: setStyle("-fx-text-fill: red;"); break;
                        case 7: setStyle("-fx-text-fill: darkred;"); break;
                        case 8: setStyle("-fx-text-fill: pink;"); break;
                        case 9: setStyle("-fx-text-fill: yellow;"); break;
                        case 10: setStyle("-fx-text-fill: yellowgreen;"); break;
                        case 11: setStyle("-fx-text-fill: lightgreen;"); break;
                        case 12: setStyle("-fx-text-fill: mediumseagreen;"); break;
                        case 13: setStyle("-fx-text-fill: turquoise;"); break;
                        case 14: setStyle("-fx-text-fill: blueviolet;"); break;
                        default: setStyle(""); break;
                    }
                } else {
                    setStyle("");
                }
                setText(item != null ? item.toString() : "");
            }
        });
    }

    public BorderPane getRoot() {
        return root;
    }

    // Add this method to the ScoreBoardController class
    public void displayElapsedTime() {
        RootController rootController = (RootController) ((Stage) ScoreTable.getScene().getWindow()).getUserData();
        Contador contador = rootController.getContador();
        String elapsedTime = contador.getFormattedElapsedTime();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tiempo Transcurrido");
        alert.setHeaderText(null);
        alert.setContentText("Tiempo transcurrido desde que presionaste READY hasta que derrotaste al Boss: " + elapsedTime);
        alert.showAndWait();
    }

    public void addScoreEntry(ScoreEntry entry) {
        scoreList.add(entry);
        // Ordenar la lista por puntuación en orden descendente
        scoreList.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());

        // Limitar la lista a 15 entradas
        if (scoreList.size() > 15) {
            if (scoreList.indexOf(entry) >= 15) {
                // Mostrar mensaje si el jugador no se posiciona en los 15 mejores
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Puntuación");
                alert.setHeaderText(null);
                alert.setContentText("No te has posicionado en los 15 mejores, ¡Inténtalo la próxima vez!");
                alert.showAndWait();
            }
            scoreList.remove(15);
        }
    }

    public ObservableList<ScoreEntry> getScoreList() {
        return scoreList;
    }

    //Efecto arcoiris
    public void applyRainbowEffect(Label label) {
        Timeline timeline = new Timeline();
        int duration = 100; // Duration in milliseconds for each color transition

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
    void onDownloadButtonAction(ActionEvent event) {
        System.out.println("Botón de descarga presionado");

        // Obtener el nombre del jugador desde el controlador principal
        RootController rootController = (RootController) ((Stage) ((Button) event.getSource()).getScene().getWindow()).getUserData();
        String playerName = rootController.getPlayerName();  // Obtener el nombre del jugador

        // Obtener el tiempo total del contador
        Contador contador = rootController.getContador();
        String elapsedTime = contador.getFormattedElapsedTime();

        // Verificar si el jugador está en la lista de los 15 mejores
        boolean playerInTop15 = scoreList.stream()
                .anyMatch(entry -> entry.getPlayerName().equals(playerName));

        if (!playerInTop15) {
            // Si el jugador no está en la lista, deshabilitar el botón de descarga
            DescargarDiplomaButton.setDisable(true);
            System.out.println("El jugador no está en los 15 mejores, el botón ha sido deshabilitado.");

            // Mostrar alerta al jugador
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No te has posicionado");
            alert.setHeaderText(null);  // Si no quieres un encabezado en el mensaje
            alert.setContentText("No te has posicionado en los mejores, ¡Intentalo la próxima vez!");
            alert.showAndWait();  // Mostrar la alerta y esperar a que el usuario la cierre

            return;  // Salir del metodo si el jugador no está en la lista
        }

        // Obtener la entrada del jugador
        ScoreEntry playerEntry = scoreList.stream()
                .filter(entry -> entry.getPlayerName().equals(playerName))
                .findFirst()
                .orElse(null);

        if (playerEntry == null) {
            System.out.println("No se encontró la entrada del jugador.");
            return;
        }

        // Crear un FileChooser para que el usuario elija dónde guardar el archivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Certificado");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(((Button) event.getSource()).getScene().getWindow());

        if (file == null) {
            System.out.println("El usuario canceló la operación de guardado.");
            return;
        }

        // Si el jugador está en los 15 mejores, generar el certificado PDF
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Cargar la imagen de fondo
            Image background = Image.getInstance(getClass().getResource("/images/FondoDiploma.jpg"));
            background.setAbsolutePosition(0, 0);
            background.scaleToFit(document.getPageSize().getWidth(), document.getPageSize().getHeight());
            document.add(background);

            // Cargar la fuente "Press Start 2P" desde el recurso
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/PressStart2P-Regular.ttf");

            // Fuentes y colores
            BaseFont baseFont = BaseFont.createFont("fonts/PressStart2P-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, 24, Font.BOLD, BaseColor.RED);
            Font textFont = new Font(baseFont, 18, Font.NORMAL, BaseColor.GREEN);
            Font playerNameFont = new Font(baseFont, 18, Font.BOLD, BaseColor.MAGENTA);

            // Crear y agregar el título
            Paragraph title = new Paragraph("Certificado de Logro", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Agregar espacio
            document.add(new Paragraph("\n\n"));

            // Crear y agregar el contenido
            Paragraph content = new Paragraph();
            content.setAlignment(Element.ALIGN_CENTER);
            content.setLeading(24);
            content.add(new Chunk("Este certificado se otorga a \n", textFont));
            content.add(new Chunk(playerName, playerNameFont));
            content.add(new Chunk(" por haber entrado en los 15 mejores jugadores de Espacios Invasores.", textFont));
            document.add(content);

            // Agregar el nuevo contenido con vidas
            Paragraph livesContent = new Paragraph();
            livesContent.setAlignment(Element.ALIGN_CENTER);
            livesContent.setLeading(50);
            livesContent.add(new Chunk("Con ", textFont));
            livesContent.add(new Chunk(String.valueOf(playerEntry.getLives()), playerNameFont));
            livesContent.add(new Chunk(" vidas restantes", textFont));
            document.add(livesContent);

            // Agregar el nuevo contenido con puntuación
            Paragraph scoreContent = new Paragraph();
            scoreContent.setAlignment(Element.ALIGN_CENTER);
            scoreContent.setLeading(50);
            scoreContent.add(new Chunk("Con una puntuación de:", textFont));
            scoreContent.add(new Chunk(String.valueOf(playerEntry.getScore()), playerNameFont));
            document.add(scoreContent);

            // Puntos
            Paragraph scoreContentp = new Paragraph();
            scoreContentp.setAlignment(Element.ALIGN_CENTER);
            scoreContentp.setLeading(24);
            scoreContentp.add(new Chunk(" puntos.", textFont));
            document.add(scoreContentp);

            // Agregar el nuevo contenido con el tiempo total
            Paragraph timeContent = new Paragraph();
            timeContent.setAlignment(Element.ALIGN_CENTER);
            timeContent.setLeading(50);
            timeContent.add(new Chunk("Con un tiempo de: ", textFont));
            timeContent.add(new Chunk(elapsedTime, playerNameFont));
            document.add(timeContent);

            // Agregar espacio
            document.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));

            // Agregar mensaje recordatorio
            Paragraph finalContent = new Paragraph();
            finalContent.setAlignment(Element.ALIGN_CENTER);
            finalContent.setLeading(24);
            finalContent.add(new Chunk("Este diploma solo servirá para demostración de sus habilidades,\n" + "única y exclusivamente.", textFont));
            document.add(finalContent);

            // Crear y agregar la firma
            Paragraph signature = new Paragraph("Espacios Invasores", new Font(baseFont, 18, Font.NORMAL, BaseColor.BLUE));
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            // Agregar un marco alrededor del contenido
            Rectangle border = new Rectangle(36, 800, 560, 36); // Ajustar las coordenadas y dimensiones
            border.setBorder(Rectangle.BOX);
            border.setBorderWidth(2);
            border.setBorderColor(BaseColor.LIGHT_GRAY);
            document.add(border);

            // Cargar y agregar la imagen de la nave principal
            Image image = Image.getInstance(getClass().getResource("/images/NavePrincipal.png"));
            image.setAlignment(Image.ALIGN_CENTER);
            image.scaleToFit(50, 50); // Ajustar el tamaño de la imagen
            image.setAbsolutePosition(50, 600);
            document.add(image);

            // Cargar y agregar la imagen de la nave principal
            Image image2 = Image.getInstance(getClass().getResource("/images/Enemigo Tanque-1.png.png"));
            image2.setAlignment(Image.ALIGN_CENTER);
            image2.scaleToFit(50, 50); // Ajustar el tamaño de la imagen
            image2.setAbsolutePosition(50, 200);
            document.add(image2);

            // Cargar y agregar la imagen de la nave principal
            Image image3 = Image.getInstance(getClass().getResource("/images/Enemigocaballero1png.png"));
            image3.setAlignment(Image.ALIGN_CENTER);
            image3.scaleToFit(50, 50); // Ajustar el tamaño de la imagen
            image3.setAbsolutePosition(500, 600);
            document.add(image3);

            // Cargar y agregar la imagen de la nave principal
            Image image4 = Image.getInstance(getClass().getResource("/images/Primer Boss-1.png.png"));
            image4.setAlignment(Image.ALIGN_CENTER);
            image4.scaleToFit(100, 100); // Ajustar el tamaño de la imagen
            image4.setAbsolutePosition(450, 300);
            document.add(image4);

            // Cargar y agregar la imagen de la nave principal
            Image image5 = Image.getInstance(getClass().getResource("/images/Bala aliada-1.png.png"));
            image5.setAlignment(Image.ALIGN_CENTER);
            image5.scaleToFit(100, 100); // Ajustar el tamaño de la imagen
            image5.setAbsolutePosition(450, 250);
            document.add(image5);

            // Cargar y agregar la imagen de la nave principal
            Image image6 = Image.getInstance(getClass().getResource("/images/Bala aliada-1.png.png"));
            image6.setAlignment(Image.ALIGN_CENTER);
            image6.scaleToFit(100, 100); // Ajustar el tamaño de la imagen
            image6.setAbsolutePosition(470, 270);
            document.add(image6);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    @FXML
    void onResetButtonAction(ActionEvent event) {
        if (rootController != null) {
            rootController.restartGame();
        } else {
            System.err.println("RootController is not set.");
        }
    }

    @FXML
    void onExitButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Salir del Juego");
        alert.setHeaderText(null);
        alert.setContentText("¡Hasta la próxima!");

        // Apply the "Press Start 2P" font to the alert content
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Press Start 2P Regular'; -fx-font-size: 14px;");

        alert.showAndWait();
        Platform.exit();
        System.exit(0);
    }
}