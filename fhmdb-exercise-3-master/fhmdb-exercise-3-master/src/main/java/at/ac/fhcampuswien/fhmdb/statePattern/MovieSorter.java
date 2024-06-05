package at.ac.fhcampuswien.fhmdb.statePattern;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;

public class MovieSorter {
    private MovieSorterState state;

    public MovieSorter() {
        this.state = new UnsortedState();
    }

    public void setState(MovieSorterState state) {
        this.state = state;
    }

    public MovieSorterState getState() {
        return state;
    }

    public void sortMovies(ObservableList<Movie> movies) {
        state.sort(movies);
    }
}
