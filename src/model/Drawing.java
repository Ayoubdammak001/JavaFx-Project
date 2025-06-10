package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Classe représentant un dessin (collection de formes)
 * Utilise le pattern Observer pour notifier les changements
 */
@SuppressWarnings("deprecation")
public class Drawing extends Observable {
    private List<Shape> shapes;
    private String name;
    private long id;
    
    public Drawing() {
        this.shapes = new ArrayList<>();
        this.name = "Nouveau Dessin";
        this.id = System.currentTimeMillis();
    }
    
    public Drawing(String name) {
        this();
        this.name = name;
    }
    
    public void addShape(Shape shape) {
        shapes.add(shape);
        setChanged();
        notifyObservers("SHAPE_ADDED");
    }
    
    public void removeShape(Shape shape) {
        if (shapes.remove(shape)) {
            setChanged();
            notifyObservers("SHAPE_REMOVED");
        }
    }
    
    public void clear() {
        shapes.clear();
        setChanged();
        notifyObservers("DRAWING_CLEARED");
    }
    
    public List<Shape> getShapes() {
        return new ArrayList<>(shapes);
    }
    
    public Shape getShapeAt(double x, double y) {
        // Retourne la dernière forme (celle du dessus) qui contient le point
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape shape = shapes.get(i);
            if (shape.contains(x, y)) {
                return shape;
            }
        }
        return null;
    }
    
    public int getShapeCount() {
        return shapes.size();
    }
    
    public boolean isEmpty() {
        return shapes.isEmpty();
    }
    
    // Getters et setters
    public String getName() { return name; }
    public void setName(String name) { 
        this.name = name;
        setChanged();
        notifyObservers("NAME_CHANGED");
    }
    
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
}
