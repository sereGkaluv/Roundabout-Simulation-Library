package at.fhv.itm3.s2.roundabout.ui.controllers;

import at.fhv.itm3.s2.roundabout.api.util.observable.ObserverType;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.ui.controllers.core.JfxController;
import at.fhv.itm3.s2.roundabout.ui.util.BufferedImageTranscoder;
import at.fhv.itm3.s2.roundabout.ui.util.DaemonThreadFactory;
import at.fhv.itm3.s2.roundabout.ui.util.ViewLoader;
import at.fhv.itm3.s2.roundabout.util.dto.Component;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.*;

public class MainViewController extends JfxController {

    private static final Logger LOGGER = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private static final int DEFAULT_SIM_SPEED_VALUE = 0;
    private static final int MAX_SIM_SPEED_VALUE = 100;

    private static final SimpleBooleanProperty IS_SIMULATION_RUNNING = new SimpleBooleanProperty(false);
    private static final SimpleBooleanProperty IS_SIMULATION_PAUSED = new SimpleBooleanProperty(false);

    private static final SimpleDoubleProperty CURRENT_SIM_SPEED = new SimpleDoubleProperty(0);

    private static final BufferedImageTranscoder BUFFERED_IMAGE_TRANSCODER = new BufferedImageTranscoder();


    @FXML private Button btnStartSimulation;
    @FXML private Button btnGenReportSimulation;
    @FXML private Button btnPauseSimulation;
    @FXML private Button btnProceedSimulation;
    @FXML private Button btnDoStepOfSimulation;
    @FXML private BorderPane borderPaneContainer;
    @FXML private Label lblCurrentSimSpeed;
    @FXML private Slider sliderSimSpeed;
    @FXML private Label lblProgress;
    @FXML private ProgressBar progressBar;
    @FXML private StackPane stackPane;
    @FXML private Pane drawPane;
    @FXML private ImageView imageView;
    @FXML private HBox hBoxContainer;
    @FXML private VBox vBoxContainer;
    @FXML private ScrollPane scrollPane;
    @FXML private ScrollBar scrollBarStats;

    private Runnable startRunnable;
    private Runnable finishRunnable;
    private Runnable pauseRunnable;
    private Runnable doStepRunnable;
    private Runnable proceedRunnable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnStartSimulation.managedProperty().bind(IS_SIMULATION_RUNNING.not());
        btnStartSimulation.visibleProperty().bind(btnStartSimulation.managedProperty());

        btnGenReportSimulation.managedProperty().bind(IS_SIMULATION_RUNNING);
        btnGenReportSimulation.visibleProperty().bind(btnGenReportSimulation.managedProperty());

        btnPauseSimulation.disableProperty().bind(IS_SIMULATION_RUNNING.and(IS_SIMULATION_PAUSED));
        btnPauseSimulation.managedProperty().bind(IS_SIMULATION_RUNNING);
        btnPauseSimulation.visibleProperty().bind(btnPauseSimulation.managedProperty());

        btnDoStepOfSimulation.disableProperty().bind(IS_SIMULATION_RUNNING.and(IS_SIMULATION_PAUSED.not()));
        btnDoStepOfSimulation.managedProperty().bind(IS_SIMULATION_RUNNING);
        btnDoStepOfSimulation.visibleProperty().bind(btnDoStepOfSimulation.managedProperty());

        btnProceedSimulation.disableProperty().bind(IS_SIMULATION_RUNNING.and(IS_SIMULATION_PAUSED.not()));
        btnProceedSimulation.managedProperty().bind(IS_SIMULATION_RUNNING);
        btnProceedSimulation.visibleProperty().bind(btnProceedSimulation.managedProperty());

        sliderSimSpeed.setShowTickMarks(true);
        sliderSimSpeed.setShowTickLabels(true);
        sliderSimSpeed.setMajorTickUnit(25);
        sliderSimSpeed.setBlockIncrement(5);
        sliderSimSpeed.setMin(DEFAULT_SIM_SPEED_VALUE);
        sliderSimSpeed.setMax(MAX_SIM_SPEED_VALUE);
        sliderSimSpeed.managedProperty().bind(IS_SIMULATION_RUNNING.not());
        sliderSimSpeed.visibleProperty().bind(sliderSimSpeed.managedProperty());
        sliderSimSpeed.valueProperty().bindBidirectional(CURRENT_SIM_SPEED);

        lblCurrentSimSpeed.textProperty().bind(Bindings.format("Simulation factor (x = 0 - max speed, x > 0 - from slow to fast) : %.2f x", CURRENT_SIM_SPEED));
        lblCurrentSimSpeed.managedProperty().bind(sliderSimSpeed.managedProperty());
        lblCurrentSimSpeed.visibleProperty().bind(sliderSimSpeed.visibleProperty());

