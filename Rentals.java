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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Rentals extends Application {

    // Helper method to load customers into the combo box and maintain a mapping of customer names to their IDs
    private static void loadCustomers(ComboBox<String> comboBoxCustomer, Map<String, Integer> customerIds) {
        comboBoxCustomer.getItems().clear();
        customerIds.clear();
// SQL query to select active customers ordered by name
        String sql = "SELECT id, Fullname FROM Clients WHERE isactive = 1 ORDER BY Fullname";
        try (Connection conn = DatabaseHandler.connect()) {
            if (conn == null) {
                System.err.println("Cannot load customers: database connection unavailable.");
                return;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String customerName = rs.getString("Fullname"); 
                    customerIds.put(customerName, rs.getInt("id")); 
                    comboBoxCustomer.getItems().add(customerName); 
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error loading customers: " + ex.getMessage());
        }
    }

    //load genres and maintain a mapping of genre names to their IDs
    private static void loadGenres(ComboBox<String> comboBoxGenre, Map<String, Integer> genreIds) {
        comboBoxGenre.getItems().clear();
        genreIds.clear();

        String sql = "SELECT id, genre FROM Genres WHERE isactive = 1 ORDER BY genre";
        try (Connection conn = DatabaseHandler.connect()) {
            if (conn == null) {
                System.err.println("Cannot load genres: database connection unavailable.");
                return;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String genreName = rs.getString("genre");
                    genreIds.put(genreName, rs.getInt("id"));
                    comboBoxGenre.getItems().add(genreName);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error loading genres: " + ex.getMessage());
        }
    }
// Load movies based on the selected genre and maintain a mapping of movie titles to their IDs
    private static void loadMoviesForGenre(ComboBox<String> comboBoxMovie, Map<String, Integer> movieIds, String genreName) {
        comboBoxMovie.getItems().clear();
        movieIds.clear();

        String sql = "SELECT Movies.id, Movies.Title " +
                "FROM Movies " +
                "INNER JOIN Genres ON Movies.genre_id = Genres.id " +
                "WHERE Movies.isactive = 1 AND Genres.isactive = 1"; // Base SQL query to select active movies with their genres    

                // Add genre filter if a specific genre is selected
        if (genreName != null && !genreName.isBlank()) {
            sql += " AND Genres.genre = ?";
        }

        sql += " ORDER BY Movies.Title";

        try (Connection conn = DatabaseHandler.connect()) {
            if (conn == null) {
                System.err.println("Cannot load movies: database connection unavailable.");
                return;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                if (genreName != null && !genreName.isBlank()) {
                    pstmt.setString(1, genreName);
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String movieTitle = rs.getString("Title"); // Get movie title
                        movieIds.put(movieTitle, rs.getInt("id")); // Map movie title to its ID
                        comboBoxMovie.getItems().add(movieTitle); // Add movie title to the combo box
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error loading movies: " + ex.getMessage());
        }
    }

    // Load currently borrowed rentals and maintain a mapping of display text to rental IDs for easy retrieval when returning movies
    private static void loadBorrowedRentals(ComboBox<String> comboBoxBorrowed, Map<String, Integer> rentalIds) {
        comboBoxBorrowed.getItems().clear();
        rentalIds.clear();

        // SQL query to select active rentals with customer and movie details
        String sql = "SELECT Rentals.id, Clients.Fullname, Movies.Title, Rentals.rental_date " +
                "FROM Rentals " +
                "INNER JOIN Clients ON Rentals.client_id = Clients.id " +
                "INNER JOIN Movies ON Rentals.movie_id = Movies.id " +
                "WHERE Rentals.isreturned = 0 " +
                "ORDER BY Rentals.id DESC";

        try (Connection conn = DatabaseHandler.connect()) {
            if (conn == null) {
                System.err.println("Cannot load borrowed rentals: database connection unavailable.");
                return;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int rentalId = rs.getInt("id");
                    String displayText = "#" + rentalId + " | " + rs.getString("Fullname") + " -> "
                            + rs.getString("Title") + " | " + rs.getDate("rental_date"); // Create display text for the combo box
                    rentalIds.put(displayText, rentalId); // Map display text to rental ID for easy retrieval
                    comboBoxBorrowed.getItems().add(displayText);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error loading borrowed rentals: " + ex.getMessage());
        }
    }

    // Load returned rentals to display in the returned rentals combo box
    private static void loadReturnedRentals(ComboBox<String> comboBoxReturned) {
        comboBoxReturned.getItems().clear();

        // SQL query to select returned rentals with customer and movie details
        String sql = "SELECT Rentals.id, Clients.Fullname, Movies.Title, Rentals.return_date " +
                "FROM Rentals " +
                "INNER JOIN Clients ON Rentals.client_id = Clients.id " +
                "INNER JOIN Movies ON Rentals.movie_id = Movies.id " +
                "WHERE Rentals.isreturned = 1 " +
                "ORDER BY Rentals.id DESC";

        try (Connection conn = DatabaseHandler.connect()) {
            if (conn == null) {
                System.err.println("Cannot load returned rentals: database connection unavailable.");
                return;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String displayText = "#" + rs.getInt("id") + " | " + rs.getString("Fullname") + " -> "
                            + rs.getString("Title") + " | " + rs.getDate("return_date"); // Create display text for the combo box
                    comboBoxReturned.getItems().add(displayText); // Add returned rental to the combo box
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error loading returned rentals: " + ex.getMessage());
        }
    }

     @Override
    public void start(Stage stage) {
        DatabaseHandler.setupDatabase();

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

        Map<String, Integer> customerIds = new HashMap<>();
        Map<String, Integer> genreIds = new HashMap<>();
        Map<String, Integer> movieIds = new HashMap<>();
        Map<String, Integer> rentalIds = new HashMap<>();

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
// Load initial data into combo boxes
        loadCustomers(comboBoxCustomer, customerIds);
        loadGenres(comboBoxGenre, genreIds);
        loadMoviesForGenre(comboBoxMovie, movieIds, comboBoxGenre.getValue());
        loadBorrowedRentals(comboBoxBorrowed, rentalIds);
        loadReturnedRentals(comboBoxReturned);

// Set up event handler to load movies when a genre is selected
        comboBoxGenre.setOnAction(e -> loadMoviesForGenre(comboBoxMovie, movieIds, comboBoxGenre.getValue()));

        // Save button action to insert a new rental into the database
        btnSave.setOnAction(e -> {
            String selectedCustomer = comboBoxCustomer.getValue();
            String selectedMovie = comboBoxMovie.getValue();

            if (selectedCustomer == null || selectedMovie == null) {
                System.err.println("Please select a customer and a movie before saving a rental.");
                return;
            }

            Integer customerId = customerIds.get(selectedCustomer);
            Integer movieId = movieIds.get(selectedMovie);
            if (customerId == null || movieId == null) {
                System.err.println("Could not resolve the selected customer or movie.");
                return;
            }

            String sql = "INSERT INTO Rentals(client_id, movie_id, rental_date) VALUES(?, ?, ?)";
            try (Connection conn = DatabaseHandler.connect()) {
                if (conn == null) {
                    System.err.println("Cannot save rental: database connection unavailable.");
                    return;
                }
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, customerId); // Set the customer ID
                    pstmt.setInt(2, movieId); // Set the movie ID
                    pstmt.setDate(3, Date.valueOf(LocalDate.now())); // Set the rental date to today
                    pstmt.executeUpdate(); // Execute the insert statement
                }

                loadCustomers(comboBoxCustomer, customerIds); // Refresh active customers from DB
                loadGenres(comboBoxGenre, genreIds); // Refresh active genres from DB
                loadMoviesForGenre(comboBoxMovie, movieIds, comboBoxGenre.getValue()); // Refresh movie list for selected genre
                loadBorrowedRentals(comboBoxBorrowed, rentalIds); // Refresh the borrowed rentals combo box to show the new rental
                loadReturnedRentals(comboBoxReturned); // Refresh the returned rentals combo box in case there are any changes
                System.out.println("Rental Saved to MySQL.");
            } catch (SQLException ex) { ex.printStackTrace(); }
        });

        // Return movie action
        btnReturn.setOnAction(e -> {
            String selectedRental = comboBoxBorrowed.getValue();
            if (selectedRental == null) {
                System.err.println("Please select a borrowed rental to return.");
                return;
            }

            Integer rentalId = rentalIds.get(selectedRental);
            if (rentalId == null) {
                System.err.println("Could not resolve the selected rental.");
                return;
            }

            String sql = "UPDATE Rentals SET return_date = ?, isreturned = 1 WHERE id = ?";
            try (Connection conn = DatabaseHandler.connect()) {
                if (conn == null) {
                    System.err.println("Cannot return movie: database connection unavailable.");
                    return;
                }
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setDate(1, Date.valueOf(LocalDate.now())); // Set the return date to today
                    pstmt.setInt(2, rentalId); // Set the rental ID to update the correct record
                    pstmt.executeUpdate();
                }

                loadCustomers(comboBoxCustomer, customerIds); // Refresh active customers from DB
                loadGenres(comboBoxGenre, genreIds); // Refresh active genres from DB
                loadMoviesForGenre(comboBoxMovie, movieIds, comboBoxGenre.getValue()); // Refresh movie list for selected genre
                loadBorrowedRentals(comboBoxBorrowed, rentalIds); // Refresh the borrowed rentals combo box to remove the returned rental
                loadReturnedRentals(comboBoxReturned); // Refresh the returned rentals combo box to show the returned rental
                System.out.println("Movie Returned and Updated in MySQL.");
            } catch (SQLException ex) { ex.printStackTrace(); }
        });

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
