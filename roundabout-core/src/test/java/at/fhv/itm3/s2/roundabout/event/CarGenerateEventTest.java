package at.fhv.itm3.s2.roundabout.event;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import desmoj.core.simulator.Experiment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CarGenerateEventTest {

    private RoundaboutSimulationModel roundaboutSimulationModel;

    @Before
    public void setUp() {
        roundaboutSimulationModel = new RoundaboutSimulationModel(
            null,
            "",
            false,
            false
        );

        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        roundaboutSimulationModel.connectToExperiment(exp);
    }

    @Test
    public void eventRoutineTest() throws Exception {
        StreetSection section = new StreetSection(
            10.0,
            null,
            null,
                roundaboutSimulationModel,
            null,
            false
        );

        Assert.assertTrue(section.isEmpty());

        CarGenerateEvent event = RoundaboutEventFactory.getInstance().createCarGenerateEvent(roundaboutSimulationModel);
        event.eventRoutine(section);

        Assert.assertFalse(section.isEmpty());
    }
}