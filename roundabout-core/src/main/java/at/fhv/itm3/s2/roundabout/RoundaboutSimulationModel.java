package at.fhv.itm3.s2.roundabout;

import at.fhv.itm14.trafsim.model.ModelFactory;
import at.fhv.itm3.s2.roundabout.api.controller.ITrafficLightController;
import desmoj.core.dist.ContDist;
import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RoundaboutSimulationModel extends Model {

    public static final Double DEFAULT_MIN_TIME_BETWEEN_CAR_ARRIVALS = 3.5;
    public static final Double DEFAULT_MAX_TIME_BETWEEN_CAR_ARRIVALS = 10.0;
    public static final Double DEFAULT_MIN_DISTANCE_FACTOR_BETWEEN_CARS = 0.0;
    public static final Double DEFAULT_MAX_DISTANCE_FACTOR_BETWEEN_CARS = 1.0;
    public static final Double DEFAULT_MAIN_ARRIVAL_RATE_FOR_ONE_WAY_STREETS = 1.0;
    public static final Double DEFAULT_STANDARD_CAR_ACCELERATION_TIME = 2.0;
    public static final Double DEFAULT_STANDARD_CAR_SPEED = 5.0;
    public static final Double DEFAULT_STANDARD_CAR_LENGTH = 4.0;

    public final Double minDistanceBetweenCars;
    public final Double maxDistanceBetweenCars;
    public final Double minTimeBetweenCarArrivals;
    public final Double maxTimeBetweenCarArrivals;
    public final Double mainArrivalRateForOneWayStreets;
    public final Double standardCarAccelerationTime;
    public final Double standardCarSpeed;
    public final Double standardCarLength;

    private static final long MODEL_SEED = new Random().nextLong();
    private static final TimeUnit MODEL_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * Random number stream used to calculate a distance between two cars.
     * See {@link RoundaboutSimulationModel#init()} method for stream parameters.
     */
    private ContDistUniform distanceFactorBetweenCars;

    private ContDist timeBetweenCarArrivalsOnOneWayStreets;

    /**
     * Random number stream used to draw a time between two car arrivals.
     * See {@link RoundaboutSimulationModel#init()} method for stream parameters.
     */
    private ContDistUniform timeBetweenCarArrivalsOnMainFlow;
    private ITrafficLightController trafficLightsController;

    /**
     * Constructs a new RoundaboutSimulationModel
     *
     * @param model        the model this model is part of (set to null when there is no such model)
     * @param name         this model's name
     * @param showInReport flag to indicate if this model shall produce output to the report file
     * @param showInTrace  flag to indicate if this model shall produce output to the trace file
     */
    public RoundaboutSimulationModel(
            Model model,
            String name,
            boolean showInReport,
            boolean showInTrace
    ) {
        this(
                model, name, showInReport, showInTrace,
                DEFAULT_MIN_TIME_BETWEEN_CAR_ARRIVALS, DEFAULT_MAX_TIME_BETWEEN_CAR_ARRIVALS,
                DEFAULT_STANDARD_CAR_SPEED, DEFAULT_STANDARD_CAR_LENGTH
        );
    }

    /**
     * Constructs a new RoundaboutSimulationModel
     *
     * @param model        the model this model is part of (set to null when there is no such model)
     * @param name         this model's name
     * @param showInReport flag to indicate if this model shall produce output to the report file
     * @param showInTrace  flag to indicate if this model shall produce output to the trace file
     */
    public RoundaboutSimulationModel(
            Model model,
            String name,
            boolean showInReport,
            boolean showInTrace,
            Double minTimeBetweenCarArrivals,
            Double maxTimeBetweenCarArrivals,
            Double standardCarSpeed,
            Double standardCarLength
    ) {
        this(
                model, name, showInReport, showInTrace,
                minTimeBetweenCarArrivals, maxTimeBetweenCarArrivals,
                DEFAULT_MIN_DISTANCE_FACTOR_BETWEEN_CARS, DEFAULT_MAX_DISTANCE_FACTOR_BETWEEN_CARS,
                DEFAULT_MAIN_ARRIVAL_RATE_FOR_ONE_WAY_STREETS,
                DEFAULT_STANDARD_CAR_ACCELERATION_TIME, standardCarSpeed, standardCarLength
        );
    }

    /**
     * Constructs a new RoundaboutSimulationModel
     *
     * @param model        the model this model is part of (set to null when there is no such model)
     * @param name         this model's name
     * @param showInReport flag to indicate if this model shall produce output to the report file
     * @param showInTrace  flag to indicate if this model shall produce output to the trace file
     */
    public RoundaboutSimulationModel(
            Model model,
            String name,
            boolean showInReport,
            boolean showInTrace,
            Double minTimeBetweenCarArrivals,
            Double maxTimeBetweenCarArrivals,
            Double minDistanceBetweenCars,
            Double maxDistanceBetweenCars,
            Double mainArrivalRateForOneWayStreets,
            Double standardCarAccelerationTime,
            Double standardCarSpeed,
            Double standardCarLength
    ) {
        super(model, name, showInReport, showInTrace);

        this.minTimeBetweenCarArrivals = minTimeBetweenCarArrivals;
        this.maxTimeBetweenCarArrivals = maxTimeBetweenCarArrivals;
        this.minDistanceBetweenCars = minDistanceBetweenCars;
        this.maxDistanceBetweenCars = maxDistanceBetweenCars;
        this.mainArrivalRateForOneWayStreets = mainArrivalRateForOneWayStreets;
        this.standardCarAccelerationTime = standardCarAccelerationTime;
        this.standardCarSpeed = standardCarSpeed;
        this.standardCarLength = standardCarLength;
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
        distanceFactorBetweenCars = new ContDistUniform(
                this,
                "DistanceFactorBetweenCarsStream",
                minDistanceBetweenCars,
                maxDistanceBetweenCars,
                true,
                false
        );
        distanceFactorBetweenCars.setSeed(MODEL_SEED);

        timeBetweenCarArrivalsOnMainFlow = new ContDistUniform(
                this,
                "TimeBetweenCarArrivalsStream",
                minTimeBetweenCarArrivals,
                maxTimeBetweenCarArrivals,
                true,
                false
        );
        timeBetweenCarArrivalsOnMainFlow.setSeed(MODEL_SEED);

        if (mainArrivalRateForOneWayStreets != null) {
            timeBetweenCarArrivalsOnOneWayStreets = ModelFactory.getInstance(this).createContDistConstant(mainArrivalRateForOneWayStreets);
        }
    }

    /**
     * Returns a sample of the random stream {@link ContDistUniform} used to determine the distance factor between cars.
     *
     * @return a {@code distanceFactorBetweenCars} sample as double.
     */
    public double getRandomDistanceFactorBetweenCars() {
        return distanceFactorBetweenCars.sample();
    }

    /**
     * Returns a sample of the random stream {@link ContDistUniform} used to determine the time between car arrivals.
     *
     * @return a {@code timeBetweenCarArrivalsOnMainFlow} sample as double.
     */
    public double getRandomTimeBetweenCarArrivalsOnMainFlow() {
        return timeBetweenCarArrivalsOnMainFlow.sample();
    }

    /**
     * Returns configured model {@link TimeUnit}.
     *
     * @return configured model {@link TimeUnit}.
     */
    public TimeUnit getModelTimeUnit() {
        return MODEL_TIME_UNIT;
    }

    /**
     * Returns model current time in configured model {@link TimeUnit}.
     *
     * @return model current time.
     */
    public double getCurrentTime() {
        return currentModel().getExperiment().getSimClock().getTime().getTimeAsDouble(getModelTimeUnit());
    }

    public ContDist getTimeBetweenCarArrivalsOnOneWayStreets() {
        return timeBetweenCarArrivalsOnOneWayStreets;
    }

    /**
     * This method returns the acceleration time which is not influenced by driver behaviour.
     *
     * @return standardCarAccelerationTime
     */
    public Double getStandardCarAccelerationTime() {
        return standardCarAccelerationTime;
    }

    /**
     * Sets the specific traffic light controller for this roundabout.
     *
     * @param trafficLightsController
     */
    public void setTrafficLightsController(ITrafficLightController trafficLightsController) {
        this.trafficLightsController = trafficLightsController;
    }

    public ITrafficLightController getTrafficLightsController() {
        return trafficLightsController;
    }
}
