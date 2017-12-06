package at.fhv.itm3.s2.roundabout.api.entity;

import java.util.List;

public interface ICar {
    double getLastUpdateTime();

    void setLastUpdateTime(double lastUpdateTime);

    IDriverBehaviour getDriverBehaviour();

    void setDriverBehaviour(IDriverBehaviour driverBehaviour);

    /**
     * returns the next streetSection in the route after the current StreetSection
     * returns null, if the currentStreetSection is the last StreetSection in the route
     */
    IStreetSection getNextStreetSection();

    double getLength();

    void setLength(double length);

    IStreetSection getDestination();

    /**
     * returns the route of the car, which is a list<StreetSection>
     */
    List<IStreetSection> getRoute();

    IStreetSection getCurrentSection();

    /**
     * sets the currentStreetSection to the StreetSection of the parameter
     * @param streetSection - new current StreetSection
     */
    void setCurrentSection(IStreetSection currentSection);
}
