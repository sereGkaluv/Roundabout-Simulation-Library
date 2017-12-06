package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import com.google.common.math.DoubleMath;
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
       ICar firstCar = this.getFirstCar();
        if(firstCar == null){
            return false;
        }
        return DoubleMath.fuzzyEquals(this.getCarPositions().get(firstCar), this.getLength(), 0.5); // Todo: sergii firstCar.getDriverBehaviour().getMaxDistance()
    }

    @Override
    public boolean firstCarCouldEnterNextSection() {
        if (this.isFirstCarOnExitPoint()) {
            ICar firstCarInQueue = this.getFirstCar();

            if (firstCarInQueue != null) {
                IStreetSection nextStreetSection = firstCarInQueue.getNextStreetSection();

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


    /**
     * moveFirstCarToNextSection removes the first car from the queue and puts it into the
     * queue of the next streetSection of the route of the car, if there is one.
     * If the current streetSection was the last one of the route the car disappears in a sink.
     */
    public void moveFirstCarToNextSection() {
        throw new NotImplementedException();
    }

    @Override
    public void addCar(ICar car) {
        throw new NotImplementedException();
    }
}
