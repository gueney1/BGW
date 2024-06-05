package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.api.MovieApiException;
import at.ac.fhcampuswien.fhmdb.database.*;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.enums.SortedState; // Stellen Sie sicher, dass dies korrekt importiert ist
import at.ac.fhcampuswien.fhmdb.statePattern.AscendingState;
import at.ac.fhcampuswien.fhmdb.statePattern.DescendingState;
import at.ac.fhcampuswien.fhmdb.statePattern.MovieSorter;
import at.ac.fhcampuswien.fhmdb.statePattern.UnsortedState;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import at.ac.fhcampuswien.fhmdb.ui.UserDialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MovieListController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXComboBox releaseYearComboBox;

    @FXML
    public JFXComboBox ratingFromComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies;

    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    private MovieSorter movieSorter = new MovieSorter();

    protected SortedState sortedState = SortedState.NONE; // Deklarieren und initialisieren Sie die Variable

    private final ClickEventHandler onAddToWatchlistClicked = (clickedItem) -> {
        if (clickedItem instanceof Movie movie) {
            WatchlistMovieEntity watchlistMovieEntity = new WatchlistMovieEntity(movie.getId());
            try {
                WatchlistRepository repository = WatchlistRepository.getInstance();
                repository.addToWatchlist(watchlistMovieEntity);
            } catch (DataBaseException e) {
                UserDialog dialog = new UserDialog("Database Error", "Could not add movie to watchlist");
                dialog.show();
                e.printStackTrace();
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeState();
        initializeLayout();
    }

    public void initializeState() {
        List<Movie> result;
        try {
            result = MovieAPI.getAllMovies();
            writeCache(result);
        } catch (MovieApiException e) {
            UserDialog dialog = new UserDialog("MovieAPI Error", "Could not load movies from api. Get movies from db cache instead");
            dialog.show();
            result = readCache();
        }

        setMovies(result);
        setMovieList(result);
    }

    private List<Movie> readCache() {
        try {
            MovieRepository movieRepository = MovieRepository.getInstance();
            return MovieEntity.toMovies(movieRepository.getAllMovies());
        } catch (DataBaseException e) {
            UserDialog dialog = new UserDialog("DB Error", "Could not load movies from DB");
            dialog.show();
            return new ArrayList<>();
        }
    }

    private void writeCache(List<Movie> movies) {
        try {
            MovieRepository movieRepository = MovieRepository.getInstance();
            movieRepository.removeAll();
            movieRepository.addAllMovies(movies);
        } catch (DataBaseException e) {
            UserDialog dialog = new UserDialog("DB Error", "Could not write movies to DB");
            dialog.show();
        }
    }

    public void initializeLayout() {
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(movieListView -> new MovieCell(onAddToWatchlistClicked));

        genreComboBox.getItems().add("No filter");
        genreComboBox.getItems().addAll(Genre.values());
        genreComboBox.setPromptText("Filter by Genre");

        releaseYearComboBox.getItems().add("No filter");
        Integer[] years = new Integer[124];
        for (int i = 0; i < years.length; i++) {
            years[i] = 1900 + i;
        }
        releaseYearComboBox.getItems().addAll(years);
        releaseYearComboBox.setPromptText("Filter by Release Year");

        ratingFromComboBox.getItems().add("No filter");
        Integer[] ratings = new Integer[11];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = i;
        }
        ratingFromComboBox.getItems().addAll(ratings);
        ratingFromComboBox.setPromptText("Filter by Rating");
    }

    public void setMovies(List<Movie> movies) {
        allMovies = movies;
    }

    public void setMovieList(List<Movie> movies) {
        observableMovies.clear();
        observableMovies.addAll(movies);
    }

    public void sortMovies() {
        if (movieSorter.getState() instanceof DescendingState || movieSorter.getState() instanceof UnsortedState) {
            movieSorter.setState(new AscendingState());
            movieSorter.sortMovies(observableMovies);
            sortedState = SortedState.ASCENDING;
        } else if (sortedState == SortedState.ASCENDING) {
            movieSorter.setState(new DescendingState());
            movieSorter.sortMovies(observableMovies);
            sortedState = SortedState.DESCENDING;
        }
    }

    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }
}
