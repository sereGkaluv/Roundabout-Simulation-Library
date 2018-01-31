package at.fhv.itm3.s2.roundabout.api.entity;

/**
 * Created by Karin on 05.01.2018.
 */
public interface ICarCountable {

    /**
     * Returns the number of cars that have entered the component.
     *
     * @return the number of cars as int.
     */
    long getNrOfEnteredCars();

    /**
     * Returns the number of cars that have left the component.
     *
     * @return the number of cars as int.
     */
    long getNrOfLeftCars();
}
