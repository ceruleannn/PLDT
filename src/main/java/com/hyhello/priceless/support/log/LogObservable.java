package com.hyhello.priceless.support.log;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class LogObservable extends Observable {

    private static final LogObservable observable = new LogObservable();
    private LogObservable(){

    }

    public static LogObservable getInstance(){
        return observable;
    }

    public void update(Marker marker, String message){

        super.setChanged();

        notifyObservers(message);
    }

    @Override
    public void addObserver(Observer o) {
        //TODO marker subscribe
        super.addObserver(o);
    }
}
