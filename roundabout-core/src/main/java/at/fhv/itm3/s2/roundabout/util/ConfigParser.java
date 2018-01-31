package at.fhv.itm3.s2.roundabout.util;

import at.fhv.itm14.trafsim.model.ModelFactory;
import at.fhv.itm14.trafsim.model.entities.AbstractConsumer;
import at.fhv.itm14.trafsim.model.entities.AbstractProducer;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.intersection.FixedCirculationController;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.ConsumerType;
import at.fhv.itm3.s2.roundabout.api.entity.IRoundaboutStructure;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.controller.IntersectionController;
import at.fhv.itm3.s2.roundabout.entity.*;
import at.fhv.itm3.s2.roundabout.util.dto.*;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;

import javax.xml.bind.JAXB;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class ConfigParser {
    private static final String MIN_TIME_BETWEEN_CAR_ARRIVALS = "MIN_TIME_BETWEEN_CAR_ARRIVALS";
    private static final String MAX_TIME_BETWEEN_CAR_ARRIVALS = "MAX_TIME_BETWEEN_CAR_ARRIVALS";
    private static final String MIN_DISTANCE_FACTOR_BETWEEN_CARS = "MIN_DISTANCE_FACTOR_BETWEEN_CARS";
    private static final String MAX_DISTANCE_FACTOR_BETWEEN_CARS = "MAX_DISTANCE_FACTOR_BETWEEN_CARS";
    private static final String MAIN_ARRIVAL_RATE_FOR_ONE_WAY_STREETS = "MAIN_ARRIVAL_RATE_FOR_ONE_WAY_STREETS";
    private static final String STANDARD_CAR_ACCELERATION_TIME = "STANDARD_CAR_ACCELERATION_TIME";

    private static final String INTERSECTION_SIZE = "INTERSECTION_SIZE";
    private static final String INTERSECTION_SERVICE_DELAY = "INTERSECTION_SERVICE_DELAY";
    private static final String INTERSECTION_TRAVERSE_TIME = "INTERSECTION_TRAVERSE_TIME";
    private static final String CONTROLLER_GREEN_DURATION = "CONTROLLER_GREEN_DURATION";
    private static final String CONTROLLER_YELLOW_DURATION = "CONTROLLER_YELLOW_DURATION";
    private static final String CONTROLLER_PHASE_SHIFT_TIME = "CONTROLLER_PHASE_SHIFT_TIME";

    private static final Map<String, Map<String, RoundaboutSource>> SOURCE_REGISTRY = new HashMap<>();
    private static final Map<String, Map<String, RoundaboutSink>> SINK_REGISTRY = new HashMap<>();
    private static final Map<String, Map<String, StreetSection>> SECTION_REGISTRY = new HashMap<>();
    private static final Comparator<Track> TRACK_COMPARATOR = Comparator.comparingLong(Track::getOrder);
    private static final Function<Connector, List<Track>> SORTED_TRACK_EXTRACTOR = co -> co.getTrack().stream().sorted(TRACK_COMPARATOR).collect(Collectors.toList());

    private String filename;

    public ConfigParser(String filename) {
        this.filename = filename;
    }

    public ModelConfig loadConfig() throws ConfigParserException {
        File configFile = new File(filename);
        if (!configFile.exists()) {
            throw new ConfigParserException("No such config file " + filename);
        }
        return JAXB.unmarshal(configFile, ModelConfig.class);
    }

    public IRoundaboutStructure generateRoundaboutStructure(ModelConfig modelConfig, Experiment experiment) {
        final Map<String, String> parameters = handleParameters(modelConfig);

        RoundaboutSimulationModel model = new RoundaboutSimulationModel(
            null,
            modelConfig.getName(),
            false,
            false,
            extractParameter(parameters::get, Double::valueOf, MIN_TIME_BETWEEN_CAR_ARRIVALS),
            extractParameter(parameters::get, Double::valueOf, MAX_TIME_BETWEEN_CAR_ARRIVALS),
            extractParameter(parameters::get, Double::valueOf, MIN_DISTANCE_FACTOR_BETWEEN_CARS),
            extractParameter(parameters::get, Double::valueOf, MAX_DISTANCE_FACTOR_BETWEEN_CARS),
            extractParameter(parameters::get, Double::valueOf, MAIN_ARRIVAL_RATE_FOR_ONE_WAY_STREETS),
            extractParameter(parameters::get, Double::valueOf, STANDARD_CAR_ACCELERATION_TIME)
        );
        model.connectToExperiment(experiment);

        IRoundaboutStructure roundaboutStructure = new RoundaboutStructure(model, parameters);

        handleComponents(roundaboutStructure, modelConfig.getComponents());
        handleConnectors(null, modelConfig.getComponents().getConnectors());

        return roundaboutStructure;
    }
    private Map<String, String> handleParameters(ModelConfig modelConfig) {
        final Map<String, String> modelParameters = new HashMap<>();
        Consumer<Parameter> parameterRegistrator = p -> modelParameters.put(p.getName(), p.getValue());
        modelConfig.getParameters().getParameter().forEach(parameterRegistrator);

        final List<Component> componentList = modelConfig.getComponents().getComponent();
        for (Component component : componentList) {
            component.getParameters().getParameter().forEach(parameterRegistrator);
        }
        return modelParameters;
    }

    private void handleComponents(IRoundaboutStructure roundaboutStructure, Components components) {
        for (Component component : components.getComponent()) {
            switch (component.getType()) {
                case ROUNDABOUT: {
                    handleRoundabout(roundaboutStructure, component);
                    break;
                }

                case INTERSECTION: {
                    handleIntersection(roundaboutStructure, component);
                    break;
                }

                default: throw new IllegalArgumentException("Unknown component type detected.");
            }
        }
    }

    private void handleRoundabout(IRoundaboutStructure roundaboutStructure, Component roundaboutComponent) {
        final Model model = roundaboutStructure.getModel();

        // Handle configuration.
        handleSections(
            roundaboutComponent.getId(),
            roundaboutComponent.getSections(),
            model
        );

        handleSinks(
            roundaboutComponent.getId(),
            roundaboutComponent.getSinks(),
            model
        );

        handleSources(
            roundaboutComponent.getId(),
            roundaboutComponent.getSources(),
            model
        );

        handleConnectors(
            roundaboutComponent.getId(),
            roundaboutComponent.getConnectors()
        );
    }

    private void handleIntersection(IRoundaboutStructure roundaboutStructure, Component intersectionComponent) {
        final Model model = roundaboutStructure.getModel();

        // Init intersection.
        final RoundaboutIntersection intersection = new RoundaboutIntersection(
            model,
            intersectionComponent.getName(),
            false,
            Integer.parseInt(roundaboutStructure.getParameter(INTERSECTION_SIZE))
        );

        intersection.setServiceDelay(
            extractParameter(roundaboutStructure::getParameter, Double::valueOf, INTERSECTION_SERVICE_DELAY)
        );
        final FixedCirculationController ic = ModelFactory.getInstance(model).createOneWayController(
            intersection,
            extractParameter(roundaboutStructure::getParameter, Double::valueOf, CONTROLLER_GREEN_DURATION),
            extractParameter(roundaboutStructure::getParameter, Double::valueOf, CONTROLLER_YELLOW_DURATION),
            extractParameter(roundaboutStructure::getParameter, Double::valueOf, CONTROLLER_PHASE_SHIFT_TIME)
        );
        intersection.attachController(ic);

        // Handle the rest of configuration.
        handleSections(
            intersectionComponent.getId(),
            intersectionComponent.getSections(),
            model
        );

        handleSinks(
            intersectionComponent.getId(),
            intersectionComponent.getSinks(),
            model
        );

        handleSources(
            intersectionComponent.getId(),
            intersectionComponent.getSources(),
            model
        );

        // Handle connectors (is specific for intersections).
        final IntersectionController intersectionController = IntersectionController.getInstance();
        final Map<String, Integer> DIRECTION_MAP = new HashMap<>();

        // Assembling intersection sections (init connection queues, connectors, etc.)
        int directionIndexer = 0;
        double intersectionTraverseTime = Double.valueOf(roundaboutStructure.getParameter(INTERSECTION_TRAVERSE_TIME));
        for (Connector connector : intersectionComponent.getConnectors().getConnector()) {
            final List<Track> trackList = SORTED_TRACK_EXTRACTOR.apply(connector);
            for (Track track : trackList) {
                // In direction.
                final String fromSectionId = track.getFromSectionId();
                final Street fromSection = resolveStreet(intersectionComponent.getId(), fromSectionId, track.getFromSectionType());
                if (!DIRECTION_MAP.containsKey(fromSectionId)) {
                    DIRECTION_MAP.put(fromSectionId, directionIndexer++);

                    // Handling section -> intersections connectors, should be done only once per section.
                    // Connection intersection -> section is handled via connection queue.
                    final String connectorId = String.format("%s_%s_is", connector.getId(), fromSectionId);
                    final Collection<IConsumer> previousSections = new LinkedHashSet<>();
                    previousSections.add(fromSection);

                    final Collection<IConsumer> nextSections = new LinkedHashSet<>();
                    nextSections.add(intersection);

                    final StreetConnector streetConnector = new StreetConnector(connectorId, previousSections, nextSections);
                    streetConnector.initializeTrack(fromSection, track.getFromSectionType(), intersection, ConsumerType.INTERSECTION);
                }
                final int inDirection = DIRECTION_MAP.get(fromSectionId);
                intersectionController.setIntersectionInDirectionMapping(intersection, fromSection, inDirection);

                // Out direction.
                final String toSectionId = track.getToSectionId();
                final Street toSection = resolveStreet(intersectionComponent.getId(), toSectionId, track.getToSectionType());
                if (!DIRECTION_MAP.containsKey(toSectionId)) {
                    DIRECTION_MAP.put(toSectionId, directionIndexer++);
                }
                final int outDirection = DIRECTION_MAP.get(toSectionId);
                intersectionController.setIntersectionOutDirectionMapping(intersection, toSection, outDirection);

                // Connection queue.
                if (fromSection == null || toSection == null) {
                    throw new IllegalArgumentException(String.format(
                        "Please check if \"from\" section: \"%s\" and \"to\" section: \"%s\" were declared correctly." +
                        "(Connector id: \"%s\", Track id: \"%s\")",
                        fromSectionId,
                        toSectionId,
                        connector.getId(),
                        track.getOrder()
                    ));
                }

                final AbstractProducer producer = fromSection.toProducer();
                intersection.attachProducer(inDirection, producer);

                final AbstractConsumer consumer = toSection.toConsumer();
                intersection.attachConsumer(outDirection, consumer);

                intersection.createConnectionQueue(
                    producer,
                    new AbstractConsumer[]{consumer},
                    new double[]{intersectionTraverseTime},
                    new double[]{1} // probability should be always 1 in our case?
                );
            }
        }
    }

    private Map<String, RoundaboutSource> handleSources(String scopeComponentId, Sources sources, Model model) {
        return sources.getSource().stream().collect(toMap(
            Source::getId,
            so -> {
                final Street street = resolveSection(scopeComponentId, so.getSectionId());
                final RoundaboutSource source = new RoundaboutSource(model, so.getId(), false, street);
                if (!SOURCE_REGISTRY.containsKey(scopeComponentId)) {
                    SOURCE_REGISTRY.put(scopeComponentId, new HashMap<>());
                }

                SOURCE_REGISTRY.get(scopeComponentId).put(so.getId(), source);
                return source;
            }
        ));
    }

    private Map<String, RoundaboutSink> handleSinks(String scopeComponentId, Sinks sinks, Model model) {
        return sinks.getSink().stream().collect(toMap(
            Sink::getId,
            sk -> {
                final RoundaboutSink sink = new RoundaboutSink(model, sk.getId(), false);
                if (!SINK_REGISTRY.containsKey(scopeComponentId)) {
                    SINK_REGISTRY.put(scopeComponentId, new HashMap<>());
                }

                SINK_REGISTRY.get(scopeComponentId).put(sk.getId(), sink);
                return sink;
            }
        ));
    }

    private Map<String, StreetSection> handleSections(String scopeComponentId, Sections sections, Model model) {
        return sections.getSection().stream().collect(toMap(
            Section::getId,
            s -> {
                final StreetSection streetSection = new StreetSection(s.getLength(), model, s.getId(), false);
                if (!SECTION_REGISTRY.containsKey(scopeComponentId)) {
                    SECTION_REGISTRY.put(scopeComponentId, new HashMap<>());
                }

                SECTION_REGISTRY.get(scopeComponentId).put(s.getId(), streetSection);
                return streetSection;
            }
        ));
    }

    private Map<String, StreetConnector> handleConnectors(String scopeComponentId, Connectors connectors) {
        return connectors.getConnector().stream().collect(toMap(
            Connector::getId,
            co -> {
                final Collection<IConsumer> previousSections = new LinkedHashSet<>();
                final Collection<IConsumer> nextSections = new LinkedHashSet<>();

                final List<Consumer<StreetConnector>> trackInitializers = new LinkedList<>();
                final List<Track> trackList = SORTED_TRACK_EXTRACTOR.apply(co);
                for (Track track : trackList) {
                    final String fromComponentId = track.getFromComponentId() != null ? track.getFromComponentId() : scopeComponentId;
                    final Street fromSection = resolveStreet(fromComponentId, track.getFromSectionId(), track.getFromSectionType());
                    if (!previousSections.contains(fromSection)) previousSections.add(fromSection);

                    final String toComponentId = track.getToComponentId() != null ? track.getToComponentId() : scopeComponentId;
                    final Street toSection = resolveStreet(toComponentId, track.getToSectionId(), track.getToSectionType());
                    if (!nextSections.contains(toSection)) nextSections.add(toSection);

                    trackInitializers.add(connector -> connector.initializeTrack(
                            fromSection, track.getFromSectionType(), toSection, track.getToSectionType()
                    ));
                }

                final StreetConnector streetConnector = new StreetConnector(co.getId(), previousSections, nextSections);
                trackInitializers.forEach(streetConnectorConsumer -> streetConnectorConsumer.accept(streetConnector));

                return streetConnector;
            }
        ));
    }

    private Street resolveStreet(String componentId, String streetId, ConsumerType consumerType) {
        switch (consumerType) {
            case ROUNDABOUT_EXIT: return resolveSink(componentId, streetId);
            default: return resolveSection(componentId, streetId);
        }
    }

    private StreetSection resolveSection(String componentId, String sectionId) {
        return SECTION_REGISTRY.containsKey(componentId) ? SECTION_REGISTRY.get(componentId).get(sectionId) : null;
    }

    private RoundaboutSink resolveSink(String componentId, String sinkId) {
        return SINK_REGISTRY.containsKey(componentId) ? SINK_REGISTRY.get(componentId).get(sinkId) : null;
    }

    private <K, V, R> R extractParameter(Function<K, V> supplier, Function<V, R> converter, K constant) {
        return converter.apply(supplier.apply(constant));
    }
}
