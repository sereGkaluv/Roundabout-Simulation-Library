package at.fhv.itm3.s2.roundabout;

import at.fhv.itm3.s2.roundabout.entity.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutSource;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.ui.controller.UIController;
import at.fhv.itm3.s2.roundabout.util.ConfigParser;
import at.fhv.itm3.s2.roundabout.util.ILogger;
import at.fhv.itm3.s2.roundabout.util.dto.ModelConfig;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeInstant;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

public class Main extends Application {

    private static final Logger LOGGER = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private static final String DEFAULT_ROUNDABOUT_CONFIG_FILENAME = "/roundabout.xml";
    private static final String ARGUMENT_CONFIG = "cfg";

    private static final String DEFAULT_UI_RESOURCE = "/fxml/ui_roundabout_south_v2.fxml";

    private static final double EXPERIMENT_STOP_TIME = 250 * 7 * 10000; // 250 times * 7 days -> 250 weeks
    private static final TimeUnit EXPERIMENT_TIME_UNIT = TimeUnit.DAYS;

    private static final boolean IS_PROGRESS_BAR_SHOWN = true;
    private static final boolean IS_TRACE_ENABLED = false;
    private static final boolean IS_DEBUG_ENABLED = false;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            final Map<String, String> startArgs = getParameters().getNamed();

            final String roundaboutConfigFileName = startArgs.getOrDefault(ARGUMENT_CONFIG, DEFAULT_ROUNDABOUT_CONFIG_FILENAME);
            final ConfigParser configParser = new ConfigParser(roundaboutConfigFileName);
            final ModelConfig modelConfig = configParser.loadConfig();

            Experiment.setReferenceUnit(EXPERIMENT_TIME_UNIT);
            final Experiment experiment = configParser.assembleExperiment(modelConfig, IS_PROGRESS_BAR_SHOWN);
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

            initUI(
                stage,
                "Roundabout Dornbirn South Simulation",
                getClass().getResource(DEFAULT_UI_RESOURCE),
                configParser.getSourceResolver(),
                configParser.getStreetSectionResolver(),
                configParser.getSinkResolver()
            );

            // Starting experiment
            experiment.start();

            if (IS_TRACE_ENABLED || IS_DEBUG_ENABLED) {
                // Should be wrapped into if guard to prevent NPE when trace / debug are disabled above.
                experiment.report();
            }

            experiment.finish();

        } catch (Exception e) {
            LOGGER.error("Failed to start.", e);
        }
    }

    private static void initUI(
        Stage stage,
        String title,
        URL resourceUrl,
        BiFunction<String, String, RoundaboutSource> sourceResolver,
        BiFunction<String, String, StreetSection> streetSectionResolver,
        BiFunction<String, String, RoundaboutSink> sinkResolver
    ) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(resourceUrl);
        final Pane root = fxmlLoader.load();

        final UIController uiController = fxmlLoader.getController();
        uiController.initSourceObservers(sourceResolver);
        uiController.initStreetSectionObservers(streetSectionResolver);
        uiController.initSinkObservers(sinkResolver);

        final Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
