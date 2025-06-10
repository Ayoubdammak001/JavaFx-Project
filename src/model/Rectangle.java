package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Classe représentant un rectangle
 */
public class Rectangle extends Shape {
    private double width, height;
    
    public Rectangle(double x, double y, double width, double height, Color color, double strokeWidth) {
        super(x, y, color, strokeWidth);
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setLineWidth(strokeWidth);
        gc.strokeRect(x, y, width, height);
    }
    
    @Override
    public boolean contains(double pointX, double pointY) {
        return pointX >= x && pointX <= x + width && 
               pointY >= y && pointY <= y + height;
    }
    
    @Override
    public String toStringRepresentation() {
        return String.format("Rectangle[x=%.2f,y=%.2f,width=%.2f,height=%.2f,color=%s,strokeWidth=%.2f]",
                x, y, width, height, color.toString(), strokeWidth);
    }
    
    // Getters et setters
    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }
    
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
}
