package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.intersection.Intersection;
import at.fhv.itm14.trafsim.model.events.CarDepartureEvent;
import at.fhv.itm14.trafsim.persistence.model.DTO;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.*;
import at.fhv.itm3.s2.roundabout.controller.CarController;
import at.fhv.itm3.s2.roundabout.controller.IntersectionController;
import at.fhv.itm3.s2.roundabout.event.CarCouldLeaveSectionEvent;
import at.fhv.itm3.s2.roundabout.event.RoundaboutEventFactory;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class StreetSection extends Street {

    private static final double INITIAL_CAR_POSITION = 0;

    private final double length;

    private final LinkedList<ICar> carQueue;
    private final Map<ICar, Double> carPositions;

    private IStreetConnector nextStreetConnector;
    private IStreetConnector previousStreetConnector;

    protected IntersectionController intersectionController;

    public StreetSection(
        double length,
        Model model,
        String modelDescription,
        boolean showInTrace
    ) {
        super(model, modelDescription, showInTrace);

        this.length = length;

        this.carQueue = new LinkedList<>();
        this.carPositions = new HashMap<>();
        this.intersectionController = IntersectionController.getInstance();
    }

    @Override
    public double getLength() {
        return length;
    }

    @Override
    public void addCar(ICar car) {
        if (carQueue == null) {
            throw new IllegalStateException("carQueue in section cannot be null");
        }

        carQueue.addLast(car);
        carPositions.put(car, INITIAL_CAR_POSITION);
        this.carCounter++;

        // call carDelivered events for last section, so the car position
        // of the current car (that has just left the last section successfully)
        // can be removed (saves memory)
        // caution! that requires to call traverseToNextSection before calling this method
        Car c = CarController.getCar(car);
        IConsumer consumer = car.getLastSection();
        if (consumer instanceof Street) {
            ((Street)consumer).carDelivered(null, c, true);
        }
    }

    @Override
    public ICar removeFirstCar() {
        return carQueue.removeFirst();
    }

    @Override
    public ICar getFirstCar() {
        final List<ICar> carQueue = getCarQueue();

        if (carQueue.size() > 0) {
            return carQueue.get(0);
        }
        return null;
    }

    @Override
    public ICar getLastCar() {
        final List<ICar> carQueue = getCarQueue();

        if (carQueue.size() > 0) {
            return carQueue.get(carQueue.size() - 1);
        }
        return null;
    }

    @Override
    public List<ICar> getCarQueue()
    throws IllegalStateException {
        if (carQueue == null) {
            throw new IllegalStateException("carQueue in section cannot be null");
        }

        return Collections.unmodifiableList(carQueue);
    }

    @Override
    public boolean isEmpty() {
        final List<ICar> carQueue = getCarQueue();
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
    public void setPreviousStreetConnector(IStreetConnector previousStreetConnector) {
        this.previousStreetConnector = previousStreetConnector;
    }

    @Override
    public void setNextStreetConnector(IStreetConnector nextStreetConnector) {
        this.nextStreetConnector = nextStreetConnector;
    }

    @Override
    public Map<ICar, Double> getCarPositions() {
        return Collections.unmodifiableMap(carPositions);
    }

    @Override
    public void updateAllCarsPositions() {
        final double currentTime = getRoundaboutModel().getCurrentTime();
        final List<ICar> carQueue = getCarQueue();

        // Updating positions for all cars.
        ICar previousCar = null;
        for (ICar currentCar : carQueue) {
            final IDriverBehaviour carDriverBehaviour = currentCar.getDriverBehaviour();
            final double carLastUpdateTime = currentCar.getLastUpdateTime();
            final double carSpeed = carDriverBehaviour.getSpeed();
            final double carPosition = getCarPositionOrDefault(currentCar, INITIAL_CAR_POSITION);

            // Calculate distance to next car / end of street section based on distributed driver behaviour values.
            final double distanceToNextCar = calculateDistanceToNextCar(
                carDriverBehaviour.getMinDistanceToNextCar(),
                carDriverBehaviour.getMaxDistanceToNextCar(),
                getRoundaboutModel().getRandomDistanceFactorBetweenCars()
            );

            // Calculate possible car positions.
            final double maxTheoreticallyPossiblePositionValue = calculateMaxPossibleCarPosition(
                getLength(),
                distanceToNextCar,
                getCarPosition(previousCar),
                previousCar
            );

            final double maxActuallyPossiblePositionValue = carPosition + (currentTime - carLastUpdateTime) * carSpeed;

            // Select the new RoundaboutCar position based on previous calculations.
            final double newCarPosition = Math.min(
                maxTheoreticallyPossiblePositionValue,
                maxActuallyPossiblePositionValue
            );

            currentCar.setLastUpdateTime(currentTime);
            carPositions.put(currentCar, newCarPosition);

            previousCar = currentCar;
        }
    }

    @Override
    public boolean isFirstCarOnExitPoint() {
        final ICar firstCar = getFirstCar();
        if (firstCar != null && firstCar.getDriverBehaviour() != null) {
            final double distanceToSectionEnd = Math.abs(getLength() - getCarPosition(firstCar));
            return distanceToSectionEnd <= firstCar.getDriverBehaviour().getMaxDistanceToNextCar();
        }
        return false;
    }

    @Override
    public boolean firstCarCouldEnterNextSection() {
        updateAllCarsPositions();
        if (isFirstCarOnExitPoint()) {
            ICar firstCarInQueue = getFirstCar();

            if (firstCarInQueue != null) {
                IConsumer nextConsumer = firstCarInQueue.getNextSection();

                if (nextConsumer == null) { // car at destination
                    return true;
                }

                if (nextConsumer instanceof Street) {
                    Street nextStreet = (Street) nextConsumer;
                    if (nextStreet.isEnoughSpace(firstCarInQueue.getLength())) {
                        IStreetConnector previousStreetConnector = getPreviousStreetConnector();
                        if (previousStreetConnector != null) {
                            Set<IConsumer> precedenceSections = getPreviousStreetConnector().getNextSections();
                            precedenceSections.remove(this);

                            for (IConsumer precedenceSection : precedenceSections) {
                                if (precedenceSection instanceof Street) {
                                    Street street = (Street)precedenceSection;
                                    if (street.isFirstCarOnExitPoint()) {
                                        return false;
                                    }
                                }
                            }
                        }
                        return true;
                    }
                } else if (nextConsumer instanceof Intersection) {
                    return true; // TODO: is that correct?
                }
            }
        }

        return false;
    }

    @Override
    public boolean isEnoughSpace(double length) {
        final double freeSpace = calculateFreeSpace();
        return length < freeSpace;
    }

    @Override
    public void moveFirstCarToNextSection()
    throws IllegalStateException {
        ICar firstCar = removeFirstCar();
        if (firstCar != null) {
            if (!Objects.equals(firstCar.getCurrentSection(), firstCar.getDestination())) {
                IConsumer nextSection = firstCar.getNextSection();
                if (nextSection != null && nextSection instanceof Street) {
                    // this order of calls is important!
                    // Move logically first car to next section.
                    firstCar.traverseToNextSection();
                    // Move physically first car to next section.
                    ((Street)nextSection).addCar(firstCar);
                } else if (nextSection != null && nextSection instanceof Intersection) {
                    Intersection intersection = (Intersection)nextSection;
                    Car car = CarController.getCar(firstCar);
                    int outDirection = intersectionController.getOutDirectionOfIConsumer(intersection, firstCar.getSectionAfterNextSection());
                    car.setNextDirection(outDirection);
                    // this is made without the CarDepartureEvent of the existing implementation
                    // because it can not handle traffic jam
                    if (!intersection.isFull()) {
                        intersection.carEnter(car, intersectionController.getInDirectionOfIConsumer(intersection, this));
                        firstCar.traverseToNextSection();
                    } else {
                        // TODO: traffic jam
                    }
                } else {
                    throw new IllegalStateException("RoundaboutCar can not move further. Next section does not exist.");
                }
            }
        }
    }


    @Override
    public boolean carCouldEnterNextSection() {
        throw new IllegalStateException("Street section is not empty, but last car could not be determined.");
    }

    @Override
    public int getNrOfEnteredCars() {
        return this.carCounter;
    }

    private double getCarPosition(ICar car) {
        if (car != null) {
            return getCarPositions().getOrDefault(car, INITIAL_CAR_POSITION);
        }
        return -1;
    }

    private double getCarPositionOrDefault(ICar car, double defaultValue) {
        return getCarPositions().getOrDefault(car, defaultValue);
    }

    private double calculateFreeSpace() {
        updateAllCarsPositions();

        ICar lastCar = getLastCar();
        if (lastCar != null) {
            final double lastCarPosition = getCarPosition(lastCar);
            return Math.max(lastCarPosition - lastCar.getLength(), 0);
        }

        // Otherwise whole section is empty.
        return getLength();
    }

    private static double calculateDistanceToNextCar(
        double carMinDistanceToNextCar,
        double carMaxDistanceToNextCar,
        double randomDistanceFactorBetweenCars
    ) {
        final double carVariationDistanceToNextCar = carMaxDistanceToNextCar - carMinDistanceToNextCar;
        return carMinDistanceToNextCar + carVariationDistanceToNextCar * randomDistanceFactorBetweenCars;
    }

    private static double calculateMaxPossibleCarPosition(
        double lengthInMeters,
        double distanceToNextCar,
        double previousCarPosition,
        ICar previousCar
    ) {
        if (previousCar != null) {
            return previousCarPosition - previousCar.getLength() - distanceToNextCar;
        } else {
            return lengthInMeters - distanceToNextCar;
        }
    }

    @Override
    public void carEnter(Car car) {
        ICar iCar = CarController.getICar(car);
        iCar.traverseToNextSection();
        addCar(iCar);
        double traverseTime = iCar.getTimeToTraverseCurrentSection();
        CarCouldLeaveSectionEvent carCouldLeaveSectionEvent = RoundaboutEventFactory.getInstance().createCarCouldLeaveSectionEvent(
            getRoundaboutModel()
        );
        carCouldLeaveSectionEvent.schedule(this, new TimeSpan(traverseTime, TimeUnit.SECONDS));
    }

    @Override
    public boolean isFull() {
        return false; // TODO: implement
    }

    private RoundaboutSimulationModel getRoundaboutModel() {
        final Model model = getModel();
        if (model instanceof RoundaboutSimulationModel) {
            return (RoundaboutSimulationModel) model;
        } else {
            throw new IllegalArgumentException("Not suitable roundaboutSimulationModel.");
        }
    }

    @Override
    public void carDelivered(CarDepartureEvent carDepartureEvent, Car car, boolean successful) {
        if (successful) {
            // remove carPosition of car that has just left
            ICar iCar = CarController.getICar(car);
            carPositions.remove(iCar);
        } else {
            // TODO: traffic jam
        }

    }

    @Override
    public DTO toDTO() {
        return null;
    }
}
