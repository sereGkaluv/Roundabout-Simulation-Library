package at.fhv.itm3.s2.roundabout.event;

import at.fhv.itm3.s2.roundabout.entity.ModelStructure;
import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.mocks.SectionMock;
import at.fhv.itm3.s2.roundabout.mocks.ToggleTrafficLightStateEventMock;
import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Experiment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.when;

public class ToggleTrafficLightStateEventTest {

    private RoundaboutSimulationModel model;

    @Before
    public void setUp() {
        model = new RoundaboutSimulationModel(null, "", false, false, 3.5, 10.0);
        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        Experiment.setReferenceUnit(TimeUnit.SECONDS);
        model.connectToExperiment(exp);
        model.registerModelStructure(new ModelStructure(model));
    }

    @Test
    public void carCouldLeaveEventIsNotCalled() throws SuspendExecution {
        ToggleTrafficLightStateEventMock eventMock = new ToggleTrafficLightStateEventMock(model, null, false);
        StreetSection section = new SectionMock(false, false, true, 0).getSection();

        when(section.isTrafficLightActive()).thenReturn(true);
        when(section.isTrafficLightFreeToGo()).thenReturn(false);

        eventMock.eventRoutine(section);

        Assert.assertEquals(0, eventMock.getCreateCarCouldLeaveSectionEventCounter());
    }

    @Test
    public void carCouldLeaveEventIsCalled() throws SuspendExecution {
        ToggleTrafficLightStateEventMock eventMock = new ToggleTrafficLightStateEventMock(model, null, false);
        StreetSection section = new SectionMock(false, false, true, 0).getSection();

        when(section.isTrafficLightActive()).thenReturn(true);
        when(section.isTrafficLightFreeToGo()).thenReturn(true);

        eventMock.eventRoutine(section);

        Assert.assertEquals(1, eventMock.getCreateCarCouldLeaveSectionEventCounter());
    }
}
