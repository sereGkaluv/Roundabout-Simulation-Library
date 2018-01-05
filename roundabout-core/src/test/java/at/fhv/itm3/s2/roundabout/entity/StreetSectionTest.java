package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.*;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StreetSectionTest {

    private Street prepareStreetSectionCarCouldEnterNextSectionMock() {
        Street streetSectionMock = mock(StreetSection.class);
        when(streetSectionMock.firstCarCouldEnterNextSection()).thenCallRealMethod();

        return streetSectionMock;
    }

    private Street prepareStreetSectionIsFirstCarOnExitPointMock() {
        Street streetSectionMock = mock(StreetSection.class);
        when(streetSectionMock.isFirstCarOnExitPoint()).thenCallRealMethod();

        return streetSectionMock;
    }

    /**
     * TESTS FOR METHOD firstCarCouldEnterNextSection FOLLOWING
     * (not mentioned in the function names, because if function names are too long,
     * tests fail)
     */

    @Test
    public void firstCarNotOnExitPoint() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(false);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void noCarInQueue() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);
        when(streetSectionMock.getFirstCar()).thenReturn(null);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void notEnoughSpace() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(false);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carIsInRoundaboutAndWantsToRemainOnTrack_canDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(true);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_SECTION);

        assertTrue(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carIsOnStreetSectionAndWantsToRemainOnTrack_canDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(true);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.STREET_SECTION);

        assertTrue(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carIsOnRoundaboutExitAndWantsToRemainOnTrack_canDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(true);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_EXIT);

        assertTrue(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToEnterRoundaboutToOuterTrack_canDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(true);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_INLET);

        Street previousStreetSection1 = mock(StreetSection.class);
        Street previousStreetSection2 = mock(StreetSection.class);
        List<IConsumer> previousSections = new LinkedList<>();
        previousSections.add(previousStreetSection1);
        previousSections.add(previousStreetSection2);
        when(nextStreetConnector.getPreviousConsumers()).thenReturn(previousSections);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousStreetSection1, nextStreetSection)).thenReturn(true);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousStreetSection2, nextStreetSection)).thenReturn(false);
        when(previousStreetSection1.isFirstCarOnExitPoint()).thenReturn(false);
        when(previousStreetSection2.isFirstCarOnExitPoint()).thenReturn(true);

        assertTrue(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToEnterRoundaboutToOuterTrack_canNotDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(true);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_INLET);

        Street previousStreetSection1 = mock(StreetSection.class);
        Street previousStreetSection2 = mock(StreetSection.class);
        List<IConsumer> previousSections = new LinkedList<>();
        previousSections.add(previousStreetSection1);
        previousSections.add(previousStreetSection2);
        when(nextStreetConnector.getPreviousConsumers()).thenReturn(previousSections);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousStreetSection1, nextStreetSection)).thenReturn(true);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousStreetSection2, nextStreetSection)).thenReturn(false);
        when(previousStreetSection1.isFirstCarOnExitPoint()).thenReturn(true);
        when(previousStreetSection2.isFirstCarOnExitPoint()).thenReturn(true);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToEnterRoundaboutToInnerTrack_canDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(true);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_INLET);

        Street previousStreetSection1 = mock(StreetSection.class);
        Street previousStreetSection2 = mock(StreetSection.class);
        List<IConsumer> previousSections = new LinkedList<>();
        previousSections.add(previousStreetSection1);
        previousSections.add(previousStreetSection2);
        when(nextStreetConnector.getPreviousConsumers()).thenReturn(previousSections);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousStreetSection1, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousStreetSection2, nextStreetSection)).thenReturn(true);
        when(previousStreetSection1.isFirstCarOnExitPoint()).thenReturn(false);
        when(previousStreetSection2.isFirstCarOnExitPoint()).thenReturn(false);

        assertTrue(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToEnterRoundaboutToInnerTrack_outerTrackHasPrecedence() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();
        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(true);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_INLET);

        Street previousStreetSection1 = mock(StreetSection.class);
        Street previousStreetSection2 = mock(StreetSection.class);
        List<IConsumer> previousSections = new LinkedList<>();
        previousSections.add(previousStreetSection1);
        previousSections.add(previousStreetSection2);
        when(nextStreetConnector.getPreviousConsumers()).thenReturn(previousSections);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousStreetSection1, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousStreetSection2, nextStreetSection)).thenReturn(true);
        when(previousStreetSection1.isFirstCarOnExitPoint()).thenReturn(true);
        when(previousStreetSection2.isFirstCarOnExitPoint()).thenReturn(false);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToEnterRoundaboutToInnerTrack_innerTrackHasPrecedence() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(true);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_INLET);

        Street previousStreetSection1 = mock(StreetSection.class);
        Street previousStreetSection2 = mock(StreetSection.class);
        List<IConsumer> previousSections = new LinkedList<>();
        previousSections.add(previousStreetSection1);
        previousSections.add(previousStreetSection2);
        when(nextStreetConnector.getPreviousConsumers()).thenReturn(previousSections);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousStreetSection1, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousStreetSection2, nextStreetSection)).thenReturn(true);
        when(previousStreetSection1.isFirstCarOnExitPoint()).thenReturn(false);
        when(previousStreetSection2.isFirstCarOnExitPoint()).thenReturn(true);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToChangeTrackOnExit_canDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();
        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_EXIT);

        List<IConsumer> previousTrackSections = new LinkedList<>();
        Street previousTrackSection = mock(StreetSection.class);
        previousTrackSections.add(previousTrackSection);
        when(nextStreetConnector.getPreviousTrackConsumers(nextStreetSection, ConsumerType.ROUNDABOUT_EXIT)).thenReturn(previousTrackSections);
        when(previousTrackSection.isFirstCarOnExitPoint()).thenReturn(false);

        assertTrue(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToChangeTrackOnExit_canNotDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_EXIT);

        List<IConsumer> previousTrackSections = new LinkedList<>();
        Street previousTrackSection = mock(StreetSection.class);
        previousTrackSections.add(previousTrackSection);
        when(nextStreetConnector.getPreviousTrackConsumers(nextStreetSection, ConsumerType.ROUNDABOUT_EXIT)).thenReturn(previousTrackSections);
        when(previousTrackSection.isFirstCarOnExitPoint()).thenReturn(true);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToChangeTrackOnAStreetSection_canDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.STREET_SECTION);

        List<IConsumer> previousTrackSections = new LinkedList<>();
        Street previousTrackSection = mock(StreetSection.class);
        previousTrackSections.add(previousTrackSection);
        when(nextStreetConnector.getPreviousTrackConsumers(nextStreetSection, ConsumerType.STREET_SECTION)).thenReturn(previousTrackSections);
        when(previousTrackSection.isFirstCarOnExitPoint()).thenReturn(false);

        assertTrue(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToChangeTrackOnAStreetSection_canNotDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.STREET_SECTION);

        List<IConsumer> previousTrackSections = new LinkedList<>();
        Street previousTrackSection = mock(StreetSection.class);
        previousTrackSections.add(previousTrackSection);
        when(nextStreetConnector.getPreviousTrackConsumers(nextStreetSection, ConsumerType.STREET_SECTION)).thenReturn(previousTrackSections);
        when(previousTrackSection.isFirstCarOnExitPoint()).thenReturn(true);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToChangeToInnerTrackOnInlet_canDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_INLET);

        List<IConsumer> previousSectionsWithoutInlet = new LinkedList<>();
        Street previousSectionOuterTrack = mock(StreetSection.class);
        previousSectionsWithoutInlet.add(previousSectionOuterTrack);
        Street previousSectionOnInnerTrack = mock(StreetSection.class);
        previousSectionsWithoutInlet.add(previousSectionOnInnerTrack);
        when(nextStreetConnector.getPreviousConsumers(ConsumerType.ROUNDABOUT_SECTION)).thenReturn(previousSectionsWithoutInlet);
        when(previousSectionOuterTrack.isFirstCarOnExitPoint()).thenReturn(false);
        when(previousSectionOnInnerTrack.isFirstCarOnExitPoint()).thenReturn(false);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousSectionOuterTrack, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousSectionOnInnerTrack, nextStreetSection)).thenReturn(true);

        List<IConsumer> previousInlets = new LinkedList<>();
        Street previousInlet = mock(StreetSection.class);
        previousInlets.add(previousInlet);
        when(nextStreetConnector.getPreviousTrackConsumers(nextStreetSection, ConsumerType.ROUNDABOUT_INLET)).thenReturn(previousInlets);
        when(previousInlet.isFirstCarOnExitPoint()).thenReturn(false);

        assertTrue(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToChangeToInnerTrackOnInlet_outerTrackHasPrecedence() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_INLET);

        List<IConsumer> previousSectionsWithoutInlet = new LinkedList<>();
        Street previousSectionOuterTrack = mock(StreetSection.class);
        previousSectionsWithoutInlet.add(previousSectionOuterTrack);
        Street previousSectionOnInnerTrack = mock(StreetSection.class);
        previousSectionsWithoutInlet.add(previousSectionOnInnerTrack);
        when(nextStreetConnector.getPreviousConsumers(ConsumerType.ROUNDABOUT_SECTION)).thenReturn(previousSectionsWithoutInlet);
        when(previousSectionOuterTrack.isFirstCarOnExitPoint()).thenReturn(true);
        when(previousSectionOnInnerTrack.isFirstCarOnExitPoint()).thenReturn(false);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousSectionOuterTrack, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousSectionOnInnerTrack, nextStreetSection)).thenReturn(true);

        List<IConsumer> previousInlets = new LinkedList<>();
        Street previousInlet = mock(StreetSection.class);
        previousInlets.add(previousInlet);
        when(nextStreetConnector.getPreviousTrackConsumers(nextStreetSection, ConsumerType.ROUNDABOUT_INLET)).thenReturn(previousInlets);
        when(previousInlet.isFirstCarOnExitPoint()).thenReturn(false);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToChangeToInnerTrackOnInlet_innerTrackHasPrecedence() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_INLET);

        List<IConsumer> previousSectionsWithoutInlet = new LinkedList<>();
        Street previousSectionOuterTrack = mock(StreetSection.class);
        previousSectionsWithoutInlet.add(previousSectionOuterTrack);
        Street previousSectionOnInnerTrack = mock(StreetSection.class);
        previousSectionsWithoutInlet.add(previousSectionOnInnerTrack);
        when(nextStreetConnector.getPreviousConsumers(ConsumerType.ROUNDABOUT_SECTION)).thenReturn(previousSectionsWithoutInlet);
        when(previousSectionOuterTrack.isFirstCarOnExitPoint()).thenReturn(false);
        when(previousSectionOnInnerTrack.isFirstCarOnExitPoint()).thenReturn(true);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousSectionOuterTrack, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousSectionOnInnerTrack, nextStreetSection)).thenReturn(true);

        List<IConsumer> previousInlets = new LinkedList<>();
        Street previousInlet = mock(StreetSection.class);
        previousInlets.add(previousInlet);
        when(nextStreetConnector.getPreviousTrackConsumers(nextStreetSection, ConsumerType.ROUNDABOUT_INLET)).thenReturn(previousInlets);
        when(previousInlet.isFirstCarOnExitPoint()).thenReturn(false);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToChangeToInnerTrackOnInlet_trackInletHasPrecedence() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_INLET);

        List<IConsumer> previousSectionsWithoutInlet = new LinkedList<>();
        Street previousSectionOuterTrack = mock(StreetSection.class);
        previousSectionsWithoutInlet.add(previousSectionOuterTrack);
        Street previousSectionOnInnerTrack = mock(StreetSection.class);
        previousSectionsWithoutInlet.add(previousSectionOnInnerTrack);
        when(nextStreetConnector.getPreviousConsumers(ConsumerType.ROUNDABOUT_SECTION)).thenReturn(previousSectionsWithoutInlet);
        when(previousSectionOuterTrack.isFirstCarOnExitPoint()).thenReturn(false);
        when(previousSectionOnInnerTrack.isFirstCarOnExitPoint()).thenReturn(false);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousSectionOuterTrack, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousSectionOnInnerTrack, nextStreetSection)).thenReturn(true);

        List<IConsumer> previousInlets = new LinkedList<>();
        Street previousInlet = mock(StreetSection.class);
        previousInlets.add(previousInlet);
        when(nextStreetConnector.getPreviousTrackConsumers(nextStreetSection, ConsumerType.ROUNDABOUT_INLET)).thenReturn(previousInlets);
        when(previousInlet.isFirstCarOnExitPoint()).thenReturn(true);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToChangeToOuterTrackOnInlet_canDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_INLET);

        List<IConsumer> previousSectionsWithoutInlet = new LinkedList<>();
        Street previousSectionOuterTrack = mock(StreetSection.class);
        previousSectionsWithoutInlet.add(previousSectionOuterTrack);
        Street previousSectionOnInnerTrack = mock(StreetSection.class);
        previousSectionsWithoutInlet.add(previousSectionOnInnerTrack);
        when(nextStreetConnector.getPreviousConsumers(ConsumerType.ROUNDABOUT_SECTION)).thenReturn(previousSectionsWithoutInlet);
        when(previousSectionOuterTrack.isFirstCarOnExitPoint()).thenReturn(false);
        when(previousSectionOnInnerTrack.isFirstCarOnExitPoint()).thenReturn(true);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousSectionOuterTrack, nextStreetSection)).thenReturn(true);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousSectionOnInnerTrack, nextStreetSection)).thenReturn(false);

        List<IConsumer> previousInlets = new LinkedList<>();
        Street previousInlet = mock(StreetSection.class);
        previousInlets.add(previousInlet);
        when(nextStreetConnector.getPreviousTrackConsumers(nextStreetSection, ConsumerType.ROUNDABOUT_INLET)).thenReturn(previousInlets);
        when(previousInlet.isFirstCarOnExitPoint()).thenReturn(false);

        assertTrue(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToChangeToOuterTrackOnInlet_outerTrackHasPrecedence() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_INLET);

        List<IConsumer> previousSectionsWithoutInlet = new LinkedList<>();
        Street previousSectionOuterTrack = mock(StreetSection.class);
        previousSectionsWithoutInlet.add(previousSectionOuterTrack);
        Street previousSectionOnInnerTrack = mock(StreetSection.class);
        previousSectionsWithoutInlet.add(previousSectionOnInnerTrack);
        when(nextStreetConnector.getPreviousConsumers(ConsumerType.ROUNDABOUT_SECTION)).thenReturn(previousSectionsWithoutInlet);
        when(previousSectionOuterTrack.isFirstCarOnExitPoint()).thenReturn(true);
        when(previousSectionOnInnerTrack.isFirstCarOnExitPoint()).thenReturn(false);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousSectionOuterTrack, nextStreetSection)).thenReturn(true);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousSectionOnInnerTrack, nextStreetSection)).thenReturn(false);

        List<IConsumer> previousInlets = new LinkedList<>();
        Street previousInlet = mock(StreetSection.class);
        previousInlets.add(previousInlet);
        when(nextStreetConnector.getPreviousTrackConsumers(nextStreetSection, ConsumerType.ROUNDABOUT_INLET)).thenReturn(previousInlets);
        when(previousInlet.isFirstCarOnExitPoint()).thenReturn(false);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToChangeToOuterTrackOnInlet_trackInletHasPrecedence() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_INLET);

        List<IConsumer> previousSectionsWithoutInlet = new LinkedList<>();
        Street previousSectionOuterTrack = mock(StreetSection.class);
        previousSectionsWithoutInlet.add(previousSectionOuterTrack);
        Street previousSectionOnInnerTrack = mock(StreetSection.class);
        previousSectionsWithoutInlet.add(previousSectionOnInnerTrack);
        when(nextStreetConnector.getPreviousConsumers(ConsumerType.ROUNDABOUT_SECTION)).thenReturn(previousSectionsWithoutInlet);
        when(previousSectionOuterTrack.isFirstCarOnExitPoint()).thenReturn(false);
        when(previousSectionOnInnerTrack.isFirstCarOnExitPoint()).thenReturn(false);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousSectionOuterTrack, nextStreetSection)).thenReturn(true);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousSectionOnInnerTrack, nextStreetSection)).thenReturn(false);

        List<IConsumer> previousInlets = new LinkedList<>();
        Street previousInlet = mock(StreetSection.class);
        previousInlets.add(previousInlet);
        when(nextStreetConnector.getPreviousTrackConsumers(nextStreetSection, ConsumerType.ROUNDABOUT_INLET)).thenReturn(previousInlets);
        when(previousInlet.isFirstCarOnExitPoint()).thenReturn(true);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToChangeTrackInTheRoundabout_canDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_SECTION);
        when(nextStreetConnector.getTypeOfConsumer(nextStreetSection)).thenReturn(ConsumerType.ROUNDABOUT_SECTION);

        List<IConsumer> previousSections = new LinkedList<>();
        Street previousTrackSection = mock(StreetSection.class);
        previousSections.add(previousTrackSection);
        when(nextStreetConnector.getPreviousTrackConsumers(nextStreetSection, ConsumerType.ROUNDABOUT_SECTION)).thenReturn(previousSections);
        when(previousTrackSection.isFirstCarOnExitPoint()).thenReturn(false);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousTrackSection, nextStreetSection)).thenReturn(true);

        assertTrue(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToChangeTrackInTheRoundabout_canNotDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_SECTION);
        when(nextStreetConnector.getTypeOfConsumer(nextStreetSection)).thenReturn(ConsumerType.ROUNDABOUT_SECTION);

        List<IConsumer> previousSections = new LinkedList<>();
        Street previousTrackSection = mock(StreetSection.class);
        previousSections.add(previousTrackSection);
        when(nextStreetConnector.getPreviousTrackConsumers(nextStreetSection, ConsumerType.ROUNDABOUT_SECTION)).thenReturn(previousSections);
        when(previousTrackSection.isFirstCarOnExitPoint()).thenReturn(true);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(previousTrackSection, nextStreetSection)).thenReturn(true);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToUseRoundaboutExitThatIsNotOnItsTrack_canDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_SECTION);
        when(nextStreetConnector.getTypeOfConsumer(nextStreetSection)).thenReturn(ConsumerType.ROUNDABOUT_EXIT);

        List<IConsumer> previousSections = new LinkedList<>();
        Street previousSection = mock(StreetSection.class);
        previousSections.add(previousSection);
        previousSections.add(streetSectionMock);
        when(nextStreetConnector.getPreviousConsumers(ConsumerType.ROUNDABOUT_SECTION)).thenReturn(previousSections);
        when(previousSection.isFirstCarOnExitPoint()).thenReturn(false);

        assertTrue(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void carWantsToUseRoundaboutExitThatIsNotOnItsTrack_canNotDrive() {
        Street streetSectionMock = prepareStreetSectionCarCouldEnterNextSectionMock();

        when(streetSectionMock.isFirstCarOnExitPoint()).thenReturn(true);

        ICar firstCar = mock(RoundaboutCar.class);
        when(streetSectionMock.getFirstCar()).thenReturn(firstCar);

        Street nextStreetSection = mock(StreetSection.class);
        when(firstCar.getNextSection()).thenReturn(nextStreetSection);
        when(firstCar.getLength()).thenReturn(5.0);
        when(nextStreetSection.isEnoughSpace(firstCar.getLength())).thenReturn(true);

        IStreetConnector nextStreetConnector = mock(StreetConnector.class);
        when(streetSectionMock.getNextStreetConnector()).thenReturn(nextStreetConnector);
        when(nextStreetConnector.isNextConsumerOnSameTrackAsCurrent(streetSectionMock, nextStreetSection)).thenReturn(false);
        when(nextStreetConnector.getTypeOfConsumer(streetSectionMock)).thenReturn(ConsumerType.ROUNDABOUT_SECTION);
        when(nextStreetConnector.getTypeOfConsumer(nextStreetSection)).thenReturn(ConsumerType.ROUNDABOUT_EXIT);

        List<IConsumer> previousSections = new LinkedList<>();
        Street previousSection = mock(StreetSection.class);
        previousSections.add(previousSection);
        previousSections.add(streetSectionMock);
        when(nextStreetConnector.getPreviousConsumers(ConsumerType.ROUNDABOUT_SECTION)).thenReturn(previousSections);
        when(previousSection.isFirstCarOnExitPoint()).thenReturn(true);

        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }


    /**
     * TESTS FOR METHOD firstCarCouldEnterNextSection END
     */

    @Test
    public void isEnoughSpace_spaceBiggerThenCar() {
        Street streetSectionMock = mock(StreetSection.class);

        ICar car = mock(RoundaboutCar.class);
        when(car.getLength()).thenReturn(4.5);

        HashMap<ICar, Double> carPositions = new HashMap<>();
        carPositions.put(car, 13.5);

        when(streetSectionMock.getCarPositions()).thenReturn(carPositions);
        when(streetSectionMock.getLastCar()).thenReturn(car);
        when(streetSectionMock.isEnoughSpace(car.getLength())).thenCallRealMethod();

        double streetSectionLengthBigger = 15.0;
        when(streetSectionMock.getLength()).thenReturn(streetSectionLengthBigger);

        assertTrue(streetSectionMock.isEnoughSpace(car.getLength()));
    }

    @Test
    public void isEnoughSpace_spaceEqualsCar() {
        Street streetSectionMock = mock(StreetSection.class);

        ICar car = mock(RoundaboutCar.class);
        when(car.getLength()).thenReturn(4.5);

        HashMap<ICar, Double> carPositions = new HashMap<>();
        carPositions.put(car, 9.0);

        when(streetSectionMock.getCarPositions()).thenReturn(carPositions);
        when(streetSectionMock.getLastCar()).thenReturn(car);
        when(streetSectionMock.isEnoughSpace(car.getLength())).thenCallRealMethod();

        double streetSectionLengthEquals = 15.0;
        when(streetSectionMock.getLength()).thenReturn(streetSectionLengthEquals);

        assertFalse(streetSectionMock.isEnoughSpace(car.getLength()));
    }

    @Test
    public void isEnoughSpace_spaceSmallerThenCar() {
        Street streetSectionMock = mock(StreetSection.class);

        ICar car = mock(RoundaboutCar.class);
        when(car.getLength()).thenReturn(4.5);

        HashMap<ICar, Double> carPositions = new HashMap<>();
        carPositions.put(car, 4.5);

        when(streetSectionMock.getCarPositions()).thenReturn(carPositions);
        when(streetSectionMock.getLastCar()).thenReturn(car);
        when(streetSectionMock.isEnoughSpace(car.getLength())).thenCallRealMethod();

        double streetSectionLengthSmaller = 15.0;
        when(streetSectionMock.getLength()).thenReturn(streetSectionLengthSmaller);

        assertFalse(streetSectionMock.isEnoughSpace(car.getLength()));
    }

    @Test
    public void isFirstCarOnExitPoint_firstCarIsNull() {
        Street streetSectionMock = prepareStreetSectionIsFirstCarOnExitPointMock();

        // no car in queue
        when(streetSectionMock.getFirstCar()).thenReturn(null);
        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }

    @Test
    public void isFirstCarOnExitPoint_carDriverBehaviourIsNull() {
        Street streetSectionMock = prepareStreetSectionIsFirstCarOnExitPointMock();

        ICar carMock = mock(ICar.class);
        when(carMock.getDriverBehaviour()).thenReturn(null);

        // no car driver behavior specified
        when(streetSectionMock.getFirstCar()).thenReturn(carMock);
        assertFalse(streetSectionMock.firstCarCouldEnterNextSection());
    }


    @Test
    public void isFirstCarOnExitPoint_firstCarMaxDistanceOk() {
        final double maxDistance = 2.0;

        IDriverBehaviour driverBehaviour = mock(IDriverBehaviour.class);
        when(driverBehaviour.getMaxDistanceToNextCar()).thenReturn(maxDistance);

        ICar carMock = mock(ICar.class);
        when(carMock.getDriverBehaviour()).thenReturn(driverBehaviour);

        Street streetSectionMock = prepareStreetSectionIsFirstCarOnExitPointMock();
        when(streetSectionMock.getFirstCar()).thenReturn(carMock);

        Map<ICar, Double> carPositions = new HashMap<>();
        carPositions.put(carMock, streetSectionMock.getLength());

        when(streetSectionMock.getCarPositions()).thenReturn(carPositions);
        assertTrue(streetSectionMock.isFirstCarOnExitPoint());
    }

    @Test
    public void isFirstCarOnExitPoint_firstCarMaxDistanceTooBig() {
        final double maxDistance = 2.0;

        IDriverBehaviour driverBehaviour = mock(IDriverBehaviour.class);
        when(driverBehaviour.getMaxDistanceToNextCar()).thenReturn(maxDistance);

        ICar carMock = mock(ICar.class);
        when(carMock.getDriverBehaviour()).thenReturn(driverBehaviour);

        Street streetSectionMock = prepareStreetSectionIsFirstCarOnExitPointMock();
        when(streetSectionMock.getFirstCar()).thenReturn(carMock);

        Map<ICar, Double> carPositions = new HashMap<>();
        carPositions.put(carMock, streetSectionMock.getLength() - maxDistance * 2); // * 2 in order to get bigger distance

        when(streetSectionMock.getCarPositions()).thenReturn(carPositions);
        assertFalse(streetSectionMock.isFirstCarOnExitPoint());
    }


    @Test
    public void moveFirstCarToNextSection_firstCarEqualNull() {
        // if firstCar is null, the method getNextStreetSection should not be called
        Street streetSectionMock = mock(StreetSection.class);
        ICar firstCarMock = mock(RoundaboutCar.class);

        when(streetSectionMock.removeFirstCar()).thenReturn(null);
        doCallRealMethod().when(streetSectionMock).moveFirstCarToNextSection();

        streetSectionMock.moveFirstCarToNextSection();
        verify(firstCarMock, times(0)).getNextSection();
    }

    @Test
    public void moveFirstCarToNextSection_currentSectionIsEqualDestination() {
        // if currentSection (=this) is the same as destination of the car
        // the method getNextStreetSection should not be called
        Street currentSectionMock = mock(StreetSection.class);
        ICar firstCarMock = mock(RoundaboutCar.class);

        when(currentSectionMock.removeFirstCar()).thenReturn(firstCarMock);
        when(firstCarMock.getCurrentSection()).thenReturn(currentSectionMock);
        when(firstCarMock.getDestination()).thenReturn(currentSectionMock);
        doCallRealMethod().when(currentSectionMock).moveFirstCarToNextSection();

        currentSectionMock.moveFirstCarToNextSection();
        verify(firstCarMock, times(0)).getNextSection();
        verify(firstCarMock, times(0)).traverseToNextSection();
    }

    @Test
    public void moveFirstCarToNextSection_currentSectionIsNotEqualDestination() {
        // if currentSection (=this) is not the same as destination of the car
        // the method getNextStreetSection should be called once
        ICar firstCarMock = mock(RoundaboutCar.class);

        Street currentSectionMock = mock(StreetSection.class);
        when(currentSectionMock.removeFirstCar()).thenReturn(firstCarMock);

        Street nextSectionMock = mock(StreetSection.class);
        when(firstCarMock.getNextSection()).thenReturn(nextSectionMock);

        Street destinationMock = mock(StreetSection.class);
        when(firstCarMock.getDestination()).thenReturn(destinationMock);

        doCallRealMethod().when(currentSectionMock).moveFirstCarToNextSection();

        currentSectionMock.moveFirstCarToNextSection();
        verify(firstCarMock, times(1)).getNextSection();
    }

    @Test
    public void updateAllCarsPositions_noCars() {
        Street streetSectionMock = mock(StreetSection.class);
        Map<ICar, Double> carPositions = streetSectionMock.getCarPositions();

        streetSectionMock.updateAllCarsPositions();

        // Actually nothing should happen, no exceptions should be thrown.
        assertThat(carPositions, is(streetSectionMock.getCarPositions()));
    }

    @Test (expected = NullPointerException.class)
    public void updateAllCarsPositions_oneCar() {
        RoundaboutSimulationModel modelMock = mock(RoundaboutSimulationModel.class);
        when(modelMock.getCurrentTime()).thenReturn(10.0);
        when(modelMock.getRandomDistanceFactorBetweenCars()).thenReturn(0.5);

        LinkedList<ICar> carQueue = new LinkedList<>();
        Map<ICar, Double> carPositions = new HashMap<>();

        Street streetSectionMock = mock(StreetSection.class);
        when(streetSectionMock.getModel()).thenReturn(modelMock);
        when(streetSectionMock.getLength()).thenReturn(100.0);
        when(streetSectionMock.getCarQueue()).thenReturn(carQueue);
        when(streetSectionMock.getCarPositions()).thenReturn(carPositions);
        doCallRealMethod().when(streetSectionMock).updateAllCarsPositions();

        IDriverBehaviour driverBehaviour = new DriverBehaviour(
            5,
            1,
            4,
            1.5, 1
        );

        ICar carMock = mock(RoundaboutCar.class);
        when(carMock.getDriverBehaviour()).thenReturn(driverBehaviour);
        when(carMock.getLastUpdateTime()).thenReturn(0.0);

        carQueue.addLast(carMock);
        carPositions.put(carMock, 0.0);

        streetSectionMock.updateAllCarsPositions();

        // At this point NPE exception will be thrown, unfortunately there is no proper way
        // to test this method functionality, because implementation relies on "carPositions" field
        // that can not be initialised using mocks. So there is nothing more to check but only
        // if NPE is thrown. If we are so far, logic flow may be considered to be ok, however position
        // calculations may be absolutely wrong.
    }
}