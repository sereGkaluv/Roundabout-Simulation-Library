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


    /**
     * moveFirstCarToNextSection removes the first car from the queue and puts it into the
     * queue of the next streetSection of the route of the car, if there is one.
     * If the current streetSection was the last one of the route the car disappears in a sink.
     */
    public void moveFirstCarToNextSection() {
        Car firstCar = (Car)carQueue.poll();
        if (firstCar!=null){
            StreetSection currentSection = firstCar.getCurrentSection();
            int currentSectionIndex = firstCar.getRoute().indexOf(currentSection);
            if (currentSectionIndex < (firstCar.getRoute().size()-1)){
                StreetSection nextSection = firstCar.getRoute().get(currentSectionIndex+1);
                nextSection.addCar(firstCar);
                firstCar.setCurrentSection(nextSection);
            }
        }
    }

    /**
     * addCar adds a car to the queue of the streetSection
     * @param car The car to add.
     */
    @Override
    public void addCar(ICar car) {
        carQueue.add(car);
    }
}
