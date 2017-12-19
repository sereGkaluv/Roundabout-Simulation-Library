package at.fhv.itm3.s2.roundabout.integration;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import at.fhv.itm3.s2.roundabout.mocks.RouteGenerator;
import at.fhv.itm3.s2.roundabout.mocks.RouteType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

public class CarDrivingStreetIntegration {

    private RoundaboutSimulationModel model;
    private Experiment exp;

    @Before
    public void setUp() {
        model = new RoundaboutSimulationModel(null, "", false, false);
        exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);
        exp.setShowProgressBar(false);
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

        exp.stop(new TimeInstant(60,TimeUnit.SECONDS));

        RouteGenerator routeGenerator = new RouteGenerator(model);

        IRoute route = routeGenerator.getRoute(RouteType.ONE_ONEWAYSTREET_ONE_STREETSECTION);
        AbstractSource source = route.getSource();

        source.startGeneratingCars();

        Street sink = route.getSink();

        exp.start();

        exp.finish();

        Assert.assertEquals(2, sink.getNrOfEnteredCars());

    }

    @Test
    public void oneStreetSectionOneOneWayStreetTwoCars_carsShouldEnterSinks() {

        exp.stop(new TimeInstant(60,TimeUnit.SECONDS));

        RouteGenerator routeGenerator = new RouteGenerator(model);

        IRoute route = routeGenerator.getRoute(RouteType.ONE_STREETSECTION_ONE_ONEWAYSTREET);
        AbstractSource source = route.getSource();

        source.startGeneratingCars();

        Street sink = route.getSink();

        exp.start();

        exp.finish();

        Assert.assertEquals(2, sink.getNrOfEnteredCars());

    }

    @Test
    public void twoOneWayStreetsTwoCars_carsShouldEnterSinks() {

        exp.stop(new TimeInstant(60,TimeUnit.SECONDS));

        RouteGenerator routeGenerator = new RouteGenerator(model);

        IRoute route = routeGenerator.getRoute(RouteType.TWO_ONEWAYSTREETS);
        AbstractSource source = route.getSource();

        source.startGeneratingCars();

        Street sink = route.getSink();

        exp.start();

        exp.finish();

        Assert.assertEquals(2, sink.getNrOfEnteredCars());

    }
}
