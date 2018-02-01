package at.fhv.itm3.s2.roundabout.api.entity;

import java.util.Observable;

/**
 * Custom implementation of observable that sets changed flag every time {@link Observable#notifyObservers(Object)} will be called.
 */
public class RoundaboutObservable extends Observable {
    @Override
    public void notifyObservers(Object arg) {
        super.setChanged();
        super.notifyObservers(arg);
    }
}
