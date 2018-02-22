package at.fhv.itm3.s2.roundabout.integration;

import at.fhv.itm3.s2.roundabout.entity.ModelStructure;
import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.*;
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
        model = new RoundaboutSimulationModel(null, "", false, false, 0.5, 0.5);
        exp = new Experiment("RoundaboutSimulationModel Experiment");
        Experiment.setReferenceUnit(TimeUnit.SECONDS);
        model.connectToExperiment(exp);
        model.registerModelStructure(new ModelStructure(model));
        exp.setShowProgressBar(false);
        CarController.clear();
    }

    @Test
    public void twoStreetSectionsTwoCars_noStops() {

        exp.stop(new TimeInstant(60, model.getModelTimeUnit()));

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
            // ! - We can not predict how cars will be generated anymore (after car generation event delay was randomised)
            Assert.assertFalse(car.isWaiting());
        }
    }

    @Test
    public void precedence_oneCarStaysOnTrack_oneCarWantsToChangeTrackAndHasToGivePrecedence_oneStop() {

        exp.stop(new TimeInstant(60, model.getModelTimeUnit()));

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
        // ! - We can not predict how cars will be generated anymore (after car generation event delay was randomised)

        List<ICar> cars = sink1.getEnteredCars();
        Assert.assertFalse(cars.get(0).isWaiting());
        Assert.assertFalse(cars.get(1).isWaiting());

        // Check if cars has leaved the system.
        Assert.assertTrue(CarController.getICars().isEmpty());
    }

    @Test
    public void precedence_fourCarStaysOnTrack_fourCarWantsToChangeTrackAndHasToGivePrecedence_maxOneStopPerCar() {

        exp.stop(new TimeInstant(100, model.getModelTimeUnit()));

        RouteGeneratorMock routeGeneratorMock = new RouteGeneratorMock(model);

        IRoute route1 = routeGeneratorMock.getRoute(RouteType.STOPS_FOUR_CARS_CHANGE_TRACK);
        AbstractSource source1 = route1.getSource();

        IRoute route2 = routeGeneratorMock.getRoute(RouteType.STOPS_FOUR_CARS_STAY_ON_TRACK);
        AbstractSource source2 = route2.getSource();

        // start generating cars simultaneously so one have to give precedence to another
        source1.startGeneratingCars(0.0);
        source2.startGeneratingCars(0.0);

        RoundaboutSinkMock sink1 = (RoundaboutSinkMock) route1.getSink();

        exp.start();
        exp.finish();

        Assert.assertEquals(8, sink1.getNrOfEnteredCars());
        // ! - We can not predict how cars will be generated anymore (after car generation event delay was randomised)
    }
}
