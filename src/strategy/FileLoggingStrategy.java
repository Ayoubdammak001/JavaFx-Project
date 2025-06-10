package strategy;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Stratégie de journalisation dans un fichier
 */
public class FileLoggingStrategy implements LoggingStrategy {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String filename;
    
    public FileLoggingStrategy(String filename) {
        this.filename = filename;
    }
    
    public FileLoggingStrategy() {
        this("drawing_app.log");
    }
    
    @Override
    public void log(String message) {
        writeToFile("[" + LocalDateTime.now().format(formatter) + "] " + message);
    }
    
    @Override
    public void logAction(String action, String details) {
        log("ACTION: " + action + " - " + details);
    }
    
    @Override
    public void logError(String error) {
        writeToFile("[" + LocalDateTime.now().format(formatter) + "] ERROR: " + error);
    }
    
    private void writeToFile(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            writer.println(message);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans le fichier de log: " + e.getMessage());
        }
    }
    
    @Override
    public String getStrategyName() {
        return "File Logging (" + filename + ")";
    }
}
