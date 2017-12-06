package at.fhv.itm3.s2.roundabout;

import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;

import java.util.Random;

public class RoundaboutModel extends Model {

    public static final double MIN_TIME_BETWEEN_CAR_ARRIVALS = 3.5;
    public static final double MAX_TIME_BETWEEN_CAR_ARRIVALS = 10.0;

    /**
     * Random number stream used to draw a time between two car arrivals.
     * See init() method for stream parameters.
     */
    private ContDistUniform timeBetweenCarArrivals;

    /**
     * Constructs a new RoundaboutModel
     *
     * @param model         the model this model is part of (set to null when there is no such model)
     * @param name          this model's name
     * @param showInReport  flag to indicate if this model shall produce output to the report file
     * @param showInTrace   flag to indicate if this model shall produce output to the trace file
     */
    public RoundaboutModel(Model model, String name, boolean showInReport, boolean showInTrace) {
        super(model, name, showInReport, showInTrace);
    }

    @Override
    public String description() {
        return "Roundabout simulation model";
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
