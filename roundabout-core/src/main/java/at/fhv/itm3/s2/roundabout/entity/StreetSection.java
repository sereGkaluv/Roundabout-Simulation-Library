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


    public StreetSection(double length, IStreetConnector previousStreetConnector, IStreetConnector nextStreetConnector, Model model, String modelDescription, boolean showInTrace) {
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

    @Override
    public void updateAllCarsPositions() {
        final double currentTime = getCurrentTime();

        // Updating positions for all cars.
        ICar previousCar = null;
        for (ICar currentCar : carQueue) {
            final double carLastUpdateTime = currentCar.getLastUpdateTime();
            final double carSpeed = currentCar.getDriverBehaviour().getSpeed();
            final double carPosition = carPositions.getOrDefault(currentCar, INITIAL_CAR_POSITION);

            // Calculate distance to next car / end of street section based on distributed driver behaviour values.
            final double distanceToNextCar = 0; //TODO finish.
            currentCar.getDriverBehaviour().getMinDistanceToNextCar();
            currentCar.getDriverBehaviour().getMaxDistanceToNextCar();

            final double maxTheoreticallyPossiblePositionValue = getMaxPossibleCarPosition(
                getLength(),
                distanceToNextCar,
                carPositions.get(previousCar),
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
        if (this.isFirstCarOnExitPoint()) {
            ICar firstCarInQueue = this.getFirstCar();

            if (firstCarInQueue != null) {
                IStreetSection nextStreetSection = firstCarInQueue.getNextStreetSection();

                if (nextStreetSection == null) { // car at destination
                    return true;
                }

                if (nextStreetSection.isEnoughSpace(firstCarInQueue.getLength())) {
                    Set<IStreetSection> precendenceSections = this.getPreviousStreetConnector().getPreviousSections();
                    precendenceSections.remove(this);

                    for (IStreetSection precendenceSection : precendenceSections) {
                        if (precendenceSection.isFirstCarOnExitPoint()) {
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
        double freeSpace = this.getFreeSpace();

        return length < freeSpace;
    }

    private double getFreeSpace() {
        this.updateAllCarsPositions();

        ICar lastCar = this.getLastCar();
        if (lastCar != null) {
            double lastCarPosition = this.getCarPositions().get(lastCar);
            return this.getLength() - lastCarPosition;
        }

        if (this.isEmpty()) {
            return this.getLength();
        }

    @Override
    public boolean carCouldEnterNextSection() {
        throw new NotImplementedException();
        throw new IllegalStateException("street section is not empty, but last car could not be determined");
    }

    @Override
    /**
     * moveFirstCarToNextSection removes the first car from the queue and puts it into the
     * queue of the next streetSection of the route of the car, if there is one.
     * If the current streetSection was the last one of the route the car disappears in a sink.
     */
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

    /**
     * addCar adds a car to the queue of the streetSection
     *
     * @param car The car to add.
     */
    @Override
    public void addCar(ICar car) {
        carQueue.add(car);
    }

    /**
     * removes the first car of the queue and returns the first Car
     *
     * @return
     */
    @Override
    public ICar removeFirstCar() {
        return carQueue.poll();
    }

    @Override
    public ICar getFirstCar() {
        if (carQueue == null) {
            throw new IllegalStateException("carQueue in section cannot be null");
        }

        return carQueue.peek();
    }

    @Override
    public ICar getLastCar() {
        if (carQueue == null) {
            throw new IllegalStateException("carQueue in section cannot be null");
        } else if (!(carQueue instanceof List)) {
            throw new IllegalStateException("carQueue must be an implementation of List");
        }

        int indexLastCar = carQueue.size() - 1;
        return ((List<ICar>) carQueue).get(indexLastCar);
    }

    private double getCurrentTime() {
        return currentModel().getExperiment().getSimClock().getTime().getTimeAsDouble();
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
}
