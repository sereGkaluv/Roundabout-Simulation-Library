package at.fhv.itm3.s2.roundabout.api.entity;

public interface ICar {

    /**
     * Calculates the time a car needs to traverse the current street section it is standing on
     *
     * @return  the traverse time in seconds
     */
    double getTimeToTraverseSection();

    double getTimeToTraverseSection(IStreetSection section);

    IStreetSection getNextStreetSectionOfFirstCar();

    double getTransitionTime();
}
