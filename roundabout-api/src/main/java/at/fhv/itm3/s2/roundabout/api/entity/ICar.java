package at.fhv.itm3.s2.roundabout.api.entity;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.IProducer;

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
     * Calculates the time the car needs to traverse the current {@link Street} it is standing on.
     *
     * @return the traverse time in model time units.
     */
    double getTimeToTraverseCurrentSection();

    /**
     * Calculates the time the car needs to traverse a given {@link Street}.
     *
     * @param section the {@link Street} we are interested in how long the car needs to traverse it.
     * @return the traverse time in model time units.
     */
    double getTimeToTraverseSection(IConsumer section);

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
     * Returns a reference to the last {@link IProducer} present in the car route,
     * where the car currently belongs to.
     *
     * @return reference to {@link IProducer} where car was last located
     */
    IConsumer getLastSection();

    /**
     * Return a reference to a current {@link Street} present in car route,
     * where car currently belongs to.
     *
     * @return reference to {@link Street} where car is currently located.
     */
    IConsumer getCurrentSection();

    /**
     * Car will be logically traversed to next (following) {@link Street} in predefined route.
     */
    void traverseToNextSection();

    /**
     * Returns reference to the next {@link Street} scheduled
     * in car pre-calculated route.
     *
     * @return reference to next {@link Street}.
     */
    IConsumer getNextSection();

    /**
     * Returns reference to the section after the next {@link IConsumer} scheduled
     * in car pre-calculated route.
     *
     * @return  reference to section after next {@link IConsumer}
     */
    IConsumer getSectionAfterNextSection();

    /**
     * Return the last available section specified in car route.
     *
     * @return reference to last instance of {@link Street} in route.
     */
    IConsumer getDestination();

    /**
     * Used for statistical values. Starts a stopwatch to determine the
     * overall time spent in the system. Delegates the call to the Trafsim car object.
     */
    void enterSystem();

    /**
     * Used for statistical values. Stops the stopwatch that is used for
     * determining the overall time spent in the system. Delegates the call to the Trafsim car object.
     * @return
     */
    double leaveSystem();

    /**
     * Used for statistical values. Starts a stopwatch to determine the
     * time spent on the roundabout.
     */
    void enterRoundabout();

    /**
     * Used for statistical values. Stops the stopwatch that is used for
     * determining the time spent on the roundabout.
     */
    void leaveRoundabout();

    /**
     * Used for statistical values. Returns the mean time used for crossing
     * a roundabout.
     *
     * @return  the mean time the car used to pass a roundabout as model time units
     */
    double getMeanRoundaboutPassTime();

    /**
     * Used for statistical values. Returns the number of roundabouts crossed.
     *
     * @return  the number of roundabouts crossed as int
     */
    long getRoundaboutPassedCount();

    /**
     * Used for statistical values. Starts a stopwatch that is used for
     * determining the time spent on an intersection. Delegates the call to the Trafsim car object.
     */
    void enterIntersection();

    /**
     * Used for statistical values. Stops the stopwatch that is used for
     * determining the time spent on the intersection. Delegates the call to the Trafsim car object.
     */
    void leaveIntersection();

    /**
     * Used for statistical values. Starts a stopwatch that is used for
     * determining the time spent waiting. Delegates the call to the Trafsim car object.
     */
    void startWaiting();

    /**
     * Used for statistical values. Stops the stopwatch that is used for
     * determining the time spent waiting. Delegates the call to the Trafsim car object.
     */
    void stopWaiting();

    /**
     * Used for statistical values. Checks if the car is currently waiting by checking if the
     * corresponding stopwatch is running. Delegates the call to the Trafsim car object.
     *
     * @return  true if the corresponding stopwatch is running (startWaiting was called before withouth calling
     * stopWaiting afterwards), else false
     */
    boolean isWaiting();

    /**
     * Used for statistical values. Returns the time the car has spent in the system.
     * Delegates the call to the Trafsim car object.
     *
     * @return  the time the car has spent in the system as model time units
     */
    double getTimeSpentInSystem();

    /**
     *  Used for statistical values. Returns the mean time spent waiting.
     *  Delegates the call to the Trafsim car object.
     *
     * @return  the mean time the car spent waiting per stop as model time units
     */
    double getMeanWaitingTime();

    /**
     * Used for statistical values. Returns the number of stops the car has to made.
     * Delegates the call to the Trafsim car object.
     *
     * @return  the number of stops the car has to made as int
     */
    long getStopCount();

    /**
     * Used for statistical values. Returns the number of intersections the car crossed.
     * Delegates the call to the Trafsim car object.
     *
     * @return  the number of intersections the car crossed as int
     */
    long getIntersectionPassedCount();

    /**
     * Used for statistical values. Returns the mean time used for passing and intersection.
     * Delegates the call to the Trafsim car object.
     *
     * @return  the mean time the car used for passing an intersection as model time units
     */
    double getMeanIntersectionPassTime();

    /**
     * Returns the distance the car can lay back in a given time.
     *
     * @param time  the time as model time unit
     * @return      the distance as model length unit
     */
    double getCoveredDistanceInTime(double time);
}
