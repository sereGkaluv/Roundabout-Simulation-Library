package at.fhv.itm3.s2.roundabout.ui.executable;


import at.fhv.itm3.s2.roundabout.api.entity.IRoundaboutStructure;
import at.fhv.itm3.s2.roundabout.ui.controllers.MainViewController;
import at.fhv.itm3.s2.roundabout.ui.util.DaemonThreadFactory;
import at.fhv.itm3.s2.roundabout.ui.util.ViewLoader;
import at.fhv.itm3.s2.roundabout.util.ConfigParser;
import at.fhv.itm3.s2.roundabout.util.dto.ModelConfig;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

/**
 * This is Utility class which starts the whole application.
 */
public class MainApp extends Application {

    private static final Logger LOGGER = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private static final String DEFAULT_TITLE = "TRAFSIM";

    private static final int DEFAULT_WIDTH = 1200;
    private static final int DEFAULT_HEIGHT = 900;

    private static final double EXPERIMENT_STOP_TIME = 3;
    private static final TimeUnit EXPERIMENT_TIME_UNIT = TimeUnit.DAYS;

    private static final boolean IS_TRACE_ENABLED = false;
    private static final boolean IS_DEBUG_ENABLED = false;

    private static final boolean IS_PROGRESS_BAR_SHOWN = true;

    /**
     * Default (empty) constructor for this utility class.
     */
    public MainApp() {
    }

    @Override
    public void stop() throws Exception {
    }

    @Override
    public void start(Stage initStage) {
        try {
            ViewLoader<MainViewController> viewLoader = ViewLoader.loadView(MainViewController.class);
            Parent mainStage = (Parent) viewLoader.loadNode();

            final ConfigParser configParser = new ConfigParser("/at/fhv/itm3/s2/roundabout/model/model.xml");
            final ModelConfig modelConfig = configParser.loadConfig();


            final Experiment experiment = initExperiment("Trafsim experiment", IS_PROGRESS_BAR_SHOWN);
            IRoundaboutStructure roundaboutStructure = configParser.generateRoundaboutStructure(modelConfig, experiment);

            viewLoader.getController().generateComponentStatContainers(
                modelConfig.getComponents().getComponent(),
                configParser.getSectionRegistry(),
                configParser.getSinkRegistry()
            );

            prepareNewStage(mainStage).show();

            new DaemonThreadFactory().newThread(() ->  {
                Experiment.setReferenceUnit(EXPERIMENT_TIME_UNIT);
                experiment.stop(new TimeInstant(
                    EXPERIMENT_STOP_TIME,
                    EXPERIMENT_TIME_UNIT
                ));

                //final Experiment experiment = initExperiment(model, IS_PROGRESS_BAR_SHOWN);

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

                roundaboutStructure.getRoutes().keySet().forEach(s -> s.startGeneratingCars(1));
                // Starting experiment
                experiment.start();
                //roundaboutStructure.getRoutes().keySet().forEach(s -> s.startGeneratingCars(1));

                if (IS_TRACE_ENABLED || IS_DEBUG_ENABLED) {
                    // Should be wrapped into if guard to prevent NPE when trace / debug are disabled above.
                    experiment.report();
                }

                experiment.finish();
            }).start();

        } catch (Throwable t) {
            LOGGER.error("Error occurred during start of the application.", t);
        }
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

    private static Experiment initExperiment(String description, boolean isShowProgressBar) {
        Experiment experiment = new Experiment(description);

        experiment.setShowProgressBar(isShowProgressBar);
        return experiment;
    }
}
