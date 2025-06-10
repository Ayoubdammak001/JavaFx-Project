package command;

import model.Drawing;
import model.Shape;
import utils.Logger;

/**
 * Commande pour supprimer une forme
 */
public class RemoveShapeCommand implements Command {
    private final Drawing drawing;
    private final Shape shape;
    private final Logger logger;
    
    public RemoveShapeCommand(Drawing drawing, Shape shape) {
        this.drawing = drawing;
        this.shape = shape;
        this.logger = Logger.getInstance();
    }
    
    @Override
    public void execute() {
        drawing.removeShape(shape);
        logger.logAction("REMOVE_SHAPE", shape.toStringRepresentation());
    }
    
    @Override
    public void undo() {
        drawing.addShape(shape);
        logger.logAction("UNDO_REMOVE_SHAPE", shape.toStringRepresentation());
    }
    
    @Override
    public String getDescription() {
        return "Supprimer " + shape.getClass().getSimpleName();
    }
}
