package at.fhv.itm3.s2.roundabout.mocks;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.event.CarCouldLeaveSectionEvent;
import at.fhv.itm3.s2.roundabout.event.RoundaboutEventFactory;
import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import org.mockito.Mockito;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.when;

public class CarCouldLeaveSectionEventMock extends CarCouldLeaveSectionEvent{

    private int createCarCouldLeaveSectionEventCounter = 0;

    private RoundaboutSimulationModel model;

    /**
     * Constructs a new CarCouldLeaveSectionEvent
     *
     * @param model       the model this event belongs to
     * @param name        this event's name
     * @param showInTrace flag to indicate if this event shall produce output for the trace
     */
    public CarCouldLeaveSectionEventMock(Model model, String name, boolean showInTrace) {
        super(model, name, showInTrace);
        this.model = (RoundaboutSimulationModel)model;
        initMockingComponents();
    }

    private void initMockingComponents() {

        this.roundaboutEventFactory = Mockito.mock(RoundaboutEventFactory.class);

        when(this.roundaboutEventFactory.createCarCouldLeaveSectionEvent((RoundaboutSimulationModel)notNull())).then(invocationOnMock -> {
            createCarCouldLeaveSectionEventCounter++;
            return new CarCouldLeaveSectionEventMock(this.model, "", false);
        });
    }

    public int getCreateCarCouldLeaveSectionEventCounter() {
        return createCarCouldLeaveSectionEventCounter;
    }

    @Override
    public void eventRoutine(Street donorStreet) throws SuspendExecution {
        super.eventRoutine(donorStreet);
    }

}
