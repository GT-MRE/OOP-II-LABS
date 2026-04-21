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
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customers extends Application {
    
    @Override
    public void start(Stage stage) {
        DatabaseHandler.setupDatabase();
        // UI elements
        Text labelName = new Text("Name:");
        TextField textFieldName = new TextField();

        Text labelPhone = new Text("Phone:");
        TextField textFieldPhone = new TextField();

        Text labelEmail = new Text("Email:");
        TextField textFieldEmail = new TextField();

        Button btnSave = new Button("Save Customer");

        Text labelRegistered = new Text("Registered:");
        ComboBox<String> comboBoxRegistered = new ComboBox<>();

        Button btnRemove = new Button("Remove Customer");

        // Gridpane configuration
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(600,400);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(labelName, 0, 0);
        gridPane.add(textFieldName, 1, 0);
        gridPane.add(labelPhone, 0, 1);
        gridPane.add(textFieldPhone, 1, 1);
        gridPane.add(labelEmail, 0, 2);
        gridPane.add(textFieldEmail, 1, 2);
        gridPane.add(labelRegistered, 0, 4);
        gridPane.add(comboBoxRegistered, 1, 4);
        gridPane.add(btnSave, 1, 3);
        gridPane.add(btnRemove, 1, 5);

        // Load active customers saved in previous sessions.
        String loadSql = "SELECT Fullname FROM Clients WHERE isactive = 1 ORDER BY Fullname";
        try (Connection conn = DatabaseHandler.connect()) {
            if (conn != null) {
                PreparedStatement pstmt = conn.prepareStatement(loadSql);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    comboBoxRegistered.getItems().add(rs.getString("Fullname")); // Add active customers to the combo box
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error loading customers: " + ex.getMessage());
        }


       // Save button action to insert customer into the database
        btnSave.setOnAction(e -> {
            String name = textFieldName.getText();
            String phone = textFieldPhone.getText();
            String email = textFieldEmail.getText();
            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) return; 
            // SQL statement to insert a new customer into the Clients table
            String sql = "INSERT INTO Clients(Fullname, Phone, Email, isactive) VALUES(?, ?, ?, 1)";
            try (Connection conn = DatabaseHandler.connect()) {
                if (conn == null) {
                    System.err.println("Cannot save customer: database connection unavailable.");
                    return;
                }
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, name); // Sets customer name
                pstmt.setString(2, phone); // Sets customer phone
                pstmt.setString(3, email); // Sets customer email
                pstmt.executeUpdate(); // Executes the insert statement
                comboBoxRegistered.getItems().add(name); // Add the new customer to the registered customers combo box  
                textFieldName.clear();
                textFieldPhone.clear();
                textFieldEmail.clear();
            } catch (SQLException ex) {
                System.err.println("Error saving customer: " + ex.getMessage());
            }
        });

        // Remove customer action
        btnRemove.setOnAction(e -> {
            String selectedCustomer = comboBoxRegistered.getValue();
            if (selectedCustomer == null) return;
            
            String sql = "UPDATE Clients SET isactive = 0 WHERE Fullname = ?";
            try (Connection conn = DatabaseHandler.connect()) {
                if (conn == null) {
                    System.err.println("Cannot remove customer: database connection unavailable.");
                    return;
                }
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, selectedCustomer); // Set the customer name to deactivate
                pstmt.executeUpdate(); // Execute the update statement
                comboBoxRegistered.getItems().remove(selectedCustomer); // Remove from combo box
            } catch (SQLException ex) {
                System.err.println("Error removing customer: " + ex.getMessage());
            }
        });
        // Styling
        btnSave.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size: 13pt;");
        btnRemove.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size: 13pt;");

        labelName.setStyle("-fx-font: normal bold 20px 'serif';");
        labelPhone.setStyle("-fx-font: normal bold 20px 'serif';");
        labelEmail.setStyle("-fx-font: normal bold 20px 'serif';");
        labelRegistered.setStyle("-fx-font: normal bold 20px 'serif';");

        gridPane.setStyle("-fx-background-color: BEIGE;");

        // Create a scene and place it in the stage
        Scene scene = new Scene(gridPane);
        stage.setScene(scene); 
        stage.setTitle("Customers");
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}

    