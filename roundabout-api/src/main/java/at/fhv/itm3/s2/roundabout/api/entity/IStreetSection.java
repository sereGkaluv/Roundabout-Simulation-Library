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
     * Checks if first car in street section is able to enter the next section, depending on its predefined route.
     *
     * @return true = car can enter next section, false = car can not enter next section
     */
    boolean firstCarCouldEnterNextSection();

    /**
     * Checks if there is enough space in the section, depending on the car's length.
     *
     * @param length length of the car
     * @return true = enough space, false = not enough space
     */
    boolean isEnoughSpace(double length);

    /**
     * Moves the first car in the section to the next section.
     */
    void moveFirstCarToNextSection();

    /**
     * Adds a new car to the street section.
     *
     * @param car The car to add.
     */
    void addCar(ICar car);

    /**
     * Gets first car in Section.
     *
     * @return first car in section
     */
    ICar getFirstCar();

    /**
     * Gets last car in Section.
     *
     * @return last car in section
     */
    ICar getLastCar();
}
