package at.fhv.itm3.s2.roundabout.adapter;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm14.trafsim.model.entities.OneWayStreet;
import at.fhv.itm14.trafsim.persistence.model.DTO;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.controller.CarController;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutCar;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import desmoj.core.simulator.Model;

import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class OneWayStreetAdapter extends Street {

    private OneWayStreet street;
    private final LinkedList<ICar> carQueue;

    private IStreetConnector nextStreetConnector;
    private IStreetConnector previousStreetConnector;

    public OneWayStreetAdapter(OneWayStreet street,
                               Model model,
                               String modelDescription,
                               boolean showInTrace) {
        super(model, modelDescription, showInTrace);
        this.street = street;
        this.carQueue = new LinkedList<>();
    }

    @Override
    public double getLength() {
        return 0;
    }

    @Override
    public void addCar(ICar car) {
        Car c = CarController.getCar(car);
        carEnter(c);
        carQueue.addLast(car);
    }

    @Override
    public ICar getFirstCar() {
        return carQueue.getFirst();
    }

    @Override
    public ICar getLastCar() {
        return carQueue.getLast();
    }

    @Override
    public ICar removeFirstCar() {
        return carQueue.removeFirst();
    }

    @Override
    public boolean isEmpty() {
        return carQueue.isEmpty();
    }

    @Override
    public IStreetConnector getNextStreetConnector() {
        return this.nextStreetConnector;
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
        this.nextStreetConnector = nextStreetConnector;
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
        return true;
    }

    @Override
    public boolean isEnoughSpace(double length) {
        return !street.isFull();
    }

    @Override
    public void moveFirstCarToNextSection() throws IllegalStateException {
        ICar firstCar = removeFirstCar();
        if (firstCar != null) {
            if (!Objects.equals(firstCar.getCurrentSection(), firstCar.getDestination())) {
                Street nextSection = firstCar.getNextSection();
                if (nextSection != null) {
                    // Move physically first car to next section.
                    nextSection.addCar(firstCar);
                    // Move logically first car to next section.
                    firstCar.traverseToNextSection();

                } else {
                    throw new IllegalStateException("RoundaboutCar can not move further. Next section does not exist.");
                }
            }
        }
    }

    @Override
    public boolean carCouldEnterNextSection() {
        return false;
    }

    @Override
    public int getNrOfEnteredCars() {
        return this.carCounter;
    }

    @Override
    public void carEnter(Car car) {
        this.street.carEnter(car);
        carCounter++;
    }

    @Override
    public boolean isFull() {
        return this.street.isFull();
    }

    @Override
    public DTO toDTO() {
        return this.street.toDTO();
    }
}
