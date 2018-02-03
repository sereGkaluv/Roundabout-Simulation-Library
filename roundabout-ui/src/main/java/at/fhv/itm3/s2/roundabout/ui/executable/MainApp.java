package at.fhv.itm3.s2.roundabout.ui.executable;


import at.fhv.itm3.s2.roundabout.api.entity.IModelStructure;
import at.fhv.itm3.s2.roundabout.ui.controllers.MainViewController;
import at.fhv.itm3.s2.roundabout.ui.util.DaemonThreadFactory;
import at.fhv.itm3.s2.roundabout.ui.util.ViewLoader;
import at.fhv.itm3.s2.roundabout.util.ConfigParser;
import at.fhv.itm3.s2.roundabout.util.dto.ModelConfig;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.SimClock;
import desmoj.core.simulator.TimeInstant;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This is Utility class which starts the whole application.
 */
public class MainApp extends Application {

    private static final Logger LOGGER = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private static final String DEFAULT_TITLE = "TRAFSIM";

    private static final int DEFAULT_WIDTH = 1200;
    private static final int DEFAULT_HEIGHT = 950;

    private static final double EXPERIMENT_STOP_TIME = 3;
    private static final TimeUnit EXPERIMENT_TIME_UNIT = TimeUnit.DAYS;

    private static final boolean IS_TRACE_ENABLED = false;
    private static final boolean IS_DEBUG_ENABLED = false;

    /**
     * Default (empty) constructor for this utility class.
     */
    public MainApp() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void start(Stage initStage) {
        try {
            ViewLoader<MainViewController> viewLoader = ViewLoader.loadView(MainViewController.class);
            Parent mainStage = (Parent) viewLoader.loadNode();

            final ConfigParser configParser = new ConfigParser("/at/fhv/itm3/s2/roundabout/model/model_dornbirn_sued_with_intersection.xml");
            final ModelConfig modelConfig = configParser.loadConfig();

            final Experiment experiment = new Experiment("Trafsim experiment");
            experiment.setShowProgressBar(false);

            IModelStructure modelStructure = configParser.generateRoundaboutStructure(modelConfig, experiment);

            final MainViewController mainViewController = viewLoader.getController();
            mainViewController.generateComponentStatContainers(
                modelConfig.getComponents().getComponent(),
                configParser.getSectionRegistry(),
                configParser.getSinkRegistry()
            );

            prepareNewStage(mainStage).show();
            modelStructure.getRoutes().keySet().forEach(s -> s.startGeneratingCars(0));

            Thread thread = initExperimentThread(
                experiment,
                mainViewController::getCurrentSimSpeed,
                mainViewController::setProgress
            );

            mainViewController.setStartRunnable(thread::start);
            mainViewController.setStopRunnable(experiment::stop);

        } catch (Throwable t) {
            LOGGER.error("Error occurred during start of the application.", t);
        }
    }

    private Thread initExperimentThread(
        Experiment experiment,
        Supplier<Double> executionSpeedRateSupplier,
        Consumer<Double> progressConsumer
    ) {
        return new DaemonThreadFactory().newThread(() ->  {
            Experiment.setReferenceUnit(EXPERIMENT_TIME_UNIT);
            experiment.stop(new TimeInstant(
                EXPERIMENT_STOP_TIME,
                EXPERIMENT_TIME_UNIT
            ));

            if (IS_TRACE_ENABLED) {
                experiment.tracePeriod(
                    new TimeInstant(0),
                    new TimeInstant(70, EXPERIMENT_TIME_UNIT)
                );
            }

            if (IS_DEBUG_ENABLED) {
                experiment.tracePeriod(
                    new TimeInstant(0),
                    new TimeInstant(70, EXPERIMENT_TIME_UNIT)
                );
            }

            final SimClock simClock = experiment.getSimClock();
            simClock.addObserver((o, arg) -> {
                final double progress = simClock.getTime().getTimeAsDouble() / experiment.getStopTime().getTimeAsDouble();
                progressConsumer.accept(progress);
            });

            //set real time, default value is 0
            experiment.setExecutionSpeedRate(executionSpeedRateSupplier.get());

            // Starting experiment
            experiment.start();

            if (IS_TRACE_ENABLED || IS_DEBUG_ENABLED) {
                // Should be wrapped into if guard to prevent NPE when trace / debug are disabled above.
                experiment.report();
            }

            experiment.finish();
        });
    }

    /**
     * Displays the new stage for the application.
     *
     * @param pane node to be shown.
     * @return returns instance of the stage
     */
    private Stage prepareNewStage(Parent pane) {
        Scene scene = new Scene(pane, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle(DEFAULT_TITLE);

        return primaryStage;
    }

    /**
     * Default main method. Starts "this" application.
     *
     * @param args the command line arguments passed to the application.
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
