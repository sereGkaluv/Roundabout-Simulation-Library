package at.fhv.itm3.s2.roundabout.ui.controllers;

import at.fhv.itm3.s2.roundabout.entity.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.ui.controllers.core.JfxController;
import at.fhv.itm3.s2.roundabout.ui.util.ViewLoader;
import at.fhv.itm3.s2.roundabout.util.dto.Component;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;

public class MainViewController extends JfxController {

    private static final int DEFAULT_SIM_SPEED_VALUE = 0;
    private static final int MAX_SIM_SPEED_VALUE = 50;
    private static final SimpleBooleanProperty IS_SIMULATION_RUNNING = new SimpleBooleanProperty(false);
    private static final SimpleBooleanProperty IS_SIMULATION_PAUSED = new SimpleBooleanProperty(false);

    @FXML private Button btnStartSimulation;
    @FXML private Button btnPauseSimulation;
    @FXML private BorderPane borderPaneContainer;
    @FXML private Label lblCurrentSimSpeed;
    @FXML private Slider sliderSimSpeed;
    @FXML private Label lblProgress;
    @FXML private ProgressBar progressBar;
    @FXML private ImageView imageView;
    @FXML private HBox hBoxContainer;

    private Runnable startRunnable;
    private Runnable stopRunnable;
    private Runnable proceedRunnable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageView.fitWidthProperty().bind(borderPaneContainer.widthProperty());
        lblCurrentSimSpeed.textProperty().bind(Bindings.format("%.2f", sliderSimSpeed.valueProperty()));
        btnStartSimulation.setText("Start Simulation");
        btnPauseSimulation.setText("Pause Simulation");
        sliderSimSpeed.setShowTickMarks(true);
        sliderSimSpeed.setShowTickLabels(true);
        sliderSimSpeed.setMajorTickUnit(25);
        sliderSimSpeed.setBlockIncrement(5);
        sliderSimSpeed.setMin(DEFAULT_SIM_SPEED_VALUE);
        sliderSimSpeed.setMax(MAX_SIM_SPEED_VALUE);

        lblProgress.textProperty().bind(progressBar.progressProperty().multiply(100).asString("%.0f %%"));

        sliderSimSpeed.visibleProperty().bind(IS_SIMULATION_RUNNING.not());

        //initialize Start/Cancel Button
        btnStartSimulation.setOnAction(e -> {
            if(IS_SIMULATION_RUNNING.get()){
                btnStartSimulation.setText("Start Simulation");
                stopRunnable.run();
            }else{
                btnStartSimulation.setText("Cancel Simulation");
                startRunnable.run();
            }
            IS_SIMULATION_RUNNING.set(!IS_SIMULATION_RUNNING.get());
        });

        //Adding the shadow when the mouse cursor is on
        btnStartSimulation.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> btnStartSimulation.setEffect(new DropShadow()));
        //Removing the shadow when the mouse cursor is off
        btnStartSimulation.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> btnStartSimulation.setEffect(null));

        //initialize Pause/Proceed Button
        btnPauseSimulation.setOnAction(e -> {
            if(IS_SIMULATION_PAUSED.get()){
                btnPauseSimulation.setText("Proceed Simulation");
                stopRunnable.run();
            }else{
                btnPauseSimulation.setText("Pause Simulation");
                proceedRunnable.run();
            }
            IS_SIMULATION_PAUSED.set(!IS_SIMULATION_PAUSED.get());
        });

        //Adding the shadow when the mouse cursor is on
        btnPauseSimulation.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> btnPauseSimulation.setEffect(new DropShadow()));
        //Removing the shadow when the mouse cursor is off
        btnPauseSimulation.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> btnPauseSimulation.setEffect(null));
    }

    public void setProgress(double progressValue) {
        Platform.runLater(() -> progressBar.setProgress(progressValue));
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
        return Double.valueOf(lblCurrentSimSpeed.getText());
    }

    public void setStartRunnable(Runnable startRunnable) {
        this.startRunnable = startRunnable;
    }

    public void setStopRunnable(Runnable stopRunnable) {
        this.stopRunnable = stopRunnable;
    }

    public void setProceedRunnable(Runnable proceedRunnable) {
        this.proceedRunnable = proceedRunnable;
    }
}
