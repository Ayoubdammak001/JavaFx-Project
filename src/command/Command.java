package command;

/**
 * Interface pour le Command Pattern
 */
public interface Command {
    void execute();
    void undo();
    String getDescription();
}
