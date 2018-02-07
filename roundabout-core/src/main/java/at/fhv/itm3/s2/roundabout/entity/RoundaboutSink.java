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

    @Override
    public double getLength() {
        return 0;
    }

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

    @Override
    public ICar getFirstCar() {
        return null;
    }

    @Override
    public ICar getLastCar() {
        return null;
    }

    @Override
    public List<ICar> getCarQueue()
    throws IllegalStateException {
        return null;
    }

    @Override
    public ICar removeFirstCar() {
        return null;
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
        return this.previousStreetConnector;
    }

    @Override
    public void setPreviousStreetConnector(IStreetConnector previousStreetConnector) {
        this.previousStreetConnector = previousStreetConnector;
    }

    @Override
    public void setNextStreetConnector(IStreetConnector nextStreetConnector) {

    }

    @Override
    public Map<ICar, Double> getCarPositions() {
        return null;
    }

    @Override
    public void updateAllCarsPositions() {

    }

    @Override
    public boolean isFirstCarOnExitPoint() {
        return false;
    }

    @Override
    public boolean firstCarCouldEnterNextSection() {
        return false;
    }

    @Override
    public boolean isEnoughSpace(double length) {
        return true;
    }

    @Override
    public void moveFirstCarToNextSection() throws IllegalStateException {

    }

    @Override
    public boolean carCouldEnterNextSection() {
        return false;
    }

    @Override
    public void carEnter(Car car) {
        addCar(CarController.getICar(car));
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public void carDelivered(CarDepartureEvent carDepartureEvent, Car car, boolean successful) {

    }

    @Override
    public DTO toDTO() {
        return null;
    }

    @Override
    public double getMeanRoundaboutPassTimeForEnteredCars() {
        return meanRoundaboutPassTimeSum / getNrOfEnteredCars();
    }

    @Override
    public double getMeanTimeSpentInSystemForEnteredCars() {
        return meanTimeSpentInSystemSum / getNrOfEnteredCars();
    }

    @Override
    public double getMeanWaitingTimePerStopForEnteredCars() {
        return meanWaitingTimePerStopSum / getNrOfEnteredCars();
    }

    @Override
    public double getMeanStopCountForEnteredCars() {
        return stopCountSum / getNrOfEnteredCars();
    }

    @Override
    public double getMeanIntersectionPassTimeForEnteredCars() {
        return meanIntersectionPassTimeSum / getNrOfEnteredCars();
    }
}
