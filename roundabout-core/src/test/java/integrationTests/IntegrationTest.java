package integrationTests;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.Source;
import at.fhv.itm3.s2.roundabout.adapter.Street;
import at.fhv.itm3.s2.roundabout.api.entity.ISource;
import at.fhv.itm3.s2.roundabout.api.entity.IStreet;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import mocks.CarGenerateEventMock;
import mocks.RouteGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class IntegrationTest {

    private RoundaboutSimulationModel model;
    private Experiment exp;

    @Before
    public void setUp() {
        model = new RoundaboutSimulationModel(null, "", false, false);
        exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);
    }

    @Test
    public void twoSectionsTwoCars_carsShouldEnterSinks() {

        exp.stop(new TimeInstant(60,TimeUnit.SECONDS));

        RouteGenerator routeGenerator = new RouteGenerator(model);
        CarGenerateEventMock carGenerateEventMock = new CarGenerateEventMock(model, "", false, 2, routeGenerator);
        ISource source = carGenerateEventMock.getSource();

        carGenerateEventMock.eventRoutine((Street)source.getConnectedStreet());

        IStreet sink = carGenerateEventMock.getSink();

        exp.start();

        exp.finish();

        Assert.assertEquals(2, sink.getNrOfEnteredCars());
    }
}
