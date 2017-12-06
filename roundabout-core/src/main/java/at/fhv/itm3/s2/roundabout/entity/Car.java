package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.*;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;

import java.util.List;

public class Car implements ICar {

    private double length;
    private double lastUpdateTime;
    private IDriverBehaviour driverBehaviour;
    private final IRoute route;
    private IStreetSection currentSection;

    public Car(double length, IDriverBehaviour driverBehaviour, IRoute route) {
        this.route = route;
        this.setLength(length);
        this.setLastUpdateTime(0);
        this.setDriverBehaviour(driverBehaviour);
        this.setCurrentSection(!route.isEmpty() ? (StreetSection) route.getSection(0) : null);
    }

    @Override
    public double getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public void setLastUpdateTime(double lastUpdateTime) {
        if (lastUpdateTime >= 0) {
            this.lastUpdateTime = lastUpdateTime;
        } else {
            throw new IllegalArgumentException("last update time must be positive");
        }
    }

    @Override
    public IDriverBehaviour getDriverBehaviour() {
        return driverBehaviour;
    }

    @Override
    public void setDriverBehaviour(IDriverBehaviour driverBehaviour) {
        this.driverBehaviour = driverBehaviour;
    }

    @Override
    public double getLength() {
        return length;
    }

    @Override
    public void setLength(double length) {
        if(length > 0) {
            this.length = length;
        } else {
            throw new IllegalArgumentException("length must be positive");
        }
    }

    @Override
    public IStreetSection getDestination() {
        return !route.isEmpty() ? route.getSection(route.getNumberOfSections() - 1) : null;
    }

    @Override
    public IRoute getRoute() {
        return route;
    }

    @Override
    public IStreetSection getNextStreetSection() {
        return null;
    }

    public IStreetSection getCurrentSection() {
        return currentSection;
    }

    @Override
    public void setCurrentSection(IStreetSection currentSection) {
        if (this.currentSection == null || route.isNewSectionBehindOldSection(currentSection, this.currentSection)) {
            this.currentSection = currentSection;
        } else {
            throw new IllegalArgumentException("actual street section must be in route and must follow last section");
        }
    }

    @Override
    public double getTimeToTraverseSection() {
        return 0;
    }

    @Override
    public double getTimeToTraverseSection(IStreetSection section) {
        return 0;
    }

    @Override
    public double getTransitionTime() {
        return 0;
    }

    @Override
    public IStreetSection getNextSection() {
        return null;
    }
}
