import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GuiLauncher extends Application {

    @Override
    public void start(Stage stage) {
        
        // Buttons for the GUIs
        Button moviesButton = new Button("Open Movies");
        Button rentalsButton = new Button("Open Rentals");
        Button customersButton = new Button("Open Customers");
        Button genresButton = new Button("Open Genres");

        moviesButton.setPrefWidth(220);
        rentalsButton.setPrefWidth(220);
        customersButton.setPrefWidth(220);
        genresButton.setPrefWidth(220);

        
        String buttonStyle = "-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font-size: 13pt;";
        moviesButton.setStyle(buttonStyle);
        rentalsButton.setStyle(buttonStyle);
        customersButton.setStyle(buttonStyle);
        genresButton.setStyle(buttonStyle);

        // Event handling
        moviesButton.setOnAction(event -> openGui(new Movies()));
        rentalsButton.setOnAction(event -> openGui(new Rentals()));
        customersButton.setOnAction(event -> openGui(new Customers()));
        genresButton.setOnAction(event -> openGui(new Genres()));

        // Vertical Box layout for the buttons
        VBox layout = new VBox(12, moviesButton, rentalsButton, customersButton, genresButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: BEIGE;");

        Scene scene = new Scene(layout, 360, 260);
        stage.setTitle("Video Library Launcher");
        stage.setScene(scene);
        stage.show();
    }

    // Method to open the respective GUIs
    private void openGui(Application app) {
        Stage newStage = new Stage();
        try {
            app.start(newStage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to open GUI", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
