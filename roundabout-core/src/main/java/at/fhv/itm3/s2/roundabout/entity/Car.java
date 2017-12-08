package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.*;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;

public class Car implements ICar {

    private final double length;
    private final IDriverBehaviour driverBehaviour;
    private final IRoute route;

    private double lastUpdateTime;
    private IStreetSection currentSection;

    public Car(double length, IDriverBehaviour driverBehaviour, IRoute route) {
        this.length = length;
        this.driverBehaviour = driverBehaviour;
        this.route = route;

        this.setLastUpdateTime(0);
        this.setCurrentSection(route.getStartSection());
    }

    @Override
    public double getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public void setLastUpdateTime(double lastUpdateTime)
    throws IllegalArgumentException {
        if (lastUpdateTime >= 0) {
            this.lastUpdateTime = lastUpdateTime;
        } else {
            throw new IllegalArgumentException("last update time must be positive");
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
    public double getLength() {
        return length;
    }

    @Override
    public IDriverBehaviour getDriverBehaviour() {
        return driverBehaviour;
    }


    @Override
    public IRoute getRoute() {
        return route;
    }

    @Override
    public IStreetSection getCurrentSection() {
        return currentSection;
    }

    @Override
    public IStreetSection getNextSection() {
        return null;
    }

    @Override
    public void setCurrentSection(IStreetSection currentSection) {
        if (this.currentSection == null || route.isSectionABehindSectionB(currentSection, this.currentSection)) {
            this.currentSection = currentSection;
        } else {
            throw new IllegalArgumentException("actual street section must be in route and must follow last section");
        }
    }

    @Override
    public IStreetSection getDestination() {
        return route.getDestinationSection();
    }
}
