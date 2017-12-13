package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.*;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;

import java.util.Iterator;

public class Car implements ICar {

    private final double length;
    private final Iterator<IStreetSection> routeIterator;

    private double lastUpdateTime;
    private final IDriverBehaviour driverBehaviour;
    private IRoute route;
    private IStreetSection currentSection;
    private IStreetSection nextSection;

    public Car(double length, IDriverBehaviour driverBehaviour, IRoute route)
    throws IllegalArgumentException {
        this.length = length;

        if (driverBehaviour != null) {
            this.driverBehaviour = driverBehaviour;
        } else {
            throw new IllegalArgumentException("Driver behaviour should not be null.");
        }

        if (route != null) {
            this.route = route;
            this.routeIterator = route.getRoute().iterator();
            // The below order is important!
            this.currentSection = retrieveNextRouteSection();
            this.nextSection = retrieveNextRouteSection();
        } else {
            throw new IllegalArgumentException("Route should not be null.");
        }

        this.setLastUpdateTime(0);
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
        //TODO getTimeToTraverseSection()
        return 0;
    }

    @Override
    public double getTimeToTraverseSection(IStreetSection section) {
        //TODO getTimeToTraverseSection(IStreetSection section)
        return 0;
    }

    @Override
    public double getTransitionTime() {
        //TODO getTransitionTime()
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
        return nextSection;
    }

    @Override
    public void traverseToNextSection() {
        this.currentSection = this.nextSection;
        this.nextSection = retrieveNextRouteSection();
    }

    @Override
    public IStreetSection getDestination() {
        return route.getDestinationSection();
    }

    @Override
    public void setRoute(IRoute route) {
        this.route = route;
    }

    private IStreetSection retrieveNextRouteSection() {
        return routeIterator.hasNext() ? routeIterator.next() : null;
    }
}
