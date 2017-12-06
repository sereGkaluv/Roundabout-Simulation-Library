package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class StreetSection extends Entity implements IStreetSection {

    private static final double INITIAL_CAR_POSITION = 0;

    private final Model model;

    private final double lengthInMeters;

    private final LinkedList<ICar> carQueue;
    private final Map<ICar, Double> carPositions;

    private final IStreetConnector nextStreetConnector;
    private final IStreetConnector previousStreetConnector;


    public StreetSection(double lengthInMeters, IStreetConnector previousStreetConnector, IStreetConnector nextStreetConnector, Model model, String s, boolean b) {
        super(model, s, b);

        this.lengthInMeters = lengthInMeters;
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
    public double getLengthInMeters() {
        return lengthInMeters;
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
                getLengthInMeters(),
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
        throw new NotImplementedException();
    }

    @Override
    public boolean carCouldEnterNextSection() {
        throw new NotImplementedException();
    }

    @Override
    public void moveFirstCarToNextSection() {
        throw new NotImplementedException();
    }

    @Override
    public void addCar(ICar car) {
        throw new NotImplementedException();
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
