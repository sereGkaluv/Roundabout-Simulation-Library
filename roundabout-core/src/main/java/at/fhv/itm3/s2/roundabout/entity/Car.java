package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;

import java.util.List;

public class Car implements ICar {
    private double length;
    private double lastUpdateTime;
    private DriverBehaviour driverBehaviour;
    private final List<StreetSection> route;
    private StreetSection currentSection;

    public Car(double length, DriverBehaviour driverBehaviour, List<StreetSection> route) {
        this.setLength(length);
        this.setLastUpdateTime(0);
        this.setDriverBehaviour(driverBehaviour);
        this.setCurrentSection(!route.isEmpty() ? route.get(0) : null);
        this.route = route;
    }

    public double getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(double lastUpdateTime) {
        if(lastUpdateTime > 0){
            this.lastUpdateTime = lastUpdateTime;
        } else {
            throw new IllegalArgumentException("last update time must be positive");
        }
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
        if(length > 0) {
            this.length = length;
        } else {
            throw new IllegalArgumentException("length must be positive");
        }
    }

    public StreetSection getDestination() {
        return !route.isEmpty() ? route.get(route.size() - 1) : null;
    }

    public List<StreetSection> getRoute() {
        return route;
    }

    public StreetSection getCurrentSection() {
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
