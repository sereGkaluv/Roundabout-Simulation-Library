package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class StreetSectionTest {
    @Test
    public void firstCarCouldEnterNextSection() throws Exception {
        IStreetSection streetSectionMock = mock(StreetSection.class);
        when(streetSectionMock.firstCarCouldEnterNextSection()).thenCallRealMethod();

        // first car not on exit point
        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(false);
        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());

        // no car in queue
        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);
        when(streetSectionMock.getFirstCar()).thenReturn(null);
        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());

        // not enough space
        ICar firstCar = mock(Car.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);
        IStreetSection nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(false);
        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());

        // precendence section has car on exit point
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);
        IStreetConnector streetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getPreviousStreetConnector()).thenReturn(streetConnector);
        HashSet<IStreetSection> precendenceSections = new HashSet<>();
        IStreetSection streetSectionOne = mock(StreetSection.class);
        when(streetSectionOne.isFirstCarOnExitPoint()).thenReturn(true);
        IStreetSection streetSectionTwo = mock(StreetSection.class);
        when(streetSectionTwo.isFirstCarOnExitPoint()).thenReturn(false);
        precendenceSections.add(streetSectionMock);
        precendenceSections.add(streetSectionOne);
        precendenceSections.add(streetSectionTwo);
        when(streetConnector.getPreviousSections()).thenReturn(precendenceSections);
        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());

        // everything ok
        when(streetSectionOne.isFirstCarOnExitPoint()).thenReturn(false);
        assertTrue(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void isEnoughSpace() throws Exception {
        IStreetSection streetSectionMock = mock(StreetSection.class);
        ICar car = mock(Car.class);
        when(car.getLength()).thenReturn(4.5);
        HashMap<ICar, Double> carPositions = new HashMap<>();
        carPositions.put(car, 3.5);
        when(streetSectionMock.getCarPositions()).thenReturn(carPositions);
        when(streetSectionMock.getLastCar()).thenReturn(car);
        when(streetSectionMock.isEnoughSpace(car.getLength())).thenCallRealMethod();

        double streetSectionLengthBigger = 10.0;
        when(streetSectionMock.getLength()).thenReturn(streetSectionLengthBigger);
        assertTrue(streetSectionMock.isEnoughSpace(car.getLength()));

        double streetSectionLengthEquals = 8.0;
        when(streetSectionMock.getLength()).thenReturn(streetSectionLengthEquals);
        assertFalse(streetSectionMock.isEnoughSpace(car.getLength()));

        double streetSectionLengthSmaller = 6.0;
        when(streetSectionMock.getLength()).thenReturn(streetSectionLengthSmaller);
        assertFalse(streetSectionMock.isEnoughSpace(car.getLength()));

    }

    @Test
    public void moveFirstCarToNextSection_firstCarEqualNull() throws Exception {
        // if firstCar is null, the method getNextStreetSection should not be called
        IStreetSection streetSectionMock = mock(StreetSection.class);
        ICar firstCarMock = mock(Car.class);
        when(streetSectionMock.removeFirstCar()).thenReturn(null);
        doCallRealMethod().when(streetSectionMock).moveFirstCarToNextSection();
        streetSectionMock.moveFirstCarToNextSection();
        verify(firstCarMock, times(0)).getNextStreetSection();
    }

    @Test
    public void moveFirstCarToNextSection_currentSectionIsEqualDestination() throws Exception {
        // if currentSection (=this) is the same as destination of the car
        // the method getNextStreetSection should not be called
        IStreetSection streetSectionMock = mock(StreetSection.class);
        ICar firstCarMock = mock(Car.class);
        when(streetSectionMock.removeFirstCar()).thenReturn(firstCarMock);
        when(firstCarMock.getDestination()).thenReturn(streetSectionMock);
        doCallRealMethod().when(streetSectionMock).moveFirstCarToNextSection();
        streetSectionMock.moveFirstCarToNextSection();
        verify(firstCarMock, times(0)).getNextStreetSection();
    }

    @Test
    public void moveFirstCarToNextSection_currentSectionIsNotEqualDestination() throws Exception {
        // if currentSection (=this) is not the same as destination of the car
        // the method getNextStreetSection should be called once
        IStreetSection streetSectionMock = mock(StreetSection.class);
        IStreetSection destinationMock = mock(StreetSection.class);
        IStreetSection nextSectionMock = mock(StreetSection.class);
        ICar firstCarMock = mock(Car.class);
        when(streetSectionMock.removeFirstCar()).thenReturn(firstCarMock);
        when(firstCarMock.getDestination()).thenReturn(destinationMock);
        when(firstCarMock.getNextStreetSection()).thenReturn(nextSectionMock);
        doCallRealMethod().when(streetSectionMock).moveFirstCarToNextSection();
        streetSectionMock.moveFirstCarToNextSection();
        verify(firstCarMock, times(1)).getNextStreetSection();
    }

}