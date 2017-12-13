package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


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

        ICar car = new Car(length, driverBehaviour, route);
        Assert.assertNotNull(car);

        //test attributes
        assertEquals(driverBehaviour, car.getDriverBehaviour());
        assertEquals(route, car.getRoute());
        assertEquals(length, car.getLength(), 0.0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIfRouteIsNull() {
        IDriverBehaviour driverBehaviour = new DriverBehaviour(10.0, 2.0, 5.0, 1.5);
        new Car(10.0, driverBehaviour, null);
    }

    @Test
    public void lastUpdateTimeGreaterThanZero() {
        ICar car = createCar();
        double lastUpdateTime = 20.0;
        car.setLastUpdateTime(lastUpdateTime);
        assertEquals(lastUpdateTime, car.getLastUpdateTime(), 0.0);
    }

    @Test (expected = IllegalArgumentException.class)
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

        return new Car(length, driverBehaviour, route);
    }

    @Test
    public void getNextSection_currentSectionIsEqualDestination() {
        IRoute routeMock = new Route();

        IStreetSection currentSectionMock = mock(StreetSection.class);
        routeMock.addSection(currentSectionMock);

        IStreetSection destinationMock = mock(StreetSection.class);
        routeMock.addSection(destinationMock);

        IDriverBehaviour driverBehaviourMock = mock(DriverBehaviour.class);
        ICar car = new Car(10, driverBehaviourMock, routeMock);

        assertEquals(car.getCurrentSection(), currentSectionMock);
        assertEquals(car.getNextSection(), destinationMock);

        car.traverseToNextSection();

        assertEquals(car.getCurrentSection(), destinationMock);
        assertNull(car.getNextSection());
    }

    @Test
    public void getNextSection_currentSectionNotEqualDestination() {
        IRoute routeMock = new Route();

        IStreetSection currentSectionMock = mock(StreetSection.class);
        routeMock.addSection(currentSectionMock);

        IStreetSection nextSectionMock = mock(StreetSection.class);
        routeMock.addSection(nextSectionMock);

        IStreetSection destinationMock = mock(StreetSection.class);
        routeMock.addSection(destinationMock);

        IDriverBehaviour driverBehaviourMock = mock(DriverBehaviour.class);
        ICar car = new Car(10, driverBehaviourMock, routeMock);

        assertEquals(car.getCurrentSection(), currentSectionMock);
        assertNotEquals(car.getCurrentSection(), destinationMock);
    }
}
