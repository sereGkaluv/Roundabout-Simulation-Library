package at.fhv.itm3.s2.roundabout.api.entity;

import java.util.List;

public interface ICar {
    double getLastUpdateTime();

    void setLastUpdateTime(double lastUpdateTime);

    IDriverBehaviour getDriverBehaviour();

    void setDriverBehaviour(IDriverBehaviour driverBehaviour);

    IStreetSection getNextStreetSection();

    double getLength();

    void setLength(double length);

    IStreetSection getDestination();

    List<IStreetSection> getRoute();

    IStreetSection getCurrentSection();

    void setCurrentSection(IStreetSection currentSection);
}
