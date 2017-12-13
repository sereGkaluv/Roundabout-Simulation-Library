package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import desmoj.core.simulator.Model;

import java.util.Iterator;

public class Car extends at.fhv.itm14.trafsim.model.entities.Car implements ICar {

    private final double length;
    private final IDriverBehaviour driverBehaviour;
    private final IRoute route;
    private final Iterator<IStreetSection> routeIterator;

    private double lastUpdateTime;
    private IStreetSection currentSection;
    private IStreetSection nextSection;

    public Car(double length, IDriverBehaviour driverBehaviour, IRoute route, Model model, String description, boolean showInTrace)
    throws IllegalArgumentException {
        super(model, description, showInTrace);

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

    private IStreetSection retrieveNextRouteSection() {
        return routeIterator.hasNext() ? routeIterator.next() : null;
    }
}
