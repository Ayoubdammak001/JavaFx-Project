package factory;

import model.Shape;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire des factories de formes
 * Utilise le pattern Factory Method avec des factories concrètes
 */
public class ShapeFactoryManager {
    
    public enum ShapeType {
        RECTANGLE, CIRCLE, LINE
    }
    
    private static final Map<ShapeType, ShapeFactory> factories = new HashMap<>();
    
    // Initialisation statique des factories
    static {
        factories.put(ShapeType.RECTANGLE, new RectangleFactory());
        factories.put(ShapeType.CIRCLE, new CircleFactory());
        factories.put(ShapeType.LINE, new LineFactory());
    }
    
    /**
     * Crée une forme en utilisant la factory appropriée
     */
    public static Shape createShape(ShapeType type, double[] parameters, Color color, double strokeWidth) {
        ShapeFactory factory = factories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Type de forme non supporté: " + type);
        }
        
        return factory.createShape(parameters, color, strokeWidth);
    }

}
