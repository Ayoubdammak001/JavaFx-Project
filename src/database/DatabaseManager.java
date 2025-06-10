package database;

import javafx.scene.control.Alert;
import model.Drawing;
import model.Shape;
import factory.ShapeFactoryManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ChoiceDialog;
import utils.Logger;

/**
 * Gestionnaire de base de données utilisant le Singleton Pattern
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private Logger logger;

    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:drawing_app.db";
    private Drawing drawing;

    private DatabaseManager() {
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDatabase() {
        try {
            // Charger le driver SQLite
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            createTables();
            System.out.println("Base de données SQLite initialisée avec succès");
        } catch (ClassNotFoundException e) {
            System.err.println("ATTENTION: Driver SQLite non trouvé. Les fonctionnalités de base de données sont désactivées.");
            System.err.println("Pour activer la base de données, téléchargez sqlite-jdbc.jar et ajoutez-le au classpath.");
            connection = null;
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'initialisation de la base de données: " + e.getMessage());
            connection = null;
        }
    }

    private void createTables() throws SQLException {
        String createDrawingsTable = """
            CREATE TABLE IF NOT EXISTS drawings (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        String createShapesTable = """
            CREATE TABLE IF NOT EXISTS shapes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                drawing_id INTEGER,
                shape_type TEXT NOT NULL,
                shape_data TEXT NOT NULL,
                FOREIGN KEY (drawing_id) REFERENCES drawings (id)
            )
        """;

        String createLogsTable = """
            CREATE TABLE IF NOT EXISTS logs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                timestamp TEXT NOT NULL,
                level TEXT NOT NULL,
                message TEXT NOT NULL
            )
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createDrawingsTable);
            stmt.execute(createShapesTable);
            stmt.execute(createLogsTable);
        }
    }

    public Drawing loadDrawing(String drawingName) throws SQLException {
        if (connection == null) {
            throw new SQLException("Connexion à la base de données non disponible");
        }

        Drawing drawing = null;
        String sqlDrawing = "SELECT * FROM drawings WHERE name = ?";
        String sqlShapes = "SELECT * FROM shapes WHERE drawing_id = ?";

        try (PreparedStatement drawingStmt = connection.prepareStatement(sqlDrawing)) {
            drawingStmt.setString(1, drawingName);
            try (ResultSet drawingRs = drawingStmt.executeQuery()) {
                if (drawingRs.next()) {
                    long drawingId = drawingRs.getLong("id");
                    drawing = new Drawing(drawingName);
                    drawing.setId(drawingId);

                    // Charger les formes associées
                    try (PreparedStatement shapeStmt = connection.prepareStatement(sqlShapes)) {
                        shapeStmt.setLong(1, drawingId);
                        try (ResultSet shapesRs = shapeStmt.executeQuery()) {
                            while (shapesRs.next()) {
                                String shapeType = shapesRs.getString("shape_type");
                                String shapeData = shapesRs.getString("shape_data");

                                // Utiliser la factory pour créer la forme
                                Shape shape = parseShapeFromString(shapeType, shapeData);
                                if (shape != null) {
                                    drawing.addShape(shape);
                                }
                            }
                        }
                    }
                }
            }
        }

        if (drawing == null) {
            throw new SQLException("Dessin non trouvé: " + drawingName);
        }

        return drawing;
    }



    public void saveDrawing(Drawing drawing) throws SQLException {
        if (connection == null) {
            System.out.println("Base de données non disponible. Sauvegarde simulée pour: " + drawing.getName());
            return;
        }

        String insertDrawing = "INSERT INTO drawings (name) VALUES (?)";
        String getLastId = "SELECT last_insert_rowid()";
        String insertShape = "INSERT INTO shapes (drawing_id, shape_type, shape_data) VALUES (?, ?, ?)";

        try (PreparedStatement drawingStmt = connection.prepareStatement(insertDrawing)) {
            drawingStmt.setString(1, drawing.getName());
            drawingStmt.executeUpdate();
        }

        // Récupération de l'ID généré
        long drawingId;
        try (Statement stmt = connection.createStatement();
             ResultSet keys = stmt.executeQuery(getLastId)) {
            if (keys.next()) {
                drawingId = keys.getLong(1);
                drawing.setId(drawingId);
            } else {
                throw new SQLException("Impossible de récupérer l'ID du dessin");
            }
        }

        // Insertion des formes associées
        try (PreparedStatement shapeStmt = connection.prepareStatement(insertShape)) {
            for (Shape shape : drawing.getShapes()) {
                shapeStmt.setLong(1, drawingId);
                shapeStmt.setString(2, shape.getClass().getSimpleName());
                shapeStmt.setString(3, shape.toStringRepresentation());
                shapeStmt.executeUpdate();
            }
        }
    }

    public void insertLog(String timestamp, String level, String message) throws SQLException {
        if (connection == null) {
            // Base de données non disponible, ne pas lever d'exception
            return;
        }

        String sql = "INSERT INTO logs (timestamp, level, message) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, timestamp);
            stmt.setString(2, level);
            stmt.setString(3, message);
            stmt.executeUpdate();
        }
    }

    public List<String> getDrawingNames() throws SQLException {
        List<String> names = new ArrayList<>();

        if (connection == null) {
            // Retourner une liste vide si la base de données n'est pas disponible
            return names;
        }

        String sql = "SELECT name FROM drawings ORDER BY created_date DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        }
        return names;
    }

    /**
     * Parse une forme depuis sa représentation string en utilisant ShapeFactory
     * Format attendu: "Rectangle[x=10.00,y=20.00,width=100.00,height=50.00,color=0xff0000ff,strokeWidth=2.00]"
     */
    private Shape parseShapeFromString(String shapeType, String shapeData) {
        try {
            // Extraire les valeurs entre crochets
            int start = shapeData.indexOf('[');
            int end = shapeData.indexOf(']');
            if (start == -1 || end == -1) return null;

            String params = shapeData.substring(start + 1, end);
            String[] pairs = params.split(",");

            double x = 0, y = 0, width = 0, height = 0, radius = 0, endX = 0, endY = 0;
            javafx.scene.paint.Color color = javafx.scene.paint.Color.BLACK;
            double strokeWidth = 2.0;

            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length != 2) continue;

                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                switch (key) {
                    case "x":
                    case "startX":
                        x = Double.parseDouble(value);
                        break;
                    case "y":
                    case "startY":
                        y = Double.parseDouble(value);
                        break;
                    case "width":
                        width = Double.parseDouble(value);
                        break;
                    case "height":
                        height = Double.parseDouble(value);
                        break;
                    case "radius":
                        radius = Double.parseDouble(value);
                        break;
                    case "endX":
                        endX = Double.parseDouble(value);
                        break;
                    case "endY":
                        endY = Double.parseDouble(value);
                        break;
                    case "color":
                        color = javafx.scene.paint.Color.web(value);
                        break;
                    case "strokeWidth":
                        strokeWidth = Double.parseDouble(value);
                        break;
                }
            }

            // Utiliser ShapeFactoryManager pour créer la forme selon le type
            switch (shapeType) {
                case "Rectangle":
                    double[] rectParams = {x, y, width, height};
                    return ShapeFactoryManager.createShape(ShapeFactoryManager.ShapeType.RECTANGLE, rectParams, color, strokeWidth);
                case "Circle":
                    double[] circleParams = {x, y, radius};
                    return ShapeFactoryManager.createShape(ShapeFactoryManager.ShapeType.CIRCLE, circleParams, color, strokeWidth);
                case "Line":
                    double[] lineParams = {x, y, endX, endY};
                    return ShapeFactoryManager.createShape(ShapeFactoryManager.ShapeType.LINE, lineParams, color, strokeWidth);
                default:
                    System.err.println("Type de forme non reconnu: " + shapeType);
                    return null;
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du parsing de la forme: " + e.getMessage());
            return null;
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la base de données: " + e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
