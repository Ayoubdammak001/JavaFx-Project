package factory;

import model.Shape;
import javafx.scene.paint.Color;

/**
 * Interface abstraite pour le Factory Method Pattern
 * Chaque type de forme aura sa propre factory concrète
 */
public abstract class ShapeFactory {

    /**
     * Méthode factory abstraite pour créer une forme
     */
    public abstract Shape createShape(double[] parameters, Color color, double strokeWidth);


    /**
     * Méthode pour valider les paramètres
     */
    public abstract boolean validateParameters(double[] parameters);

}
