package at.fhv.itm3.s2.roundabout.ui.controllers;

import at.fhv.itm3.s2.roundabout.entity.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.ui.controllers.core.JfxController;
import at.fhv.itm3.s2.roundabout.ui.util.ViewLoader;
import at.fhv.itm3.s2.roundabout.util.dto.Component;
import desmoj.core.simulator.SimClock;
import desmoj.core.simulator.TimeInstant;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
    private boolean isSimulationRunning = false;

    @FXML private Button btnStartSimulation;
    @FXML private BorderPane borderPaneContainer;
    @FXML private Label lblCurrentSimSpeed;
    @FXML private Slider sliderSimSpeed;
    @FXML private Label lblProgress;
    @FXML private ProgressBar progressBar;
    @FXML private ImageView imageView;
    @FXML private HBox hBoxContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageView.fitWidthProperty().bind(borderPaneContainer.widthProperty());
        lblCurrentSimSpeed.textProperty().bind(Bindings.format("%.2f", sliderSimSpeed.valueProperty()));
        btnStartSimulation.setText("Start Simulation");
        sliderSimSpeed.setShowTickMarks(true);
        sliderSimSpeed.setShowTickLabels(true);
        sliderSimSpeed.setMajorTickUnit(25);
        sliderSimSpeed.setBlockIncrement(5);
        sliderSimSpeed.setMin(DEFAULT_SIM_SPEED_VALUE);
        sliderSimSpeed.setMax(MAX_SIM_SPEED_VALUE);

        lblProgress.textProperty().bind(progressBar.progressProperty().multiply(100).asString("%.0f %%"));

        btnStartSimulation.setOnAction(e -> {
            isSimulationRunning = !isSimulationRunning;
            if(isSimulationRunning){
                btnStartSimulation.setText("Cancel Simulation");
                sliderSimSpeed.setVisible(false);
                //TODO start simulation
            }else{
                btnStartSimulation.setText("Start Simulation");
                sliderSimSpeed.setVisible(true);
                //TODO stop simulation
            }
        });

        DropShadow shadow = new DropShadow();
        //Adding the shadow when the mouse cursor is on
        btnStartSimulation.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> btnStartSimulation.setEffect(shadow));
        //Removing the shadow when the mouse cursor is off
        btnStartSimulation.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> btnStartSimulation.setEffect(null));
    }

    public void setProgressSupplier(SimClock simClock, TimeInstant stopTime) {
        simClock.addObserver((o, arg) -> {
            final double progress = simClock.getTime().getTimeAsDouble() / stopTime.getTimeAsDouble();
            Platform.runLater(() -> progressBar.setProgress(progress));
        });
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

    public Label getLblCurrentSimSpeed(){ return lblCurrentSimSpeed; }
    public boolean isSimulationRunning(){ return isSimulationRunning; }
}
