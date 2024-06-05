package at.ac.fhcampuswien.fhmdb.statePattern;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;

public class AscendingState implements MovieSorterState{
    public void sort(ObservableList<Movie> movies) {
        movies.sort((movie1, movie2)-> movie1.getTitle().compareTo(movie2.getTitle()));
    }
}
