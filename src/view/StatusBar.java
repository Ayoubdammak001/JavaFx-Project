package view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import controller.DrawingController;
import utils.Logger;

/**
 * Barre de statut de l'application
 */
public class StatusBar extends HBox {
    private Label statusLabel;
    private Label toolLabel;
    private Label loggingLabel;
    private DrawingController controller;
    private Logger logger;
    
    public StatusBar(DrawingController controller) {
        this.controller = controller;
        this.logger = Logger.getInstance();
        
        setPadding(new Insets(5, 10, 5, 10));
        setSpacing(10);
        setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #cccccc; -fx-border-width: 1 0 0 0;");
        
        createStatusElements();
        updateStatus();
    }
    
    private void createStatusElements() {
        statusLabel = new Label("Prêt");
        toolLabel = new Label("Outil: Rectangle");
        loggingLabel = new Label("Log: " + logger.getCurrentStrategyName());
        
        // Espaceur pour pousser les éléments vers la droite
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        getChildren().addAll(statusLabel, spacer, toolLabel, new Label("|"), loggingLabel);
    }
    
    public void updateStatus() {
        String currentTool = controller.getCurrentTool();
        toolLabel.setText("Outil: " + getToolDisplayName(currentTool));
        loggingLabel.setText("Log: " + logger.getCurrentStrategyName());
    }
    
    public void setStatus(String status) {
        statusLabel.setText(status);
    }
    
    private String getToolDisplayName(String tool) {
        switch (tool) {
            case "SELECT": return "Sélection";
            case "RECTANGLE": return "Rectangle";
            case "CIRCLE": return "Cercle";
            case "LINE": return "Ligne";
            default: return tool;
        }
    }
}
