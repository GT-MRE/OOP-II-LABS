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

public class Rentals extends Application {
     @Override
    public void start(Stage stage) {
        // UI elements
       Text labelCustomer = new Text("Customer:");
       ComboBox<String> comboBoxCustomer = new ComboBox<>();
       comboBoxCustomer.setPrefWidth(250);

       Text labelGenre = new Text("Genre:");
       ComboBox<String> comboBoxGenre = new ComboBox<>();
       comboBoxGenre.setPrefWidth(250);
       
       Text labelMovie = new Text("Movies:");
        ComboBox<String> comboBoxMovie = new ComboBox<>();
        comboBoxMovie.setPrefWidth(250);

        Button btnSave = new Button("Save Rented Movie");

        Text labelBorrowed = new Text("Borrowed:");
        ComboBox<String> comboBoxBorrowed = new ComboBox<>();
        comboBoxBorrowed.setPrefWidth(250);

        Button btnReturn = new Button("Return Movie");

        Text labelReturned = new Text("Returned:");
        ComboBox<String> comboBoxReturned = new ComboBox<>();
        comboBoxReturned.setPrefWidth(250);

        //GridPane configuration
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(600,450);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(15);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(labelCustomer, 0, 0);
        gridPane.add(comboBoxCustomer, 1, 0);   

        gridPane.add(labelGenre, 0, 1);
        gridPane.add(comboBoxGenre, 1, 1);

        gridPane.add(labelMovie, 0, 2);
        gridPane.add(comboBoxMovie, 1, 2);

        gridPane.add(labelBorrowed, 0, 4);
        gridPane.add(comboBoxBorrowed, 1, 4);

        gridPane.add(btnSave, 1, 3);
        gridPane.add(btnReturn, 1, 5);

        gridPane.add(labelReturned, 0, 6);
        gridPane.add(comboBoxReturned, 1, 6);

        //Styling
        btnSave.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size: 13pt;");
        btnReturn.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size: 13pt;");

        labelCustomer.setStyle("-fx-font: normal bold 20px 'serif';");
        labelGenre.setStyle("-fx-font: normal bold 20px 'serif';");
        labelMovie.setStyle("-fx-font: normal bold 20px 'serif';");
        labelBorrowed.setStyle("-fx-font: normal bold 20px 'serif';");
        labelReturned.setStyle("-fx-font: normal bold 20px 'serif';");
        
        gridPane.setStyle("-fx-background-color: BEIGE;");

        // Set the scene and show the stage
        Scene scene = new Scene(gridPane);
        stage.setTitle("Movie Rentals");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
