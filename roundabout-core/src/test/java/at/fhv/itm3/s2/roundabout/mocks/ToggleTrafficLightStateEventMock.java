package at.fhv.itm3.s2.roundabout.mocks;

import at.fhv.itm14.trafsim.model.events.TrafficLightGreenEvent;
import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.event.RoundaboutEventFactory;
import at.fhv.itm3.s2.roundabout.event.ToggleTrafficLightStateEvent;
import desmoj.core.simulator.Model;
import org.mockito.Mockito;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.when;

public class ToggleTrafficLightStateEventMock extends ToggleTrafficLightStateEvent {

    private final RoundaboutSimulationModel model;
    private int createCarCouldLeaveSectionEventCounter;

    /**
     * Constructs a new {@link ToggleTrafficLightStateEvent}.
     *
     * @param model       the model this event belongs to.
     * @param name        this event's name.
     * @param showInTrace flag to indicate if this event shall produce output for the trace.
     */
    public ToggleTrafficLightStateEventMock(Model model, String name, boolean showInTrace) {
        super(model, name, showInTrace);
        this.model = (RoundaboutSimulationModel) model;
        initMockingComponents();
    }

    private void initMockingComponents() {
        this.createCarCouldLeaveSectionEventCounter = 0;

        this.roundaboutEventFactory = Mockito.mock(RoundaboutEventFactory.class);

        when(this.roundaboutEventFactory.createToggleTrafficLightStateEvent((RoundaboutSimulationModel) notNull())).then(
            invocationOnMock -> new ToggleTrafficLightStateEventMock(this.model, "", false)
        );

        when(this.roundaboutEventFactory.createCarCouldLeaveSectionEvent((RoundaboutSimulationModel) notNull())).then(invocationOnMock -> {
            this.createCarCouldLeaveSectionEventCounter++;
            return new CarCouldLeaveSectionEventMock(this.model, "", false);
        });
    }

    public int getCreateCarCouldLeaveSectionEventCounter() {
        return this.createCarCouldLeaveSectionEventCounter;
    }

}
