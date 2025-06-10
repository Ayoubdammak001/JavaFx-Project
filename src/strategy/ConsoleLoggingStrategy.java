package strategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Stratégie de journalisation dans la console
 */
public class ConsoleLoggingStrategy implements LoggingStrategy {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void log(String message) {
        System.out.println("[" + LocalDateTime.now().format(formatter) + "] " + message);
    }
    
    @Override
    public void logAction(String action, String details) {
        log("ACTION: " + action + " - " + details);
    }
    
    @Override
    public void logError(String error) {
        System.err.println("[" + LocalDateTime.now().format(formatter) + "] ERROR: " + error);
    }
    
    @Override
    public String getStrategyName() {
        return "Console Logging";
    }
}
