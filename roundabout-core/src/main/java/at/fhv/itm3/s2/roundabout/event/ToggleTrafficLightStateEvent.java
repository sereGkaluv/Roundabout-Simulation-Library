package at.fhv.itm3.s2.roundabout.event;

import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Event;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeSpan;

public class ToggleTrafficLightStateEvent extends Event<Street> {
    /**
     * A reference to the {@link RoundaboutSimulationModel} the {@link ToggleTrafficLightStateEvent} is part of.
     */
    private RoundaboutSimulationModel roundaboutSimulationModel;

    /**
     * Instance of {@link RoundaboutEventFactory} for creating new events.
     * (protected because of testing)
     */
    protected RoundaboutEventFactory roundaboutEventFactory;

    /**
     * Constructs a new {@link ToggleTrafficLightStateEvent}.
     *
     * @param model       the model this event belongs to.
     * @param name        this event's name.
     * @param showInTrace flag to indicate if this event shall produce output for the trace.
     */
    public ToggleTrafficLightStateEvent(Model model, String name, boolean showInTrace) {
        super(model, name, showInTrace);

        roundaboutEventFactory = RoundaboutEventFactory.getInstance();

        if (model instanceof RoundaboutSimulationModel) {
            roundaboutSimulationModel = (RoundaboutSimulationModel) model;
        } else {
            throw new IllegalArgumentException("No suitable model given over.");
        }
    }

    /**
     * Toggles the traffic lights state "from free to go" to "stop" and vice versa.
     *
     * @param donorStreet the street car will move away from.
     * @throws SuspendExecution Marker exception for Quasar (inherited).
     */
    @Override
    public void eventRoutine(Street donorStreet) throws SuspendExecution {
        donorStreet.setTrafficLightFreeToGo( !donorStreet.isTrafficLightFreeToGo() );
        if(!donorStreet.isTrafficLightTriggeredByJam()) {
            // cyclic traffic light
            donorStreet.setTrafficLightFreeToGo( !donorStreet.isTrafficLightFreeToGo() );

            // generate CarCouldLeaveSectionEvent if traffic light is free to go
            if (donorStreet.isTrafficLightFreeToGo()) {
                // triggered to green
                // TODO simulate acceleration
                roundaboutEventFactory.createCarCouldLeaveSectionEvent(roundaboutSimulationModel).schedule(donorStreet, new TimeSpan(0));
                roundaboutEventFactory.createToggleTrafficLightStateEvent(roundaboutSimulationModel).schedule(
                        donorStreet,
                        new TimeSpan(
                                donorStreet.getGreenPhaseDurationOfTrafficLight(),
                                roundaboutSimulationModel.getModelTimeUnit()
                        )
                );
            } else {
                roundaboutEventFactory.createToggleTrafficLightStateEvent(roundaboutSimulationModel).schedule(
                        donorStreet,
                        new TimeSpan(
                                donorStreet.getRedPhaseDurationOfTrafficLight(),
                                roundaboutSimulationModel.getModelTimeUnit()
                        )
                );
            }
        }


    }
}
