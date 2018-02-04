package at.fhv.itm3.s2.roundabout.event;

import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;

public class RoundaboutEventFactory {

    /**
     * Holds a static instance of a {@link RoundaboutEventFactory} object.
     */
    private static RoundaboutEventFactory instance;

    /**
     * Private constructor so it isn't possible to create a new
     * {@link RoundaboutEventFactory} object via the constructor.
     * Use {@link #getInstance()} instead.
     */
    private RoundaboutEventFactory() {
    }

    /**
     * Returns a singleton instance of a {@link RoundaboutEventFactory}.
     *
     * @return a {@link RoundaboutEventFactory} instance.
     */
    public static RoundaboutEventFactory getInstance() {
        if (instance == null) {
            instance = new RoundaboutEventFactory();
        }
        return instance;
    }

    /**
     * Creates a new {@link CarCouldLeaveSectionEvent} within the given model.
     *
     * @param model the model the event is part of.
     * @return the newly created {@link CarCouldLeaveSectionEvent}.
     */
    public CarCouldLeaveSectionEvent createCarCouldLeaveSectionEvent(RoundaboutSimulationModel model) {
        return new CarCouldLeaveSectionEvent(model, "CarCouldLeaveSectionEvent", true);
    }

    /**
     * Creates a new {@link CarGenerateEvent} within the given model.
     *
     * @param model the model the event is part of.
     * @return the newly created {@link CarGenerateEvent}.
     */
    public CarGenerateEvent createCarGenerateEvent(RoundaboutSimulationModel model) {
        return new CarGenerateEvent(model, "CarGenerateEvent", true);
    }

    /**
     * Creates a new {@link ToggleTrafficLightStateEvent} within the given model.
     *
     * @param model the model the event is part of.
     * @return the newly created {@link ToggleTrafficLightS
     * tateEvent}.
     */
    public ToggleTrafficLightStateEvent createToggleTrafficLightStateEvent(RoundaboutSimulationModel model) {
        return new ToggleTrafficLightStateEvent(model, "ToggleTrafficLightStateEvent", true);
    }
}
