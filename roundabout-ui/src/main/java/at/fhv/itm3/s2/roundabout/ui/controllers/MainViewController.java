package at.fhv.itm3.s2.roundabout.ui.controllers;

import at.fhv.itm3.s2.roundabout.entity.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.ui.controllers.core.JfxController;
import at.fhv.itm3.s2.roundabout.ui.util.ViewLoader;
import at.fhv.itm3.s2.roundabout.util.dto.Component;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class MainViewController extends JfxController {

    private static final int DEFAULT_SIM_SPEED_VALUE = 0;
    private static final int MAX_SIM_SPEED_VALUE = 10;

    @FXML private Label lblCurrentSimSpeed;
    @FXML private Label lblMinSimSpeed;
    @FXML private Label lblMaxSimSpeed;
    @FXML private Slider sliderSimSpeed;
    @FXML private ScrollPane scrollPane;
    @FXML private ImageView imageView;

    @FXML private HBox hBoxContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageView.fitWidthProperty().bind(scrollPane.widthProperty());

        lblCurrentSimSpeed.textProperty().bind(sliderSimSpeed.valueProperty().asString());
        lblMinSimSpeed.textProperty().bind(sliderSimSpeed.minProperty().asString());
        lblMaxSimSpeed.textProperty().bind(sliderSimSpeed.maxProperty().asString());

        sliderSimSpeed.setMin(DEFAULT_SIM_SPEED_VALUE);
        sliderSimSpeed.setMax(MAX_SIM_SPEED_VALUE);
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
}
