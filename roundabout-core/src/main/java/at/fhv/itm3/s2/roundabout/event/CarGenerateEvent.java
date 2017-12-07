package at.fhv.itm3.s2.roundabout.event;

import at.fhv.itm3.s2.roundabout.RoundaboutModel;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.controller.RouteController;
import at.fhv.itm3.s2.roundabout.entity.Car;
import at.fhv.itm3.s2.roundabout.entity.DriverBehaviour;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import java.util.concurrent.TimeUnit;


public class CarGenerateEvent extends Event<StreetSection> {

    /**
     * A reference to the RoundaboutModel the CarCouldLeaveSectionEvent is part of
     */
    private RoundaboutModel myModel;

    /**
     * Instance of RoundaboutEventFactory for creating new events
     */
    private RoundaboutEventFactory roundaboutEventFactory;

    /**
     * Constructs a new CarCouldLeaveSectionEvent
     *
     * @param model         the model this event belongs to
     * @param name          this event's name
     * @param showInTrace   flag to indicate if this event shall produce output for the trace
     */
    public CarGenerateEvent(Model model, String name, boolean showInTrace) {
        super(model, name, showInTrace);

        roundaboutEventFactory = RoundaboutEventFactory.getInstance();

        if (model instanceof RoundaboutModel) {
            myModel = (RoundaboutModel)model;
        } else {
            throw new IllegalArgumentException("No suitable model given over.");
        }

    }

    /**
     * The event routine describes the generation (arrival) of a new car.
     *
     * A new car is generated and added to the given section. A new CarCouldLeaveSectionEvent is
     * scheduled for the time the car needs to traverse this section at optimal conditions, which means
     * that the car knows how long it needs to reach the end of this section if it can drive to the end of
     * it without the need to stop and after this time the section checks if a car could leave the section.
     * At the end the event routine schedules a new CarGenerateEvent with a normally distributed time.
     *
     * @param section           the section the car is added
     */
    @Override
    public void eventRoutine(StreetSection section) {
        // TODO: use meaningful values!!
        ICar car = new Car(1, new DriverBehaviour(1, 1, 1, 1), RouteController.getInstance(myModel).generateNewRoute());
        section.addCar(car);
        roundaboutEventFactory.createCarCouldLeaveSectionEvent(myModel).schedule(section, new TimeSpan(car.getTimeToTraverseSection(), TimeUnit.SECONDS));
        roundaboutEventFactory.createCarGenerateEvent(myModel).schedule(section, new TimeSpan(myModel.getRandomTimeBetweenCarArrivals(), TimeUnit.SECONDS));
    }
}