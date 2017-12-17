package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm3.s2.roundabout.adapter.OneWayStreetAdapter;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;

import java.util.Iterator;

public class RoundaboutCar implements ICar {

    private final Car car;
    private final double length;
    private final IRoute route;
    private final IDriverBehaviour driverBehaviour;
    private final Iterator<Street> routeIterator;

    private double lastUpdateTime;

    private Street currentSection;
    private Street nextSection;

    public RoundaboutCar(Car car, double length, IDriverBehaviour driverBehaviour, IRoute route)
    throws IllegalArgumentException {

        if (car != null) {
            this.car = car;
        } else {
            throw new IllegalArgumentException("Car should not be null.");
        }

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

    public Car getOldImplementationCar() {
        return car;
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
    public double getTimeToTraverseCurrentSection() {

        return getTimeToTraverseSection(this.getCurrentSection());
    }

    @Override
    public double getTimeToTraverseSection(Street section) {

        if (section instanceof StreetSection) {
            double carPosition = 0;

            if (section.getCarPositions().containsKey(this)) {
                carPosition = section.getCarPositions().get(this);
            }

            double remainingLength = section.getLength() - carPosition;
            return remainingLength / this.getDriverBehaviour().getSpeed();
        } else if (section instanceof OneWayStreetAdapter) {
            return 0; // TODO: is that enough?
        } else {
            throw new IllegalStateException("Street needs to be instance of StreetSection or OneWayStreetAdapter.");
        }
    }

    @Override
    public double getTransitionTime() {
        //TODO getTransitionTime()
        return 3;
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
    public Street getCurrentSection() {
        return currentSection;
    }

    @Override
    public Street getNextSection() {
        return nextSection;
    }

    @Override
    public void traverseToNextSection() {
        this.currentSection = this.nextSection;
        this.nextSection = retrieveNextRouteSection();
    }

    @Override
    public Street getDestination() {
        return route.getDestinationSection();
    }

    private Street retrieveNextRouteSection() {
        return routeIterator.hasNext() ? routeIterator.next() : null;
    }
}
