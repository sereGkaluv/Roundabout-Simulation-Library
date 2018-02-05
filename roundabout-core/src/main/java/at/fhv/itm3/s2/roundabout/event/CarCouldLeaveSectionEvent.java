package at.fhv.itm3.s2.roundabout.event;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.intersection.Intersection;
import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

import java.util.concurrent.TimeUnit;

public class CarCouldLeaveSectionEvent extends Event<Street> {

    /**
     * A reference to the {@link RoundaboutSimulationModel} the {@link CarCouldLeaveSectionEvent} is part of.
     */
    private RoundaboutSimulationModel roundaboutSimulationModel;

    /**
     * Instance of {@link RoundaboutEventFactory} for creating new events.
     * (protected because of testing)
     */
    protected RoundaboutEventFactory roundaboutEventFactory;

    /**
     * Constructs a new {@link CarCouldLeaveSectionEvent}.
     *
     * @param model the model this event belongs to.
     * @param name this event's name.
     * @param showInTrace flag to indicate if this event shall produce output for the trace.
     */
    public CarCouldLeaveSectionEvent(Model model, String name, boolean showInTrace) {
        super(model, name, showInTrace);

        roundaboutEventFactory = RoundaboutEventFactory.getInstance();

        if (model instanceof RoundaboutSimulationModel) {
            roundaboutSimulationModel = (RoundaboutSimulationModel) model;
        } else {
            throw new IllegalArgumentException("No suitable model given over.");
        }
    }

    /**
     * The event routine describes the moving of a car from one section to the next section.
     *
     * If the given section has a car at its exit point, it is checked if this car could leave this
     * section and enter the next one. If that is true, a new {@link CarCouldLeaveSectionEvent} for the next
     * section (the section the car enters) is scheduled after the time the car needs to traverse that
     * section. After that the car is moved from this section to the next one.
     * If the current section (the section in which the car was before moving) is not empty, a new
     * CarCouldLeaveSectionEvent is scheduled for the current section after the transition time of the
     * first car (only if this time is passed it makes sense to update all car positions on this time and
     * check if there is another car to leave the section).
     * Moreover for all previous sections of the current section a new {@link CarCouldLeaveSectionEvent} is immediately
     * scheduled to check if the previous sections have a car which could enter the current section.
     *
     * @param donorStreet the street car will move away from.
     * @throws SuspendExecution Marker exception for Quasar (inherited).
     */
    @Override
    public void eventRoutine(Street donorStreet) throws SuspendExecution {
        if (donorStreet.firstCarCouldEnterNextSection()) {

            // schedule a CarCouldLeaveSectionEvent for the next section, so it is thrown when the car should be able to
            // leave the next section under optimal conditions
            IConsumer nextSection = donorStreet.getFirstCar().getNextSection();
            if (nextSection != null && nextSection instanceof StreetSection) {
                roundaboutEventFactory.createCarCouldLeaveSectionEvent(roundaboutSimulationModel).schedule(
                    (StreetSection) nextSection,
                    new TimeSpan((donorStreet.getFirstCar().getTimeToTraverseSection(nextSection)), roundaboutSimulationModel.getModelTimeUnit())
                );
                donorStreet.moveFirstCarToNextSection();
            }  else if (nextSection != null && (nextSection instanceof RoundaboutSink || nextSection instanceof Intersection)) {
                donorStreet.moveFirstCarToNextSection();
            }

            // if the current section is not empty, schedule a new CarCouldLeaveSectionEvent after the time the first
            // car in the section needs to move away from its current position (this time is depending on whether the
            // car is standing or driving
            if (!donorStreet.isEmpty()) {
                roundaboutEventFactory.createCarCouldLeaveSectionEvent(roundaboutSimulationModel).schedule(
                    donorStreet,
                    new TimeSpan(donorStreet.getFirstCar().getTransitionTime(), roundaboutSimulationModel.getModelTimeUnit())
                );
            }

            // schedule a new CarCouldLeaveSectionEvent for the previous sections (if there are any) so the previous section
            // check if they have a car which could enter the current section because there might be space for a new car
            IStreetConnector previousStreetConnector = donorStreet.getPreviousStreetConnector();
            if (previousStreetConnector != null) {
                for (IConsumer previousSection : previousStreetConnector.getPreviousConsumers()) {
                    if (previousSection != null && previousSection instanceof StreetSection) {
                        roundaboutEventFactory.createCarCouldLeaveSectionEvent(roundaboutSimulationModel).schedule(
                            (StreetSection )previousSection,
                            new TimeSpan(0, roundaboutSimulationModel.getModelTimeUnit())
                        );
                    }
                }
            }
        }
    }
}