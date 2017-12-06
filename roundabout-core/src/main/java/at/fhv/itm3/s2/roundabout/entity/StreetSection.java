package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class StreetSection extends Entity implements IStreetSection {

    private double length;
    private IStreetConnector nextStreetConnector;
    private IStreetConnector previousStreetConnector;
    private Map<ICar, Double> carPositions;
    private Queue<ICar> carQueue;

    public StreetSection(double length, IStreetConnector previousStreetConnector, IStreetConnector nextStreetConnector, Model model, String modelDescription, boolean showInTrace) {
        super(model, modelDescription, showInTrace);
        this.length = length;
        carQueue = new LinkedList<>();
        carPositions = new HashMap<>();
        this.previousStreetConnector = previousStreetConnector;
        this.nextStreetConnector = nextStreetConnector;
    }

    @Override
    public double getLength() {
        return length;
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
                IStreetSection nextStreetSection = firstCarInQueue.getNextSection();

                if (nextStreetSection == null) { // car at destination
                    return true;
                }

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
            double freeSpace = this.getLength() - lastCarPosition;

            return freeSpace;
        }

        if (this.isEmpty()) {
            return this.getLength();
        }

        throw new IllegalStateException("street section is not empty, but last car could not be determined");
    }

    public void moveFirstCarToNextSection() {
        throw new NotImplementedException();
    }

    /**
     * Adds a car to the section and places it at the physical position 0.0 (regarding the length of the section)
     *
     * @param car The car to add.
     */
    @Override
    public void addCar(ICar car) {
        if (carQueue == null) {
            throw new IllegalStateException("carQueue in section cannot be null");
        }
        carQueue.add(car);
        carPositions.put(car, 0.0);
    }

    @Override
    public ICar getFirstCar() {
        if (carQueue == null) {
            throw new IllegalStateException("carQueue in section cannot be null");
        }

        return carQueue.peek();
    }

    @Override
    public ICar getLastCar() {
        if (carQueue == null) {
            throw new IllegalStateException("carQueue in section cannot be null");
        } else if (!(carQueue instanceof List)) {
            throw new IllegalStateException("carQueue must be an implementation of List");
        }

        int indexLastCar = carQueue.size() - 1;
        return ((List<ICar>) carQueue).get(indexLastCar);
    }
}
