package at.fhv.itm3.s2.roundabout.event;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.controller.CarController;
import at.fhv.itm3.s2.roundabout.controller.RouteController;
import at.fhv.itm3.s2.roundabout.entity.DriverBehaviour;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutCar;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutSource;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import java.util.concurrent.TimeUnit;


public class CarGenerateEvent extends Event<AbstractSource> {

    /**
     * A reference to the {@link RoundaboutSimulationModel} the {@link CarCouldLeaveSectionEvent} is part of.
     */
    private RoundaboutSimulationModel roundaboutSimulationModel;

    /**
     * Instance of {@link RoundaboutEventFactory} for creating new events.
     */
    protected RoundaboutEventFactory roundaboutEventFactory;

    /**
     * Instance of {@link RouteController} for creating new routes.
     * (protected because of testing)
     */
    protected RouteController routeController;

    /**
     * Constructs a new {@link CarCouldLeaveSectionEvent}.
     *
     * @param model the model this event belongs to.
     * @param name this event's name.
     * @param showInTrace flag to indicate if this event shall produce output for the trace.
     */
    public CarGenerateEvent(Model model, String name, boolean showInTrace) {
        super(model, name, showInTrace);

        roundaboutEventFactory = RoundaboutEventFactory.getInstance();

        if (model instanceof RoundaboutSimulationModel) {
            roundaboutSimulationModel = (RoundaboutSimulationModel)model;
        } else {
            throw new IllegalArgumentException("No suitable model given over.");
        }

        routeController = RouteController.getInstance(roundaboutSimulationModel);

    }

    /**
     * The event routine describes the generation (arrival) of a new car.
     *
     * A new car is generated and added to the given section. A new {@link CarCouldLeaveSectionEvent} is
     * scheduled for the time the car needs to traverse this section at optimal conditions, which means
     * that the car knows how long it needs to reach the end of this section if it can drive to the end of
     * it without the need to stop and after this time the section checks if a car could leave the section.
     * At the end the event routine schedules a new {@link CarGenerateEvent} with a normally distributed time.
     *
     * @param source instance of {@link AbstractSource} in which the car is generated
     */
    @Override
    public void eventRoutine(AbstractSource source) {
        // TODO: use meaningful values!!
        Car car = new Car(roundaboutSimulationModel, "", false);
        IRoute route = this.routeController.getRandomRoute(source);
        DriverBehaviour driverBehaviour = new DriverBehaviour(5.6, 2.0, 2.0, 1, 1);
        ICar roundaboutCar = new RoundaboutCar(getModel(), car, 3.8, driverBehaviour, route);
        roundaboutCar.enterSystem();
        CarController.addCarMapping(car, roundaboutCar);
        IConsumer nextSection = source.getConnectedStreet();

        if (nextSection instanceof StreetSection) {
            ((Street)nextSection).addCar(roundaboutCar);
            double traverseTime = roundaboutCar.getTimeToTraverseCurrentSection();
            CarCouldLeaveSectionEvent carCouldLeaveSectionEvent = roundaboutEventFactory.createCarCouldLeaveSectionEvent(roundaboutSimulationModel);
            carCouldLeaveSectionEvent.schedule((Street)nextSection, new TimeSpan(traverseTime, TimeUnit.SECONDS));

            double carGenerateRatio;
            if (source instanceof RoundaboutSource) {
                carGenerateRatio = ((RoundaboutSource) source).getGenerateRatio();
            } else {
                throw new IllegalStateException("Source should be of type RoundaboutSource");
            }

            double timeBetweenCarArrivals = roundaboutSimulationModel.getRandomTimeBetweenCarArrivalsOnMainFlow() * carGenerateRatio;
            CarGenerateEvent carGenerateEvent = roundaboutEventFactory.createCarGenerateEvent(roundaboutSimulationModel);
            carGenerateEvent.schedule(source, new TimeSpan(timeBetweenCarArrivals, TimeUnit.SECONDS));
        } else {
            throw new IllegalStateException("NextSection should be of type Street");
        }

    }
}