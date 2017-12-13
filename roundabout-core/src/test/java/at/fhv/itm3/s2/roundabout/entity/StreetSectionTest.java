package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StreetSectionTest {

    private IStreetSection prepareStreetSectionCarCouldEnterNextSectionMock() {
        IStreetSection streetSectionMock = mock(StreetSection.class);
        when(streetSectionMock.firstCarCouldEnterNextSection()).thenCallRealMethod();

        return streetSectionMock;
    }

    @Test
    public void firstCarCouldEnterNextSection_firstCarNotOnExitPoint() {
        IStreetSection streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(false);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void firstCarCouldEnterNextSection_noCarInQueue() {
        IStreetSection streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);
        when(streetSectionMock.getFirstCar()).thenReturn(null);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void firstCarCouldEnterNextSection_notEnoughSpace() {
        IStreetSection streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(Car.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        IStreetSection nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(false);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void firstCarCouldEnterNextSection_precedenceSectionHasCarOnExitPoint() {
        IStreetSection streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(Car.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        IStreetSection nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
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
    }

    @Test
    public void firstCarCouldEnterNextSection_couldEnterNextSection() {
        IStreetSection streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(Car.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        IStreetSection nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector streetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getPreviousStreetConnector()).thenReturn(streetConnector);

        HashSet<IStreetSection> precendenceSections = new HashSet<>();

        IStreetSection streetSectionOne = mock(StreetSection.class);
        when(streetSectionOne.isFirstCarOnExitPoint()).thenReturn(false);
        IStreetSection streetSectionTwo = mock(StreetSection.class);
        when(streetSectionTwo.isFirstCarOnExitPoint()).thenReturn(false);

        precendenceSections.add(streetSectionMock);
        precendenceSections.add(streetSectionOne);
        precendenceSections.add(streetSectionTwo);
        when(streetConnector.getPreviousSections()).thenReturn(precendenceSections);

        assertTrue(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void isEnoughSpace_spaceBiggerThenCar() {
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
    }

    @Test
    public void isEnoughSpace_spaceEqualsCar() {
        IStreetSection streetSectionMock = mock(StreetSection.class);

        ICar car = mock(Car.class);
        when(car.getLength()).thenReturn(4.5);

        HashMap<ICar, Double> carPositions = new HashMap<>();
        carPositions.put(car, 3.5);

        when(streetSectionMock.getCarPositions()).thenReturn(carPositions);
        when(streetSectionMock.getLastCar()).thenReturn(car);
        when(streetSectionMock.isEnoughSpace(car.getLength())).thenCallRealMethod();

        double streetSectionLengthEquals = 8.0;
        when(streetSectionMock.getLength()).thenReturn(streetSectionLengthEquals);

        assertFalse(streetSectionMock.isEnoughSpace(car.getLength()));
    }

    @Test
    public void isEnoughSpace_spaceSmallerThenCar() {
        IStreetSection streetSectionMock = mock(StreetSection.class);

        ICar car = mock(Car.class);
        when(car.getLength()).thenReturn(4.5);

        HashMap<ICar, Double> carPositions = new HashMap<>();
        carPositions.put(car, 3.5);

        when(streetSectionMock.getCarPositions()).thenReturn(carPositions);
        when(streetSectionMock.getLastCar()).thenReturn(car);
        when(streetSectionMock.isEnoughSpace(car.getLength())).thenCallRealMethod();

        double streetSectionLengthSmaller = 6.0;
        when(streetSectionMock.getLength()).thenReturn(streetSectionLengthSmaller);

        assertFalse(streetSectionMock.isEnoughSpace(car.getLength()));
    }

}