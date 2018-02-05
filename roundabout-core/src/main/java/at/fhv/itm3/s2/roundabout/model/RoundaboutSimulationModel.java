package at.fhv.itm3.s2.roundabout.model;

import at.fhv.itm14.trafsim.model.ModelFactory;
import com.sun.org.apache.bcel.internal.generic.SWAP;
import desmoj.core.dist.ContDist;
import desmoj.core.dist.ContDistNormal;
import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;
import org.apache.commons.math.stat.descriptive.rank.Max;
import org.apache.commons.math.stat.descriptive.rank.Min;
import org.jcp.xml.dsig.internal.dom.DOMUtils;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RoundaboutSimulationModel extends Model {

    public static final Double DEFAULT_MIN_TIME_BETWEEN_CAR_ARRIVALS = 3.5;
    public static final Double DEFAULT_MAX_TIME_BETWEEN_CAR_ARRIVALS = 10.0;
    public static final Double DEFAULT_MIN_DISTANCE_FACTOR_BETWEEN_CARS = 0.0;
    public static final Double DEFAULT_MAX_DISTANCE_FACTOR_BETWEEN_CARS = 1.0;
    public static final Double DEFAULT_MAIN_ARRIVAL_RATE_FOR_ONE_WAY_STREETS = 1.0;
    public static final Double DEFAULT_STANDARD_CAR_ACCELERATION_TIME = 2.0;
    public static final Double DEFAULT_MIN_CAR_LENGTH = 3.0;
    public static final Double DEFAULT_MAX_CAR_LENGTH = 19.5;
    public static final Double DEFAULT_EXPECTED_CAR_LENGTH = 4.5;
    public static final Double DEFAULT_MIN_TRUCK_LENGTH = 3.0;
    public static final Double DEFAULT_MAX_TRUCK_LENGTH = 19.5;
    public static final Double DEFAULT_EXPECTED_TRUCK_LENGTH = 4.5;
    public static final Double DEFAULT_CAR_RATIO_PER_TOTAL_VEHICLE = 0.8;

    public static final Double VEHICLE_LENGTH_STEPSIZE = 0.1;

    public final Double minDistanceFactorBetweenCars;
    public final Double maxDistanceFactorBetweenCars;
    public final Double minTimeBetweenCarArrivals;
    public final Double maxTimeBetweenCarArrivals;
    public final Double meanTimeBetweenCarArrivals;
    public final Double mainArrivalRateForOneWayStreets;
    public final Double standardCarAccelerationTime;
    public final Double minCarLength;
    public final Double maxCarLength;
    public final Double expectedCarLength;
    public final Double minTruckLength;
    public final Double maxTruckLength;
    public final Double expectedTruckLength;
    public final Double carRatioPerTotalVehicle;

    private static final long MODEL_SEED = new Random().nextLong();
    private static final TimeUnit MODEL_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * Random number stream used to calculate a distance between two cars.
     * See {@link RoundaboutSimulationModel#init()} method for stream parameters.
     */
    private ContDistUniform distanceFactorBetweenCars;

    private ContDistNormal lengthOfCar;
    private ContDistNormal lengthOfTruck;
    private ContDistUniform typeOfVehicle;

    private ContDist timeBetweenCarArrivalsOnOneWayStreets;

    /**
     * Random number stream used to draw a time between two car arrivals.
     * See {@link RoundaboutSimulationModel#init()} method for stream parameters.
     */
    private ContDistNormal timeBetweenCarArrivals;

    /**
     * Constructs a new RoundaboutSimulationModel
     *
     * @param model the model this model is part of (set to null when there is no such model)
     * @param name this model's name
     * @param showInReport flag to indicate if this model shall produce output to the report file
     * @param showInTrace flag to indicate if this model shall produce output to the trace file
     */
    public RoundaboutSimulationModel(
        Model model,
        String name,
        boolean showInReport,
        boolean showInTrace
    ) {
        this(
            model, name, showInReport, showInTrace,
            DEFAULT_MIN_TIME_BETWEEN_CAR_ARRIVALS, DEFAULT_MAX_TIME_BETWEEN_CAR_ARRIVALS
        );
    }

    /**
     * Constructs a new RoundaboutSimulationModel
     *
     * @param model the model this model is part of (set to null when there is no such model)
     * @param name this model's name
     * @param showInReport flag to indicate if this model shall produce output to the report file
     * @param showInTrace flag to indicate if this model shall produce output to the trace file
     */
    public RoundaboutSimulationModel(
        Model model,
        String name,
        boolean showInReport,
        boolean showInTrace,
        double minTimeBetweenCarArrivals,
        double maxTimeBetweenCarArrivals
    ) {
        this(
            model, name, showInReport, showInTrace,
            minTimeBetweenCarArrivals, maxTimeBetweenCarArrivals,
            DEFAULT_MIN_DISTANCE_FACTOR_BETWEEN_CARS, DEFAULT_MAX_DISTANCE_FACTOR_BETWEEN_CARS,
            DEFAULT_MAIN_ARRIVAL_RATE_FOR_ONE_WAY_STREETS,
            DEFAULT_STANDARD_CAR_ACCELERATION_TIME,
            DEFAULT_MIN_CAR_LENGTH, DEFAULT_MAX_CAR_LENGTH, DEFAULT_EXPECTED_CAR_LENGTH,
            DEFAULT_MIN_TRUCK_LENGTH, DEFAULT_MAX_TRUCK_LENGTH, DEFAULT_EXPECTED_TRUCK_LENGTH,
            DEFAULT_CAR_RATIO_PER_TOTAL_VEHICLE
        );
    }

    /**
     * Constructs a new RoundaboutSimulationModel
     *
     * @param model the model this model is part of (set to null when there is no such model)
     * @param name this model's name
     * @param showInReport flag to indicate if this model shall produce output to the report file
     * @param showInTrace flag to indicate if this model shall produce output to the trace file
     */
    public RoundaboutSimulationModel(
        Model model,
        String name,
        boolean showInReport,
        boolean showInTrace,
        Double minTimeBetweenCarArrivals,
        Double maxTimeBetweenCarArrivals,
        Double minDistanceFactorBetweenCars,
        Double maxDistanceFactorBetweenCars,
        Double mainArrivalRateForOneWayStreets,
        Double standardCarAccelerationTime,
        Double minCarLength,
        Double maxCarLength,
        Double expectedCarLength,
        Double minTruckLength,
        Double maxTruckLength,
        Double expectedTruckLength,
        Double carRatioPerTotalVehicle
    ) {
        super(model, name, showInReport, showInTrace);

        this.minTimeBetweenCarArrivals = minTimeBetweenCarArrivals;
        this.maxTimeBetweenCarArrivals = maxTimeBetweenCarArrivals;
        this.meanTimeBetweenCarArrivals = (minTimeBetweenCarArrivals + maxTimeBetweenCarArrivals) / 2;
        this.minDistanceFactorBetweenCars = minDistanceFactorBetweenCars;
        this.maxDistanceFactorBetweenCars = maxDistanceFactorBetweenCars;
        this.mainArrivalRateForOneWayStreets = mainArrivalRateForOneWayStreets;
        this.standardCarAccelerationTime = standardCarAccelerationTime;
        this.minCarLength = minCarLength;
        this.maxCarLength = maxCarLength;
        this.expectedCarLength = expectedCarLength;
        this.minTruckLength = minTruckLength;
        this.maxTruckLength = maxTruckLength;
        this.expectedTruckLength = expectedTruckLength;
        this.carRatioPerTotalVehicle = carRatioPerTotalVehicle;
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
            minDistanceFactorBetweenCars,
            maxDistanceFactorBetweenCars,
            true,
            false
        );
        distanceFactorBetweenCars.setSeed(MODEL_SEED);

        timeBetweenCarArrivals = new ContDistNormal(
            this,
            "TimeBetweenCarArrivalsStream",
                getMeanTimeBetweenCarArrivals(),
                getStdDeviationTimeBetweenCarArrivals(),
            true,
            false
        );
        timeBetweenCarArrivals.setSeed(MODEL_SEED);

        // calculate the standard deviation (of skew normal distribution) for vehicle length
        ArrayList<Double> listTmp = new ArrayList<>();
        Double mean = 0.;
        for(double curLength = minCarLength; curLength <= maxCarLength;
            curLength += VEHICLE_LENGTH_STEPSIZE) {
            listTmp.add(curLength);
            mean += curLength;
        }
        mean /= listTmp.size();
        Double variancePartSum = 0.0;
        for(Double curVal : listTmp)  {
            curVal = Math.pow(curVal-mean,2); //preparation vor variance
            variancePartSum += curVal;
        }
        Double lowerRatio = 1/(maxCarLength-minCarLength)*(expectedCarLength-minCarLength); //Ratio for the smaller trucks
        Double upperRatio = 1-lowerRatio;
        Double variance = Math.sqrt(variancePartSum/listTmp.size());

        lengthOfCar = new ContDistNormal(
            this,
            "LengthOfCar",
            expectedCarLength,
            variance*lowerRatio,
            variance*upperRatio,
            true,
            false
        );

        listTmp.clear();
        mean = 0.;
        for(double curLength = minTruckLength; curLength <= maxTruckLength;
            curLength += VEHICLE_LENGTH_STEPSIZE) {
            listTmp.add(curLength);
            mean += curLength;
        }
        mean /= listTmp.size();
        variancePartSum = 0.0;
        for(Double curVal : listTmp)  {
            curVal = Math.pow(curVal-mean,2); //preparation vor variance
            variancePartSum += curVal;
        }
        lowerRatio = 1/(maxTruckLength-minTruckLength)*(expectedTruckLength-minTruckLength); //Ratio for the smaller trucks
        upperRatio = 1-lowerRatio;
        variance = Math.sqrt(variancePartSum/listTmp.size());

        lengthOfTruck = new ContDistNormal(
            this,
            "LengthOfTruck",
            expectedTruckLength,
            variance*lowerRatio,
            variance*upperRatio,
            true,
            false
        );

        if(carRatioPerTotalVehicle > 1.0) throw new IllegalArgumentException("carRatioPerTotalVehicle must not bigger than 1.");
        typeOfVehicle = new ContDistUniform(
            this,
            "LengthOfVehicle",
            0.0,
            1.0,
            true,
            false
        );

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
     * Returns a min value of time between car arrivals.
     *
     * @return min time between car arrivals.
     */
    public double getMinTimeBetweenCarArrivals() {
        return minTimeBetweenCarArrivals;
    }

    /**
     * Returns a max value of time between car arrivals.
     *
     * @return max time between car arrivals.
     */
    public double getMaxTimeBetweenCarArrivals() {
        return maxTimeBetweenCarArrivals;
    }

    /**
     * Returns a mean value of time between car arrivals (is calculated based on min and max).
     *
     * @return mean time between car arrivals.
     */
    public double getMeanTimeBetweenCarArrivals() {
        return meanTimeBetweenCarArrivals;
    }

    public double getStdDeviationTimeBetweenCarArrivals() {
        return Math.abs(getMaxTimeBetweenCarArrivals() - getMeanTimeBetweenCarArrivals());
    }

    /**
     * Returns a sample of the random stream {@link ContDistUniform} used to determine the time between car arrivals.
     *
     * @return a {@code timeBetweenCarArrivals} sample as double.
     */
    public double getRandomTimeBetweenCarArrivals() {
        final double value = timeBetweenCarArrivals.sample();
        return Math.max(Math.min(value, maxTimeBetweenCarArrivals), minDistanceFactorBetweenCars);
    }

    /**
     * Returns a sample of the random stream {@link ContDistNormal} used to determine the length of a vehicle
     *
     * @return a {@code getRandomLengthOfVehicle} sample as double.
     */
    public double getRandomLengthOfVehicle () {
        return (typeOfVehicle.sample() <= carRatioPerTotalVehicle) ? getRandomLengthOfCar() : getRandomLengthOfTruck();
    }

    /**
     * Returns a sample of the random stream {@link ContDistNormal} used to determine the length of a car.
     *
     * @return a {@code getRandomLengthOfCar} sample as double.
     */
    public double getRandomLengthOfCar(){
        return Math.max( Math.min(lengthOfCar.sample(), maxCarLength), minCarLength);
    }

    /**
     * Returns a sample of the random stream {@link ContDistNormal} used to determine the length of a truck.
     *
     * @return a {@code getRandomLengthOfTruck} sample as double.
     */
    public double getRandomLengthOfTruck(){
        return Math.max( Math.min(lengthOfTruck.sample(), maxTruckLength), minTruckLength);
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
}
