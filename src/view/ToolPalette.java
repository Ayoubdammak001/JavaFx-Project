package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import controller.DrawingController;

/**
 * Palette d'outils pour sélectionner les formes et paramètres
 */
public class ToolPalette extends VBox {
    private DrawingController controller;
    private ToggleGroup toolGroup;
    private ColorPicker colorPicker;
    private Slider strokeWidthSlider;
    
    public ToolPalette(DrawingController controller) {
        this.controller = controller;
        
        setPadding(new Insets(10));
        setSpacing(10);
        setPrefWidth(200);
        setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 1 0 0;");
        
        createToolSelection();
        createColorSelection();
        createActionButtons();
    }
    
    private void createToolSelection() {
        Label toolLabel = new Label("Outils de dessin:");
        toolLabel.setStyle("-fx-font-weight: bold;");
        
        toolGroup = new ToggleGroup();
        
        RadioButton selectTool = new RadioButton("Sélectionner");
        RadioButton rectangleTool = new RadioButton("Rectangle");
        RadioButton circleTool = new RadioButton("Cercle");
        RadioButton lineTool = new RadioButton("Ligne");
        
        selectTool.setToggleGroup(toolGroup);
        rectangleTool.setToggleGroup(toolGroup);
        circleTool.setToggleGroup(toolGroup);
        lineTool.setToggleGroup(toolGroup);
        
        // Sélectionner l'outil rectangle par défaut
        rectangleTool.setSelected(true);
        controller.setCurrentTool("RECTANGLE");
        
        // Event handlers
        selectTool.setOnAction(e -> controller.setCurrentTool("SELECT"));
        rectangleTool.setOnAction(e -> controller.setCurrentTool("RECTANGLE"));
        circleTool.setOnAction(e -> controller.setCurrentTool("CIRCLE"));
        lineTool.setOnAction(e -> controller.setCurrentTool("LINE"));
        
        getChildren().addAll(toolLabel, selectTool, rectangleTool, circleTool, lineTool);
    }
    
    private void createColorSelection() {
        Label colorLabel = new Label("Couleur:");
        colorLabel.setStyle("-fx-font-weight: bold;");
        
        colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setOnAction(e -> controller.setCurrentColor(colorPicker.getValue()));
        
        getChildren().addAll(new Separator(), colorLabel, colorPicker);
    }
    

    
    private void createActionButtons() {
        Label actionsLabel = new Label("Actions:");
        actionsLabel.setStyle("-fx-font-weight: bold;");
        
        Button undoButton = new Button("Annuler");
        Button redoButton = new Button("Refaire");
        Button clearButton = new Button("Effacer tout");
        
        undoButton.setOnAction(e -> controller.undo());
        redoButton.setOnAction(e -> controller.redo());
        clearButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Effacer le dessin");
            alert.setContentText("Êtes-vous sûr de vouloir effacer tout le dessin ?");
            
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                controller.clearDrawing();
            }
        });
        
        // Style des boutons
        undoButton.setPrefWidth(150);
        redoButton.setPrefWidth(150);
        clearButton.setPrefWidth(150);
        
        getChildren().addAll(new Separator(), actionsLabel, undoButton, redoButton, clearButton);
        
        // Informations sur le dessin
        Label infoLabel = new Label("Informations:");
        infoLabel.setStyle("-fx-font-weight: bold;");
        
        Label shapeCountLabel = new Label("Formes: 0");
        
        // Mettre à jour le compteur de formes
        controller.getDrawing().addObserver((o, arg) -> {
            int count = controller.getDrawing().getShapeCount();
            shapeCountLabel.setText("Formes: " + count);
        });
        
        getChildren().addAll(new Separator(), infoLabel, shapeCountLabel);
    }
}
