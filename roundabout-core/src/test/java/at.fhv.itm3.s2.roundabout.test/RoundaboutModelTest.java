package at.fhv.itm3.s2.roundabout.test;

import at.fhv.itm3.s2.roundabout.RoundaboutModel;
import desmoj.core.simulator.Experiment;
import org.junit.Assert;
import org.junit.Test;

public class RoundaboutModelTest {

    @Test
    public void getTimeBetweenCarArrivalsTest() {
        RoundaboutModel model = new RoundaboutModel(null, "", false, false);

        // create experiment and connect it with the model
        // this is necessary because otherwise there is a NullPointerException thrown
        Experiment exp = new Experiment("RoundaboutModel Experiment");
        model.connectToExperiment(exp);

        double sample = model.getTimeBetweenCarArrivals();
        Assert.assertTrue(sample >= RoundaboutModel.MIN_TIME_BETWEEN_CAR_ARRIVALS);
        Assert.assertTrue(sample <= RoundaboutModel.MAX_TIME_BETWEEN_CAR_ARRIVALS);
    }
}
