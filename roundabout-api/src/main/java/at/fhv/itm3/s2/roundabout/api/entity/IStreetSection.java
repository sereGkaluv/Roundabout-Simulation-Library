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
    IStreetConnector getNextStreetConnector();

    /**
     * Gets the previous street connector if available.
     *
     * @return
     */
    IStreetConnector getPreviousStreetConnector();

    /**
     * Gets all car positions of the street section.
     *
     * @return
     */
    Map<ICar, Double> getCarPositions();

    /**
     * Recalculates all car positions in the street section,
     * starting from the very first car to very last car in section.
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

    /**
     * Adds a new car to the street section.
     * @param car The car to add.
     */
    void addCar(ICar car);
}
