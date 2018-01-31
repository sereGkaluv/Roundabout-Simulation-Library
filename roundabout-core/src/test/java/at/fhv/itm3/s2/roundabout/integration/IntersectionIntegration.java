package at.fhv.itm3.s2.roundabout.integration;

import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSink;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutIntersection;
import at.fhv.itm3.s2.roundabout.mocks.RouteGeneratorMock;
import at.fhv.itm3.s2.roundabout.mocks.RouteType;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class IntersectionIntegration {

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
    public void intersectionWith2DirectionsAndStreetSections_twoCarsShouldEnterSinks() {

        exp.stop(new TimeInstant(10000, TimeUnit.SECONDS));

        RouteGeneratorMock routeGeneratorMock = new RouteGeneratorMock(model);

        IRoute route = routeGeneratorMock.getRoute(RouteType.STREETSECTION_INTERSECTION_STREETSECTION_TWO_CARS);
        AbstractSource source = route.getSource();
        RoundaboutIntersection intersection = (RoundaboutIntersection)route.getSectionAt(1);
        intersection.getController().start();

        source.startGeneratingCars(0.0);

        AbstractSink sink = route.getSink();

        exp.start();

        exp.finish();

        Assert.assertEquals(2, sink.getNrOfEnteredCars());
    }
}
