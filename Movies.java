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


public class Movies extends Application {
    
    @Override
    public void start(Stage stage) {
        // UI elements
        Text labelGenre = new Text("Genre:");
        ComboBox<String> comboBoxGenre = new ComboBox<>();

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
