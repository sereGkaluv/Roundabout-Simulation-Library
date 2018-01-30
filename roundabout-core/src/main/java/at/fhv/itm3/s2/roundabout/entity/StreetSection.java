package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
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
        this(length, model, modelDescription, showInTrace, false);
    }

    public StreetSection(
            double length,
            Model model,
            String modelDescription,
            boolean showInTrace,
            boolean trafficLightActive
    ) {
        super(model, modelDescription, showInTrace, trafficLightActive);

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
    public void addCar(ICar iCar) {
        if (carQueue == null) {
            throw new IllegalStateException("carQueue in section cannot be null");
        }

        carQueue.addLast(iCar);
        carPositions.put(iCar, iCar.getLength());
        incrementEnteredCarCounter();

        IStreetConnector connector = null;
        if (previousStreetConnector != null) {
            connector = previousStreetConnector;
        } else if (nextStreetConnector != null) {
            connector = nextStreetConnector;
        }

        if (connector!= null) {
            if (connector.getTypeOfConsumer(this) == ConsumerType.ROUNDABOUT_INLET) {
                iCar.enterRoundabout();
            } else if (connector.getTypeOfConsumer(this) == ConsumerType.ROUNDABOUT_EXIT) {
                iCar.leaveRoundabout();
            }
        }

        // call carDelivered events for last section, so the car position
        // of the current car (that has just left the last section successfully
        // can be removed (saves memory)
        // caution! that requires to call traverseToNextSection before calling this method
        Car car = CarController.getCar(iCar);
        IConsumer consumer = iCar.getLastSection();
        if (consumer instanceof Street) {
            ((Street)consumer).carDelivered(null, car, true);
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
            final double carLastUpdateTime = currentCar.getLastUpdateTime();

            if (carLastUpdateTime != currentTime) {
                final IDriverBehaviour carDriverBehaviour = currentCar.getDriverBehaviour();
                final double carSpeed = carDriverBehaviour.getSpeed();
                final double carPosition = getCarPosition(currentCar);

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
                double newCarPosition = Math.min(
                        maxTheoreticallyPossiblePositionValue,
                        maxActuallyPossiblePositionValue
                );

                if (newCarPosition < carPosition) {
                    newCarPosition = carPosition;
                }

                if (carPosition == newCarPosition && !currentCar.isWaiting()) {
                    currentCar.startWaiting();
                } else if ((carPosition != newCarPosition || carPosition == currentCar.getLength()) &&
                        currentCar.isWaiting() &&
                        newCarPosition - carPosition > currentCar.getLength()) {
                    currentCar.stopWaiting();
                }

                currentCar.setLastUpdateTime(currentTime);
                carPositions.put(currentCar, newCarPosition);
            }

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

        if (isTrafficLightActive() && !isTrafficLightFreeToGo()) {
            return false;
        }

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

                        // PRECEDENCE CHECK
                        IStreetConnector nextConnector = getNextStreetConnector();
                        ConsumerType currentConsumerType = nextConnector.getTypeOfConsumer(this);

                        if (nextConnector.isNextConsumerOnSameTrackAsCurrent(this, nextStreet)) {
                            switch (currentConsumerType) {
                                // case 1: car is in the roundabout and wants to remain on the track
                                // (it has precedence)
                                case ROUNDABOUT_SECTION:
                                // case 2: car is on a normal street section and wants to remain on the track
                                case STREET_SECTION:
                                // case 3: car is on a roundabout exit and wants to remain on the track
                                case ROUNDABOUT_EXIT:
                                    return true;
                                // case 4: car wants to enter the roundabout from an inlet
                                // (it has to give precedence to all cars in the roundabout that are on tracks
                                // the car has to cross)
                                case ROUNDABOUT_INLET:
                                    List<IConsumer> previousStreets = nextConnector.getPreviousConsumers();
                                    for (IConsumer previousStreet: previousStreets) {
                                        if (!(previousStreet instanceof Street)) {
                                            throw new IllegalStateException("All previous IConsumer should be of type Street");
                                        }
                                        ((Street)previousStreet).updateAllCarsPositions();
                                        if (((Street)previousStreet).isFirstCarOnExitPoint()) {
                                            firstCarInQueue.startWaiting();
                                            return false;
                                        }
                                        if (nextConnector.isNextConsumerOnSameTrackAsCurrent(previousStreet, nextStreet)) {
                                            break;
                                        }
                                    }
                                    break;
                            }
                        } else {
                            switch (currentConsumerType) {
                                // case 5: car wants to change the track in the roundabout exit
                                // (it has to give precedence to a car on that track)
                                case ROUNDABOUT_EXIT:
                                // case 6: car wants to change the track on a streetsection
                                // (it has to give precedence to a car on that track)
                                case STREET_SECTION:
                                    List<IConsumer> streetsThatHavePrecedence = nextConnector.getPreviousTrackConsumers(nextStreet, currentConsumerType);
                                    for (IConsumer precedenceSection: streetsThatHavePrecedence) {
                                        if (!(precedenceSection instanceof Street)) {
                                            throw new IllegalStateException("All previous IConsumer should be of type Street");
                                        }
                                        ((Street)precedenceSection).updateAllCarsPositions();
                                        if (((Street)precedenceSection).isFirstCarOnExitPoint()) {
                                            firstCarInQueue.startWaiting();
                                            return false;
                                        }
                                    }
                                    break;
                                // case 7: car is on a roundabout inlet and wants to change to another
                                // roundabout section that is not on its track
                                // (it has to give precedence to all cars in the roundabout that are on tracks
                                // the car has to cross and to all cars on the inlets of the track it wants to change to)
                                case ROUNDABOUT_INLET:
                                    List<IConsumer> previousStreets = nextConnector.getPreviousConsumers(ConsumerType.ROUNDABOUT_SECTION);
                                    for (IConsumer previousStreet: previousStreets) {
                                        if (!(previousStreet instanceof Street)) {
                                            throw new IllegalStateException("All previous IConsumer should be of type Street");
                                        }
                                        ((Street)previousStreet).updateAllCarsPositions();
                                        if (((Street)previousStreet).isFirstCarOnExitPoint()) {
                                            firstCarInQueue.startWaiting();
                                            return false;
                                        }
                                        if (nextConnector.isNextConsumerOnSameTrackAsCurrent(previousStreet, nextStreet)) {
                                            break;
                                        }
                                    }
                                    List<IConsumer> inlets = nextConnector.getPreviousTrackConsumers(nextStreet, ConsumerType.ROUNDABOUT_INLET);
                                    for (IConsumer inlet: inlets) {
                                        if (!(inlet instanceof Street)) {
                                            throw new IllegalStateException("All previous IConsumer should be of type Street");
                                        }
                                        ((Street)inlet).updateAllCarsPositions();
                                        if (((Street)inlet).isFirstCarOnExitPoint()) {
                                            firstCarInQueue.startWaiting();
                                            return false;
                                        }
                                    }
                                    break;
                                case ROUNDABOUT_SECTION:
                                    ConsumerType nextConsumerType = nextConnector.getTypeOfConsumer(nextStreet);
                                    List<IConsumer> previousSections;
                                    switch (nextConsumerType) {
                                        // case 8: the car is in the roundabout and wants to change to a roundabout section
                                        // on another track (it has to give precedence to the cars that are on the previous
                                        // sections of this track)
                                        case ROUNDABOUT_SECTION:
                                            previousSections = nextConnector.getPreviousTrackConsumers(nextStreet, ConsumerType.ROUNDABOUT_SECTION);
                                            for (IConsumer previousSection: previousSections) {
                                                if (!(previousSection instanceof Street)) {
                                                    throw new IllegalStateException("All previous IConsumer should be of type Street");
                                                }
                                                ((Street)previousSection).updateAllCarsPositions();
                                                if (((Street)previousSection).isFirstCarOnExitPoint()) {
                                                    firstCarInQueue.startWaiting();
                                                    return false;
                                                }
                                            }
                                            break;
                                        // case 9: the car is in the roundabout and wants to leave the roundabout over an exit
                                        // that lies not on its track (it has to give precedence to all cars in the roundabout that
                                        // are on tracks it has to cross)
                                        case ROUNDABOUT_EXIT:
                                            previousSections = nextConnector.getPreviousConsumers(ConsumerType.ROUNDABOUT_SECTION);
                                            int indexOfCurrentSection = previousSections.indexOf(this);
                                            for (int i = indexOfCurrentSection - 1; i >= 0; i--) {
                                                IConsumer previousSection = previousSections.get(i);
                                                if (!(previousSection instanceof Street)) {
                                                    throw new IllegalStateException("All previous IConsumer should be of type Street");
                                                }
                                                ((Street)previousSection).updateAllCarsPositions();
                                                if (((Street)previousSection).isFirstCarOnExitPoint()) {
                                                    firstCarInQueue.startWaiting();
                                                    return false;
                                                }
                                            }
                                            break;
                                        default:
                                            throw new IllegalStateException("After a ROUNDABOUT_SECTION only another ROUNDABOUT_SECTION or a ROUNDABOUT_EXIT is allowed");
                                    }
                                    break;
                            }
                        }
                        return true;
                    } else {
                        firstCarInQueue.startWaiting();
                    }
                } else if (nextConsumer instanceof RoundaboutIntersection) {
                    return true; // because Intersection is never full (isFull() of Intersection returns always false)
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
                } else if (nextSection != null && nextSection instanceof RoundaboutIntersection) {
                    RoundaboutIntersection intersection = (RoundaboutIntersection) nextSection;
                    Car car = CarController.getCar(firstCar);
                    int outDirection = intersectionController.getOutDirectionOfIConsumer(intersection, firstCar.getSectionAfterNextSection());
                    car.setNextDirection(outDirection);
                    // this is made without the CarDepartureEvent of the existing implementation
                    // because it can not handle traffic jam
                    intersection.carEnter(car, intersectionController.getInDirectionOfIConsumer(intersection, this));
                    firstCar.traverseToNextSection();
                } else {
                    throw new IllegalStateException("Car can not move further. Next section does not exist.");
                }
            }
        }
    }


    @Override
    public boolean carCouldEnterNextSection() {
        throw new IllegalStateException("Street section is not empty, but last car could not be determined.");
    }

    private double getCarPosition(ICar car) {
        if (car != null) {
            return getCarPositions().get(car);
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
        // this method is only used by an Intersection object
        // and this has to call this method always even if there is not
        // enough space for another car (because otherwise a RuntimeException
        // is thrown). So the check if there is enough space for this car
        // is made here and if there isn't enough space than a car is lost
        // and the counter is incremented
        if (isEnoughSpace(iCar.getLength())) {
            iCar.traverseToNextSection();
            addCar(iCar);
            double traverseTime = iCar.getTimeToTraverseCurrentSection();
            CarCouldLeaveSectionEvent carCouldLeaveSectionEvent = RoundaboutEventFactory.getInstance().createCarCouldLeaveSectionEvent(
                    getRoundaboutModel()
            );
            carCouldLeaveSectionEvent.schedule(this, new TimeSpan(traverseTime, TimeUnit.SECONDS));
        } else {
            incrementLeftCarsCounter();
            car.leaveSystem();
            CarController.addLostCar(this, iCar);
        }
    }

    @Override
    public boolean isFull() {
        // this method is only used by an Intersection object
        // so it is necessary that this always returns false
        // because a RuntimeException is thrown when this is true
        // (check if car can really enter the section is made in
        // method carEnter(Car car))
        return false;
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
        }
    }

    @Override
    public DTO toDTO() {
        return null;
    }
}
