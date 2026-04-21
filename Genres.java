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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Genres extends Application {

    @Override
    public void start(Stage stage) {
        DatabaseHandler.setupDatabase();
        
        // Labels
        Text text1 = new Text("Name:");
        Text text2 = new Text("Registered:");

        // Test Field for Name and ComboBox for Registered Movies
        TextField textField1 = new TextField();
        ComboBox<String> comboBox = new ComboBox<>();

        // Buttons
        Button button1 = new Button("Save");
        Button button2 = new Button("Remove");

        // Grid Pane 
        GridPane gridPane = new GridPane();

        // Grid Pane Configuration 
        gridPane.setMinSize(600, 400);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        // Vertical and Horizontal gaps between columns
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        // Alignment 
        gridPane.setAlignment(Pos.CENTER);

        // Arrange nodes in the grid
        gridPane.add(text1, 0, 0);       
        gridPane.add(textField1, 1, 0);  
        gridPane.add(button1, 1, 1);     
        
        gridPane.add(text2, 0, 2);       
        gridPane.add(comboBox, 1, 2);    
        
        gridPane.add(button2, 1, 3);
        
        
        // Save button action to insert genre into the database
        button1.setOnAction(e -> {
            String genreName = textField1.getText();
            if (genreName.isEmpty()) return;

            String sql = "INSERT INTO Genres(genre, isactive) VALUES(?, 1)";
            try (Connection conn = DatabaseHandler.connect()) {
                if (conn == null) {
                    System.err.println("Cannot save genre: database connection unavailable.");
                    return;
                }
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, genreName); // Sets genre name
                pstmt.executeUpdate(); // Executes the insert statement
                textField1.clear(); // Clear the text field after saving
                System.out.println("Genre saved: " + genreName);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // Used to make a genre inactive in the database
        button2.setOnAction(e -> {
            String selectedGenre = comboBox.getValue();
            if (selectedGenre == null) return;

            String sql = "UPDATE Genres SET isactive = 0 WHERE genre = ?";
            try (Connection conn = DatabaseHandler.connect()) {
                if (conn == null) {
                    System.err.println("Cannot remove genre: database connection unavailable.");
                    return;
                }
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, selectedGenre); // Sets the genre to be removed
                pstmt.executeUpdate(); // Executes the update statement
                comboBox.getItems().remove(selectedGenre); // Remove from ComboBox
                System.out.println("Genre removed: " + selectedGenre);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        
        //  Styling nodes
        button1.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size: 13pt;");
        button2.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size: 13pt;");

        text1.setStyle("-fx-font: normal bold 20px 'serif';");
        text2.setStyle("-fx-font: normal bold 20px 'serif';");
        
        gridPane.setStyle("-fx-background-color: BEIGE;");

        // Creating a scene object
        Scene scene = new Scene(gridPane);

        // Setting title to the Stage
        stage.setTitle("Movie Library System");

        // Adding scene to the stage
        stage.setScene(scene);

        // Displaying the contents of the stage
        stage.show();
    }

   
    public static void main(String[] args) {
        launch(args);
    }
}