package at.fhv.itm3.s2.roundabout;

import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;
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
            false,
                3.5,
                10.0
        );

        // create experiment and connect it with the model
        // this is necessary because otherwise there is a NullPointerException thrown
        Experiment exp = new Experiment("RoundaboutSimulationModel Experiment");
        model.connectToExperiment(exp);

        double sample = model.getRandomTimeBetweenCarArrivals();
        Assert.assertTrue(sample >= model.minTimeBetweenCarArrivals);
        Assert.assertTrue(sample <= model.maxTimeBetweenCarArrivals);
    }
}
