package command;

import java.util.Stack;

/**
 * Gestionnaire de commandes pour Undo/Redo
 */
public class CommandManager {
    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;
    
    public CommandManager() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }
    
    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Effacer l'historique redo après une nouvelle commande
    }
    
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }
    
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }
    
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }
    
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
    
    public String getLastUndoDescription() {
        return undoStack.isEmpty() ? "" : undoStack.peek().getDescription();
    }
    
    public String getLastRedoDescription() {
        return redoStack.isEmpty() ? "" : redoStack.peek().getDescription();
    }
    
    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
}
