package at.fhv.itm3.s2.roundabout.ui.controllers;

import at.fhv.itm3.s2.roundabout.api.util.observable.ObserverType;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.ui.controllers.core.JfxController;
import at.fhv.itm3.s2.roundabout.ui.util.DaemonThreadFactory;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

public class StatsViewController extends JfxController {

    private static final String KEY_FORMAT = "%s%s";

    private static final String SINK_MIN_SUFFIX = "_min";
    private static final String SINK_AVG_SUFFIX = "_avg";
    private static final String SINK_MAX_SUFFIX = "_max";

    private static final String AVG = "AVG:";
    private static final String NOT_AVAILABLE = "N/A";

    private static final int TRAFFIC_LIGHT_INDICATOR_SIZE = 8;

    private static final Function<Double, String> DOUBLE_STRING_FORMATTER_FUNCTION = v -> String.format("%.2f", v);

    private final Map<String, Long> sinkPSStats = new HashMap<>();
    private final Map<String, Double> sinkMinStats = new HashMap<>();
    private final Map<String, Double> sinkAvgStats = new HashMap<>();
    private final Map<String, Double> sinkMaxStats = new HashMap<>();

    @FXML private Label lblStatsTitle;

    @FXML private VBox sectionIdContainer;
    @FXML private VBox sectionISContainer;
    @FXML private VBox sectionPSContainer;

    @FXML private VBox sinkIdContainer;
    @FXML private VBox sinkPSContainer;
    @FXML private VBox sinkMinContainer;
    @FXML private VBox sinkAvgContainer;
    @FXML private VBox sinkMaxContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void generateStatLabels(String title, Collection<StreetSection> streetSections, Collection<RoundaboutSink> sinks) {
        Platform.runLater(() -> lblStatsTitle.setText(title));

        generateStreetSectionLabels(streetSections);
        generateSinkLabels(sinks);
    }

    private void generateStreetSectionLabels(Collection<StreetSection> streetSections) {
        streetSections.stream().sorted(Comparator.comparing(StreetSection::getId)).forEach(streetSection -> {

            final Label lblSourceId = new Label(streetSection.getId());
            final Rectangle trafficLight = new Rectangle(TRAFFIC_LIGHT_INDICATOR_SIZE, TRAFFIC_LIGHT_INDICATOR_SIZE, Color.GRAY);
            trafficLight.setStroke(Color.BLACK);
            lblSourceId.setGraphic(trafficLight);
            sectionIdContainer.getChildren().add(lblSourceId);

            final Label lblSourceIS = new Label(NOT_AVAILABLE);
            sectionISContainer.getChildren().add(lblSourceIS);

            final Label lblSourcePS = new Label(NOT_AVAILABLE);
            sectionPSContainer.getChildren().add(lblSourcePS);

            streetSection.addObserver(ObserverType.TRAFFIC_LIGHT, ((o, arg) -> {
                if (streetSection.isTrafficLightFreeToGo()) {
                    trafficLight.setFill(Color.GREEN);
                } else if (!streetSection.isTrafficLightActive()) {
                    trafficLight.setFill(Color.GRAY);
                } else {
                    trafficLight.setFill(Color.RED);
                }
            }));

            streetSection.addObserver(ObserverType.CAR_ENTERED, (o, arg) -> {
                final String rawValue = toStringOrEmpty(arg) ;
                final long longValue = rawValue != null ? Long.valueOf(rawValue) : 0;
                final long is_counter = Math.max(longValue - streetSection.getNrOfLeftCars(), 0);
                Platform.runLater(() ->
                    lblSourceIS.setText(toStringOrEmpty(is_counter))
                );
            });

            streetSection.addObserver(ObserverType.CAR_LEFT, (o, arg) -> {
                final String rawValue = toStringOrEmpty(arg);
                final long longValue = rawValue != null ? Long.valueOf(rawValue) : 0;
                Platform.runLater(() ->
                    lblSourcePS.setText(rawValue)
                );

                final long is_counter = Math.max(streetSection.getNrOfEnteredCars() - longValue, 0);
                Platform.runLater(() ->
                    lblSourceIS.setText(toStringOrEmpty(is_counter))
                );
            });
        });
    }

