package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import desmoj.core.simulator.Experiment;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;


/**
 * Created by manue on 12.12.2017.
 */
public class CarTest {

    @Test
    public void shouldInitializeCorrectly() {
        double length = 10.0;
        IDriverBehaviour driverBehaviour = new DriverBehaviour(10.0, 2.0,5.0, 1.5);
        IRoute route = new Route();
        IStreetSection streetSectionMock = mock(StreetSection.class);
        route.addSection(streetSectionMock);

        RoundaboutSimulationModel model = new RoundaboutSimulationModel(null, "", false, false);
        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);

        ICar car = new Car(length, driverBehaviour, route, model, "description", false);
        Assert.assertNotNull(car);

        //test attributes
        Assert.assertEquals(driverBehaviour, car.getDriverBehaviour());
        Assert.assertEquals(route, car.getRoute());
        Assert.assertEquals(length, car.getLength(), 0.0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIfRouteIsNull() {
        IDriverBehaviour driverBehaviour = new DriverBehaviour(10.0, 2.0, 5.0, 1.5);

        RoundaboutSimulationModel model = new RoundaboutSimulationModel(null, "", false, false);
        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);

        new Car(10.0, driverBehaviour, null, model, "description", false);
    }

    @Test
    public void lastUpdateTimeGreaterThanZero() {
        ICar car = createCar();
        double lastUpdateTime = 20.0;
        car.setLastUpdateTime(lastUpdateTime);
        Assert.assertEquals(lastUpdateTime, car.getLastUpdateTime(), 0.0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIfUpdateTimeLessThanZero() {
        ICar car = createCar();
        double lastUpdateTime = -20.0;
        car.setLastUpdateTime(lastUpdateTime);
        Assert.assertEquals(lastUpdateTime, car.getLastUpdateTime(), 0.0);
    }

    private ICar createCar() {
        double length = 10.0;
        IDriverBehaviour driverBehaviour = new DriverBehaviour(10.0, 2.0,5.0, 1.5);
        IRoute route = new Route();
        IStreetSection streetSectionMock = mock(StreetSection.class);
        route.addSection(streetSectionMock);

        RoundaboutSimulationModel model = new RoundaboutSimulationModel(null, "", false, false);
        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);

        return new Car(length, driverBehaviour, route, model, "description", false);
    }
}
