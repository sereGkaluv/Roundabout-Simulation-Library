package at.fhv.itm3.s2.roundabout.api.entity;

import java.util.Map;

public interface IStreetSection {

    /**
     * Gets physical length of the street section.
     *
     * @return The length in meters.
     */
    double getLengthInMeters();

    /**
     * Checks if the street section is empty.
     *
     * @return True if street section is empty.
     */
    boolean isEmpty();

    /**
     * Gets the next street connector if available.
     *
     * @return
     */
    StreetConnector getNextStreetConnector();

    /**
     * Gets the previous street connector if available.
     *
     * @return
     */
    StreetConnector getPreviousStreetConnector();

    /**
     * Gets all car positions of the street section.
     *
     * @return
     */
    Map<Car, Double> getCarPositions();

    /**
     * Recalculates all car positions in the street section.
     */
    void updateAllCarsPositions();

    /**
     * Checks if the first car in the street section is on the exit point.
     *
     * @return
     */
    boolean isFirstCarOnExitPoint();

    /**
     * Checks if a car could enter the next available section.
     * @return
     */
    boolean carCouldEnterNextSection();

    /**
     * Moves the first car in the section to the next section.
     */
    void moveFirstCarToNextSection();
}
