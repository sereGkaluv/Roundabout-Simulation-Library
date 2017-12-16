package at.fhv.itm3.s2.roundabout.api.entity;

public interface ICar {

    /**
     * Returns the last update time.
     * This value will be changed every time car attributes will be somehow modified.
     *
     * @return last update time.
     */
    double getLastUpdateTime();

    /**
     * Sets the last update time.
     *
     * @param lastUpdateTime time value to be set.
     * @throws IllegalArgumentException when given time is not > 0.
     */
    void setLastUpdateTime(double lastUpdateTime)
    throws IllegalArgumentException;

    /**
     * Calculates the time the car needs to traverse the current {@link IStreet} it is standing on.
     *
     * @return the traverse time in model time units.
     */
    double getTimeToTraverseCurrentSection();

    /**
     * Calculates the time the car needs to traverse a given {@link IStreet}.
     *
     * @param section the {@link IStreet} we are interested in how long the car needs to traverse it.
     * @return the traverse time in model time units.
     */
    double getTimeToTraverseSection(IStreet section);

    /**
     * Calculates the time the car needs until it has moved away from its current spot.
     *
     * @return the transition time in model time units.
     */
    double getTransitionTime();

    /**
     * Returns actual length of {@code this} car.
     *
     * @return the length of the car.
     */
    double getLength();

    /**
     * Returns (reference) car driver behavior {@link IDriverBehaviour}.
     *
     * @return instance of {@link IDriverBehaviour}.
     */
    IDriverBehaviour getDriverBehaviour();

    /**
     * Returns predefined car route.
     *
     * @return car route in form of {@link IRoute}.
     */
    IRoute getRoute();

    /**
     * Return a reference to a current {@link IStreet} present in car route,
     * where car currently belongs to.
     *
     * @return reference to {@link IStreet} where car is currently located.
     */
    IStreet getCurrentSection();

    /**
     * Car will be logically traversed to next (following) {@link IStreet} in predefined route.
     */
    void traverseToNextSection();

    /**
     * Returns reference to the next {@link IStreet} scheduled
     * in car pre-calculated route.
     *
     * @return reference to next {@link IStreet}.
     */
    IStreet getNextSection();

    /**
     * Return the last available section specified in car route.
     *
     * @return reference to last instance of {@link IStreet} in route.
     */
    IStreet getDestination();
}
