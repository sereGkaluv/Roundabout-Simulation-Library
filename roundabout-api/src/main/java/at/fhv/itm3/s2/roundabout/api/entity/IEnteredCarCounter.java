package at.fhv.itm3.s2.roundabout.api.entity;

/**
 * Created by Karin on 05.01.2018.
 */
public interface IEnteredCarCounter {

    /**
     * Returns the number of cars that have entered the sink.
     *
     * @return the number of cars as int.
     */
    long getNrOfEnteredCars();
}
