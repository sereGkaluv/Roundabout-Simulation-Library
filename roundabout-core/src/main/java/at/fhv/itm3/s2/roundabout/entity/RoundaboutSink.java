package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.events.CarDepartureEvent;
import at.fhv.itm14.trafsim.persistence.model.DTO;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSink;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.controller.CarController;
import desmoj.core.simulator.Model;

import java.util.*;

public class RoundaboutSink extends AbstractSink {

    private IStreetConnector previousStreetConnector;

    private double meanRoundaboutPassTimeSum;
    private double meanTimeSpentInSystemSum;
    private double meanWaitingTimePerStopSum;
    private double stopCountSum;
    private double meanIntersectionPassTimeSum;

    public RoundaboutSink(Model owner, String name, boolean showInTrace) {
        this(UUID.randomUUID().toString(), owner, name, showInTrace);
    }

    public RoundaboutSink(String id, Model owner, String name, boolean showInTrace) {
        super(id, owner, name, showInTrace);

        this.meanRoundaboutPassTimeSum = 0;
        this.meanTimeSpentInSystemSum = 0;
        this.meanWaitingTimePerStopSum = 0;
        this.stopCountSum = 0;
        this.meanIntersectionPassTimeSum = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLength() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCar(ICar iCar) {
        iCar.leaveSystem();
        incrementEnteredCarCounter();
        updateStats(iCar);

        IConsumer consumer = iCar.getLastSection();
        if (consumer instanceof Street) {
            Car car = CarController.getCar(iCar);
            ((Street)consumer).carDelivered(null, car, true);
        }
        CarController.removeCarMapping(iCar);
        carObserver.notifyObservers(iCar);
        incrementLeftCarCounter();
    }

    public void updateStats(ICar car) {
        meanRoundaboutPassTimeSum += car.getMeanRoundaboutPassTime();
        meanTimeSpentInSystemSum += car.getTimeSpentInSystem();
        meanWaitingTimePerStopSum += car.getMeanWaitingTime();
        stopCountSum += car.getStopCount();
        meanIntersectionPassTimeSum += car.getMeanIntersectionPassTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ICar getFirstCar() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ICar getLastCar() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ICar> getCarQueue()
    throws IllegalStateException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ICar removeFirstCar() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IStreetConnector getNextStreetConnector() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IStreetConnector getPreviousStreetConnector() {
        return this.previousStreetConnector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPreviousStreetConnector(IStreetConnector previousStreetConnector) {
        this.previousStreetConnector = previousStreetConnector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNextStreetConnector(IStreetConnector nextStreetConnector) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<ICar, Double> getCarPositions() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAllCarsPositions() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFirstCarOnExitPoint() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean firstCarCouldEnterNextSection() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnoughSpace(double length) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveFirstCarToNextSection() throws IllegalStateException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean carCouldEnterNextSection() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void carEnter(Car car) {
        addCar(CarController.getICar(car));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFull() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void carDelivered(CarDepartureEvent carDepartureEvent, Car car, boolean successful) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DTO toDTO() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getMeanRoundaboutPassTimeForEnteredCars() {
        return meanRoundaboutPassTimeSum / getNrOfEnteredCars();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getMeanTimeSpentInSystemForEnteredCars() {
        return meanTimeSpentInSystemSum / getNrOfEnteredCars();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getMeanWaitingTimePerStopForEnteredCars() {
        return meanWaitingTimePerStopSum / getNrOfEnteredCars();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getMeanStopCountForEnteredCars() {
        return stopCountSum / getNrOfEnteredCars();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getMeanIntersectionPassTimeForEnteredCars() {
        return meanIntersectionPassTimeSum / getNrOfEnteredCars();
    }
}
