package at.fhv.itm3.s2.roundabout.event;

import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.mocks.CarGenerateEventMock;
import at.fhv.itm3.s2.roundabout.mocks.RouteGeneratorMock;
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
            false,
                3.5,
                10.0
        );

        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        roundaboutSimulationModel.connectToExperiment(exp);
    }

    @Test
    public void eventRoutineTest() {
        StreetSection section = new StreetSection(
            10.0,
            roundaboutSimulationModel,
            null,
            false
        );

        Assert.assertTrue(section.isEmpty());

        RouteGeneratorMock routeGeneratorMock = new RouteGeneratorMock(roundaboutSimulationModel);
        CarGenerateEvent event = new CarGenerateEventMock(roundaboutSimulationModel, "", false, routeGeneratorMock);
        event.eventRoutine(section);

        Assert.assertFalse(section.isEmpty());
    }
}