package at.fhv.itm3.s2.roundabout.api.entity;

import java.util.List;

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
    IStreetSection getNextStreetSection();

    /**
     * Return the last available section specified in car route.
     *
     * @return reference to last instance of {@link IStreetSection} in route.
     */
    IStreetSection getDestination();

    /**
     * Returns predefined, unmodifiable, ordered collection of {@link List<IStreetSection>}.
     *
     * @return car route in form of {@link List<IStreetSection>}.
     */
    List<IStreetSection> getRoute();
}
