package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import desmoj.core.simulator.Experiment;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by manue on 12.12.2017.
 */
public class RoundaboutCarTest {

    private RoundaboutSimulationModel getPreparedModel() {
        RoundaboutSimulationModel model = new RoundaboutSimulationModel(null, "", false, false, 3.5, 10.0);
        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);

        return model;
    }

    @Test
    public void shouldInitializeCorrectly() {
        double length = 10.0;
        IDriverBehaviour driverBehaviour = new DriverBehaviour(10.0, 2.0, 5.0, 1.5, 1);
        IRoute route = new Route();
        Street streetSectionMock = mock(StreetSection.class);
        route.addSection(streetSectionMock);

        RoundaboutSimulationModel model = getPreparedModel();
        Car car = new Car(model, "", false);
        ICar iCar = new RoundaboutCar(model, length, car, driverBehaviour, route);
        Assert.assertNotNull(car);

        //test attributes
        assertEquals(driverBehaviour, iCar.getDriverBehaviour());
        assertEquals(route, iCar.getRoute());
        assertEquals(length, iCar.getLength(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIfRouteIsNull() {
        IDriverBehaviour driverBehaviour = new DriverBehaviour(10.0, 2.0, 5.0, 1.5, 1);

        RoundaboutSimulationModel model = new RoundaboutSimulationModel(null, "", false, false, 3.5, 10.0);
        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);

        Car car = new Car(model, "", false);
        new RoundaboutCar(model, 10, car, driverBehaviour, null);
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
        IDriverBehaviour driverBehaviour = new DriverBehaviour(10.0, 2.0, 5.0, 1.5, 1);
        IRoute route = new Route();

        Street streetSectionMock = mock(StreetSection.class);
        route.addSection(streetSectionMock);

        RoundaboutSimulationModel model = new RoundaboutSimulationModel(null, "", false, false, 3.5, 10.0);
        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);

        Car car = new Car(model, "", false);
        return new RoundaboutCar(model, length, car, driverBehaviour, route);
    }

    @Test
    public void getNextSection_currentSectionIsEqualDestination() {
        IRoute route = new Route();

        Street currentSectionMock = mock(StreetSection.class);
        route.addSection(currentSectionMock);

        Street destinationMock = mock(StreetSection.class);
        route.addSection(destinationMock);

        IDriverBehaviour driverBehaviourMock = mock(DriverBehaviour.class);
        RoundaboutSimulationModel model = getPreparedModel();
        Car car = new Car(model, "", false);
        ICar iCar = new RoundaboutCar(model, 10, car, driverBehaviourMock, route);

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
        RoundaboutSimulationModel model = getPreparedModel();
        Car car = new Car(model, "", false);
        ICar iCar = new RoundaboutCar(model, 10, car, driverBehaviourMock, route);

        assertEquals(iCar.getCurrentSection(), currentSectionMock);
        assertNotEquals(iCar.getCurrentSection(), destinationMock);
    }

    @Test
    public void getTimeToTraverseCurrentSection_isExpectedTime() {
        RoundaboutSimulationModel model = getPreparedModel();
        Street section = new StreetSection(10.0, model, "", false);
        IDriverBehaviour driverBehaviour = new DriverBehaviour(5.0, 2.0, 2.0, 1.5, 1);

        IRoute route = new Route();
        route.addSection(section);

        Car car = new Car(model, "", false);
        ICar iCar = new RoundaboutCar(model, 10, car, driverBehaviour, route);

        Assert.assertEquals(2.0, iCar.getTimeToTraverseCurrentSection(), 0.0);
    }

    @Test
    public void getTimeToTraverseSection_carIsNotOnThisSection() {
        RoundaboutSimulationModel model = getPreparedModel();
        Street currentSection = new StreetSection(10.0, model, "", false);
        Street otherSection = new StreetSection(20.0, model, "", false);
        IDriverBehaviour driverBehaviour = new DriverBehaviour(5.0, 2.0, 2.0, 1.5, 1);

        IRoute route = new Route();
        route.addSection(currentSection);

        Car car = new Car(model, "", false);
        ICar iCar = new RoundaboutCar(model, 10, car, driverBehaviour, route);

        Assert.assertEquals(4, iCar.getTimeToTraverseSection(otherSection), 0.0);

    }

    @Test
    public void getTransitionTime_noJam() {
        RoundaboutSimulationModel modelMock = mock(RoundaboutSimulationModel.class);
        when(modelMock.getCurrentTime()).thenReturn(5.0);

        Car oldCarMock = mock(Car.class);
        when(oldCarMock.getModel()).thenReturn(modelMock);

        RoundaboutCar carMock = mock(RoundaboutCar.class);
        when(carMock.getTransitionTime()).thenCallRealMethod();
        when(carMock.getOldImplementationCar()).thenReturn(oldCarMock);

        when(carMock.getTimeToTraverseCurrentSection()).thenReturn(8.0);
        when(carMock.getLastUpdateTime()).thenReturn(1.0);

        assertEquals(8.0, carMock.getTransitionTime(), 0.0);
    }

    @Test
    public void getTransitionTime_withJam() {
        RoundaboutSimulationModel modelMock = mock(RoundaboutSimulationModel.class);
        when(modelMock.getCurrentTime()).thenReturn(5.0);
        when(modelMock.getStandardCarAccelerationTime()).thenReturn(2.0);

        Car oldCarMock = mock(Car.class);
        when(oldCarMock.getModel()).thenReturn(modelMock);

        RoundaboutCar carMock = mock(RoundaboutCar.class);
        when(carMock.getTransitionTime()).thenCallRealMethod();
        when(carMock.getOldImplementationCar()).thenReturn(oldCarMock);

        DriverBehaviour driverBehaviourMock = mock(DriverBehaviour.class);
        when(carMock.getDriverBehaviour()).thenReturn(driverBehaviourMock);
        when(driverBehaviourMock.getAccelerationFactor()).thenReturn(2.0);


        when(carMock.getTimeToTraverseCurrentSection()).thenReturn(3.0);
        when(carMock.getLastUpdateTime()).thenReturn(1.0);

        assertEquals(7.0, carMock.getTransitionTime(), 0.0);
    }
}
