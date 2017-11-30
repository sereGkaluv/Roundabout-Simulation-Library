package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;

import java.util.List;

public class Car implements ICar {
    private double length;
    private double lastUpdateTime;
    private DriverBehaviour driverBehaviour;
    private final List<IStreetSection> route;
    private StreetSection currentSection;

    public Car(double length, IDriverBehaviour driverBehaviour, List<IStreetSection> route) {
        this.setLength(length);
        this.setLastUpdateTime(0);
        this.setDriverBehaviour((DriverBehaviour) driverBehaviour);
        this.setCurrentSection(!route.isEmpty() ? (StreetSection) route.get(0) : null);
        this.route = route;
    }

    public double getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(double lastUpdateTime) {
        if (lastUpdateTime >= 0) {
            this.lastUpdateTime = lastUpdateTime;
        } else {
            throw new IllegalArgumentException("last update time must be positive");
        }
    }

    public IDriverBehaviour getDriverBehaviour() {
        return driverBehaviour;
    }

    public void setDriverBehaviour(IDriverBehaviour driverBehaviour) {
        this.driverBehaviour = (DriverBehaviour) driverBehaviour;
    }

    @Override
    public IStreetSection getNextStreetSection() {
        return null;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        if(length > 0) {
            this.length = length;
        } else {
            throw new IllegalArgumentException("length must be positive");
        }
    }

    public IStreetSection getDestination() {
        return !route.isEmpty() ? route.get(route.size() - 1) : null;
    }

    public List<IStreetSection> getRoute() {
        return route;
    }

    public IStreetSection getCurrentSection() {
        return currentSection;
    }

    public void setCurrentSection(StreetSection currentSection) {
        if (route.contains(currentSection) && route.indexOf(currentSection) >= route.indexOf(this.currentSection)) {
            this.currentSection = currentSection;
        } else {
            throw new IllegalArgumentException("actual street section must be in route and must follow last section");
        }
    }
}
