package controller;

import command.*;
import database.DatabaseManager;
import factory.ShapeFactoryManager;
import model.*;
import strategy.*;
import utils.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Contrôleur principal de l'application
 */
public class DrawingController {
    private Drawing drawing;
    private CommandManager commandManager;
    private Logger logger;
    private DatabaseManager dbManager;
    
    // État actuel des outils
    private String currentTool = "RECTANGLE";
    private Color currentColor = Color.BLACK;
    private double currentStrokeWidth = 2.0;
    private Shape selectedShape;
    
    public DrawingController(Drawing drawing) {
        this.drawing = drawing;
        this.commandManager = new CommandManager();
        this.logger = Logger.getInstance();
        this.dbManager = DatabaseManager.getInstance();
        
        logger.log("Contrôleur de dessin initialisé");
    }
    
    public void createShape(double startX, double startY, double endX, double endY) {
        try {
            Shape shape = null;

            switch (currentTool) {
                case "RECTANGLE":
                    double width = Math.abs(endX - startX);
                    double height = Math.abs(endY - startY);
                    double rectX = Math.min(startX, endX);
                    double rectY = Math.min(startY, endY);
                    double[] rectParams = {rectX, rectY, width, height};
                    shape = ShapeFactoryManager.createShape(ShapeFactoryManager.ShapeType.RECTANGLE, rectParams, currentColor, currentStrokeWidth);
                    break;

                case "CIRCLE":
                    double radius = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
                    double[] circleParams = {startX, startY, radius};
                    shape = ShapeFactoryManager.createShape(ShapeFactoryManager.ShapeType.CIRCLE, circleParams, currentColor, currentStrokeWidth);
                    break;

                case "LINE":
                    double[] lineParams = {startX, startY, endX, endY};
                    shape = ShapeFactoryManager.createShape(ShapeFactoryManager.ShapeType.LINE, lineParams, currentColor, currentStrokeWidth);
                    break;
            }

            if (shape != null) {
                Command addCommand = new AddShapeCommand(drawing, shape);
                commandManager.executeCommand(addCommand);
                logger.logAction("CREATE_SHAPE", shape.toStringRepresentation());
            }

        } catch (Exception e) {
            logger.logError("Erreur lors de la création de la forme: " + e.getMessage());
            showError("Erreur", "Impossible de créer la forme: " + e.getMessage());
        }
    }
    
    public void selectShape(Shape shape) {
        selectedShape = shape;
        logger.logAction("SELECT_SHAPE", shape.toStringRepresentation());
    }
    
    public void editShape(Shape shape) {
        // Ouvrir une boîte de dialogue pour éditer les propriétés de la forme
        // Pour simplifier, on change juste la couleur
        logger.logAction("EDIT_SHAPE", shape.toStringRepresentation());
    }
    
    public void undo() {
        if (commandManager.canUndo()) {
            commandManager.undo();
            logger.logAction("UNDO", commandManager.getLastRedoDescription());
        }
    }
    
    public void redo() {
        if (commandManager.canRedo()) {
            commandManager.redo();
            logger.logAction("REDO", commandManager.getLastUndoDescription());
        }
    }
    
    public void newDrawing() {
        drawing.clear();
        commandManager.clear();
        logger.logAction("NEW_DRAWING", "Nouveau dessin créé");
    }
    
    public void saveDrawing() {
        try {
            if (drawing.getName().equals("Nouveau Dessin")) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Enregistrer le dessin");
                dialog.setHeaderText("Nom du dessin");
                dialog.setContentText("Entrez le nom du dessin:");
                
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent() && !result.get().trim().isEmpty()) {
                    drawing.setName(result.get().trim());
                } else {
                    return;
                }
            }
            
            dbManager.saveDrawing(drawing);
            logger.logAction("SAVE_DRAWING", "Dessin '" + drawing.getName() + "' enregistré");
            showInfo("Succès", "Dessin enregistré avec succès!");
            
        } catch (SQLException e) {
            logger.logError("Erreur lors de l'enregistrement: " + e.getMessage());
            showError("Erreur", "Impossible d'enregistrer le dessin: " + e.getMessage());
        }
    }
    
    public void openDrawing() {
        try {
            var drawingNames = dbManager.getDrawingNames();
            if (drawingNames.isEmpty()) {
                showInfo("Information", "Aucun dessin enregistré trouvé.");
                return;
            }

            // Créer une boîte de dialogue de sélection
            ChoiceDialog<String> dialog = new ChoiceDialog<>(drawingNames.get(0), drawingNames);
            dialog.setTitle("Ouvrir un dessin");
            dialog.setHeaderText("Sélectionner un dessin à ouvrir");
            dialog.setContentText("Dessins disponibles:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String selectedDrawingName = result.get();
                loadDrawing(selectedDrawingName);
            }

        } catch (SQLException e) {
            logger.logError("Erreur lors de l'ouverture: " + e.getMessage());
            showError("Erreur", "Impossible de charger les dessins: " + e.getMessage());
        }
    }

    private void loadDrawing(String drawingName) {
        try {
            // Charger le dessin depuis la base de données
            Drawing loadedDrawing = dbManager.loadDrawing(drawingName);

            // Remplacer le dessin actuel par le dessin chargé
            replaceCurrentDrawing(loadedDrawing);

            logger.logAction("LOAD_DRAWING", "Dessin '" + drawingName + "' chargé avec succès");
            showInfo("Succès", "Dessin '" + drawingName + "' ouvert avec succès!");

        } catch (SQLException e) {
            logger.logError("Erreur lors du chargement du dessin '" + drawingName + "': " + e.getMessage());
            showError("Erreur", "Impossible de charger le dessin '" + drawingName + "': " + e.getMessage());
        }
    }

    private void replaceCurrentDrawing(Drawing newDrawing) {
        // Effacer le dessin actuel
        drawing.clear();

        // Copier toutes les formes du nouveau dessin
        for (Shape shape : newDrawing.getShapes()) {
            drawing.addShape(shape);
        }

        // Mettre à jour le nom et l'ID du dessin
        drawing.setName(newDrawing.getName());
        drawing.setId(newDrawing.getId());

        // Effacer l'historique des commandes car nous avons un nouveau dessin
        commandManager.clear();
    }
    
    public void clearDrawing() {
        drawing.clear();
        commandManager.clear();
        logger.logAction("CLEAR_DRAWING", "Dessin effacé");
    }
    
    public void setLoggingStrategy(String strategy) {
        LoggingStrategy loggingStrategy;
        
        switch (strategy) {
            case "CONSOLE":
                loggingStrategy = new ConsoleLoggingStrategy();
                break;
            case "FILE":
                loggingStrategy = new FileLoggingStrategy();
                break;
            case "DATABASE":
                loggingStrategy = new DatabaseLoggingStrategy();
                break;
            default:
                return;
        }
        
        logger.setStrategy(loggingStrategy);
        logger.logAction("CHANGE_LOGGING_STRATEGY", strategy);
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void cleanup() {
        dbManager.close();
        logger.log("Application fermée proprement");
    }
    
    // Getters et setters
    public Drawing getDrawing() { return drawing; }
    public String getCurrentTool() { return currentTool; }
    public void setCurrentTool(String tool) { this.currentTool = tool; }
    public Color getCurrentColor() { return currentColor; }
    public void setCurrentColor(Color color) { this.currentColor = color; }
    public double getCurrentStrokeWidth() { return currentStrokeWidth; }
}
