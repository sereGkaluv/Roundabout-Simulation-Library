package at.fhv.itm3.s2.roundabout.ui.controllers;

import at.fhv.itm3.s2.roundabout.api.CarObserverType;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.ui.controllers.core.JfxController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StatsViewController extends JfxController {

    private static final String KEY_FORMAT = "%s%s";

    private static final String SOURCE_ID_SUFFIX = "_id";
    private static final String SOURCE_IS_SUFFIX = "_is";
    private static final String SOURCE_PS_SUFFIX = "_ps";

    private static final String SINK_ID_SUFFIX = "_id";
    private static final String SINK_MIN_SUFFIX = "_min";
    private static final String SINK_AVG_SUFFIX = "_avg";
    private static final String SINK_MAX_SUFFIX = "_max";

    private final Map<String, Double> sinkStats = new HashMap<>();
    private final Map<String, Label> labelMap = new HashMap<>();

    @FXML private Label lblStatsTitle;

    @FXML private VBox sectionIdContainer;
    @FXML private VBox sectionISContainer;
    @FXML private VBox sectionPSContainer;

    @FXML private VBox sinkIdContainer;
    @FXML private VBox sinkMinContainer;
    @FXML private VBox sinkAvgContainer;
    @FXML private VBox sinkMaxContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void generateStatLabels(String title, Collection<StreetSection> streetSections, Collection<RoundaboutSink> sinks) {
        Platform.runLater(() -> lblStatsTitle.setText(title));

        streetSections.forEach(streetSection -> {
            final String sourceId = String.format(KEY_FORMAT, streetSection.getId(), SOURCE_ID_SUFFIX);
            final String sourceIS = String.format(KEY_FORMAT, streetSection.getId(), SOURCE_IS_SUFFIX);
            final String sourcePS = String.format(KEY_FORMAT, streetSection.getId(), SOURCE_PS_SUFFIX);

            final Label lblSourceId = new Label(sourceId);
            sectionIdContainer.getChildren().add(lblSourceId);
            labelMap.put(sourceId, lblSourceId);

            final Label lblSourceIS = new Label(sourceIS);
            sectionISContainer.getChildren().add(lblSourceIS);
            labelMap.put(sourceIS, lblSourceIS);

            final Label lblSourcePS = new Label(sourcePS);
            sectionPSContainer.getChildren().add(lblSourcePS);
            labelMap.put(sourcePS, lblSourcePS);

            streetSection.addObserver(CarObserverType.CAR_ENTERED, (o, arg) -> {
                final String rawValue = toStringOrEmpty(arg);
                final long is_counter = Math.max(Long.valueOf(rawValue) - streetSection.getNrOfLeftCars(), 0);
                Platform.runLater(() ->
                    lblSourceIS.setText(toStringOrEmpty(is_counter))
                );
            });

            streetSection.addObserver(CarObserverType.CAR_LEFT, (o, arg) -> {
                final String rawValue = toStringOrEmpty(arg);
                Platform.runLater(() ->
                    lblSourcePS.setText(rawValue)
                );

                final long is_counter = Math.max(streetSection.getNrOfEnteredCars() - Long.valueOf(rawValue), 0);
                Platform.runLater(() ->
                    lblSourceIS.setText(toStringOrEmpty(is_counter))
                );
            });
        });

        sinks.forEach(sink -> {
            final String sinkId = String.format(KEY_FORMAT, sink.getId(), SINK_ID_SUFFIX);
            final String sinkMin = String.format(KEY_FORMAT, sink.getId(), SINK_MIN_SUFFIX);
            final String sinkAvg = String.format(KEY_FORMAT, sink.getId(), SINK_AVG_SUFFIX);
            final String sinkMax = String.format(KEY_FORMAT, sink.getId(), SINK_MAX_SUFFIX);

            final Label lblSinkId = new Label(sinkId);
            sinkIdContainer.getChildren().add(lblSinkId);
            labelMap.put(sinkId, lblSinkId);

            final Label lblSinkMin = new Label(sinkMin);
            sinkMinContainer.getChildren().add(lblSinkMin);
            labelMap.put(sinkMin, lblSinkMin);

            final Label lblSinkAvg = new Label(sinkAvg);
            sinkAvgContainer.getChildren().add(lblSinkAvg);
            labelMap.put(sinkAvg, lblSinkAvg);

            final Label lblSinkMax = new Label(sinkMax);
            sinkMaxContainer.getChildren().add(lblSinkMax);
            labelMap.put(sinkMax, lblSinkMax);

            sink.addObserver(CarObserverType.CAR_ENTERED, (o, arg) -> {
                final String rawValue = toStringOrEmpty(arg);
                final Double carWaitTime = Double.valueOf(rawValue);

                final double minValue = sinkStats.getOrDefault(sinkMin, Double.MAX_VALUE);
                if (carWaitTime < minValue) {
                    sinkStats.put(sinkMin, carWaitTime);

                    final String sinkMinValue = toStringOrEmpty(carWaitTime);
                    Platform.runLater(() -> lblSinkMin.setText(sinkMinValue));
                }

                final String sinkAvgValue = toStringOrEmpty(sink.getMeanWaitingTimePerStopForEnteredCars());
                Platform.runLater(() -> lblSinkAvg.setText(sinkAvgValue));

                final double maxValue = sinkStats.getOrDefault(sinkMax, Double.MIN_VALUE);
                if (carWaitTime > maxValue) {
                    sinkStats.put(sinkMax, carWaitTime);

                    final String sinkMaxValue = toStringOrEmpty(carWaitTime);
                    Platform.runLater(() -> lblSinkMax.setText(sinkMaxValue));
                }
            });
        });
    }

    private String toStringOrEmpty(Object value) {
        return value != null ? value.toString() : "0";
    }
}
