package at.fhv.itm3.s2.roundabout.integration;

import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSink;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.mocks.RoundaboutSinkMock;
import at.fhv.itm3.s2.roundabout.mocks.RouteGeneratorMock;
import at.fhv.itm3.s2.roundabout.mocks.RouteType;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CarDrivingStreetIntegration {

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
    public void twoStreetSectionsTwoCars_carsShouldEnterSinks() {

        exp.stop(new TimeInstant(60, TimeUnit.SECONDS));

        RouteGeneratorMock routeGeneratorMock = new RouteGeneratorMock(model);

        IRoute route = routeGeneratorMock.getRoute(RouteType.TWO_STREETSECTIONS_TWO_CARS);
        AbstractSource source = route.getSource();

        source.startGeneratingCars(0.0);

        AbstractSink sink = route.getSink();

        exp.start();

        exp.finish();

        Assert.assertEquals(2, sink.getNrOfEnteredCars());
    }

    @Test
    public void twoStreetSectionsOneCar_trafficLightRed() {

        exp.stop(new TimeInstant(60, TimeUnit.SECONDS));

        RouteGeneratorMock routeGeneratorMock = new RouteGeneratorMock(model);

        IRoute route = routeGeneratorMock.getRoute(RouteType.TWO_STREETSECTIONS_ONE_CAR);

        ((StreetSection) route.getStartSection()).setTrafficLightFreeToGo(false);

        AbstractSource source = route.getSource();

        source.startGeneratingCars(0.0);

        AbstractSink sink = route.getSink();

        exp.start();

        exp.finish();

        Assert.assertEquals(0, sink.getNrOfEnteredCars());
    }

    @Test
    public void twoStreetSectionsOneCar_trafficLightGreen() {

        exp.stop(new TimeInstant(60, TimeUnit.SECONDS));

        RouteGeneratorMock routeGeneratorMock = new RouteGeneratorMock(model);

        IRoute route = routeGeneratorMock.getRoute(RouteType.TWO_STREETSECTIONS_ONE_CAR);

        ((StreetSection) route.getStartSection()).setTrafficLightFreeToGo(true);

        AbstractSource source = route.getSource();

        source.startGeneratingCars(0.0);

        AbstractSink sink = route.getSink();

        exp.start();

        exp.finish();

        Assert.assertEquals(1, sink.getNrOfEnteredCars());
    }

    @Test
    public void precedence_oneCarStaysOnTrack_oneCarWantsToChangeTrackAndHasToGivePrecedence() {

        exp.stop(new TimeInstant(60, TimeUnit.SECONDS));

        RouteGeneratorMock routeGeneratorMock = new RouteGeneratorMock(model);

        IRoute route1 = routeGeneratorMock.getRoute(RouteType.ONE_CAR_STAYS_ON_TRACK);
        AbstractSource source1 = route1.getSource();

        IRoute route2 = routeGeneratorMock.getRoute(RouteType.ONE_CAR_CHANGES_TRACK);
        AbstractSource source2 = route2.getSource();

        // start generating cars simultaneously so one have to give precedence to another
        source2.startGeneratingCars(0.0);
        source1.startGeneratingCars(0.0);

        RoundaboutSinkMock sink1 = (RoundaboutSinkMock) route1.getSink();

        exp.start();
        exp.finish();

        Assert.assertEquals(2, sink1.getNrOfEnteredCars());
        // first car that enters is from source 1
        Assert.assertEquals(source1, sink1.getEnteredCars().get(0).getRoute().getSource());
        // second car that enters is from source 2
        Assert.assertEquals(source2, sink1.getEnteredCars().get(1).getRoute().getSource());
    }
}
