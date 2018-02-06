package at.fhv.itm3.s2.roundabout.util;

import at.fhv.itm14.trafsim.model.ModelFactory;
import at.fhv.itm14.trafsim.model.entities.AbstractConsumer;
import at.fhv.itm14.trafsim.model.entities.AbstractProducer;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.intersection.FixedCirculationController;
import at.fhv.itm14.trafsim.model.entities.intersection.Intersection;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.ConsumerType;
import at.fhv.itm3.s2.roundabout.api.entity.IModelStructure;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.controller.IntersectionController;
import at.fhv.itm3.s2.roundabout.controller.RouteController;
import at.fhv.itm3.s2.roundabout.entity.*;
import at.fhv.itm3.s2.roundabout.entity.Route;
import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;
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
    private static final String SIMULATION_SEED = "SIMULATION_SEED";
    private static final String MIN_TIME_BETWEEN_CAR_ARRIVALS = "MIN_TIME_BETWEEN_CAR_ARRIVALS";
    private static final String MAX_TIME_BETWEEN_CAR_ARRIVALS = "MAX_TIME_BETWEEN_CAR_ARRIVALS";
    private static final String MIN_DISTANCE_FACTOR_BETWEEN_CARS = "MIN_DISTANCE_FACTOR_BETWEEN_CARS";
    private static final String MAX_DISTANCE_FACTOR_BETWEEN_CARS = "MAX_DISTANCE_FACTOR_BETWEEN_CARS";
    private static final String MAIN_ARRIVAL_RATE_FOR_ONE_WAY_STREETS = "MAIN_ARRIVAL_RATE_FOR_ONE_WAY_STREETS";
    private static final String STANDARD_CAR_ACCELERATION_TIME = "STANDARD_CAR_ACCELERATION_TIME";
    private static final String MIN_CAR_LENGTH = "MIN_CAR_LENGTH";
    private static final String MAX_CAR_LENGTH = "MAX_CAR_LENGTH";
    private static final String EXPECTED_CAR_LENGTH = "EXPECTED_CAR_LENGTH";
    private static final String MIN_TRUCK_LENGTH = "MIN_TRUCK_LENGTH";
    private static final String MAX_TRUCK_LENGTH = "MAX_TRUCK_LENGTH";
    private static final String EXPECTED_TRUCK_LENGTH = "EXPECTED_TRUCK_LENGTH";
    private static final String CAR_RATIO_PER_TOTAL_VEHICLE = "CAR_RATIO_PER_TOTAL_VEHICLE";
    private static final String JAM_INDICATOR_IN_SECONDS = "JAM_INDICATOR_IN_SECONDS";

    private static final String INTERSECTION_SIZE = "INTERSECTION_SIZE";
    private static final String INTERSECTION_SERVICE_DELAY = "INTERSECTION_SERVICE_DELAY";
    private static final String INTERSECTION_TRAVERSE_TIME = "INTERSECTION_TRAVERSE_TIME";
    private static final String CONTROLLER_GREEN_DURATION = "CONTROLLER_GREEN_DURATION";
    private static final String CONTROLLER_YELLOW_DURATION = "CONTROLLER_YELLOW_DURATION";
    private static final String CONTROLLER_PHASE_SHIFT_TIME = "CONTROLLER_PHASE_SHIFT_TIME";

    private static final Map<String, Map<String, RoundaboutSource>> SOURCE_REGISTRY = new HashMap<>(); // componentId, sectionId, section
    private static final Map<String, Map<String, RoundaboutSink>> SINK_REGISTRY = new HashMap<>();
    private static final Map<String, Map<String, StreetSection>> SECTION_REGISTRY = new HashMap<>();
    private static final Map<AbstractSource, Map<RoundaboutSink, Route>> ROUTE_REGISTRY = new HashMap<>(); // source, sink, route
    private static final Map<String, Intersection> INTERSECTION_REGISTRY = new HashMap<>(); // componentId, intersection

    private static final double DEFAULT_ROUTE_RATIO = 0.0;

    private static final Comparator<Track> TRACK_COMPARATOR = Comparator.comparingLong(Track::getOrder);
    private static final Function<Connector, List<Track>> SORTED_TRACK_EXTRACTOR = co -> co.getTrack().stream().sorted(TRACK_COMPARATOR).collect(Collectors.toList());

    private String filename;
    private Double minStreetLength;

    public ConfigParser(String filename) {
        this.filename = filename;
    }

    public ModelConfig loadConfig() throws ConfigParserException {
        File configFile = new File(filename);
        if (!configFile.exists()) {
            configFile = new File(getClass().getResource(filename).getPath());
            if (!configFile.exists()) {
                throw new ConfigParserException("No such config file " + filename);
            }
        }
        return JAXB.unmarshal(configFile, ModelConfig.class);
    }

    public IModelStructure initRoundaboutStructure(ModelConfig modelConfig, Experiment experiment) {
        final Map<String, String> parameters = handleParameters(modelConfig);

        final List<Component> components = modelConfig.getComponents().getComponent();
        final List<Source> modelSources = components.stream().map(Component::getSources).map(Sources::getSource).flatMap(Collection::stream).collect(Collectors.toList());
        final List<Double> generatorExpectations = modelSources.stream().map(Source::getGeneratorExpectation).filter(Objects::nonNull).sorted().collect(Collectors.toList());

        // Compatibility for the rest of structure is achieved via insertion of property.
        final double generatorExpectationMedian = calculateMedian(generatorExpectations);
        parameters.put(MAX_TIME_BETWEEN_CAR_ARRIVALS, String.valueOf(generatorExpectationMedian));

        final RoundaboutSimulationModel model = new RoundaboutSimulationModel(
            extractParameter(parameters::get, Long::valueOf, SIMULATION_SEED),
            null,
            modelConfig.getName(),
            false,
            false,
            extractParameter(parameters::get, Double::valueOf, MIN_TIME_BETWEEN_CAR_ARRIVALS),
            extractParameter(parameters::get, Double::valueOf, MAX_TIME_BETWEEN_CAR_ARRIVALS),
            extractParameter(parameters::get, Double::valueOf, MIN_DISTANCE_FACTOR_BETWEEN_CARS),
            extractParameter(parameters::get, Double::valueOf, MAX_DISTANCE_FACTOR_BETWEEN_CARS),
            extractParameter(parameters::get, Double::valueOf, MAIN_ARRIVAL_RATE_FOR_ONE_WAY_STREETS),
            extractParameter(parameters::get, Double::valueOf, STANDARD_CAR_ACCELERATION_TIME),
            extractParameter(parameters::get, Double::valueOf, MIN_CAR_LENGTH),
            extractParameter(parameters::get, Double::valueOf, MAX_CAR_LENGTH),
            extractParameter(parameters::get, Double::valueOf, EXPECTED_CAR_LENGTH),
            extractParameter(parameters::get, Double::valueOf, MIN_TRUCK_LENGTH),
            extractParameter(parameters::get, Double::valueOf, MAX_TRUCK_LENGTH),
            extractParameter(parameters::get, Double::valueOf, EXPECTED_TRUCK_LENGTH),
            extractParameter(parameters::get, Double::valueOf, CAR_RATIO_PER_TOTAL_VEHICLE),
            extractParameter(parameters::get, Double::valueOf, JAM_INDICATOR_IN_SECONDS)
        );
        model.connectToExperiment(experiment);  // ! - Should be done before anything else.

        final IModelStructure modelStructure = new ModelStructure(model, parameters);
        minStreetLength = Double.parseDouble(parameters.get(MAX_TRUCK_LENGTH)) +
                          Double.parseDouble(parameters.get(MAX_DISTANCE_FACTOR_BETWEEN_CARS)) * 2;
        handleComponents(modelStructure, modelConfig.getComponents());
        if (modelConfig.getComponents().getConnectors() != null) {
            handleConnectors(null, modelConfig.getComponents().getConnectors());
        }

        // Adding intersections.
        modelStructure.addIntersections(INTERSECTION_REGISTRY.values());

        // Handling and adding routes.
        final List<Route> routes = handleRoutes(modelConfig).values().stream().map(Map::values).flatMap(Collection::stream).collect(Collectors.toList());
        modelStructure.addRoutes(routes);

        RouteController.getInstance(model).setRoutes(modelStructure.getRoutes());

        model.registerModelStructure(modelStructure);
        return modelStructure;
    }

    public Map<String, Map<String, RoundaboutSource>> getSourceRegistry() {
        return Collections.unmodifiableMap(SOURCE_REGISTRY);
    }

    public Map<String, Map<String, StreetSection>> getSectionRegistry() {
        return Collections.unmodifiableMap(SECTION_REGISTRY);
    }

    public Map<String, Map<String, RoundaboutSink>> getSinkRegistry() {
        return Collections.unmodifiableMap(SINK_REGISTRY);
    }

    private Map<String, String> handleParameters(ModelConfig modelConfig) {
        final Map<String, String> modelParameters = new HashMap<>();
        final Consumer<Parameter> parameterRegistrator = p -> modelParameters.put(p.getName(), p.getValue());
        modelConfig.getParameters().getParameter().forEach(parameterRegistrator);

        final List<Component> componentList = modelConfig.getComponents().getComponent();
        for (Component component : componentList) {
            if (component.getParameters() != null) {
                component.getParameters().getParameter().forEach(parameterRegistrator);
            }
        }
        return modelParameters;
    }

    private void handleComponents(IModelStructure modelStructure, Components modelComponents) {
        for (Component component : modelComponents.getComponent()) {
            switch (component.getType()) {
                case ROUNDABOUT: {
                    handleRoundabout(modelStructure, component);
                    break;
                }

                case INTERSECTION: {
                    handleIntersection(modelStructure, component);
                    break;
                }

                default: throw new IllegalArgumentException("Unknown component type detected.");
            }
        }
    }

    private void handleRoundabout(IModelStructure modelStructure, Component roundaboutComponent) {
        final Model model = modelStructure.getModel();

        // Handle configuration.
        final Map<String, StreetSection> sections = handleSections(
            roundaboutComponent.getId(),
            roundaboutComponent.getSections(),
            model
        );

        final Map<String, RoundaboutSink> sinks = handleSinks(
            roundaboutComponent.getId(),
            roundaboutComponent.getSinks(),
            model
        );

        final Map<String, RoundaboutSource> sources = handleSources(
            roundaboutComponent.getId(),
            roundaboutComponent.getSources(),
            model
        );

        final Map<String, StreetConnector> connectors = handleConnectors(
            roundaboutComponent.getId(),
            roundaboutComponent.getConnectors()
        );

        modelStructure.addStreets(sections.values());
        modelStructure.addStreetConnectors(connectors.values());
        modelStructure.addSources(sources.values());
        modelStructure.addSinks(sinks.values());
    }

    private void handleIntersection(IModelStructure modelStructure, Component intersectionComponent) {
        final Model model = modelStructure.getModel();

        // Init intersection.
        final RoundaboutIntersection intersection = new RoundaboutIntersection(
            model,
            intersectionComponent.getName(),
            false,
            Integer.parseInt(modelStructure.getParameter(INTERSECTION_SIZE))
        );

        intersection.setServiceDelay(
            extractParameter(modelStructure::getParameter, Double::valueOf, INTERSECTION_SERVICE_DELAY)
        );
        final FixedCirculationController ic = ModelFactory.getInstance(model).createStdFull4Controller(
            intersection,
            extractParameter(modelStructure::getParameter, Double::valueOf, CONTROLLER_GREEN_DURATION),
            extractParameter(modelStructure::getParameter, Double::valueOf, CONTROLLER_YELLOW_DURATION),
            extractParameter(modelStructure::getParameter, Double::valueOf, CONTROLLER_PHASE_SHIFT_TIME)
        );
        intersection.attachController(ic);

        // Handle the rest of configuration.
        final Map<String, StreetSection> sections = handleSections(
            intersectionComponent.getId(),
            intersectionComponent.getSections(),
            model
        );

        final Map<String, RoundaboutSink> sinks = handleSinks(
            intersectionComponent.getId(),
            intersectionComponent.getSinks(),
            model
        );

        final Map<String, RoundaboutSource> sources = handleSources(
            intersectionComponent.getId(),
            intersectionComponent.getSources(),
            model
        );

        // Handle connectors (is specific for intersections).
        final IntersectionController intersectionController = IntersectionController.getInstance();
        final Map<String, Integer> DIRECTION_MAP = new HashMap<>();

        // Assembling intersection sections (init connection queues, connectors, etc.)
        int directionIndexer = 0;
        double intersectionTraverseTime = Double.valueOf(modelStructure.getParameter(INTERSECTION_TRAVERSE_TIME));

        for (Connector connector : intersectionComponent.getConnectors().getConnector()) {
            final List<Track> trackList = SORTED_TRACK_EXTRACTOR.apply(connector);

            final Collection<IConsumer> previousSections = new LinkedHashSet<>();
            final Collection<IConsumer> nextSections = new LinkedHashSet<>();

            for (Track track : trackList) {
                // In direction.
                final String fromSectionId = track.getFromSectionId();
                final Street fromSection = resolveStreet(intersectionComponent.getId(), fromSectionId);
                if (!DIRECTION_MAP.containsKey(fromSectionId)) {
                    DIRECTION_MAP.put(fromSectionId, directionIndexer++);

                    // Handling section -> intersections connectors, should be done only once per section.
                    // Connection intersection -> section is handled via connection queue.
                    previousSections.add(fromSection);
                    nextSections.add(intersection);
                }
                final int inDirection = DIRECTION_MAP.get(fromSectionId);
                intersectionController.setIntersectionInDirectionMapping(intersection, fromSection, inDirection);

                // Out direction.
                final String toSectionId = track.getToSectionId();
                final Street toSection = resolveStreet(intersectionComponent.getId(), toSectionId);
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
            final StreetConnector streetConnector = new StreetConnector(connector.getId(), previousSections, nextSections);
            previousSections.forEach(fromSection -> streetConnector.initializeTrack(
                fromSection,
                ConsumerType.STREET_SECTION,
                intersection,
                ConsumerType.INTERSECTION
            ));
        }

        // Registering intersection.
        INTERSECTION_REGISTRY.put(intersectionComponent.getId(), intersection);

        modelStructure.addStreets(sections.values());
        modelStructure.addSources(sources.values());
        modelStructure.addSinks(sinks.values());
    }

    private Map<String, RoundaboutSource> handleSources(String scopeComponentId, Sources sources, Model model) {
        return sources.getSource().stream().collect(toMap(
            Source::getId,
            so -> {
                final Street street = resolveSection(scopeComponentId, so.getSectionId());
                final RoundaboutSource source = new RoundaboutSource(so.getId(), so.getGeneratorExpectation(), model, so.getId(), false, street);
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
                final RoundaboutSink sink = new RoundaboutSink(sk.getId(), model, sk.getId(), false);
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
                if(s.getLength() < minStreetLength) {
                    throw new IllegalArgumentException(
                        "Street must not be smaller than the biggest vehicle incl. distance to other vehicles"
                    );
                }

                final boolean isTrafficLightActive = s.getIsTrafficLightActive() != null ? s.getIsTrafficLightActive() : false;
                final StreetSection streetSection = new StreetSection(
                    s.getId(),
                    s.getLength(),
                    model,
                    s.getId(),
                    false,
                    isTrafficLightActive,
                    s.getMinGreenPhaseDuration(),
                    s.getGreenPhaseDuration(),
                    s.getRedPhaseDuration()
                );

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
                    final Street fromSection = resolveStreet(fromComponentId, track.getFromSectionId());
                    if (!previousSections.contains(fromSection)) previousSections.add(fromSection);

                    final String toComponentId = track.getToComponentId() != null ? track.getToComponentId() : scopeComponentId;
                    final Street toSection = resolveStreet(toComponentId, track.getToSectionId());
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

    private Map<AbstractSource, Map<RoundaboutSink, Route>> handleRoutes(ModelConfig modelConfig) {
        modelConfig.getComponents().getComponent().forEach(component -> {
            // Start generating roots for every source.
            component.getSources().getSource().forEach(sourceDTO -> {
                final AbstractSource source = SOURCE_REGISTRY.get(component.getId()).get(sourceDTO.getId());
                final Street connectedStreet = source.getConnectedStreet();

                final List<IConsumer> route = new LinkedList<>();
                route.add(connectedStreet);

                doDepthFirstSearch(source, route, component, modelConfig);
            });
        });

        return ROUTE_REGISTRY;
    }

    private Street resolveStreet(String componentId, String streetId) {
        final Street resolvedSection = resolveSection(componentId, streetId);
        return resolvedSection != null ? resolvedSection : resolveSink(componentId, streetId);
    }

    private StreetSection resolveSection(String componentId, String sectionId) {
        return SECTION_REGISTRY.containsKey(componentId) ? SECTION_REGISTRY.get(componentId).get(sectionId) : null;
    }

    private RoundaboutSink resolveSink(String componentId, String sinkId) {
        return SINK_REGISTRY.containsKey(componentId) ? SINK_REGISTRY.get(componentId).get(sinkId) : null;
    }

    private <K, V, R> R extractParameter(Function<K, V> supplier, Function<V, R> converter, K key)
    throws NullPointerException {
        return converter.apply(supplier.apply(key));
    }

    private Double calculateMedian(List<Double> numbers) {
        int indexLeft = (int) Math.floor((numbers.size() - 1) / 2);
        int indexRight = (int) Math.ceil((numbers.size() - 1) / 2);
        if (indexLeft == indexRight) {
            return numbers.get(indexLeft);
        }
        return (numbers.get(indexLeft) + numbers.get(indexRight)) / 2;
    }

    private void doDepthFirstSearch(
        AbstractSource source,
        List<IConsumer> routeSections,
        Component component,
        ModelConfig modelConfig
    ) {
        final IConsumer lastConsumer = routeSections.get(routeSections.size() - 1);
        if (!(lastConsumer instanceof Street)) {
            throw new IllegalArgumentException("Only instances of Street class may be included in root.");
        }
        final String currentSectionId = ((Street) lastConsumer).getId();

        // Check each connector.
        for (Connector connector : component.getConnectors().getConnector()) {
            for (Track track : connector.getTrack()) {
                if (track.getFromSectionId().equals(currentSectionId)) {
                    final String fromComponentId = track.getFromComponentId() != null ? track.getFromComponentId() : component.getId();
                    final String toComponentId = track.getToComponentId() != null ? track.getToComponentId() : component.getId();
                    final String toSectionId = track.getToSectionId();

                    final Street toSection = resolveStreet(toComponentId, toSectionId);
                    if (!routeSections.contains(toSection)) {
                        final List<IConsumer> newRouteSections = new LinkedList<>(routeSections);
                        if (component.getType() == ComponentType.INTERSECTION) {
                            newRouteSections.add((IConsumer) INTERSECTION_REGISTRY.get(component.getId()));
                        }
                        newRouteSections.add(toSection);

                        if (toSection instanceof RoundaboutSink) {
                            // Sink is reached -> end of path.
                            final RoundaboutSink sink = (RoundaboutSink) toSection;
                            if (!ROUTE_REGISTRY.containsKey(source)) {
                                ROUTE_REGISTRY.put(source, new HashMap<>());
                            }

                            final Map<RoundaboutSink, Route> targetRoutes = ROUTE_REGISTRY.get(source);
                            if (!targetRoutes.containsKey(sink) || targetRoutes.get(sink).getNumberOfSections() > newRouteSections.size()) {
                                final double flowRatio;
                                if (modelConfig.getComponents().getRoutes() != null) {
                                    final Optional<Double> optionalRatio = modelConfig.getComponents().getRoutes().getRoute().stream().filter(r ->
                                        fromComponentId.equals(r.getFromComponentId()) && source.getId().equals(r.getFromSourceId()) &&
                                        toComponentId.equals(r.getToComponentId()) && sink.getId().equals(r.getToSinkId())
                                    ).map(at.fhv.itm3.s2.roundabout.util.dto.Route::getRatio).findFirst();
                                    flowRatio = optionalRatio.orElse(DEFAULT_ROUTE_RATIO);
                                } else {
                                    flowRatio = DEFAULT_ROUTE_RATIO;
                                }
                                targetRoutes.put(sink, new Route(source, newRouteSections, flowRatio));
                            }
                            return;
                        } else {
                            doDepthFirstSearch(source, newRouteSections, component, modelConfig);
                        }
                    }
                }
            }
        }

        if (modelConfig.getComponents().getConnectors() !=  null) {
            // Check the connectors between networks components (Roundabouts or Intersections)
            for (Connector connector : modelConfig.getComponents().getConnectors().getConnector()) {
                for (Track track : connector.getTrack()) {
                    final String fromComponentId = track.getFromComponentId();
                    final String fromSectionId = track.getFromSectionId();

                    if (fromComponentId.equals(component.getId()) && fromSectionId.equals(currentSectionId)) {
                        for (Component localComponent : modelConfig.getComponents().getComponent()) {
                            final String toComponentId = track.getToComponentId();
                            final String toSectionId = track.getToSectionId();

                            if (toComponentId.equals(localComponent.getId())) {
                                final Street toSection = resolveSection(toComponentId, toSectionId);

                                final List<IConsumer> newRouteSections = new LinkedList<>(routeSections);
                                if (component.getType() == ComponentType.INTERSECTION) {
                                    newRouteSections.add((IConsumer) INTERSECTION_REGISTRY.get(component.getId()));
                                }
                                newRouteSections.add(toSection);

                                doDepthFirstSearch(source, newRouteSections, localComponent, modelConfig);
                            }
                        }
                    }
                }
            }
        }
    }
}
