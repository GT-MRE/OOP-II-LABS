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

public class Customers extends Application {
    
    @Override
    public void start(Stage stage) {
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

    