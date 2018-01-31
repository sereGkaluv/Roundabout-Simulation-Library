package at.fhv.itm3.s2.roundabout.integration;

import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSink;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.mocks.RouteGeneratorMock;
import at.fhv.itm3.s2.roundabout.mocks.RouteType;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class ZeroCarStreetIntegration {

    private RoundaboutSimulationModel model;
    private Experiment exp;

    @Before
    public void setUp() {
        model = new RoundaboutSimulationModel(null, "", false, false, 3.5, 10.0);
        exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);
        exp.setShowProgressBar(false);
    }

    @Test
    public void zeroCarsPassingTwoStreetSections() {

        exp.stop(new TimeInstant(60, TimeUnit.SECONDS));
        RouteGeneratorMock routeGeneratorMock = new RouteGeneratorMock(model);
        IRoute route = routeGeneratorMock.getRoute(RouteType.TWO_STREETSECTIONS_TWO_CARS);
        AbstractSink sink = route.getSink();

        //no car is generated as "source.startGeneratingCars();" is not called.

        exp.start();
        exp.finish();

        Assert.assertEquals(0, sink.getNrOfEnteredCars());
        Assert.assertEquals(false, exp.hasError());
    }
}
