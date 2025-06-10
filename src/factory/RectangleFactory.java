package factory;

import model.Rectangle;
import model.Shape;
import javafx.scene.paint.Color;

/**
 * Factory concrète pour créer des rectangles
 */
public class RectangleFactory extends ShapeFactory {
    
    @Override
    public Shape createShape(double[] parameters, Color color, double strokeWidth) {
        if (!validateParameters(parameters)) {
            throw new IllegalArgumentException("Paramètres insuffisants pour créer un rectangle. Attendu: [x, y, width, height]");
        }
        
        return new Rectangle(parameters[0], parameters[1], parameters[2], parameters[3], color, strokeWidth);
    }

    @Override
    public boolean validateParameters(double[] parameters) {
        return parameters != null && parameters.length >= 4;
    }
}
