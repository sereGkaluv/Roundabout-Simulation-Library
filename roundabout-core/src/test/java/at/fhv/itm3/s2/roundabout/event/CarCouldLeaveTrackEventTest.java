package at.fhv.itm3.s2.roundabout.event;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.mocks.CarCouldLeaveSectionEventMock;
import at.fhv.itm3.s2.roundabout.mocks.SectionMock;
import desmoj.core.simulator.Experiment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CarCouldLeaveTrackEventTest {

    private RoundaboutSimulationModel model;

    @Before
    public void setUp() {
        model = new RoundaboutSimulationModel(null, "", false, false);
        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);
    }

    @Test
    public void eventRoutineTest_firstCarCouldNotEnterNextSection() throws Exception {
        CarCouldLeaveSectionEventMock eventMock = new CarCouldLeaveSectionEventMock(model, null, false);
        StreetSection section = new SectionMock(false, false, true, 0).getSection();

        eventMock.eventRoutine(section);

        Assert.assertEquals(0, eventMock.getCreateCarCouldLeaveSectionEventCounter());
    }

    @Test
    public void eventRoutineTest_firstCarCouldEnterNextSection_nextSectionIsInvalid_sectionIsEmpty_noPreviousSections() throws Exception {
        CarCouldLeaveSectionEventMock eventMock = new CarCouldLeaveSectionEventMock(model, null, false);
        StreetSection section = new SectionMock(true, false, true, 0).getSection();

        eventMock.eventRoutine(section);

        Assert.assertEquals(0, eventMock.getCreateCarCouldLeaveSectionEventCounter());
    }

    @Test
    public void eventRoutineTest_firstCarCouldEnterNextSection_nextSectionIsValid_sectionIsEmpty_noPreviousSections() throws Exception {
        CarCouldLeaveSectionEventMock eventMock = new CarCouldLeaveSectionEventMock(model, null, false);
        StreetSection section = new SectionMock(true, true, true, 0).getSection();

        eventMock.eventRoutine(section);

        Assert.assertEquals(1, eventMock.getCreateCarCouldLeaveSectionEventCounter());
    }

    @Test
    public void eventRoutineTest_firstCarCouldEnterNextSection_nextSectionIsInvalid_sectionIsNotEmpty_noPreviousSections() throws Exception {
        CarCouldLeaveSectionEventMock eventMock = new CarCouldLeaveSectionEventMock(model, null, false);
        StreetSection section = new SectionMock(true, false, false, 0).getSection();

        eventMock.eventRoutine(section);

        Assert.assertEquals(1, eventMock.getCreateCarCouldLeaveSectionEventCounter());
    }

    @Test
    public void eventRoutineTest_firstCarCouldEnterNextSection_nextSectionIsInvalid_sectionIsEmpty_twoPreviousSections() throws Exception {
        CarCouldLeaveSectionEventMock eventMock = new CarCouldLeaveSectionEventMock(model, null, false);
        StreetSection section = new SectionMock(true, false, true, 2).getSection();

        eventMock.eventRoutine(section);

        Assert.assertEquals(2, eventMock.getCreateCarCouldLeaveSectionEventCounter());
    }

    @Test
    public void eventRoutineTest_firstCarCouldEnterNextSection_nextSectionIsValid_sectionIsNotEmpty_twoPreviousSections() throws Exception {
        CarCouldLeaveSectionEventMock eventMock = new CarCouldLeaveSectionEventMock(model, null, false);
        StreetSection section = new SectionMock(true, true, false, 2).getSection();

        eventMock.eventRoutine(section);

        Assert.assertEquals(4, eventMock.getCreateCarCouldLeaveSectionEventCounter());
    }
}