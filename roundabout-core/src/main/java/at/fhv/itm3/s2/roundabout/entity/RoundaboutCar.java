package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.statistics.StopWatch;
import at.fhv.itm3.s2.roundabout.controller.IntersectionController;
import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import desmoj.core.statistic.Count;
import desmoj.core.statistic.Tally;

import java.util.Iterator;

public class RoundaboutCar implements ICar {

    private final Car car;
    private final double length;
    private final IRoute route;
    private final IDriverBehaviour driverBehaviour;
    private final Iterator<IConsumer> routeIterator;
    private final StopWatch roundaboutStopWatch;
    private final Count roundaboutCounter;
    private final Tally roundaboutTime;
    private final StopWatch stopsStopWatch;

    private double lastUpdateTime;

    private IConsumer lastSection;
    private IConsumer currentSection;
    private IConsumer nextSection;
    private IConsumer sectionAfterNextSection;

    public RoundaboutCar(Model model, double length, Car car, IDriverBehaviour driverBehaviour, IRoute route)
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

        this.setLastUpdateTime(getRoundaboutModel().getCurrentTime());

        this.roundaboutStopWatch = new StopWatch(model);
        this.stopsStopWatch = new StopWatch(model);
        this.roundaboutCounter = new Count(model,  "Roundabout counter", false, false);
        this.roundaboutCounter.reset();
        this.roundaboutTime = new Tally(model, "Roundabout time", false, false);
        this.roundaboutTime.reset();
    }

    public Car getOldImplementationCar() {
        return car;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLastUpdateTime(double lastUpdateTime)
            throws IllegalArgumentException {
        if (lastUpdateTime >= 0) {
            this.lastUpdateTime = lastUpdateTime;
        } else {
            throw new IllegalArgumentException("last update time must be positive");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTimeToTraverseCurrentSection() {

        return getTimeToTraverseSection(getCurrentSection());
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLength() {
        return length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IDriverBehaviour getDriverBehaviour() {
        return driverBehaviour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRoute getRoute() {
        return route;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IConsumer getLastSection() {
        return lastSection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IConsumer getCurrentSection() {
        return currentSection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IConsumer getNextSection() {
        return nextSection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IConsumer getSectionAfterNextSection() {
        return sectionAfterNextSection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void traverseToNextSection() {
        this.lastSection = this.currentSection;
        this.currentSection = this.nextSection;
        this.nextSection = this.sectionAfterNextSection;
        this.sectionAfterNextSection = retrieveNextRouteSection();
        if (isWaiting()) {
            stopWaiting();
        }
    }

    /**
     * {@inheritDoc}
     */
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

    private IConsumer retrieveNextRouteSection() {
        return routeIterator.hasNext() ? routeIterator.next() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enterRoundabout() {
        this.roundaboutCounter.update();
        this.roundaboutStopWatch.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void leaveRoundabout() {
        if (this.roundaboutStopWatch.isRunning()) {
            double res = this.roundaboutStopWatch.stop();
            this.roundaboutTime.update(new TimeSpan(res));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getMeanRoundaboutPassTime() {
        return this.roundaboutTime.getObservations() <= 0L ? 0.0D : this.roundaboutTime.getMean();
    }

    public long getRoundaboutPassedCount() {
        return this.roundaboutCounter.getValue();
    }

    public void enterSystem() {
        car.enterSystem();
    }

    public double leaveSystem() {
        return car.leaveSystem();
    }

    public void enterIntersection() {
        car.enterIntersection();
    }

    public void leaveIntersection() {
        car.leaveIntersection();
    }

    public void startWaiting() {
        if (!isWaiting()) {
            car.startWaiting();
        }
        if (!this.stopsStopWatch.isRunning()) {
            this.stopsStopWatch.start();
        }
    }

    public void stopWaiting() {
        if (this.stopsStopWatch.isRunning()) {
            this.stopsStopWatch.stop();
        } else {
            car.stopWaiting();
        }
    }

    public boolean isWaiting() {
        return car.isWaiting();
    }

    public double getTimeSpentInSystem() {
        return car.getTimeSpentInSystem();
    }

    public double getMeanWaitingTime() {
        return car.getMeanWaitingTime();
    }

    public long getStopCount() {
        return car.getStopCount();
    }

    public long getIntersectionPassedCount() {
        return car.getIntersectionPassedCount();
    }

    public double getMeanIntersectionPassTime() {
        return car.getMeanIntersectionPassTime();
    }

    public double getCoveredDistanceInTime(double time) {
        return time * driverBehaviour.getSpeed();
    }
}
