package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

import java.util.*;

public class StreetSection extends Entity implements IStreetSection {

    private static final double INITIAL_CAR_POSITION = 0;

    private final RoundaboutSimulationModel roundaboutSimulationModel;

    private final double length;

    private final LinkedList<ICar> carQueue;
    private final Map<ICar, Double> carPositions;

    private final IStreetConnector nextStreetConnector;
    private final IStreetConnector previousStreetConnector;


    public StreetSection(
        double length,
        IStreetConnector previousStreetConnector,
        IStreetConnector nextStreetConnector,
        Model model,
        String modelDescription,
        boolean showInTrace
    ) {
        super(model, modelDescription, showInTrace);

        this.length = length;
        this.previousStreetConnector = previousStreetConnector;
        this.nextStreetConnector = nextStreetConnector;

        this.carQueue = new LinkedList<>();
        this.carPositions = new HashMap<>();

        if (model instanceof RoundaboutSimulationModel) {
            this.roundaboutSimulationModel = (RoundaboutSimulationModel) model;
        } else {
            throw new IllegalArgumentException("Not suitable roundaboutSimulationModel.");
        }
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
    }

    @Override
    public ICar removeFirstCar() {
        return carQueue.removeFirst();
    }

    @Override
    public ICar getFirstCar() {
        if (carQueue == null) {
            throw new IllegalStateException("carQueue in section cannot be null");
        }

        return carQueue.getFirst();
    }

    @Override
    public ICar getLastCar() {
        if (carQueue == null) {
            throw new IllegalStateException("carQueue in section cannot be null");
        }

        return carQueue.getLast();
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
        return Collections.unmodifiableMap(carPositions);
    }

    @Override
    public void updateAllCarsPositions() {
        final double currentTime = roundaboutSimulationModel.getCurrentTime();

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
                roundaboutSimulationModel.getRandomDistanceFactorBetweenCars()
            );

            // Calculate possible car positions.
            final double maxTheoreticallyPossiblePositionValue = calculateMaxPossibleCarPosition(
                getLength(),
                distanceToNextCar,
                getCarPosition(previousCar),
                previousCar
            );

            final double maxActuallyPossiblePositionValue = carPosition + (currentTime - carLastUpdateTime) * carSpeed;

            // Select the new Car position based on previous calculations.
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
        if (isFirstCarOnExitPoint()) {
            ICar firstCarInQueue = getFirstCar();

            if (firstCarInQueue != null) {
                IStreetSection nextStreetSection = firstCarInQueue.getNextSection();

                if (nextStreetSection == null) { // car at destination
                    return true;
                }

                if (nextStreetSection.isEnoughSpace(firstCarInQueue.getLength())) {
                    Set<IStreetSection> precedenceSections = getPreviousStreetConnector().getPreviousSections();
                    precedenceSections.remove(this);

                    for (IStreetSection precedenceSection : precedenceSections) {
                        if (precedenceSection.isFirstCarOnExitPoint()) {
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
        final double freeSpace = calculateFreeSpace();
        return length < freeSpace;
    }

    @Override
    public void moveFirstCarToNextSection() {
        ICar firstCar = removeFirstCar();
        if (firstCar != null) {
            if (firstCar.getCurrentSection() != firstCar.getDestination()) {
                IStreetSection nextSection = firstCar.getNextSection();
                // Move physically first car to next section.
                nextSection.addCar(firstCar);
                // Move logically first car to next section.
                firstCar.traverseToNextSection();
            }
        }
    }

    @Override
    public boolean carCouldEnterNextSection() {
        throw new IllegalStateException("Street section is not empty, but last car could not be determined.");
    }

    private double getCarPosition(ICar car) {
        return getCarPositions().get(car);
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
}
