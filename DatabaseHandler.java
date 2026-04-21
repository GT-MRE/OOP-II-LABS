import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {

    // Database connection parameters with environment variable overrides
    private static final String DB_HOST = getSetting("DB_HOST", "localhost"); //
    private static final String DB_PORT = getSetting("DB_PORT", "3307");
    private static final String DB_NAME = getSetting("DB_NAME", "vls_db");
    private static final String USER = getSetting("DB_USER", getSetting("MYSQL_USER", "root"));
    private static final String PASS = getSetting("DB_PASS", getSetting("MYSQL_PASSWORD", ""));
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_SERVER_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT
            + "/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            // Helper method to get configuration settings with environment variable and system property overrides

    private static String getSetting(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = System.getProperty(key);
        }
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }

    // Method to establish a connection to the MySQL database
    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Ensure the MySQL JDBC driver is loaded
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
            System.err.println("Tried DB at " + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + " with user '" + USER + "'.");
        }
        return conn;
    }
// Method to ensure the database exists before trying to connect to it
    private static void ensureDatabaseExists() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Ensure the MySQL JDBC driver is loaded
            try (Connection conn = DriverManager.getConnection(DB_SERVER_URL, USER, PASS);
                 Statement stmt = conn.createStatement())  {
                stmt.execute("CREATE DATABASE IF NOT EXISTS `" + DB_NAME + "`");
            }
        } catch (Exception e) {
            System.err.println("Could not create/check database '" + DB_NAME + "': " + e.getMessage());
        }
    }

   // Helper method to check if a specific column exists in a given table, used for schema updates
    private static boolean columnExists(Connection conn, String tableName, String columnName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        try (ResultSet columns = metaData.getColumns(null, null, tableName, columnName)) {
            return columns.next();
        }
    }

    // Method to set up the database tables
    public static void setupDatabase() {
        ensureDatabaseExists();

        Connection conn = connect();
        if (conn == null) {
            System.err.println("Database setup skipped: no database connection.");
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            
            // Creation of tables
            stmt.execute("CREATE TABLE IF NOT EXISTS Genres (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "genre VARCHAR(255) NOT NULL, " +
                    "isactive BOOLEAN DEFAULT 1)");

            stmt.execute("CREATE TABLE IF NOT EXISTS Movies (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "genre_id INT, " +
                    "Title VARCHAR(255) NOT NULL, " +
                    "isactive BOOLEAN DEFAULT 1, " +
                    "FOREIGN KEY (genre_id) REFERENCES Genres(id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS Clients (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "Fullname VARCHAR(255) NOT NULL, " +
                    "Phone VARCHAR(30), " +
                    "Email VARCHAR(255), " +
                    "isactive BOOLEAN DEFAULT 1)");

            stmt.execute("CREATE TABLE IF NOT EXISTS Rentals (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "client_id INT, " +
                    "movie_id INT, " +
                    "rental_date DATE, " +
                    "return_date DATE, " +
                    "isreturned BOOLEAN DEFAULT 0, " +
                    "FOREIGN KEY (client_id) REFERENCES Clients(id), " +
                    "FOREIGN KEY (movie_id) REFERENCES Movies(id))");

                // Make existing databases forward-compatible with the current schema.
                if (!columnExists(conn, "Clients", "Phone")) {
                    stmt.execute("ALTER TABLE Clients ADD COLUMN Phone VARCHAR(30)");
                }
                if (!columnExists(conn, "Clients", "Email")) {
                    stmt.execute("ALTER TABLE Clients ADD COLUMN Email VARCHAR(255)");
                }
                if (!columnExists(conn, "Rentals", "return_date")) {
                    stmt.execute("ALTER TABLE Rentals ADD COLUMN return_date DATE");
                }

            System.out.println("MySQL Database Tables Initialized.");
        } catch (SQLException e) {
            System.err.println("Database setup error: " + e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }
}