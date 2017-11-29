package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

import java.util.Map;

import java.util.Queue;

public class StreetSection extends Entity implements IStreetSection {

    private Queue<Car> queue;



    public StreetSection(Model model, String s, boolean b) {
        super(model, s, b);
    }

    @Override
    public double getLengthInMeters() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public IStreetConnector getNextStreetConnector() {
        return null;
    }

    @Override
    public IStreetConnector getPreviousStreetConnector() {
        return null;
    }

    @Override
    public Map<ICar, Double> getCarPositions() {
        return null;
    }

    @Override
    public void addCar(ICar car) {

    }

    public void updateAllCarsPositions() {

    }

    public boolean isFirstCarOnExitPoint() {
        return false;
    }

    public boolean carCouldEnterNextSection() {
        return false;
    }

    public void moveFirstCarToNextSection() {

    }

    public Queue<Car> getQueue() {
        return queue;
    }

    public void setQueue(Queue<Car> queue) {
        this.queue = queue;
    }
}
