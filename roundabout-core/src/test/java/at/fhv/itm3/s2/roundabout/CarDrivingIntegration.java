package at.fhv.itm3.s2.roundabout;

import at.fhv.itm3.s2.roundabout.api.entity.*;
import at.fhv.itm3.s2.roundabout.entity.*;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class CarDrivingIntegration {

    private boolean DESMOJ_TRACE = true;

    private Experiment exp;
    private RoundaboutSimulationModel model;
    private StreetSection streetSectionStart;

    private ICar createCar() {
        double length = 6.0;
        IDriverBehaviour driverBehaviour = new DriverBehaviour(3.0, 2.0, 5.0, 1.5);

        IStreetConnector streetConnector = new StreetConnector();
        streetSectionStart = new StreetSection(10.0, null, streetConnector, model, "street section start", DESMOJ_TRACE);
        model.addStartSection((StreetSection) streetSectionStart);
        IStreetSection streetSectionDestination = new StreetSection(10.0, streetConnector, null, model, "street section destination", DESMOJ_TRACE);
        model.addSection((StreetSection) streetSectionDestination);

        IRoute route = new Route();
        route.addSection(streetSectionStart);
        route.addSection(streetSectionDestination);

        return new Car(length, driverBehaviour, route, model, "description", DESMOJ_TRACE);
    }

    private void prepareModel() {
        model = new RoundaboutSimulationModel(null, "", false, DESMOJ_TRACE);
        exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);
    }

    @Before
    public void setUp() {
        prepareModel();
    }

    @Test
    public void carDrivesToDestination() {
        ICar car = createCar();
        streetSectionStart.addCar(car);
        System.out.println("start: " + car.getCurrentSection());

        int secondsInModel = 600;
        TimeInstant stopTime = new TimeInstant(secondsInModel, TimeUnit.SECONDS);
        exp.stop(stopTime); // TODO needed?

        exp.start();

        while (car.getCurrentSection() != car.getDestination()) {
            for (IStreetSection section : car.getRoute().getRoute()) {
                section.updateAllCarsPositions();
            }

            System.out.println("actual: " + car.getCurrentSection());

            if (model.getCurrentTime() == secondsInModel) {
                exp.report();
                exp.finish();
                assertTrue("destination not reached", false);
            }
        }

        System.out.println("end: " + car.getCurrentSection());

        exp.report();
        exp.finish();

        assertTrue("destination reached", true);
    }
}
