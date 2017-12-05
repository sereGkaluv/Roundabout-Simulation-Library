package at.fhv.itm3.s2.roundabout.api.entity;

import java.util.List;

public interface ICar {
    /**
     * returns the index of currentStreetSection in Route
     */
    int getCurrentIndexOfRoute();

    /**
     * returns the route of the car, which is a list<StreetSection>
     */
    public List<IStreetSection> getRoute();

    /**
     * returns the next streetSection in the route after the current StreetSection
     * returns null, if the currentStreetSection is the last StreetSection in the route
     */

    IStreetSection getNextStreetSection();

    /**
     * sets the currentStreetSection to the StreetSection of the parameter
     * @param streetSection - new current StreetSection
     */
    void setCurrentSection(IStreetSection streetSection);
}


