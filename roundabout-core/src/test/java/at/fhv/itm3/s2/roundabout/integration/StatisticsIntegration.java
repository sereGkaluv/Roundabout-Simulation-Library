package at.fhv.itm3.s2.roundabout.integration;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSink;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.controller.CarController;
import at.fhv.itm3.s2.roundabout.mocks.RoundaboutSinkMock;
import at.fhv.itm3.s2.roundabout.mocks.RouteGeneratorMock;
import at.fhv.itm3.s2.roundabout.mocks.RouteType;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatisticsIntegration {

    private RoundaboutSimulationModel model;
    private Experiment exp;

    @Before
    public void setUp() {
        model = new RoundaboutSimulationModel(null, "", false, false, 0.5, 0.5, 6.0, 2.0);
        exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);
        exp.setShowProgressBar(false);
        CarController.clear();
    }

    @Test
    public void twoStreetSectionsTwoCars_noStops() {

        exp.stop(new TimeInstant(60, TimeUnit.SECONDS));

        RouteGeneratorMock routeGeneratorMock = new RouteGeneratorMock(model);

        IRoute route = routeGeneratorMock.getRoute(RouteType.TWO_STREETSECTIONS_TWO_CARS);
        AbstractSource source = route.getSource();

        source.startGeneratingCars(0.0);

        AbstractSink sink = route.getSink();

        exp.start();

        exp.finish();

        Assert.assertEquals(2, sink.getNrOfEnteredCars());
        List<ICar> cars = CarController.getICars();
        for (ICar car: cars) {
            Assert.assertEquals(0, car.getStopCount());
        }
    }

    @Test
    public void precedence_oneCarStaysOnTrack_oneCarWantsToChangeTrackAndHasToGivePrecedence_oneStop() {

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

        List<ICar> cars = CarController.getICars();
        Assert.assertEquals(2, cars.size());
        Assert.assertEquals(1, cars.get(0).getStopCount());
        Assert.assertEquals(0, cars.get(1).getStopCount());
        Assert.assertEquals(false, cars.get(0).isWaiting());
        Assert.assertEquals(false, cars.get(1).isWaiting());
    }

    @Test
    public void precedence_fourCarStaysOnTrack_fourCarWantsToChangeTrackAndHasToGivePrecedence_maxOneStopPerCar() {

        exp.stop(new TimeInstant(300, TimeUnit.SECONDS));

        RouteGeneratorMock routeGeneratorMock = new RouteGeneratorMock(model);

        IRoute route1 = routeGeneratorMock.getRoute(RouteType.STOPS_FOUR_CARS_CHANGE_TRACK);
        AbstractSource source1 = route1.getSource();

        IRoute route2 = routeGeneratorMock.getRoute(RouteType.STOPS_FOUR_CARS_STAY_ON_TRACK);
        AbstractSource source2 = route2.getSource();

        // start generating cars simultaneously so one have to give precedence to another
        source1.startGeneratingCars(0.5);
        source2.startGeneratingCars(0.0);

        RoundaboutSinkMock sink1 = (RoundaboutSinkMock) route1.getSink();

        exp.start();
        exp.finish();

        Assert.assertEquals(8, sink1.getNrOfEnteredCars());

        // check if no car is waiting any more
        for (ICar car: sink1.getEnteredCars()) {
            Assert.assertFalse(car.isWaiting());
        }

        Assert.assertTrue(sink1.getEnteredCars().get(0).getStopCount() <= 1);
        Assert.assertTrue(sink1.getEnteredCars().get(1).getStopCount() <= 1);
        Assert.assertTrue(sink1.getEnteredCars().get(2).getStopCount() <= 1);
        Assert.assertTrue(sink1.getEnteredCars().get(3).getStopCount() <= 1);
        Assert.assertTrue(sink1.getEnteredCars().get(4).getStopCount() <= 1);
        Assert.assertTrue(sink1.getEnteredCars().get(5).getStopCount() <= 1);
        Assert.assertTrue(sink1.getEnteredCars().get(6).getStopCount() <= 1);

    }

}
