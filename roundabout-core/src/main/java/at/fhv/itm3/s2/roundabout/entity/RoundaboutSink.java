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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RoundaboutSink extends AbstractSink {

    private IStreetConnector previousStreetConnector;
    private List<ICar> enteredCars;

    public RoundaboutSink(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        enteredCars = new LinkedList<>();
    }

    @Override
    public double getLength() {
        return 0;
    }

    @Override
    public void addCar(ICar iCar) {
        incrementTotalCarCounter();
        iCar.leaveSystem();
        IConsumer consumer = iCar.getLastSection();
        if (consumer instanceof Street) {
            Car car = CarController.getCar(iCar);
            ((Street)consumer).carDelivered(null, car, true);
        }
        CarController.removeCarMapping(iCar);
        enteredCars.add(iCar);
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
        incrementTotalCarCounter();
        car.leaveSystem();
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
        double meanRoundaboutPassTime = 0;
        for (ICar car: this.enteredCars) {
            meanRoundaboutPassTime += car.getMeanRoundaboutPassTime();
        }
        return meanRoundaboutPassTime / this.enteredCars.size();
    }

    @Override
    public double getMeanTimeSpentInSystemForEnteredCars() {
        double meanTimeSpentInSystem = 0;
        for (ICar car: this.enteredCars) {
            meanTimeSpentInSystem += car.getTimeSpentInSystem();
        }
        return meanTimeSpentInSystem / this.enteredCars.size();
    }

    @Override
    public double getMeanWaitingTimePerStopForEnteredCars() {
        double meanWaitingTimePerStop = 0;
        for (ICar car: this.enteredCars) {
            meanWaitingTimePerStop += car.getMeanWaitingTime();
        }
        return meanWaitingTimePerStop / this.enteredCars.size();
    }

    @Override
    public double getMeanStopCountForEnteredCars() {
        double stopCount = 0;
        for (ICar car: this.enteredCars) {
            stopCount += car.getStopCount();
        }
        return stopCount / this.enteredCars.size();
    }

    @Override
    public double getMeanIntersectionPassTimeForEnteredCars() {
        double meanIntersectionPassTime = 0;
        for (ICar car: this.enteredCars) {
            meanIntersectionPassTime += car.getMeanIntersectionPassTime();
        }
        return meanIntersectionPassTime / this.enteredCars.size();
    }
}
