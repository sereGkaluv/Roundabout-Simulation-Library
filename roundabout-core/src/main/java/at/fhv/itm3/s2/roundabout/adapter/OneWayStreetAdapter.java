package at.fhv.itm3.s2.roundabout.adapter;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm14.trafsim.model.entities.OneWayStreet;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.controller.CarController;
import desmoj.core.simulator.Model;

import java.util.Map;

public class OneWayStreetAdapter extends Street {

    private OneWayStreet street;

    public OneWayStreetAdapter(OneWayStreet street,
                               Model model,
                               String modelDescription,
                               boolean showInTrace) {
        super(model, modelDescription, showInTrace);
        this.street = street;
    }

    @Override
    public double getLength() {
        return 0;
    }

    @Override
    public void addCar(ICar car) {
        Car c = CarController.getCar(car);
        addCar(c);
    }

    @Override
    public void addCar(Car car) {
//        this.street.carEnter(car);
        carCounter++;
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
        return null;
    }

    @Override
    public void setPreviousStreetConnector(IStreetConnector previousStreetConnector) {

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
        return false;
    }

    @Override
    public void moveFirstCarToNextSection() throws IllegalStateException {

    }

    @Override
    public boolean carCouldEnterNextSection() {
        return false;
    }

    @Override
    public int getNrOfEnteredCars() {
        return 0;
    }
}
