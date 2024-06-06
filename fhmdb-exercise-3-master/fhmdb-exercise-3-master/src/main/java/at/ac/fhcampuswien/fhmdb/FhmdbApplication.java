package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.api.MovieApiException;
import at.ac.fhcampuswien.fhmdb.controllers.ControllerFactory;
import at.ac.fhcampuswien.fhmdb.controllers.ControllerFactoryInterface;
import at.ac.fhcampuswien.fhmdb.enums.UIComponent;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class FhmdbApplication extends Application {
    @Override
    public void start(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource(UIComponent.HOME.path));
        ControllerFactoryInterface controllerFactory = ControllerFactory.getInstance();
        fxmlLoader.setControllerFactory(controllerFactory :: getController);

        try{
            Scene scene = new Scene(fxmlLoader.load(), 890, 620);
            scene.getStylesheets().add(Objects.requireNonNull(FhmdbApplication.class.getResource("/styles/styles.css")).toExternalForm());
            stage.setTitle("FHMDb!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Cannot load scene from " + UIComponent.HOME.path);
        } catch (NullPointerException e) {
            System.err.println("Path to stylesheet may be corrupt.");
        }



    }

    public static void main(String[] args) {
        launch();
    }
}


/* zum testen, ob die Filme erfolgreich von der API abegrufen werden:
  try {
            // Beispiel 1: Abrufen aller Filme
            List<Movie> allMovies = MovieAPI.getAllMovies();
            System.out.println("Alle Filme:");
            for (Movie movie : allMovies) {
                System.out.println(movie);
            }

            // Beispiel 2: Abrufen gefilterter Filme
            List<Movie> filteredMovies = MovieAPI.getAllMovies("action", Genre.ACTION, "2012", "8.0");
            System.out.println("\nGefilterte Filme:");
            for (Movie movie : filteredMovies) {
                System.out.println(movie);
            }

        } catch (MovieApiException e) {
            System.err.println("Fehler beim Abrufen der Filme: " + e.getMessage());
        }
 */