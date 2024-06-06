package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.database.*;
import at.ac.fhcampuswien.fhmdb.observerPattern.Observer;
import at.ac.fhcampuswien.fhmdb.ui.UserDialog;
import at.ac.fhcampuswien.fhmdb.ui.WatchlistCell;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WatchlistController implements Initializable, Observer {

    @FXML
    public JFXListView watchlistView;

    private WatchlistRepository watchlistRepository;

    protected ObservableList<MovieEntity> observableWatchlist = FXCollections.observableArrayList();

    private static volatile WatchlistController watchlistControllerInstance;

    private WatchlistController(){}

    public static WatchlistController getInstance(){
        if(watchlistControllerInstance == null){
            synchronized(WatchlistController.class){
                if(watchlistControllerInstance == null){
                    watchlistControllerInstance = new WatchlistController();
                }
            }
        }
        return watchlistControllerInstance;
    }

    private final ClickEventHandler onRemoveFromWatchlistClicked = (o) -> {
        if (o instanceof MovieEntity) {
            MovieEntity movieEntity = (MovieEntity) o;

            try {
                //WatchlistRepository watchlistRepository = new WatchlistRepository();
                WatchlistRepository watchlistRepository = WatchlistRepository.getInstance();
                watchlistRepository.removeFromWatchlist(movieEntity.getApiId());
                observableWatchlist.remove(movieEntity);
            } catch (DataBaseException e) {
                UserDialog dialog = new UserDialog("Database Error", "Could not remove movie from watchlist");
                dialog.show();
                e.printStackTrace();
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableWatchlist.clear();
        List<WatchlistMovieEntity> watchlist = new ArrayList<>();
        try {
            //watchlistRepository = new WatchlistRepository();
            watchlistRepository = WatchlistRepository.getInstance();
            watchlist = watchlistRepository.getWatchlist();

            //MovieRepository movieRepository = new MovieRepository();
            MovieRepository movieRepository = MovieRepository.getInstance();
            List<MovieEntity> movies = new ArrayList<>();

            for(WatchlistMovieEntity movie : watchlist) {
                movies.add(movieRepository.getMovie(movie.getApiId()));
            }

            observableWatchlist.addAll(movies);
            watchlistView.setItems(observableWatchlist);
            watchlistView.setCellFactory(movieListView -> new WatchlistCell(onRemoveFromWatchlistClicked));

        } catch (DataBaseException e) {
            UserDialog dialog = new UserDialog("Database Error", "Could not read movies from DB");
            dialog.show();
            e.printStackTrace();
        }

        if(watchlist.size() == 0) {
            watchlistView.setPlaceholder(new javafx.scene.control.Label("Watchlist is empty"));
        }

        System.out.println("WatchlistController initialized");

    }

    @Override
    public void update(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Watchlist Controller");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
