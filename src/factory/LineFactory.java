package factory;

import model.Line;
import model.Shape;
import javafx.scene.paint.Color;

/**
 * Factory concrète pour créer des lignes
 */
public class LineFactory extends ShapeFactory {
    
    @Override
    public Shape createShape(double[] parameters, Color color, double strokeWidth) {
        if (!validateParameters(parameters)) {
            throw new IllegalArgumentException("Paramètres insuffisants pour créer une ligne. Attendu: [startX, startY, endX, endY]");
        }
        
        return new Line(parameters[0], parameters[1], parameters[2], parameters[3], color, strokeWidth);
    }

    @Override
    public boolean validateParameters(double[] parameters) {
        return parameters != null && parameters.length >= 4;
    }

}
