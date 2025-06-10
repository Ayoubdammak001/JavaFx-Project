package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Classe représentant un cercle
 */
public class Circle extends Shape {
    private double radius;
    
    public Circle(double x, double y, double radius, Color color, double strokeWidth) {
        super(x, y, color, strokeWidth);
        this.radius = radius;
    }
    
    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setLineWidth(strokeWidth);
        gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
    }
    
    @Override
    public boolean contains(double pointX, double pointY) {
        double distance = Math.sqrt(Math.pow(pointX - x, 2) + Math.pow(pointY - y, 2));
        return distance <= radius;
    }
    
    @Override
    public String toStringRepresentation() {
        return String.format("Circle[x=%.2f,y=%.2f,radius=%.2f,color=%s,strokeWidth=%.2f]",
                x, y, radius, color.toString(), strokeWidth);
    }
    
    // Getters et setters
    public double getRadius() { return radius; }
    public void setRadius(double radius) { this.radius = radius; }
}
