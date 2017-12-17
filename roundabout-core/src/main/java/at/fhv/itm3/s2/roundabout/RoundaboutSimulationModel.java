package at.fhv.itm3.s2.roundabout;

import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.event.CarCouldLeaveSectionEvent;
import at.fhv.itm3.s2.roundabout.event.CarGenerateEvent;
import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RoundaboutSimulationModel extends Model {

    public static final double MIN_DISTANCE_FACTOR_BETWEEN_CARS = 0.0;
    public static final double MAX_DISTANCE_FACTOR_BETWEEN_CARS = 1.0;

    public static final double MIN_TIME_BETWEEN_CAR_ARRIVALS = 3.5;
    public static final double MAX_TIME_BETWEEN_CAR_ARRIVALS = 10.0;

    private static final long MODEL_SEED = new Random().nextLong();
    private static final TimeUnit MODEL_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * Random number stream used to calculate a distance between two cars.
     * See {@link RoundaboutSimulationModel#init()} method for stream parameters.
     */
    private ContDistUniform distanceFactorBetweenCars;

    /**
     * Random number stream used to draw a time between two car arrivals.
     * See {@link RoundaboutSimulationModel#init()} method for stream parameters.
     */
    private ContDistUniform timeBetweenCarArrivals;

    /**
     * events
     */
    private List<CarCouldLeaveSectionEvent> carCouldLeaveSectionEvents;
    private List<CarGenerateEvent> carGenerateEvents;

    /**
     * sections
     */
    private List<StreetSection> startSections;
    private List<StreetSection> sections;

    /**
     * Constructs a new RoundaboutSimulationModel
     *
     * @param model        the model this model is part of (set to null when there is no such model)
     * @param name         this model's name
     * @param showInReport flag to indicate if this model shall produce output to the report file
     * @param showInTrace  flag to indicate if this model shall produce output to the trace file
     */
    public RoundaboutSimulationModel(Model model, String name, boolean showInReport, boolean showInTrace) {
        super(model, name, showInReport, showInTrace);
        startSections = new LinkedList<>();
        sections = new LinkedList<>();
        carCouldLeaveSectionEvents = new LinkedList<>();
        carGenerateEvents = new LinkedList<>();
    }

    @Override
    public String description() {
        return "Roundabout simulation model";
    }

    @Override
    public void init() {
        distanceFactorBetweenCars = new ContDistUniform(
                this,
                "DistanceFactorBetweenCarsStream",
                MIN_DISTANCE_FACTOR_BETWEEN_CARS,
                MAX_DISTANCE_FACTOR_BETWEEN_CARS,
                true,
                false
        );
        distanceFactorBetweenCars.setSeed(MODEL_SEED);

        timeBetweenCarArrivals = new ContDistUniform(
                this,
                "TimeBetweenCarArrivalsStream",
                MIN_TIME_BETWEEN_CAR_ARRIVALS,
                MAX_TIME_BETWEEN_CAR_ARRIVALS,
                true,
                false
        );
        timeBetweenCarArrivals.setSeed(MODEL_SEED);
    }

    /**
     * Initial schedule of events.
     */
    @Override
    public void doInitialSchedules() {
        for (StreetSection section : sections) {
            CarCouldLeaveSectionEvent carCouldLeaveSectionEvent = new CarCouldLeaveSectionEvent(this, "car could leave section event", true);
            carCouldLeaveSectionEvent.schedule(section, new TimeInstant(0.001));
            carCouldLeaveSectionEvents.add(carCouldLeaveSectionEvent); // TODO needed?
        }

        for (StreetSection startSection : startSections) {
            CarGenerateEvent carGenerateEvent = new CarGenerateEvent(this, "car generate event", true);
            carGenerateEvent.schedule(startSection, new TimeInstant(0.001));
            carGenerateEvents.add(carGenerateEvent); // TODO needed?
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
     * @return a {@code timeBetweenCarArrivals} sample as double.
     */
    public double getRandomTimeBetweenCarArrivals() {
        return timeBetweenCarArrivals.sample();
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

    /**
     * Adds a section to the models start sections list (and the sections list).
     *
     * @param startSection start section
     */
    public void addStartSection(StreetSection startSection) {
        startSections.add(startSection);
        addSection(startSection);
    }

    /**
     * Adds a section to the models section list.
     *
     * @param section section
     */
    public void addSection(StreetSection section) {
        sections.add(section);
    }
}
