package at.fhv.itm3.s2.roundabout.event;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;

public class RoundaboutEventFactory {

    /**
     * Holds a static instance of a RoundaboutEventFactory object
     */
    private static RoundaboutEventFactory instance;

    /**
     * Private constructor so it isn't possible to create a new
     * RoundaboutEventFactory object via the constructor.
     * Use getInstance() instead
     */
    private RoundaboutEventFactory() {
    }

    /**
     * Returns a singleton instance of a RoundaboutEventFactory
     *
     * @return  a RoundaboutEventFactory instance
     */
    public static RoundaboutEventFactory getInstance() {
        if (instance == null) {
            instance = new RoundaboutEventFactory();
        }
        return instance;
    }

    /**
     * Creates a new CarCouldLeaveSectionEvent with the given model
     *
     * @param model     the model the event is part of
     * @return          the newly created CarCouldLeaveSectionEvent
     */
    public CarCouldLeaveSectionEvent createCarCouldLeaveSectionEvent(RoundaboutSimulationModel model) {
        return new CarCouldLeaveSectionEvent(model, "CarCouldLeaveSectionEvent", true);
    }

    /**
     * Creates a new CarGenerateEvent with the given model
     *
     * @param model     the model the event is part of
     * @return          the newly created CarGenerateEvent
     */
    public CarGenerateEvent createCarGenerateEvent(RoundaboutSimulationModel model) {
        return new CarGenerateEvent(model, "CarGenerateEvent", true);
    }
}
