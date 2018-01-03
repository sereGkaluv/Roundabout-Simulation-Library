package at.fhv.itm3.s2.roundabout.integration;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.mocks.RouteGenerator;
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
        model = new RoundaboutSimulationModel(null, "", false, false);
        exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);
        exp.setShowProgressBar(false);
    }

    @Test
    public void intersectionWith2DirectionsAndStreetSections_twoCarsShouldEnterSinks() {

        exp.stop(new TimeInstant(60, TimeUnit.SECONDS));

        RouteGenerator routeGenerator = new RouteGenerator(model);

        IRoute route = routeGenerator.getRoute(RouteType.STREETSECTION_INTERSECTION_STREETSECTION);
        AbstractSource source = route.getSource();

        source.startGeneratingCars();

        IConsumer sink = route.getSink();

        exp.start();

        exp.finish();

        Assert.assertEquals(2, ((Street)sink).getNrOfEnteredCars());
    }
}
