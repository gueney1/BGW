package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.controllers.MainController;
import at.ac.fhcampuswien.fhmdb.controllers.MovieListController;
import at.ac.fhcampuswien.fhmdb.controllers.WatchlistController;

import java.util.HashMap;
import java.util.Map;

public class ControllerFactory implements ControllerFactoryInterface {

    private static volatile ControllerFactory factoryInstance;

    private ControllerFactory(){}

    public static ControllerFactory getInstance(){
        if(factoryInstance == null){
            synchronized(ControllerFactory.class){
                if(factoryInstance == null){
                    factoryInstance = new ControllerFactory();
                }
            }
        }
        return factoryInstance;
    }

    //This class is now open to modification -> what is a better way to write this factory method
    //To return an instance of a controller class w/out having to name each class individually?
    //Answer: try catch block with dynamic instantiation of the controller class -> commented out
    @Override
    public Object getController(Class <?> controllerClass){
        /*if(controllerClass == MainController.class){
            return MainController.getInstance();
        } else if (controllerClass == MovieListController.class){
            return MovieListController.getInstance();
        } else if (controllerClass == WatchlistController.class){
            return WatchlistController.getInstance();
        }
        return null;*/
        try {
            // Use reflection to find the getInstance method
            java.lang.reflect.Method getInstanceMethod = controllerClass.getMethod("getInstance");

            // Invoke the getInstance method on the controller class
            return getInstanceMethod.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
