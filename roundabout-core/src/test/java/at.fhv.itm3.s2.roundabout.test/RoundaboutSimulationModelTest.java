package at.fhv.itm3.s2.roundabout.test;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import desmoj.core.simulator.Experiment;
import org.junit.Assert;
import org.junit.Test;

public class RoundaboutSimulationModelTest {

    @Test
    public void getTimeBetweenCarArrivalsTest_validTimeBoundary() {
        RoundaboutSimulationModel model = new RoundaboutSimulationModel(
            null,
            "",
            false,
            false
        );

        // create experiment and connect it with the model
        // this is necessary because otherwise there is a NullPointerException thrown
        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);

        double sample = model.getRandomTimeBetweenCarArrivals();
        Assert.assertTrue(sample >= RoundaboutSimulationModel.MIN_TIME_BETWEEN_CAR_ARRIVALS);
        Assert.assertTrue(sample <= RoundaboutSimulationModel.MAX_TIME_BETWEEN_CAR_ARRIVALS);
    }
}
