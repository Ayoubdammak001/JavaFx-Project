package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import controller.DrawingController;
import model.Shape;
import java.util.Observable;
import java.util.Observer;

/**
 * Canvas de dessin utilisant le pattern Observer
 */
@SuppressWarnings("deprecation")
public class DrawingCanvas extends Pane implements Observer {
    private Canvas canvas;
    private GraphicsContext gc;
    private DrawingController controller;
    private double startX, startY;
    private boolean isDrawing = false;
    
    public DrawingCanvas(DrawingController controller) {
        this.controller = controller;
        
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        
        // Observer pattern - écouter les changements du modèle
        controller.getDrawing().addObserver(this);
        
        setupEventHandlers();
        getChildren().add(canvas);
        
        // Style initial
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
    }
    
    private void setupEventHandlers() {
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseReleased(this::handleMouseReleased);
        canvas.setOnMouseClicked(this::handleMouseClicked);
    }
    
    private void handleMousePressed(MouseEvent event) {
        startX = event.getX();
        startY = event.getY();
        isDrawing = true;
        
        if (controller.getCurrentTool().equals("SELECT")) {
            Shape selectedShape = controller.getDrawing().getShapeAt(startX, startY);
            if (selectedShape != null) {
                controller.selectShape(selectedShape);
            }
        }
    }
    
    private void handleMouseDragged(MouseEvent event) {
        if (isDrawing && !controller.getCurrentTool().equals("SELECT")) {
            // Prévisualisation pendant le dessin
            redrawCanvas();
            drawPreview(event.getX(), event.getY());
        }
    }
    
    private void handleMouseReleased(MouseEvent event) {
        if (isDrawing && !controller.getCurrentTool().equals("SELECT")) {
            double endX = event.getX();
            double endY = event.getY();
            
            // Créer la forme finale
            controller.createShape(startX, startY, endX, endY);
        }
        isDrawing = false;
    }
    
    private void handleMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            // Double-clic pour éditer
            Shape shape = controller.getDrawing().getShapeAt(event.getX(), event.getY());
            if (shape != null) {
                controller.editShape(shape);
            }
        }
    }
    
    private void drawPreview(double currentX, double currentY) {
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);
        
        String tool = controller.getCurrentTool();
        switch (tool) {
            case "RECTANGLE":
                double width = Math.abs(currentX - startX);
                double height = Math.abs(currentY - startY);
                double rectX = Math.min(startX, currentX);
                double rectY = Math.min(startY, currentY);
                gc.strokeRect(rectX, rectY, width, height);
                break;
            case "CIRCLE":
                double radius = Math.sqrt(Math.pow(currentX - startX, 2) + Math.pow(currentY - startY, 2));
                gc.strokeOval(startX - radius, startY - radius, radius * 2, radius * 2);
                break;
            case "LINE":
                gc.strokeLine(startX, startY, currentX, currentY);
                break;
        }
        
        // Restaurer les paramètres de dessin
        gc.setStroke(controller.getCurrentColor());
        gc.setLineWidth(controller.getCurrentStrokeWidth());
    }
    
    private void redrawCanvas() {
        // Effacer le canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Redessiner toutes les formes
        for (Shape shape : controller.getDrawing().getShapes()) {
            shape.draw(gc);
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
        // Observer pattern - redessiner quand le modèle change
        redrawCanvas();
    }
    
    public Canvas getCanvas() {
        return canvas;
    }
}
