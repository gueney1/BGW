package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;
import at.ac.fhcampuswien.fhmdb.observerPattern.Observable;
import at.ac.fhcampuswien.fhmdb.observerPattern.Observer;

import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository implements Observable {

    private static WatchlistRepository instance;

    private List <Observer> observers;

    Dao<WatchlistMovieEntity, Long> dao;

    private WatchlistRepository() throws DataBaseException {
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
        if (isMovieInWatchlist(movie)) {
            notifyObservers("Movie already on watchlist");
            return 0;
        } else {
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
    }

    private boolean isMovieInWatchlist(WatchlistMovieEntity movie) throws DataBaseException {
        try {
            long count = dao.queryBuilder().where().eq("apiId", movie.getApiId()).countOf();
            System.out.println("Movie Api Id is: " + movie.getApiId()); //TODO: Remove
            System.out.println("Count is: " + count); //TODO: Remove
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataBaseException("Error while checking if movie is in watchlist");
        }
    }

    public int removeFromWatchlist(String apiId) throws DataBaseException {
        try {
            notifyObservers("Movie successfully removed from the list"); //TODO: Check if correct
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
            observer.update(message);
        }
    }

}