        lblProgress.textProperty().bind(progressBar.progressProperty().multiply(100).asString("Progress: %.0f %%"));
        lblProgress.managedProperty().bind(IS_SIMULATION_RUNNING);
        lblProgress.visibleProperty().bind(lblProgress.managedProperty());

        scrollBarStats.minProperty().bind(scrollPane.vminProperty());
        scrollBarStats.maxProperty().bind(scrollPane.vmaxProperty());
        scrollBarStats.valueProperty().bindBidirectional(scrollPane.vvalueProperty());

        try (InputStream file = getClass().getResourceAsStream("/at/fhv/itm3/s2/roundabout/ui/img/back.svg")) {
            final TranscoderInput transIn = new TranscoderInput(file);
            BUFFERED_IMAGE_TRANSCODER.transcode(transIn, null);
            Platform.runLater(() -> imageView.setImage(SwingFXUtils.toFXImage(BUFFERED_IMAGE_TRANSCODER.getBufferedImage(), null)));
        } catch (IOException | TranscoderException e) {
            LOGGER.error("Error occurs while loading background SVG.", e);
        }

        stackPane.minWidthProperty().bind(borderPaneContainer.prefWidthProperty().subtract(hBoxContainer.widthProperty()));
        imageView.fitWidthProperty().bind(stackPane.widthProperty());
        imageView.fitHeightProperty().bind(stackPane.heightProperty());

        initButtonListeners();
    }

    private void initButtonListeners() {
        btnStartSimulation.setOnAction(e -> {
            IS_SIMULATION_RUNNING.set(true);
            new DaemonThreadFactory().newThread(startRunnable).start();
        });

        btnGenReportSimulation.setOnAction(e -> {
            IS_SIMULATION_RUNNING.set(false);
            btnStartSimulation.setDisable(true);
            new DaemonThreadFactory().newThread(finishRunnable).start();
        });

        btnPauseSimulation.setOnAction(e -> {
            IS_SIMULATION_PAUSED.set(true);
            new DaemonThreadFactory().newThread(pauseRunnable).start();
        });

        btnDoStepOfSimulation.setOnAction(e -> {
            IS_SIMULATION_PAUSED.set(true);
            new DaemonThreadFactory().newThread(doStepRunnable).start();
        });

        btnProceedSimulation.setOnAction(e -> {
            IS_SIMULATION_PAUSED.set(false);
            new DaemonThreadFactory().newThread(proceedRunnable).start();
        });
    }

    public void setProgress(double progressValue) {
        Platform.runLater(() -> progressBar.setProgress(progressValue));
    }

    public void clearComponentStatContainers() {
        Platform.runLater(() -> vBoxContainer.getChildren().clear());
    }

    public void generateComponentStatContainers(
        Collection<Component> components,
        Map<String, Map<String, StreetSection>> streetSections,
        Map<String, Map<String, RoundaboutSink>> sinks
    ) {
        components.forEach(component ->  {
            ViewLoader<StatsViewController> viewLoader = ViewLoader.loadView(StatsViewController.class);
            Node statNode = viewLoader.loadNode();

            final Map<String, StreetSection> streetSectionMap = streetSections.get(component.getId());
            final Map<String, RoundaboutSink> sinkMap = sinks.get(component.getId());

            final Collection<StreetSection> componentStreetSections = streetSectionMap != null ? new ArrayList<>(streetSectionMap.values()) : new ArrayList<>();
            final Collection<RoundaboutSink> componentSinks = sinkMap != null ? new ArrayList<>(sinkMap.values()) : new ArrayList<>();

//            componentStreetSections.forEach(streetSection -> streetSection.addObserver(ObserverType.CAR_POSITION, (o, arg) -> {
//                Circle car = new Circle(Math.abs(new Random().nextDouble() * 400), Math.abs(new Random().nextDouble() * 400) , 2);
//                car.setFill(Color.GREEN);
//
//                Platform.runLater(() -> drawPane.getChildren().add(car));
//            }));

            viewLoader.getController().generateStatLabels(component.getName(), componentStreetSections, componentSinks);
            Platform.runLater(() -> vBoxContainer.getChildren().add(statNode));
        });
    }

    public double getCurrentSimSpeed() {
        return CURRENT_SIM_SPEED.get();
    }

    public void setStartRunnable(Runnable startRunnable) {
        this.startRunnable = startRunnable;
    }

    public void setFinishRunnable(Runnable finishRunnable) {
        this.finishRunnable = finishRunnable;
    }

    public void setPauseRunnable(Runnable pauseRunnable) {
        this.pauseRunnable = pauseRunnable;
    }

    public void setDoStepRunnable(Runnable doStepRunnable) {
        this.doStepRunnable = doStepRunnable;
    }

    public void setProceedRunnable(Runnable proceedRunnable) {
        this.proceedRunnable = proceedRunnable;
    }
}
