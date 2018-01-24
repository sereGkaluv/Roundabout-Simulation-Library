package at.fhv.itm3.s2.roundabout.api.entity;

import at.fhv.itm14.trafsim.model.entities.AbstractProSumer;
import desmoj.core.simulator.Model;

import java.util.List;
import java.util.Map;

public abstract class Street extends AbstractProSumer implements IEnteredCarCounter {

    private long enteredCarsCounter;
    private long lostCarsCounter;
    private TrafficLight trafficLight;

    public Street(Model owner, String name, boolean showInTrace) {
        this(owner, name, showInTrace, false);
    }

    public Street(Model owner, String name, boolean showInTrace, boolean trafficLightActive) {
        super(owner, name, showInTrace);

        this.enteredCarsCounter = 0;
        this.lostCarsCounter = 0;

        trafficLight = new TrafficLight(trafficLightActive);
    }

    /**
     * Gets total car counter passed via {@code this} {@link Street}.
     *
     * @return total car counter.
     */
    @Override
    public long getNrOfEnteredCars() {
        return enteredCarsCounter;
    }

    /**
     * Internal method for counter incrementation.
     */
    protected void incrementTotalCarCounter() {
        this.enteredCarsCounter++;
    }

    public long getNrOfLostCars() { return lostCarsCounter; }

    protected void incrementLostCarsCounter() { this.lostCarsCounter++; }

    /**
     * Gets physical length of the street section.
     *
     * @return The length in meters.
     */
    public abstract double getLength();

    /**
     * Adds a new car to the street section.
     *
     * @param car The car to add.
     */
    public abstract void addCar(ICar car);

    /**
     * Gets first car in Section.
     *
     * @return first car in section.
     */
    public abstract ICar getFirstCar();

    /**
     * Gets last car in Section.
     *
     * @return last car in section.
     */
    public abstract ICar getLastCar();

    /**
     * Returns car queue of this {@link Street}.
     *
     * @return unmodifiable car queue.
     * @throws IllegalStateException in case if queue equals null.
     */
    public abstract List<ICar> getCarQueue()
    throws IllegalStateException;

    /**
     * Removes the first car of the queue and returns the first car.
     *
     * @return removed car.
     */
    public abstract ICar removeFirstCar();

    /**
     * Checks if the street section is empty.
     *
     * @return True if street section is empty.
     */
    public abstract boolean isEmpty();

    /**
     * Gets the next street connector if available.
     *
     * @return reference to next {@link IStreetConnector}.
     */
    public abstract IStreetConnector getNextStreetConnector();

    /**
     * Gets the previous street connector if available.
     *
     * @return reference to previous {@link IStreetConnector}.
     */
    public abstract IStreetConnector getPreviousStreetConnector();

    /**
     * Sets the previous street connector
     *
     * @param previousStreetConnector street connector to be set
     */
    public abstract void setPreviousStreetConnector(IStreetConnector previousStreetConnector);

    /**
     *  Sets the next street connector
     *
     * @param nextStreetConnector street connector to be set
     */
    public abstract void setNextStreetConnector(IStreetConnector nextStreetConnector);

    /**
     * Gets all car positions of the street section.
     *
     * @return unmodifiable map of car positions.
     */
    public abstract Map<ICar, Double> getCarPositions();

    /**
     * Recalculates all car positions in the street section,
     * starting from the very first car to very last car in section.
     */
    public abstract void updateAllCarsPositions();

    /**
     * Checks if the first car in the street section is on the exit point.
     *
     * @return true if car is on exit point, otherwise false.
     */
    public abstract boolean isFirstCarOnExitPoint();

    /**
     * Checks if first car in street section is able to enter the next section, depending on its predefined route.
     *
     * @return true = car can enter next section, false = car can not enter next section
     */
    public abstract boolean firstCarCouldEnterNextSection();

    /**
     * Checks if there is enough space in the section, depending on the car's length.
     *
     * @param length length of the car
     * @return true = enough space, false = not enough space
     */
    public abstract boolean isEnoughSpace(double length);

    /**
     * Moves the first car from the current section to the next section.
     * In background removes the first car (if there is one) from the queue and puts it into the
     * queue of the next {@link Street} present in car route.
     *
     * @throws IllegalStateException if car cannot move further e.g. next section is null.
     */
    public abstract void moveFirstCarToNextSection()
            throws IllegalStateException;

    @Deprecated
    // TODO consider removal i think this logic can be packed into addCar method, otherwise consider rename to isCarAbleToEnter()
    public abstract boolean carCouldEnterNextSection();

    /**
     * Returns if traffic light at end of the street is active or not.
     *
     * @return true = active
     */
    public boolean isTrafficLightActive() {
        return trafficLight.isActive();
    }

    /**
     * Indicates whether the traffic light at the end of the street signals "free to go" (true) or "stop" (false), if it is active.
     * Otherwise it will always return true.
     *
     *  @return true = free to go
     */
    public boolean isTrafficLightFreeToGo() {
        return !trafficLight.isActive() || trafficLight.isFreeToGo();
    }

    /**
     * Sets the traffic light state (free to go = true, stop = false).
     *
     * @param isFreeToGo set true if cars are free to go
     * @throws IllegalStateException if traffic light is inactive
     */
    public void setTrafficLightFreeToGo(boolean isFreeToGo) throws IllegalStateException {
        trafficLight.setFreeToGo(isFreeToGo);
    }
}
