package utils;

import strategy.LoggingStrategy;
import strategy.ConsoleLoggingStrategy;

/**
 * Classe Logger utilisant le Strategy Pattern
 */
public class Logger {
    private static Logger instance;
    private LoggingStrategy strategy;
    
    private Logger() {
        this.strategy = new ConsoleLoggingStrategy(); // Stratégie par défaut
    }
    
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
    
    public void setStrategy(LoggingStrategy strategy) {
        this.strategy = strategy;
        log("Stratégie de journalisation changée vers: " + strategy.getStrategyName());
    }
    
    public void log(String message) {
        if (strategy != null) {
            strategy.log(message);
        }
    }
    
    public void logAction(String action, String details) {
        if (strategy != null) {
            strategy.logAction(action, details);
        }
    }
    
    public void logError(String error) {
        if (strategy != null) {
            strategy.logError(error);
        }
    }
    
    public String getCurrentStrategyName() {
        return strategy != null ? strategy.getStrategyName() : "Aucune stratégie";
    }
}
