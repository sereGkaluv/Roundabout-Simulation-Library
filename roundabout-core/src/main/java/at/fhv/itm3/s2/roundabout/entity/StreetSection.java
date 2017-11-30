package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class StreetSection extends Entity implements IStreetSection {

    private double lengthInMeters;
    private IStreetConnector nextStreetConnector;
    private IStreetConnector previousStreetConnector;
    private Map<ICar, Double> carPositions;
    private Queue<ICar> carQueue;

    public StreetSection(double lengthInMeters, IStreetConnector previousStreetConnector, IStreetConnector nextStreetConnector, Model model, String s, boolean b) {
        super(model, s, b);
        this.lengthInMeters = lengthInMeters;
        carQueue = new LinkedList<>();
        carPositions = new HashMap<>();
        this.previousStreetConnector = previousStreetConnector;
        this.nextStreetConnector = nextStreetConnector;
    }

    @Override
    public double getLengthInMeters() {
        return lengthInMeters;
    }

    @Override
    public boolean isEmpty() {
        return carQueue.isEmpty();
    }

    @Override
    public IStreetConnector getNextStreetConnector() {
        return nextStreetConnector;
    }

    @Override
    public IStreetConnector getPreviousStreetConnector() {
        return previousStreetConnector;
    }

    @Override
    public Map<ICar, Double> getCarPositions() {
        return carPositions;
    }

    public void updateAllCarsPositions() {
        throw new NotImplementedException();
    }

    public boolean isFirstCarOnExitPoint() {
        throw new NotImplementedException();
    }

    public boolean carCouldEnterNextSection() {
        throw new NotImplementedException();
    }

    public void moveFirstCarToNextSection() {
        throw new NotImplementedException();
    }

    public IStreetSection getNextSectionOfFirstCar() { return null; }

    @Override
    public ICar getFirstCar() {
        return null;
    }

    @Override
    public void addCar(ICar car) {
        throw new NotImplementedException();
    }
}
