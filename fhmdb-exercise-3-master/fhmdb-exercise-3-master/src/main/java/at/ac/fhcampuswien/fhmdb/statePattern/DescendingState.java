package at.ac.fhcampuswien.fhmdb.statePattern;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;

public class DescendingState implements MovieSorterState{
    public void sort(ObservableList<Movie> movies) {
        movies.sort((movie1, movie2)-> movie2.getTitle().compareTo(movie1.getTitle()));
    }
}
