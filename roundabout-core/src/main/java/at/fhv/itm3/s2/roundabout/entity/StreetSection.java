package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class StreetSection extends Entity implements IStreetSection {

    private double lengthInMeters;
    private IStreetConnector nextStreetConnector;
    private IStreetConnector previousStreetConnector;
    private Map<ICar, Double> carPositions;
    private Queue<ICar> carQueue;

    public StreetSection(double lengthInMeters, IStreetConnector previousStreetConnector, IStreetConnector nextStreetConnector, Model model, String modelDescription, boolean showInTrace) {
        super(model, modelDescription, showInTrace);
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

    }

    public boolean isFirstCarOnExitPoint() {
        return false;
    }

    @Override
    public boolean firstCarCouldEnterNextSection() {
        if (this.isFirstCarOnExitPoint()) {
            ICar firstCarInQueue = this.getFirstCar();

            if (firstCarInQueue != null) {
                IStreetSection nextStreetSection = firstCarInQueue.getNextStreetSection();

                if (nextStreetSection.isEnoughSpace(firstCarInQueue.getLength())) {
                    Set<IStreetSection> precendenceSections = this.getPreviousStreetConnector().getPreviousSections();
                    precendenceSections.remove(this);

                    for (IStreetSection precendenceSection : precendenceSections) {
                        if (precendenceSection.isFirstCarOnExitPoint()) {
                            return false;
                        }
                    }

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isEnoughSpace(double length) {
        double freeSpace = this.getFreeSpace();

        return length < freeSpace;
    }

    private double getFreeSpace() {
        this.updateAllCarsPositions();

        ICar lastCar = this.getLastCar();
        if (lastCar != null) {
            double lastCarPosition = this.getCarPositions().get(lastCar);
            double freeSpace = this.getLengthInMeters() - lastCarPosition;

            return freeSpace;
        }

        return 0;
    }

    public void moveFirstCarToNextSection() {
        throw new NotImplementedException();
    }

    @Override
    public void addCar(ICar car) {
        throw new NotImplementedException();
    }

    @Override
    public ICar getFirstCar() {
        return carQueue.peek();
    }

    @Override
    public ICar getLastCar() {
        return ((LinkedList<ICar>) carQueue).getLast();
    }
}
