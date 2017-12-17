package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import desmoj.core.simulator.Experiment;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


/**
 * Created by manue on 12.12.2017.
 */
public class CarTest {

    private RoundaboutSimulationModel getPrepareModel() {
        RoundaboutSimulationModel model = new RoundaboutSimulationModel(null, "", false, false);
        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);

        return model;
    }

    @Test
    public void shouldInitializeCorrectly() {
        double length = 10.0;
        IDriverBehaviour driverBehaviour = new DriverBehaviour(10.0, 2.0, 5.0, 1.5);
        IRoute route = new Route();
        Street streetSectionMock = mock(StreetSection.class);
        route.addSection(streetSectionMock);

        Car car = new Car(getPrepareModel(), "", false);
        ICar iCar = new RoundaboutCar(car, length, driverBehaviour, route);
        Assert.assertNotNull(car);

        //test attributes
        assertEquals(driverBehaviour, iCar.getDriverBehaviour());
        assertEquals(route, iCar.getRoute());
        assertEquals(length, iCar.getLength(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIfRouteIsNull() {
        IDriverBehaviour driverBehaviour = new DriverBehaviour(10.0, 2.0, 5.0, 1.5);

        RoundaboutSimulationModel model = new RoundaboutSimulationModel(null, "", false, false);
        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);

        Car car = new Car(model, "", false);
        new RoundaboutCar(car,10.0, driverBehaviour, null);
    }

    @Test
    public void lastUpdateTimeGreaterThanZero() {
        ICar car = createCar();
        double lastUpdateTime = 20.0;
        car.setLastUpdateTime(lastUpdateTime);
        assertEquals(lastUpdateTime, car.getLastUpdateTime(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIfUpdateTimeLessThanZero() {
        ICar car = createCar();
        double lastUpdateTime = -20.0;
        car.setLastUpdateTime(lastUpdateTime);
        assertEquals(lastUpdateTime, car.getLastUpdateTime(), 0.0);
    }

    private ICar createCar() {
        double length = 10.0;
        IDriverBehaviour driverBehaviour = new DriverBehaviour(10.0, 2.0, 5.0, 1.5);
        IRoute route = new Route();

        Street streetSectionMock = mock(StreetSection.class);
        route.addSection(streetSectionMock);

        RoundaboutSimulationModel model = new RoundaboutSimulationModel(null, "", false, false);
        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);

        Car car = new Car(model, "", false);
        return new RoundaboutCar(car, length, driverBehaviour, route);
    }

    @Test
    public void getNextSection_currentSectionIsEqualDestination() {
        IRoute route = new Route();

        Street currentSectionMock = mock(StreetSection.class);
        route.addSection(currentSectionMock);

        Street destinationMock = mock(StreetSection.class);
        route.addSection(destinationMock);

        IDriverBehaviour driverBehaviourMock = mock(DriverBehaviour.class);
        Car car = new Car(getPrepareModel(), "", false);
        ICar iCar = new RoundaboutCar(car,10, driverBehaviourMock, route);

        assertEquals(iCar.getCurrentSection(), currentSectionMock);
        assertEquals(iCar.getNextSection(), destinationMock);

        iCar.traverseToNextSection();

        assertEquals(iCar.getCurrentSection(), destinationMock);
        assertNull(iCar.getNextSection());
    }

    @Test
    public void getNextSection_currentSectionNotEqualDestination() {
        IRoute route = new Route();

        Street currentSectionMock = mock(StreetSection.class);
        route.addSection(currentSectionMock);

        Street nextSectionMock = mock(StreetSection.class);
        route.addSection(nextSectionMock);

        Street destinationMock = mock(StreetSection.class);
        route.addSection(destinationMock);

        IDriverBehaviour driverBehaviourMock = mock(DriverBehaviour.class);
        Car car = new Car(getPrepareModel(), "", false);
        ICar iCar = new RoundaboutCar(car,10, driverBehaviourMock, route);

        assertEquals(iCar.getCurrentSection(), currentSectionMock);
        assertNotEquals(iCar.getCurrentSection(), destinationMock);
    }

    @Test
    public void getTimeToTraverseCurrentSection_isExpectedTime() {
        RoundaboutSimulationModel model = getPrepareModel();
        Street section = new StreetSection(10.0, model, "", false);
        IDriverBehaviour driverBehaviour = new DriverBehaviour(5.0, 2.0, 2.0, 1.5);

        IRoute route = new Route();
        route.addSection(section);

        Car car = new Car(model, "", false);
        ICar iCar = new RoundaboutCar(car,2, driverBehaviour, route);

        Assert.assertEquals(2.0, iCar.getTimeToTraverseCurrentSection(), 0.0);
    }

    @Test
    public void getTimeToTraverseSection_carIsNotOnThisSection(){
        RoundaboutSimulationModel model = getPrepareModel();
        Street currentSection = new StreetSection(10.0, model, "", false);
        Street otherSection = new StreetSection(20.0, model, "", false);
        IDriverBehaviour driverBehaviour = new DriverBehaviour(5.0, 2.0, 2.0, 1.5);

        IRoute route = new Route();
        route.addSection(currentSection);

        Car car = new Car(model, "", false);
        ICar iCar = new RoundaboutCar(car,2, driverBehaviour, route);

        Assert.assertEquals(4, iCar.getTimeToTraverseSection(otherSection), 0.0);

    }
}