    private void generateSinkLabels(Collection<RoundaboutSink> sinks) {
        final Label lblAverageSink = new Label(AVG);
        final Label lblPSAverageSink = new Label(NOT_AVAILABLE);
        final Label lblMinAverageSink = new Label(NOT_AVAILABLE);
        final Label lblAvgAverageSink = new Label(NOT_AVAILABLE);
        final Label lblMaxAverageSink = new Label(NOT_AVAILABLE);

        sinks.stream().sorted(Comparator.comparing(RoundaboutSink::getId)).forEach(sink -> {
            final String sinkMin = String.format(KEY_FORMAT, sink.getId(), SINK_MIN_SUFFIX);
            final String sinkAvg = String.format(KEY_FORMAT, sink.getId(), SINK_AVG_SUFFIX);
            final String sinkMax = String.format(KEY_FORMAT, sink.getId(), SINK_MAX_SUFFIX);

            final Label lblSinkId = new Label(sink.getId());
            final Rectangle trafficLight = new Rectangle(TRAFFIC_LIGHT_INDICATOR_SIZE, TRAFFIC_LIGHT_INDICATOR_SIZE, Color.GRAY);
            trafficLight.setStroke(Color.BLACK);
            lblSinkId.setGraphic(trafficLight);
            sinkIdContainer.getChildren().add(lblSinkId);

            final Label lblSinkPS = new Label(NOT_AVAILABLE);
            sinkPSContainer.getChildren().add(lblSinkPS);

            final Label lblSinkMin = new Label(NOT_AVAILABLE);
            sinkMinContainer.getChildren().add(lblSinkMin);

            final Label lblSinkAvg = new Label(NOT_AVAILABLE);
            sinkAvgContainer.getChildren().add(lblSinkAvg);

            final Label lblSinkMax = new Label(NOT_AVAILABLE);
            sinkMaxContainer.getChildren().add(lblSinkMax);

            sink.addObserver(ObserverType.TRAFFIC_LIGHT, ((o, arg) -> {
                if (sink.isTrafficLightFreeToGo()) {
                    trafficLight.setFill(Color.GREEN);
                } else if (!sink.isTrafficLightActive()) {
                    trafficLight.setFill(Color.GRAY);
                } else {
                    trafficLight.setFill(Color.RED);
                }
            }));

            sink.addObserver(ObserverType.CAR_LEFT, (o, arg) -> {
                final long nrOfLeftCars = sink.getNrOfLeftCars();
                final String rawValue = toStringOrEmpty(nrOfLeftCars);
                sinkPSStats.put(sinkMax, nrOfLeftCars);

                final OptionalDouble optionalAvgSinkPSValue = sinkPSStats.values().stream().mapToLong(v -> v).average();
                final String avgSinkPSValue;
                if (optionalAvgSinkPSValue.isPresent()) {
                    avgSinkPSValue = toStringOrEmpty(optionalAvgSinkPSValue.getAsDouble(), DOUBLE_STRING_FORMATTER_FUNCTION);
                } else {
                    avgSinkPSValue = NOT_AVAILABLE;
                }
                Platform.runLater(() -> {
                    lblSinkPS.setText(rawValue);
                    lblPSAverageSink.setText(avgSinkPSValue);
                });
            });

            sink.addObserver(ObserverType.CAR_ENTITY, (o, arg) -> {
                final Double carWaitTime = sink.getMeanWaitingTimePerStopForEnteredCars();

                final double minValue = sinkMinStats.getOrDefault(sinkMin, Double.MAX_VALUE);
                if (carWaitTime < minValue || !sinkMinStats.containsKey(sinkMin)) {
                    sinkMinStats.put(sinkMin, carWaitTime);

                    final String sinkMinValue = toStringOrEmpty(carWaitTime, DOUBLE_STRING_FORMATTER_FUNCTION);
                    final OptionalDouble optionalAvgSinkMinValue = sinkMinStats.values().stream().mapToDouble(v -> v).average();
                    final String avgSinkMinValue;
                    if (optionalAvgSinkMinValue.isPresent()) {
                        avgSinkMinValue = toStringOrEmpty(optionalAvgSinkMinValue.getAsDouble(), DOUBLE_STRING_FORMATTER_FUNCTION);
                    } else {
                        avgSinkMinValue = NOT_AVAILABLE;
                    }

                    Platform.runLater(() -> {
                        lblSinkMin.setText(sinkMinValue);
                        lblMinAverageSink.setText(avgSinkMinValue);
                    });
                }

                final String sinkAvgValue = toStringOrEmpty(carWaitTime, DOUBLE_STRING_FORMATTER_FUNCTION);
                final OptionalDouble optionalAvgSinkAvgValue = sinkAvgStats.values().stream().mapToDouble(v -> v).average();
                final String avgSinkAvgValue;
                if (optionalAvgSinkAvgValue.isPresent()) {
                    avgSinkAvgValue = toStringOrEmpty(optionalAvgSinkAvgValue.getAsDouble(), DOUBLE_STRING_FORMATTER_FUNCTION);
                } else {
                    avgSinkAvgValue = NOT_AVAILABLE;
                }

                sinkAvgStats.put(sinkAvg, carWaitTime);
                Platform.runLater(() -> {
                    lblSinkAvg.setText(sinkAvgValue);
                    lblAvgAverageSink.setText(avgSinkAvgValue);
                });

                final double maxValue = sinkMaxStats.getOrDefault(sinkMax, Double.MIN_VALUE);
                if (carWaitTime > maxValue || !sinkMaxStats.containsKey(sinkMax)) {
                    sinkMaxStats.put(sinkMax, carWaitTime);

                    final String sinkMaxValue = toStringOrEmpty(carWaitTime, DOUBLE_STRING_FORMATTER_FUNCTION);
                    final OptionalDouble optionalAvgSinkMaxValue = sinkMaxStats.values().stream().mapToDouble(v -> v).average();
                    final String avgSinkMaxValue;
                    if (optionalAvgSinkMaxValue.isPresent()) {
                        avgSinkMaxValue = toStringOrEmpty(optionalAvgSinkMaxValue.getAsDouble(), DOUBLE_STRING_FORMATTER_FUNCTION);
                    } else {
                        avgSinkMaxValue = NOT_AVAILABLE;
                    }

                    Platform.runLater(() -> {
                        lblSinkMax.setText(sinkMaxValue);
                        lblMaxAverageSink.setText(avgSinkMaxValue);
                    });
                }
            });
        });

        // Adding average value labels.
        sinkIdContainer.getChildren().add(new Separator());
        sinkIdContainer.getChildren().add(lblAverageSink);

        sinkPSContainer.getChildren().add(new Separator());
        sinkPSContainer.getChildren().add(lblPSAverageSink);

        sinkMinContainer.getChildren().add(new Separator());
        sinkMinContainer.getChildren().add(lblMinAverageSink);

        sinkAvgContainer.getChildren().add(new Separator());
        sinkAvgContainer.getChildren().add(lblAvgAverageSink);

        sinkMaxContainer.getChildren().add(new Separator());
        sinkMaxContainer.getChildren().add(lblMaxAverageSink);
    }

    private <T> String toStringOrEmpty(T value) {
        return toStringOrEmpty(value, Object::toString);
    }

    private <T> String toStringOrEmpty(T value, Function<T, String> converterFunction) {
        return value != null ? converterFunction.apply(value) : null;
    }
}
