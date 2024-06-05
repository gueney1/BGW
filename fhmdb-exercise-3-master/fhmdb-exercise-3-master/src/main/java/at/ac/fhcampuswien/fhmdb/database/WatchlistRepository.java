package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;
import at.ac.fhcampuswien.fhmdb.observerPattern.Observable;
import at.ac.fhcampuswien.fhmdb.observerPattern.Observer;

import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository implements Observable{
    private static WatchlistRepository instance;
    private List<Observer> observers;
    Dao<WatchlistMovieEntity, Long> dao;

    public WatchlistRepository() throws DataBaseException {
        observers = new ArrayList<>();
        try {
            this.dao = DatabaseManager.getInstance().getWatchlistDao();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }
    }
    public static WatchlistRepository getInstance() throws DataBaseException {
        if(instance == null) {
            instance = new WatchlistRepository();
        }
        return instance;
    }

    public List<WatchlistMovieEntity> getWatchlist() throws DataBaseException {
        try {
            return dao.queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataBaseException("Error while reading watchlist");
        }
    }
    public int addToWatchlist(WatchlistMovieEntity movie) throws DataBaseException {
        try {
            // only add movie if it does not exist yet
            long count = dao.queryBuilder().where().eq("apiId", movie.getApiId()).countOf();
            if (count == 0) {
                notifyObservers("Movie was added to watchlist");
                return dao.create(movie);
            } else {
                notifyObservers("Movie already in watchlist");
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataBaseException("Error while adding to watchlist");
        }
    }

    public int removeFromWatchlist(String apiId) throws DataBaseException {
        try {
            return dao.delete(dao.queryBuilder().where().eq("apiId", apiId).query());
        } catch (Exception e) {
            throw new DataBaseException("Error while removing from watchlist");
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update("Watchlist updated.");
        }
    }

}
