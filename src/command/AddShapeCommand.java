package command;

import model.Drawing;
import model.Shape;
import utils.Logger;

/**
 * Commande pour ajouter une forme
 */
public class AddShapeCommand implements Command {
    private final Drawing drawing;
    private final Shape shape;
    private final Logger logger;
    
    public AddShapeCommand(Drawing drawing, Shape shape) {
        this.drawing = drawing;
        this.shape = shape;
        this.logger = Logger.getInstance();
    }
    
    @Override
    public void execute() {
        drawing.addShape(shape);
        logger.logAction("ADD_SHAPE", shape.toStringRepresentation());
    }
    
    @Override
    public void undo() {
        drawing.removeShape(shape);
        logger.logAction("UNDO_ADD_SHAPE", shape.toStringRepresentation());
    }
    
    @Override
    public String getDescription() {
        return "Ajouter " + shape.getClass().getSimpleName();
    }
}
