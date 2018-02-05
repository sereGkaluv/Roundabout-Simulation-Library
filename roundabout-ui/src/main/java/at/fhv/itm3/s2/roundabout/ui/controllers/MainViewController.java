package at.fhv.itm3.s2.roundabout.ui.controllers;

import at.fhv.itm3.s2.roundabout.entity.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.ui.controllers.core.JfxController;
import at.fhv.itm3.s2.roundabout.ui.util.DaemonThreadFactory;
import at.fhv.itm3.s2.roundabout.ui.util.ViewLoader;
import at.fhv.itm3.s2.roundabout.util.dto.Component;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.*;

public class MainViewController extends JfxController {

    private static final int DEFAULT_SIM_SPEED_VALUE = 0;
    private static final int MAX_SIM_SPEED_VALUE = 50;

    private static final SimpleBooleanProperty IS_SIMULATION_RUNNING = new SimpleBooleanProperty(false);
    private static final SimpleBooleanProperty IS_SIMULATION_PAUSED = new SimpleBooleanProperty(false);

    private static final SimpleDoubleProperty CURRENT_SIM_SPEED = new SimpleDoubleProperty(0);

    @FXML private Button btnStartSimulation;
    @FXML private Button btnFinishSimulation;
    @FXML private Button btnPauseSimulation;
    @FXML private Button btnProceedSimulation;
    @FXML private Button btnDoStepOfSimulation;
    @FXML private BorderPane borderPaneContainer;
    @FXML private Label lblCurrentSimSpeed;
    @FXML private Slider sliderSimSpeed;
    @FXML private Label lblProgress;
    @FXML private ProgressBar progressBar;
    @FXML private ImageView imageView;
    @FXML private HBox hBoxContainer;

    private Runnable startRunnable;
    private Runnable finishRunnable;
    private Runnable pauseRunnable;
    private Runnable doStepRunnable;
    private Runnable proceedRunnable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnStartSimulation.managedProperty().bind(IS_SIMULATION_RUNNING.not());
        btnStartSimulation.visibleProperty().bind(btnStartSimulation.managedProperty());

        btnFinishSimulation.managedProperty().bind(IS_SIMULATION_RUNNING);
        btnFinishSimulation.visibleProperty().bind(btnFinishSimulation.managedProperty());

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

        lblCurrentSimSpeed.textProperty().bind(Bindings.format("Slowdown factor: %.2f", CURRENT_SIM_SPEED));
        lblCurrentSimSpeed.managedProperty().bind(sliderSimSpeed.managedProperty());
        lblCurrentSimSpeed.visibleProperty().bind(sliderSimSpeed.visibleProperty());

        lblProgress.textProperty().bind(progressBar.progressProperty().multiply(100).asString("Progress: %.0f %%"));
        lblProgress.managedProperty().bind(IS_SIMULATION_RUNNING);
        lblProgress.visibleProperty().bind(lblProgress.managedProperty());

        imageView.fitWidthProperty().bind(borderPaneContainer.widthProperty());

        initButtonListeners();
    }

    private void initButtonListeners() {
        btnStartSimulation.setOnAction(e -> {
            IS_SIMULATION_RUNNING.set(true);
            new DaemonThreadFactory().newThread(startRunnable).start();
        });

        btnFinishSimulation.setOnAction(e -> {
            IS_SIMULATION_RUNNING.set(false);
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
        Platform.runLater(() -> hBoxContainer.getChildren().clear());
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

            viewLoader.getController().generateStatLabels(component.getName(), componentStreetSections, componentSinks);
            Platform.runLater(() -> hBoxContainer.getChildren().add(statNode));
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
