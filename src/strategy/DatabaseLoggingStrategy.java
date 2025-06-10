package strategy;

import database.DatabaseManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Stratégie de journalisation dans une base de données
 */
public class DatabaseLoggingStrategy implements LoggingStrategy {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DatabaseManager dbManager;

    public DatabaseLoggingStrategy() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public void log(String message) {
        try {
            dbManager.insertLog(LocalDateTime.now().format(formatter), "INFO", message);
        } catch (Exception e) {
            // Fallback vers la console si la base de données n'est pas disponible
            System.out.println("[DB-LOG] " + LocalDateTime.now().format(formatter) + " INFO: " + message);
        }
    }

    @Override
    public void logAction(String action, String details) {
        try {
            dbManager.insertLog(LocalDateTime.now().format(formatter), "ACTION", action + " - " + details);
        } catch (Exception e) {
            // Fallback vers la console si la base de données n'est pas disponible
            System.out.println("[DB-LOG] " + LocalDateTime.now().format(formatter) + " ACTION: " + action + " - " + details);
        }
    }

    @Override
    public void logError(String error) {
        try {
            dbManager.insertLog(LocalDateTime.now().format(formatter), "ERROR", error);
        } catch (Exception e) {
            // Fallback vers la console si la base de données n'est pas disponible
            System.err.println("[DB-LOG] " + LocalDateTime.now().format(formatter) + " ERROR: " + error);
        }
    }

    @Override
    public String getStrategyName() {
        return "Database Logging";
    }
}
