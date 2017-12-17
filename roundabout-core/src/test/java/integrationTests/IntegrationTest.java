package integrationTests;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.RoundaboutSource;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import mocks.CarGenerateEventMock;
import mocks.RoundaboutSourceMock;
import mocks.RouteGenerator;
import mocks.RouteType;
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
    public void twoStreetSectionsTwoCars_carsShouldEnterSinks() {

        exp.stop(new TimeInstant(60,TimeUnit.SECONDS));

        RouteGenerator routeGenerator = new RouteGenerator(model);

        IRoute route = routeGenerator.getRoute(RouteType.TWO_STREETSECTIONS);
        AbstractSource source = route.getSource();

        source.startGeneratingCars();

        Street sink = route.getSink();

        exp.start();

        exp.finish();

        Assert.assertEquals(2, sink.getNrOfEnteredCars());
    }

    @Test
    public void oneOneWayStreetOneStreetSectionTwoCars_carsShouldEnterSinks() {

//        exp.stop(new TimeInstant(120,TimeUnit.SECONDS));
//
//        RouteGenerator routeGenerator = new RouteGenerator(model);
//        CarGenerateEventMock carGenerateEventMock = new CarGenerateEventMock(model, "", false, 2, routeGenerator, RouteType.ONE_ONEWAYSTREET_ONE_STREETSECTION);
//
//        AbstractSource source = carGenerateEventMock.getSource();
//
//        carGenerateEventMock.eventRoutine((Street)source.getConnectedStreet());
//
//        Street sink = carGenerateEventMock.getSink();
//
//        exp.start();
//
//        exp.finish();
//
//        Assert.assertEquals(2, sink.getNrOfEnteredCars());

    }
}
