package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Classe représentant une ligne
 */
public class Line extends Shape {
    private double endX, endY;
    
    public Line(double startX, double startY, double endX, double endY, Color color, double strokeWidth) {
        super(startX, startY, color, strokeWidth);
        this.endX = endX;
        this.endY = endY;
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setLineWidth(strokeWidth);
        gc.strokeLine(x, y, endX, endY);
    }
    
    @Override
    public boolean contains(double pointX, double pointY) {
        // Vérifier si le point est proche de la ligne (tolérance de 5 pixels)
        double tolerance = 5.0;
        double lineLength = Math.sqrt(Math.pow(endX - x, 2) + Math.pow(endY - y, 2));
        
        if (lineLength == 0) return false;
        
        double distance = Math.abs((endY - y) * pointX - (endX - x) * pointY + endX * y - endY * x) / lineLength;
        
        // Vérifier aussi que le point est dans les limites de la ligne
        double minX = Math.min(x, endX) - tolerance;
        double maxX = Math.max(x, endX) + tolerance;
        double minY = Math.min(y, endY) - tolerance;
        double maxY = Math.max(y, endY) + tolerance;
        
        return distance <= tolerance && pointX >= minX && pointX <= maxX && pointY >= minY && pointY <= maxY;
    }
    
    @Override
    public String toStringRepresentation() {
        return String.format("Line[startX=%.2f,startY=%.2f,endX=%.2f,endY=%.2f,color=%s,strokeWidth=%.2f]",
                x, y, endX, endY, color.toString(), strokeWidth);
    }
    
    // Getters et setters
    public double getEndX() { return endX; }
    public void setEndX(double endX) { this.endX = endX; }
    
    public double getEndY() { return endY; }
    public void setEndY(double endY) { this.endY = endY; }
}
