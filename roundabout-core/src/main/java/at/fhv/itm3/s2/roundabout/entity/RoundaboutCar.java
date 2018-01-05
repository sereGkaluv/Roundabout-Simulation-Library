package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm3.s2.roundabout.api.entity.*;
import at.fhv.itm3.s2.roundabout.controller.IntersectionController;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import desmoj.core.simulator.Model;

import java.util.Iterator;

public class RoundaboutCar implements ICar {

    private final Car car;
    private final double length;
    private final IRoute route;
    private final IDriverBehaviour driverBehaviour;
    private final Iterator<IConsumer> routeIterator;

    private double lastUpdateTime;

    private IConsumer lastSection;
    private IConsumer currentSection;
    private IConsumer nextSection;
    private IConsumer sectionAfterNextSection;

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
            this.lastSection = null;
            this.currentSection = retrieveNextRouteSection();
            this.nextSection = retrieveNextRouteSection();
            this.sectionAfterNextSection = retrieveNextRouteSection();
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

        return getTimeToTraverseSection(getCurrentSection());
    }

    @Override
    public double getTimeToTraverseSection(IConsumer section) {

        if (section instanceof StreetSection) {
            double carPosition = 0;
            StreetSection streetSection = (StreetSection)section;

            if (streetSection.getCarPositions().containsKey(this)) {
                carPosition = streetSection.getCarPositions().get(this);
            }

            double remainingLength = streetSection.getLength() - carPosition;
            return remainingLength / this.getDriverBehaviour().getSpeed();
        } else if (section instanceof RoundaboutIntersection && section == this.currentSection) {
            RoundaboutIntersection intersection = (RoundaboutIntersection)section;
            int inDirection = IntersectionController.getInstance().getInDirectionOfIConsumer(intersection, this.lastSection);
            int outDirection = IntersectionController.getInstance().getOutDirectionOfIConsumer(intersection, this.nextSection);
            return intersection.getTimeToTraverseIntersection(inDirection, outDirection);
        }
        else {
            throw new IllegalStateException("Sections needs to be instance of StreetSection.");
        }
    }

    @Override
    public double getTransitionTime() {
        final double currentTime = getRoundaboutModel().getCurrentTime();
        final double minPossibleTransitionTime = getTimeToTraverseCurrentSection();
        final double carLastUpdateTime = getLastUpdateTime();

        if ((currentTime - carLastUpdateTime) > minPossibleTransitionTime) {
            final double standardCarAccelerationTime = getRoundaboutModel().getStandardCarAccelerationTime();
            final double carAccelerationFactor = getDriverBehaviour().getAccelerationFactor();
            final double carAccelerationTime = standardCarAccelerationTime * carAccelerationFactor;

            return minPossibleTransitionTime + carAccelerationTime;
        }

        return minPossibleTransitionTime;
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
    public IConsumer getLastSection() {
        return lastSection;
    }

    @Override
    public IConsumer getCurrentSection() {
        return currentSection;
    }

    @Override
    public IConsumer getNextSection() {
        return nextSection;
    }

    @Override
    public IConsumer getSectionAfterNextSection() {
        return sectionAfterNextSection;
    }

    @Override
    public void traverseToNextSection() {
        this.lastSection = this.currentSection;
        this.currentSection = this.nextSection;
        this.nextSection = this.sectionAfterNextSection;
        this.sectionAfterNextSection = retrieveNextRouteSection();
    }

    @Override
    public IConsumer getDestination() {
        return route.getDestinationSection();
    }

    private RoundaboutSimulationModel getRoundaboutModel() {
        final Model model = getOldImplementationCar().getModel();
        if (model instanceof RoundaboutSimulationModel) {
            return (RoundaboutSimulationModel) model;
        } else {
            throw new IllegalArgumentException("Not suitable roundaboutSimulationModel.");
        }
    }

    private Street retrieveNextRouteSection() {
        return routeIterator.hasNext() ? routeIterator.next() : null;
    }

}
