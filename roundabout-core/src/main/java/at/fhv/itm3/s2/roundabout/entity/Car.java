package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;

import java.time.LocalTime;
import java.util.List;

public class Car implements ICar {

    private LocalTime lastUpdateTime;
    private DriverBehaviour driverBehaviour;
    private double length;
    private StreetSection streetSection;
    private List<StreetSection> route;
    private StreetSection actualSection;

    public LocalTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public DriverBehaviour getDriverBehaviour() {
        return driverBehaviour;
    }

    public void setDriverBehaviour(DriverBehaviour driverBehaviour) {
        this.driverBehaviour = driverBehaviour;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public StreetSection getStreetSection() {
        return streetSection;
    }

    public void setStreetSection(StreetSection streetSection) {
        this.streetSection = streetSection;
    }

    public List<StreetSection> getRoute() {
        return route;
    }

    public void setRoute(List<StreetSection> route) {
        this.route = route;
    }

    public StreetSection getActualSection() {
        return actualSection;
    }

    public void setActualSection(StreetSection actualSection) {
        this.actualSection = actualSection;
    }
}
