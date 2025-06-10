package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import controller.DrawingController;
import model.Drawing;
import utils.Logger;

/**
 * Application principale JavaFX
 */
public class DrawingApplication extends Application {
    private DrawingController controller;
    private DrawingCanvas canvas;
    private ToolPalette toolPalette;
    private MenuBar menuBar;
    private Logger logger;

    @Override
    public void start(Stage primaryStage) {
        logger = Logger.getInstance();
        logger.log("Démarrage de l'application de dessin");

        // Initialiser le modèle et le contrôleur
        Drawing drawing = new Drawing();
        controller = new DrawingController(drawing);

        // Créer les composants de l'interface
        canvas = new DrawingCanvas(controller);
        toolPalette = new ToolPalette(controller);
        menuBar = createMenuBar();

        // Layout principal
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setLeft(toolPalette);
        root.setCenter(canvas);

        // Barre de statut
        StatusBar statusBar = new StatusBar(controller);
        root.setBottom(statusBar);

        // Configuration de la scène
        Scene scene = new Scene(root, 1000, 700);
        // scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setTitle("Application de Dessin - Design Patterns");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            logger.log("Fermeture de l'application");
            controller.cleanup();
        });

        primaryStage.show();
        logger.log("Application démarrée avec succès");
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Menu Fichier
        Menu fileMenu = new Menu("Fichier");
        MenuItem newItem = new MenuItem("Nouveau");
        MenuItem saveItem = new MenuItem("Enregistrer");
        MenuItem openItem = new MenuItem("Ouvrir");
        MenuItem exitItem = new MenuItem("Quitter");

        newItem.setOnAction(e -> controller.newDrawing());
        saveItem.setOnAction(e -> controller.saveDrawing());
        openItem.setOnAction(e -> controller.openDrawing());
        exitItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().addAll(newItem, saveItem, openItem, new SeparatorMenuItem(), exitItem);

        // Menu Edition
        Menu editMenu = new Menu("Edition");
        MenuItem undoItem = new MenuItem("Annuler");
        MenuItem redoItem = new MenuItem("Refaire");
        MenuItem clearItem = new MenuItem("Effacer tout");

        undoItem.setOnAction(e -> controller.undo());
        redoItem.setOnAction(e -> controller.redo());
        clearItem.setOnAction(e -> controller.clearDrawing());

        editMenu.getItems().addAll(undoItem, redoItem, new SeparatorMenuItem(), clearItem);

        // Menu Options
        Menu optionsMenu = new Menu("Options");
        Menu loggingMenu = new Menu("Journalisation");

        RadioMenuItem consoleLogging = new RadioMenuItem("Console");
        RadioMenuItem fileLogging = new RadioMenuItem("Fichier");
        RadioMenuItem dbLogging = new RadioMenuItem("Base de données");

        ToggleGroup loggingGroup = new ToggleGroup();
        consoleLogging.setToggleGroup(loggingGroup);
        fileLogging.setToggleGroup(loggingGroup);
        dbLogging.setToggleGroup(loggingGroup);

        consoleLogging.setSelected(true);

        consoleLogging.setOnAction(e -> controller.setLoggingStrategy("CONSOLE"));
        fileLogging.setOnAction(e -> controller.setLoggingStrategy("FILE"));
        dbLogging.setOnAction(e -> controller.setLoggingStrategy("DATABASE"));

        loggingMenu.getItems().addAll(consoleLogging, fileLogging, dbLogging);
        optionsMenu.getItems().add(loggingMenu);

        menuBar.getMenus().addAll(fileMenu, editMenu, optionsMenu);
        return menuBar;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
