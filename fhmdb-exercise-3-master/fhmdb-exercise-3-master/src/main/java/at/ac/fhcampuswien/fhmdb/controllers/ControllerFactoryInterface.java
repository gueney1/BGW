package at.ac.fhcampuswien.fhmdb.controllers;

//This interface is for creating Objects -> implementing classes can alter the type of object that will be created

public interface ControllerFactoryInterface {
    Object getController(Class <?> controllerClass);
}
