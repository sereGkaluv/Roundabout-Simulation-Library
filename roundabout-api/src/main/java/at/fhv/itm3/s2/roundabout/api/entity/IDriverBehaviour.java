package at.fhv.itm3.s2.roundabout.api.entity;

public interface IDriverBehaviour {

    /**
     * Returns riding speed of {@code this} driver.
     *
     * @return the speed of the driver.
     */
    double getSpeed();

    /**
     * Sets the riding speed of {@code this} driver.
     *
     * @param speed value to be set.
     * @throws IllegalArgumentException when given speed is not >= 0.
     */
    void setSpeed(double speed)
    throws IllegalArgumentException;

    /**
     * Returns preferred min distance to next car held by {@code this} driver.
     *
     * @return the min distance to next car of the driver.
     */
    double getMinDistanceToNextCar();

    /**
     * Sets preferred min distance to next car held by {@code this} driver.
     *
     * @param minDistanceToNextCar value to be set.
     * @throws IllegalArgumentException when given speed is not > 0.
     */
    void setMinDistanceToNextCar(double minDistanceToNextCar)
    throws IllegalArgumentException;

    /**
     * Returns preferred max distance to next car held by {@code this} driver.
     *
     * @return the max distance to next car of the driver.
     */
    double getMaxDistanceToNextCar();

    /**
     * Sets preferred max distance to next car held by {@code this} driver.
     *
     * @param maxDistanceToNextCar value to be set.
     * @throws IllegalArgumentException when given speed is not > 0.
     */
    void setMaxDistanceToNextCar(double maxDistanceToNextCar)
    throws IllegalArgumentException;

    /**
     * Returns preferred merge factor of {@code this} driver.
     *
     * @return the merge factor of the driver.
     */
    double getMergeFactor();

    /**
     * Sets the merge factor of {@code this} driver.
     *
     * @param mergeFactor value to be set.
     */
    void setMergeFactor(double mergeFactor);
}
