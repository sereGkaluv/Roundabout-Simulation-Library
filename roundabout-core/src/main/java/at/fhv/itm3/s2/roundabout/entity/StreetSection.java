package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

import java.util.*;

public class StreetSection extends Entity implements IStreetSection {

    private static final double INITIAL_CAR_POSITION = 0;

    private final Model model;

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

        if (model != null && model instanceof Model) {
            this.model = model;
        } else {
            throw new IllegalArgumentException("Not suitable model.");
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
        final double currentTime = getCurrentTime();

        // Updating positions for all cars.
        ICar previousCar = null;
        for (ICar currentCar : carQueue) {
            final double carLastUpdateTime = currentCar.getLastUpdateTime();
            final double carSpeed = currentCar.getDriverBehaviour().getSpeed();
            final double carPosition = getCarPositions().getOrDefault(currentCar, INITIAL_CAR_POSITION);

            // Calculate distance to next car / end of street section based on distributed driver behaviour values.
            final double distanceToNextCar = 0; //TODO finish.
            currentCar.getDriverBehaviour().getMinDistanceToNextCar();
            currentCar.getDriverBehaviour().getMaxDistanceToNextCar();

            final double maxTheoreticallyPossiblePositionValue = getMaxPossibleCarPosition(
                getLength(),
                distanceToNextCar,
                getCarPositions().get(previousCar),
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
        final double freeSpace = getFreeSpace();
        return length < freeSpace;
    }

    @Override
    public void moveFirstCarToNextSection() {
        ICar firstCar = removeFirstCar();
        if (firstCar != null) {
            if (firstCar.getCurrentSection() != firstCar.getDestination()) {
                IStreetSection nextSection = firstCar.getNextStreetSection();
                nextSection.addCar(firstCar);
                firstCar.setCurrentSection(nextSection);
            }
        }
    }

    @Override
    public boolean carCouldEnterNextSection() {
        throw new IllegalStateException("street section is not empty, but last car could not be determined");
    }

    private double getFreeSpace() {
        updateAllCarsPositions();

        ICar lastCar = getLastCar();
        if (lastCar != null) {
            final double lastCarPosition = getCarPositions().get(lastCar);
            return Math.max(lastCarPosition - lastCar.getLength(), 0);
        }

        // Otherwise whole section is empty.
        return getLength();
    }

    private static double getMaxPossibleCarPosition(
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

    private double getCurrentTime() {
        //TODO consider refactoring (moving) to RoundAbountModel.
        return currentModel().getExperiment().getSimClock().getTime().getTimeAsDouble();
    }
}
