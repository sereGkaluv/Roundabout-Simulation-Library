package at.fhv.itm3.s2.roundabout.api.entity;

import java.util.List;

public interface ICar {

    /**
     * Calculates the time the car needs to traverse the current street section it is standing on
     *
     * @return  the traverse time in seconds
     */
    double getTimeToTraverseSection();

    /**
     * Calculates the time the car needs to traverse a given street section
     *
     * @param section   the street section we are interested in how long the car needs to traverse it
     * @return          the traverse time in seconds
     */
    double getTimeToTraverseSection(IStreetSection section);

    /**
     * Calculates the time the car needs until it has moved away from its current spot
     *
     * @return  the transition time in seconds
     */
    double getTransitionTime();

    /**
     * Returns the next section the car will be standing on
     *
     * @return  the next section as IStreetSection
     */
    IStreetSection getNextSection();

    double getLastUpdateTime();

    void setLastUpdateTime(double lastUpdateTime);

    IDriverBehaviour getDriverBehaviour();

    void setDriverBehaviour(IDriverBehaviour driverBehaviour);

    /**
     * returns the next streetSection in the route after the current StreetSection
     * returns null, if the currentStreetSection is the last StreetSection in the route
     */
    IStreetSection getNextStreetSection();

    double getLength();

    void setLength(double length);

    IStreetSection getDestination();

    /**
     * Returns the route of the car, which is a list<StreetSection>
     */
    IRoute getRoute();

    IStreetSection getCurrentSection();

    /**
     * Sets the currentStreetSection to the StreetSection of the parameter
     * @param currentSection - the IStreetSection the car stands on
     */
    void setCurrentSection(IStreetSection currentSection);
}
