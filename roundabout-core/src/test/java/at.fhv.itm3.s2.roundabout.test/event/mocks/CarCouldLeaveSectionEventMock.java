package at.fhv.itm3.s2.roundabout.test.event.mocks;

import at.fhv.itm3.s2.roundabout.RoundaboutModel;
import at.fhv.itm3.s2.roundabout.event.CarCouldLeaveSectionEvent;
import at.fhv.itm3.s2.roundabout.event.RoundaboutEventFactory;
import desmoj.core.simulator.Model;

import org.mockito.Mockito;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.when;

public class CarCouldLeaveSectionEventMock extends CarCouldLeaveSectionEvent{

    private int createCarCouldLeaveSectionEventCounter = 0;

    private RoundaboutModel model;

    /**
     * Constructs a new CarCouldLeaveSectionEvent
     *
     * @param model       the model this event belongs to
     * @param name        this event's name
     * @param showInTrace flag to indicate if this event shall produce output for the trace
     */
    public CarCouldLeaveSectionEventMock(Model model, String name, boolean showInTrace) {
        super(model, name, showInTrace);
        this.model = (RoundaboutModel)model;
        initMockingComponents();
    }

    private void initMockingComponents() {

        this.roundaboutEventFactory = Mockito.mock(RoundaboutEventFactory.class);

        when(this.roundaboutEventFactory.createCarCouldLeaveSectionEvent((RoundaboutModel)notNull())).then(invocationOnMock -> {
            createCarCouldLeaveSectionEventCounter++;
            return new CarCouldLeaveSectionEvent(this.model, "", false);
        });
    }

    public int getCreateCarCouldLeaveSectionEventCounter() {
        return createCarCouldLeaveSectionEventCounter;
    }

}
