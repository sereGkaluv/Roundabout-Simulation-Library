package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import desmoj.core.simulator.Entity;

import java.util.List;

public class Car implements ICar {

    private double lastUpdateTime;
    private DriverBehaviour driverBehaviour;
    private double length;
    private StreetSection destination;
    private List<StreetSection> route;
    private StreetSection actualSection;

    public Car(){}

    public Car(long lastUpdateTime, DriverBehaviour driverBehaviour, double length, StreetSection destination, List<StreetSection> route, StreetSection actualSection ){
        this.lastUpdateTime = lastUpdateTime;
        this.driverBehaviour = driverBehaviour;
        this.length = length;
        this.destination = destination;
        this.route = route;
        this.actualSection = actualSection;
    }

    public double getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(double lastUpdateTime) {
        if(lastUpdateTime > 0){
            this.lastUpdateTime = lastUpdateTime;
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
        }
    }

    public StreetSection getDestination() {
        return destination;
    }

    public void setDestination(StreetSection destination) {
        this.destination = destination;
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
        if(route.contains(actualSection) && route.indexOf(actualSection) >= route.indexOf(this.actualSection)){
            this.actualSection = actualSection;
        }
    }
}
