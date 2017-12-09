package at.fhv.itm3.s2.roundabout.entity;


import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CarTest {
    @Test
    public void getNextSection_currentSectionIsNull() throws Exception {
        ICar carMock = mock(Car.class);
        carMock.setCurrentSection(null);
        when(carMock.getNextSection()).thenCallRealMethod();
        assertEquals(carMock.getNextSection(), null);
    }

    @Test
    public void getNextSection_currentSectionIsEqualDestination() throws Exception {
        Car carMock = mock(Car.class);
        IStreetSection currentSectionMock = mock(StreetSection.class);
        IStreetSection destinationMock = currentSectionMock;
        carMock.setCurrentSection(currentSectionMock);
        IRoute routeMock = mock(Route.class);
        routeMock.addSection(destinationMock);
        when(carMock.getNextSection()).thenCallRealMethod();
        assertEquals(carMock.getNextSection(), null);
    }

    @Test
    public void GetNextSection_currentSectionNotEqualDestination() throws Exception {

        IRoute routeMock = mock(Route.class);
        IStreetSection currentSectionMock = mock(StreetSection.class);
        IStreetSection nextSectionMock = mock(StreetSection.class);
        IStreetSection destinationMock = mock(StreetSection.class);
        routeMock.addSection(currentSectionMock);
        routeMock.addSection(nextSectionMock);
        routeMock.addSection(destinationMock);
        IDriverBehaviour driverBehaviourMock = mock(DriverBehaviour.class);
        ICar carMock = new Car(10, driverBehaviourMock, routeMock);
        carMock.setCurrentSection(currentSectionMock);
        carMock.setRoute(routeMock);
        when(carMock.getNextSection()).thenCallRealMethod();

    }
}
