package strategy;

/**
 * Interface pour le Strategy Pattern de journalisation
 */
public interface LoggingStrategy {
    void log(String message);
    void logAction(String action, String details);
    void logError(String error);
    String getStrategyName();
}
