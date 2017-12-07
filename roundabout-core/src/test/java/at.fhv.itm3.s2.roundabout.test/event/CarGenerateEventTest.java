package at.fhv.itm3.s2.roundabout.test.event;

import at.fhv.itm3.s2.roundabout.RoundaboutModel;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.event.CarGenerateEvent;
import at.fhv.itm3.s2.roundabout.event.RoundaboutEventFactory;
import desmoj.core.simulator.Experiment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CarGenerateEventTest {

    private RoundaboutModel model;

    @Before
    public void setUp() {
        model = new RoundaboutModel(null, "", false, false);
        Experiment exp = new Experiment("RoundaboutModel Experiment");
        model.connectToExperiment(exp);
    }

    @Test
    public void eventRoutineTest() throws Exception {
        StreetSection section = new StreetSection(10.0, null, null, model, null, false);
        Assert.assertTrue(section.isEmpty());

        CarGenerateEvent event = RoundaboutEventFactory.getInstance().createCarGenerateEvent(model);
        event.eventRoutine(section);

        Assert.assertFalse(section.isEmpty());
    }
}