package com.hyhello.priceless.support.log;

import org.springframework.stereotype.Component;

import java.util.Observable;
import java.util.Observer;

/**
 *
 */
@Component
public class TestObserver implements Observer {

    public TestObserver(){
        LogObservable.getInstance().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        //System.out.println(arg);
    }
}
