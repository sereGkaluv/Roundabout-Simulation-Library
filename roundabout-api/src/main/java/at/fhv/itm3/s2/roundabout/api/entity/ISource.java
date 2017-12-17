package at.fhv.itm3.s2.roundabout.api.entity;

public interface ISource {

    /**
     * Starts the generation of cars by scheduling the first CarGenerateEvent.
     */
    void startGeneratingCars();

    /**
     * Returns the street to which the newly generated cars are added
     *
     * @return  the connected street as {@link Street}
     */
    Street getConnectedStreet();
}
