package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
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
        IStreetSection streetSectionMock = mock(StreetSection.class);
        route.addSection(streetSectionMock);

        ICar car = new Car(length, driverBehaviour, route, getPrepareModel(), "description", false);
        Assert.assertNotNull(car);

        //test attributes
        assertEquals(driverBehaviour, car.getDriverBehaviour());
        assertEquals(route, car.getRoute());
        assertEquals(length, car.getLength(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
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

        IStreetSection streetSectionMock = mock(StreetSection.class);
        route.addSection(streetSectionMock);

        RoundaboutSimulationModel model = new RoundaboutSimulationModel(null, "", false, false);
        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);

        return new Car(length, driverBehaviour, route, model, "description", false);
    }

    @Test
    public void getNextSection_currentSectionIsEqualDestination() {
        IRoute route = new Route();

        IStreetSection currentSectionMock = mock(StreetSection.class);
        route.addSection(currentSectionMock);

        IStreetSection destinationMock = mock(StreetSection.class);
        route.addSection(destinationMock);

        IDriverBehaviour driverBehaviourMock = mock(DriverBehaviour.class);
        ICar car = new Car(10, driverBehaviourMock, route, getPrepareModel(), "description", false);

        assertEquals(car.getCurrentSection(), currentSectionMock);
        assertEquals(car.getNextSection(), destinationMock);

        car.traverseToNextSection();

        assertEquals(car.getCurrentSection(), destinationMock);
        assertNull(car.getNextSection());
    }

    @Test
    public void getNextSection_currentSectionNotEqualDestination() {
        IRoute route = new Route();

        IStreetSection currentSectionMock = mock(StreetSection.class);
        route.addSection(currentSectionMock);

        IStreetSection nextSectionMock = mock(StreetSection.class);
        route.addSection(nextSectionMock);

        IStreetSection destinationMock = mock(StreetSection.class);
        route.addSection(destinationMock);

        IDriverBehaviour driverBehaviourMock = mock(DriverBehaviour.class);
        ICar car = new Car(10, driverBehaviourMock, route, getPrepareModel(), "description", false);

        assertEquals(car.getCurrentSection(), currentSectionMock);
        assertNotEquals(car.getCurrentSection(), destinationMock);
    }

    @Test
    public void getTimeToTraverseCurrentSection_isExpectedTime() {
        RoundaboutSimulationModel model = getPrepareModel();
        IStreetSection section = new StreetSection(10.0, null, null, model, "", false);
        IDriverBehaviour driverBehaviour = new DriverBehaviour(5.0, 2.0, 2.0, 1.5);

        IRoute route = new Route();
        route.addSection(section);

        ICar car = new Car(2, driverBehaviour, route, model, "", false);

        Assert.assertEquals(2.0, car.getTimeToTraverseCurrentSection(), 0.0);
    }

    @Test
    public void getTimeToTraverseSection_carIsNotOnThisSection(){
        RoundaboutSimulationModel model = getPrepareModel();
        IStreetSection currentSection = new StreetSection(10.0, null, null, model, "", false);
        IStreetSection otherSection = new StreetSection(20.0, null, null, model, "", false);
        IDriverBehaviour driverBehaviour = new DriverBehaviour(5.0, 2.0, 2.0, 1.5);

        IRoute route = new Route();
        route.addSection(currentSection);

        ICar car = new Car(2, driverBehaviour, route, model, "", false);

        Assert.assertEquals(4, car.getTimeToTraverseSection(otherSection), 0.0);

    }
}
