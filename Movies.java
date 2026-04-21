import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.*;


public class Movies extends Application {

    // Method to load genres from the database and populate the genre combo box
    private void loadGenres(ComboBox<String> comboBoxGenre) {
        comboBoxGenre.getItems().clear(); 

        // Load genres from the database
        String sql = "SELECT genre FROM Genres WHERE isactive = 1 ORDER BY genre";
        try (Connection conn = DatabaseHandler.connect()) {
            if (conn == null) {
                System.err.println("Cannot load genres: database connection unavailable.");
                return;
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    comboBoxGenre.getItems().add(rs.getString("genre"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error loading genres: " + ex.getMessage());
        }
    }
    
    @Override
    public void start(Stage stage) {
        DatabaseHandler.setupDatabase(); // Ensure the database is set up before interacting with it
        // UI elements
        Text labelGenre = new Text("Genre:");
        ComboBox<String> comboBoxGenre = new ComboBox<>();
        comboBoxGenre.setPrefWidth(250);

        Text lblName = new Text("Name:");
        TextField textFieldName = new TextField();

        Button buttonSave = new Button("Save Movie");

        Text lblRegistered = new Text("Registered Movies:");
        ComboBox<String> comboBoxRegistered = new ComboBox<>();

        Button buttonRemove = new Button("Remove Movie ");

        // Gridpane configuration
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(600, 400);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(labelGenre, 0, 0);
        gridPane.add(comboBoxGenre, 1, 0);
        gridPane.add(lblName, 0, 1);
        gridPane.add(textFieldName, 1, 1);
        gridPane.add(buttonSave, 1, 2);
        gridPane.add(lblRegistered, 0, 3);
        gridPane.add(comboBoxRegistered, 1, 3);
        gridPane.add(buttonRemove, 1, 4);

        loadGenres(comboBoxGenre);

        
        // Load genres from the database
        buttonSave.setOnAction(e -> {
            String genre = comboBoxGenre.getValue();
            String name = textFieldName.getText();
            // Ensures that genre is selected & name is not empty
            if (genre != null && !name.isEmpty()) {
                try (Connection conn = DatabaseHandler.connect()) {
                    if (conn == null) {
                        System.err.println("Cannot save movie: database connection unavailable.");
                        return;
                    }
                    String sql = "INSERT INTO Movies (Title, genre_id) VALUES (?, (SELECT id FROM Genres WHERE genre = ?))";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, name); // Set the movie title
                    pstmt.setString(2, genre); // Set the genre
                    pstmt.executeUpdate(); // Execute the insert statement
                    comboBoxRegistered.getItems().add(name); // Add the new movie to the registered movies combo box
                    textFieldName.clear(); // Clears field after saving
                } catch (SQLException ex) {
                    System.err.println("Error saving movie: " + ex.getMessage());
                }
            }
        });

        // Remove movie action
        buttonRemove.setOnAction(e -> {
            String selectedMovie = comboBoxRegistered.getValue();
            if (selectedMovie != null) {
                try (Connection conn = DatabaseHandler.connect()) {
                    if (conn == null) {
                        System.err.println("Cannot remove movie: database connection unavailable.");
                        return;
                    }
                    String sql = "DELETE FROM Movies WHERE Title = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql); 
                    pstmt.setString(1, selectedMovie); // Set the movie title to delete
                    pstmt.executeUpdate(); // Execute the delete statement
                    comboBoxRegistered.getItems().remove(selectedMovie); // Remove from combo box
                } catch (SQLException ex) {
                    System.err.println("Error removing movie: " + ex.getMessage());
                }
            }
        });

        // Styling
        buttonSave.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size: 13pt;");
        buttonRemove.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size: 13pt;");

        lblName.setStyle("-fx-font: normal bold 20px 'serif';");
        lblRegistered.setStyle("-fx-font: normal bold 20px 'serif';");
        labelGenre.setStyle("-fx-font: normal bold 20px 'serif';");

        gridPane.setStyle("-fx-background-color: BEIGE;");

        // Create a scene and place it in the stage
        Scene scene = new Scene(gridPane);
        stage.setScene(scene);
        stage.setTitle("Movies");
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

}
