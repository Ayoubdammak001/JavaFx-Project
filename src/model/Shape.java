package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Classe abstraite représentant une forme géométrique
 */
public abstract class Shape {
    protected double x, y;
    protected Color color;
    protected double strokeWidth;
    
    public Shape(double x, double y, Color color, double strokeWidth) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.strokeWidth = strokeWidth;
    }
    
    // Méthode abstraite pour dessiner la forme
    public abstract void draw(GraphicsContext gc);
    
    // Méthode abstraite pour vérifier si un point est dans la forme
    public abstract boolean contains(double x, double y);
    
    // Méthode pour obtenir une représentation string de la forme
    public abstract String toStringRepresentation();
    
    // Getters et setters
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
    
    public double getStrokeWidth() { return strokeWidth; }
    public void setStrokeWidth(double strokeWidth) { this.strokeWidth = strokeWidth; }

}
