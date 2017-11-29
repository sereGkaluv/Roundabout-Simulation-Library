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

    private double _lengthInMeters;
    private IStreetConnector _nextStreetConnector;
    private IStreetConnector _previousStreetConnector;
    private Map<ICar, Double> _carPositions;
    private Queue<ICar> _carQueue;

    public StreetSection(double lengthInMeters, IStreetConnector previousStreetConnector, IStreetConnector nextStreetConnector, Model model, String s, boolean b) {
        super(model, s, b);
        _lengthInMeters = lengthInMeters;
        _carQueue = new LinkedList<>();
        _carPositions = new HashMap<>();
        _previousStreetConnector = previousStreetConnector;
        _nextStreetConnector = nextStreetConnector;
    }

    @Override
    public double getLengthInMeters() {
        return _lengthInMeters;
    }

    @Override
    public boolean isEmpty() {
        return _carQueue.isEmpty();
    }

    @Override
    public IStreetConnector getNextStreetConnector() {
        return _nextStreetConnector;
    }

    @Override
    public IStreetConnector getPreviousStreetConnector() {
        return _previousStreetConnector;
    }

    @Override
    public Map<ICar, Double> getCarPositions() {
        return _carPositions;
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

    @Override
    public void addCar(ICar car) {
        throw new NotImplementedException();
    }
}
