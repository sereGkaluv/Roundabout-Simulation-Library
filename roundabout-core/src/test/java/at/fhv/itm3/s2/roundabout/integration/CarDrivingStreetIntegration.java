package at.fhv.itm3.s2.roundabout.integration;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.mocks.RoundaboutSinkMock;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import at.fhv.itm3.s2.roundabout.mocks.RouteGenerator;
import at.fhv.itm3.s2.roundabout.mocks.RouteType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

        IConsumer sink = route.getSink();

        exp.start();

        exp.finish();

        Assert.assertEquals(2, ((Street)sink).getNrOfEnteredCars());
    }

    @Test
    public void precedence_oneCarStaysOnTrack_oneCarWantsToChangeTrackAndHasToGivePrecedence() {

        exp.stop(new TimeInstant(60, TimeUnit.SECONDS));

        RouteGenerator routeGenerator = new RouteGenerator(model);

        IRoute route1 = routeGenerator.getRoute(RouteType.ONE_CAR_STAYS_ON_TRACK);
        AbstractSource source1 = route1.getSource();

        IRoute route2 = routeGenerator.getRoute(RouteType.ONE_CAR_CHANGES_TRACK);
        AbstractSource source2 = route2.getSource();

        // start generating cars simultaneously so one have to give precedence to another
        source2.startGeneratingCars();
        source1.startGeneratingCars();

        RoundaboutSinkMock sink1 = (RoundaboutSinkMock)route1.getSink();

        exp.start();
        exp.finish();

        Assert.assertEquals(2, sink1.getNrOfEnteredCars());
        // first car that enters is from source 1
        Assert.assertEquals(source1, sink1.getEnteredCars().get(0).getRoute().getSource());
        // second car that enters is from source 2
        Assert.assertEquals(source2, sink1.getEnteredCars().get(1).getRoute().getSource());
    }
}
