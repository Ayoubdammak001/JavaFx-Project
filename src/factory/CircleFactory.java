package factory;

import model.Circle;
import model.Shape;
import javafx.scene.paint.Color;

/**
 * Factory concrète pour créer des cercles
 */
public class CircleFactory extends ShapeFactory {
    
    @Override
    public Shape createShape(double[] parameters, Color color, double strokeWidth) {
        if (!validateParameters(parameters)) {
            throw new IllegalArgumentException("Paramètres insuffisants pour créer un cercle. Attendu: [x, y, radius]");
        }
        
        return new Circle(parameters[0], parameters[1], parameters[2], color, strokeWidth);
    }

    @Override
    public boolean validateParameters(double[] parameters) {
        return parameters != null && parameters.length >= 3;
    }

}
