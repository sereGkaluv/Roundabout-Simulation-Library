package at.fhv.itm3.s2.roundabout.integration;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSink;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutIntersection;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.mocks.RouteGeneratorMock;
import at.fhv.itm3.s2.roundabout.mocks.RouteType;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TrafficJamIntegration {

    private RoundaboutSimulationModel model;
    private Experiment exp;

    @Before
    public void setUp() {
        model = new RoundaboutSimulationModel(null, "", false, false, 3.5, 10.0, 6.0, 2.0);
        exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);
        exp.setShowProgressBar(false);
    }

    @Test
    public void twoStreetSectionsWithTwoCars_lastStreetSectionCouldNotBeEntered() {

        exp.stop(new TimeInstant(10000, TimeUnit.SECONDS));

        RouteGeneratorMock routeGeneratorMock = new RouteGeneratorMock(model);

        IRoute route = routeGeneratorMock.getRoute(RouteType.TWO_STREETSECTIONS_ONE_STREETSECTIONMOCK_TWO_CARS);
        AbstractSource source = route.getSource();

        source.startGeneratingCars(0.0);

        AbstractSink sink = route.getSink();

        exp.start();

        exp.finish();

        Street streetWithTrafficJam = (StreetSection)route.getSectionAt(1);

        Assert.assertEquals(0, sink.getNrOfEnteredCars());
        Assert.assertEquals(2, streetWithTrafficJam.getCarQueue().size());
    }

    @Test
    public void intersection_streetSectionAfterIntersectionIsFull() {

        exp.stop(new TimeInstant(10000, TimeUnit.SECONDS));

        RouteGeneratorMock routeGeneratorMock = new RouteGeneratorMock(model);

        IRoute route = routeGeneratorMock.getRoute(RouteType.STREETSECTION_INTERSECTION_STREETSECTIONMOCK_TEN_CARS);
        AbstractSource source = route.getSource();
        RoundaboutIntersection intersection = (RoundaboutIntersection)route.getSectionAt(1);
        intersection.getController().start();

        source.startGeneratingCars(0.0);

        AbstractSink sink = route.getSink();

        exp.start();

        exp.finish();

        Street streetAfterIntersection = (StreetSection)route.getSectionAt(2);

        Assert.assertEquals(0, sink.getNrOfEnteredCars());
        Assert.assertTrue(streetAfterIntersection.getNrOfLostCars() > 0);
    }
}
