package at.fhv.itm3.s2.roundabout.api.entity;

public interface ICar {

    /**
     * Returns actual length of {@code this} car.
     *
     * @return the length of the car.
     */
    double getLength();

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
     * Calculates the time the car needs to traverse the current street section it is standing on.
     *
     * @return the traverse time in seconds.
     */
    double getTimeToTraverseSection();

    /**
     * Calculates the time the car needs to traverse a given street section.
     *
     * @param section the street section we are interested in how long the car needs to traverse it.
     * @return the traverse time in seconds.
     */
    double getTimeToTraverseSection(IStreetSection section);

    /**
     * Calculates the time the car needs until it has moved away from its current spot.
     *
     * @return the transition time in seconds.
     */
    double getTransitionTime();

    /**
     * Returns (reference) car driver behavior {@link IDriverBehaviour}.
     *
     * @return instance of {@link IDriverBehaviour}.
     */
    IDriverBehaviour getDriverBehaviour();

    /**
     * Return a reference to a current {@link IStreetSection} present in car route,
     * where car currently belongs to.
     *
     * @return reference to {@link IStreetSection} where car is currently located.
     */
    IStreetSection getCurrentSection();

    /**
     * Sets {@link IStreetSection} (should be present in car route), where car currently belongs to.
     *
     * @param currentSection reference of {@link IStreetSection} to be stored.
     * @throws IllegalArgumentException when given {@link IStreetSection} does not belong to current car route.
     */
    void setCurrentSection(IStreetSection currentSection)
    throws IllegalArgumentException;

    /**
     * Returns reference to the next {@link IStreetSection} scheduled
     * in car pre-calculated route.
     *
     * @return reference to next {@link IStreetSection}.
     */
    IStreetSection getNextSection();

    /**
     * Return the last available section specified in car route.
     *
     * @return reference to last instance of {@link IStreetSection} in route.
     */
    IStreetSection getDestination();

    /**
     * Returns predefined car route.
     *
     * @return car route in form of {@link IRoute}.
     */
    IRoute getRoute();
}
