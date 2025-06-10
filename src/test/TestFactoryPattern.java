package test;

import factory.ShapeFactoryManager;
import factory.RectangleFactory;
import factory.CircleFactory;
import factory.LineFactory;
import model.Shape;
import javafx.scene.paint.Color;

/**
 * Test simple pour vérifier le bon fonctionnement du pattern Factory Method
 */
public class TestFactoryPattern {

    public static void main(String[] args) {
        System.out.println("=== Test du Pattern Factory Method ===");

        try {
            // Test avec ShapeFactoryManager
            System.out.println("\n--- Test avec ShapeFactoryManager ---");

            // Test création Rectangle
            double[] rectParams = {10.0, 20.0, 100.0, 50.0};
            Shape rectangle = ShapeFactoryManager.createShape(
                ShapeFactoryManager.ShapeType.RECTANGLE,
                rectParams,
                Color.RED,
                2.0
            );
            System.out.println("Rectangle créé: " + rectangle.toStringRepresentation());

            // Test création Circle
            double[] circleParams = {50.0, 50.0, 25.0};
            Shape circle = ShapeFactoryManager.createShape(
                ShapeFactoryManager.ShapeType.CIRCLE,
                circleParams,
                Color.BLUE,
                1.5
            );
            System.out.println("Circle créé: " + circle.toStringRepresentation());

            // Test création Line
            double[] lineParams = {0.0, 0.0, 100.0, 100.0};
            Shape line = ShapeFactoryManager.createShape(
                ShapeFactoryManager.ShapeType.LINE,
                lineParams,
                Color.GREEN,
                3.0
            );
            System.out.println("Line créée: " + line.toStringRepresentation());

            // Test avec les factories individuelles
            System.out.println("\n--- Test avec les factories individuelles ---");

            RectangleFactory rectFactory = new RectangleFactory();
            Shape rect2 = rectFactory.createShape(rectParams, Color.YELLOW, 1.0);
            System.out.println("Rectangle via RectangleFactory: " + rect2.toStringRepresentation());

            CircleFactory circleFactory = new CircleFactory();
            Shape circle2 = circleFactory.createShape(circleParams, Color.PURPLE, 2.5);
            System.out.println("Circle via CircleFactory: " + circle2.toStringRepresentation());

            LineFactory lineFactory = new LineFactory();
            Shape line2 = lineFactory.createShape(lineParams, Color.ORANGE, 1.5);
            System.out.println("Line via LineFactory: " + line2.toStringRepresentation());

            System.out.println("\n=== Test réussi ! Le pattern Factory Method fonctionne correctement ===");

        } catch (Exception e) {
            System.err.println("Erreur lors du test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
