package at.fhv.itm3.s2.roundabout;

import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;

import java.util.Random;

public class RoundaboutModel extends Model {

    private static double MIN_TIME_BETWEEN_CAR_ARRIVALS = 3.5;
    private static double MAX_TIME_BETWEEN_CAR_ARRIVALS = 10.0;

    /**
     * Random number stream used to draw a delivery lag for an order.
     * Describes the time an order of goods needs to arrive in the
     * warehouse after ordering.
     * See init() method for stream parameters.
     */
    private ContDistUniform timeBetweenCarArrivals;


    public RoundaboutModel(Model model, String s, boolean b, boolean b1) {
        super(model, s, b, b1);
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public void doInitialSchedules() {

    }

    @Override
    public void init() {

        timeBetweenCarArrivals = new ContDistUniform(this, "TimeBetweenCarArrivalsStream", MIN_TIME_BETWEEN_CAR_ARRIVALS, MAX_TIME_BETWEEN_CAR_ARRIVALS, true, false);
        timeBetweenCarArrivals.setSeed(new Random().nextLong());
    }

    /**
     * Returns a sample of the random stream used to determine the time between car arrivals.
     *
     * @return  a timeBetweenCarArrivals sample as double
     */
    public double getTimeBetweenCarArrivals() {
        return timeBetweenCarArrivals.sample();
    }
}
