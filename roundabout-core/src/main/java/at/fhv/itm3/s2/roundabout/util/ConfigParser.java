package at.fhv.itm3.s2.roundabout.util;

import at.fhv.itm14.trafsim.model.ModelFactory;
import at.fhv.itm14.trafsim.model.entities.AbstractConsumer;
import at.fhv.itm14.trafsim.model.entities.AbstractProducer;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.intersection.FixedCirculationController;
import at.fhv.itm3.s2.roundabout.api.entity.ConsumerType;
import at.fhv.itm3.s2.roundabout.api.entity.IModelStructure;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.api.entity.*;
import at.fhv.itm3.s2.roundabout.api.entity.ConsumerType;
import at.fhv.itm3.s2.roundabout.api.entity.IModelStructure;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.controller.IntersectionController;
import at.fhv.itm3.s2.roundabout.entity.*;
import at.fhv.itm3.s2.roundabout.model.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.util.dto.*;
import co.paralleluniverse.common.util.Tuple;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;

import javax.vecmath.Tuple2d;
import javax.xml.bind.JAXB;
import java.io.File;
import java.util.*;
import java.util.function.BiFunction;
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

    private static final Map<String, Map<String, RoundaboutSource>> SOURCE_REGISTRY = new HashMap<>(); // component ID, Section ID, Section
    private static final Map<String, Map<String, RoundaboutSink>> SINK_REGISTRY = new HashMap<>();
    private static final Map<String, Map<String, StreetSection>> SECTION_REGISTRY = new HashMap<>();
    private static final Comparator<Track> TRACK_COMPARATOR = Comparator.comparingLong(Track::getOrder);
    private static final Function<Connector, List<Track>> SORTED_TRACK_EXTRACTOR = co -> co.getTrack().stream().sorted(TRACK_COMPARATOR).collect(Collectors.toList());

    private String filename;
    private Map<String, ArrayList<RouteSegmentsId>> routes = new HashMap<>(); // ID <Source&&Sink> and Route<ComponentID,StreetID>

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

    public Experiment assembleExperiment(ModelConfig modelConfig, boolean isProgressBarShown) throws ConfigParserException {
        final Map<String, String> parameters = handleParameters(modelConfig);
        final RoundaboutSimulationModel model = new RoundaboutSimulationModel(
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
        final Experiment experiment = initExperiment("Stock management experiment", model, isProgressBarShown);

        final IModelStructure modelStructure = new ModelStructure(model, parameters);

        handleComponents(modelStructure, modelConfig.getComponents());
        handleConnectors(null, modelConfig.getComponents().getConnectors());

        initializeRoutes(modelConfig);

        return experiment;
    }

    public BiFunction<String, String, StreetSection> getStreetSectionResolver() {
        return (componentId, elementId) -> SECTION_REGISTRY.get(componentId).get(elementId);
    }

    public BiFunction<String, String, RoundaboutSource> getSourceResolver() {
        return (componentId, elementId) -> SOURCE_REGISTRY.get(componentId).get(elementId);
    }

    public BiFunction<String, String, RoundaboutSink> getSinkResolver() {
        return (componentId, elementId) -> SINK_REGISTRY.get(componentId).get(elementId);
    }

    private void initializeRoutes(ModelConfig modelConfig){
        for( Component componentIt : modelConfig.getComponents().getComponent()){ //Iterate through Roundabouts and Intersections
            //start from every possible source
            for (Source sourceIt : componentIt.getSources().getSource()) {
                String currentStreetSectionID = sourceIt.getSectionId();
                ArrayList<RouteSegmentsId> currentPath = new ArrayList();
                currentPath.add(new RouteSegmentsId (componentIt.getId(), currentStreetSectionID));
                DepthFirstSearch(currentStreetSectionID, currentPath, componentIt, modelConfig);
            }
        }
    }

    void DepthFirstSearch(String currentStreetID,
                          ArrayList<RouteSegmentsId> currentPath,
                          Component component,
                          ModelConfig modelConfig){
        // check each connector
        for(Connector connectorIt : component.getConnectors().getConnector()){
            for(Track trackIt : connectorIt.getTrack()){
                if(trackIt.getFromSectionId().equals(currentStreetID)){
                    ArrayList<RouteSegmentsId> currentPathTmp = new ArrayList<>(currentPath);
                    String nextStreetID = trackIt.getToSectionId();
                    if(checkStreetIsSink(nextStreetID)){ // sink is reached = end of path
                        currentPathTmp.add(new RouteSegmentsId(component.getId(), nextStreetID));

                        String sourceId = currentPath.get(0).getComponentId() + " " + currentPath.get(0).getStreetId();
                        String sinkId = component.getId() + " " + nextStreetID;
                        String routeID = sourceId + " " + sinkId;

                        if(routes.containsKey(routeID) &&
                           routes.get(routeID).size() < currentPathTmp.size()) {
                           return;
                        }

                        addRoute(sourceId, sinkId, currentPathTmp);
                        return;
                    }

                    boolean continueDFS = true;
                    for(RouteSegmentsId routeSegIdIt : currentPath){
                        if (routeSegIdIt.contains(component.getId(), nextStreetID)) continueDFS = false;
                    }

                    if(continueDFS){ // do not loop
                        currentPathTmp.add(new RouteSegmentsId(component.getId(), nextStreetID));
                        DepthFirstSearch(nextStreetID, currentPathTmp, component, modelConfig);
                    }
                }
            }
        }

        // check the connectors between networks components (Roundabouts or Intersections)
        for (Connector connectorIt : modelConfig.getComponents().getConnectors().getConnector()) {
            for(Track trackIt : connectorIt.getTrack()) {
                if (trackIt.getFromSectionId().equals(currentStreetID) &&
                    trackIt.getFromComponentId().equals(component.getId())) {

                    String toComponentID = trackIt.getToComponentId();
                    String nextStreetID = trackIt.getToSectionId();

                    // get access to the "toSection"
                    for (Component component1 : modelConfig.getComponents().getComponent()) {
                        if(component1.getId().equals(toComponentID)){
                            ArrayList<RouteSegmentsId> currentPathTmp = new ArrayList<>(currentPath);
                            DepthFirstSearch(nextStreetID, currentPathTmp, component1, modelConfig);
                        }

                    }
                }
            }
        }

        return;
    }

    public boolean checkStreetIsSink (String ID){
        for(String componentIDIt : SINK_REGISTRY.keySet()){
            for(String sectionIDIt : SINK_REGISTRY.get(componentIDIt).keySet()){
                if(sectionIDIt.equals(ID)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Street getStreetFromID (String ID){
        for(String componentIDIt : SECTION_REGISTRY.keySet()){
            for(String sectionIDIt : SECTION_REGISTRY.get(componentIDIt).keySet()){
                if(sectionIDIt.equals(ID)) {
                    return SECTION_REGISTRY.get(componentIDIt).get(sectionIDIt);
                }
            }
        }
        throw new IllegalArgumentException( ID + " is not a legit Street ID Name.");
    }

    public void addRoute(String source , String sink, ArrayList<RouteSegmentsId> route) {
        String key = source + " " + sink;
        routes.put(key, route);
    }

    public Map<String, ArrayList<RouteSegmentsId>> getRoutes() {
        return routes;
    }

    public ArrayList<RouteSegmentsId> getRoute(String start, String destination){
        String key = start + " " + destination;
        return routes.get(key);
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

    private void handleComponents(IModelStructure roundaboutStructure, Components components) {
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

    private void handleRoundabout(IModelStructure roundaboutStructure, Component roundaboutComponent) {
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

    private void handleIntersection(IModelStructure roundaboutStructure, Component intersectionComponent) {
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

    private static Experiment initExperiment(String description, Model model, boolean isShowProgressBar) {
        Experiment experiment = new Experiment(description);
        model.connectToExperiment(experiment); // ! - Should be done before anything else.

        experiment.setShowProgressBar(isShowProgressBar);

        // Just to be sure everything is initialised as expected.
        model.reset();
        model.init();

        return experiment;
    }

    public Map<String, List<Route>> initRoutes() {
        final Map<String, List<Route>> resultMap = new HashMap<>();

        // Route so_s6_t1  --> Sk_s6_t1
        final List<Route> so_s6_t1_routes = new LinkedList<>();
        final Route route1 = new Route();
        route1.addSource(SOURCE_REGISTRY.get("ro1").get("so_s6_t1"));
        route1.addSection(SECTION_REGISTRY.get("ro1").get("s6_t1"));
        route1.addSection(SINK_REGISTRY.get("ro1").get("sk_s6_t1"));
        so_s6_t1_routes.add(route1);
        resultMap.put("so_s6_t1", so_s6_t1_routes);

        // all routes from so_s5_t1
        // Route so_s5_t1 --> ro1 sk_s13_t1
        final List<Route> so_s5_t1_routes = new LinkedList<>();
        final Route route2 = new Route();
        route2.addSource(SOURCE_REGISTRY.get("ro1").get("so_s5_t1"));
        route2.addSection(SECTION_REGISTRY.get("ro1").get("s5_t1"));
        route2.addSection(SECTION_REGISTRY.get("ro1").get("s7_t1"));
        route2.addSection(SECTION_REGISTRY.get("ro1").get("s12_t1"));
        route2.addSection(SECTION_REGISTRY.get("ro1").get("s13_t1"));
        route2.addSection(SINK_REGISTRY.get("ro1").get("sk_s13_t1"));
        so_s5_t1_routes.add(route2);
        // Route so_s5_t1 --> ro1 sk_s17_t1
        final Route route3 = new Route();
        route3.addSource(SOURCE_REGISTRY.get("ro1").get("so_s5_t1"));
        route3.addSection(SECTION_REGISTRY.get("ro1").get("s5_t1"));
        route3.addSection(SECTION_REGISTRY.get("ro1").get("s7_t1"));
        route3.addSection(SECTION_REGISTRY.get("ro1").get("s12_t1"));
        route3.addSection(SECTION_REGISTRY.get("ro1").get("s16_t1"));
        route3.addSection(SECTION_REGISTRY.get("ro1").get("s17_t1"));
        route3.addSection(SINK_REGISTRY.get("ro1").get("sk_s17_t1"));
        so_s5_t1_routes.add(route3);
        // Route so_s5_t1 --> is1 sk_s2_t1
        final Route route4 = new Route();
        route4.addSource(SOURCE_REGISTRY.get("ro1").get("so_s5_t1"));
        route4.addSection(SECTION_REGISTRY.get("ro1").get("s5_t1"));
        route4.addSection(SECTION_REGISTRY.get("ro1").get("s7_t1"));
        route4.addSection(SECTION_REGISTRY.get("ro1").get("s12_t1"));
        route4.addSection(SECTION_REGISTRY.get("ro1").get("s16_t1"));
        route4.addSection(SECTION_REGISTRY.get("ro1").get("s19_t1"));
        route4.addSection(SECTION_REGISTRY.get("ro1").get("s1_t1"));
        route4.addSection(SECTION_REGISTRY.get("is1").get("s2_t1"));
        route4.addSection(SINK_REGISTRY.get("is1").get("sk_s2_t1"));
        so_s5_t1_routes.add(route4);
        resultMap.put("so_s5_t1", so_s5_t1_routes);

        //all routes from  ro1 so_s10_t1
        // route so_s10_t1 --> sk_s17_t1
        final List<Route> so_s10_t1_routes = new LinkedList<>();
        final Route route5 = new Route();
        route5.addSource(SOURCE_REGISTRY.get("ro1").get("so_s10_t1"));
        route5.addSection(SECTION_REGISTRY.get("ro1").get("s10_t1"));
        route5.addSection(SECTION_REGISTRY.get("ro1").get("s16_t1"));
        route5.addSection(SECTION_REGISTRY.get("ro1").get("s17_t1"));
        route5.addSection(SINK_REGISTRY.get("ro1").get("sk_s17_t1"));
        so_s10_t1_routes.add(route5);
        // route s0_s10_t1 --> is1 sk_s2_t1
        final Route route6 = new Route();
        route6.addSource(SOURCE_REGISTRY.get("ro1").get("so_s10_t1"));
        route6.addSection(SECTION_REGISTRY.get("ro1").get("s10_t1"));
        route6.addSection(SECTION_REGISTRY.get("ro1").get("s16_t1"));
        route6.addSection(SECTION_REGISTRY.get("ro1").get("s19_t1"));
        route6.addSection(SECTION_REGISTRY.get("ro1").get("s1_t1"));
        route6.addSection(SECTION_REGISTRY.get("is1").get("s2_t1"));
        route6.addSection(SINK_REGISTRY.get("is1").get("sk_s2_t1"));
        so_s10_t1_routes.add(route6);
        resultMap.put("so_s10_t1", so_s10_t1_routes);

        //all routes from  ro1 so_s10_t2
        // route so_s10_t2 --> sk_s4_t1
        final List<Route> so_s10_t2_routes = new LinkedList<>();
        final Route route7 = new Route();
        route7.addSource(SOURCE_REGISTRY.get("ro1").get("so_s10_t2"));
        route7.addSection(SECTION_REGISTRY.get("ro1").get("s10_t2"));
        route7.addSection(SECTION_REGISTRY.get("ro1").get("s16_t2"));
        route7.addSection(SECTION_REGISTRY.get("ro1").get("s19_t2"));
        route7.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route7.addSection(SECTION_REGISTRY.get("ro1").get("s4_t1"));
        route7.addSection(SINK_REGISTRY.get("ro1").get("sk_s4_t1"));
        so_s10_t2_routes.add(route7);
        resultMap.put("so_s10_t2", so_s10_t2_routes);

        //all routes from  ro1 so_s11_t1
        // route so_s11_t1 --> sk_s11_t1
        final List<Route> so_s11_t1_routes = new LinkedList<>();
        final Route route8 = new Route();
        route8.addSource(SOURCE_REGISTRY.get("ro1").get("so_s11_t1"));
        route8.addSection(SECTION_REGISTRY.get("ro1").get("s11_t1"));
        route8.addSection(SINK_REGISTRY.get("ro1").get("sk_s11_t1"));
        so_s11_t1_routes.add(route8);
        resultMap.put("so_s11_t1", so_s11_t1_routes);

        //all routes from  ro1 so_s14_t1
        // route so_s14_t1 --> is1 sk_s2_t1
        final List<Route> so_s14_t1_routes = new LinkedList<>();
        final Route route9 = new Route();
        route9.addSource(SOURCE_REGISTRY.get("ro1").get("so_s14_t1"));
        route9.addSection(SECTION_REGISTRY.get("ro1").get("s14_t1"));
        route9.addSection(SECTION_REGISTRY.get("ro1").get("s19_t1"));
        route9.addSection(SECTION_REGISTRY.get("ro1").get("s1_t1"));
        route9.addSection(SECTION_REGISTRY.get("is1").get("s2_t1"));
        route9.addSection(SINK_REGISTRY.get("is1").get("sk_s2_t1"));
        so_s14_t1_routes.add(route9);
        resultMap.put("so_s14_t1", so_s14_t1_routes);

        //all routes from  ro1 so_s15_t1
        // route so_s15_t1 -->  sk_s15_t1
        final List<Route> so_s15_t1_routes = new LinkedList<>();
        final Route route10 = new Route();
        route10.addSource(SOURCE_REGISTRY.get("ro1").get("so_s15_t1"));
        route10.addSection(SECTION_REGISTRY.get("ro1").get("s15_t1"));
        route10.addSection(SINK_REGISTRY.get("ro1").get("sk_s15_t1"));
        so_s15_t1_routes.add(route9);
        resultMap.put("so_s15_t1", so_s15_t1_routes);

        //all routes from  ro1 so_s14_t2
        // route so_s14_t2 --> ro1 sk_s4_t1
        final List<Route> so_s14_t2_routes = new LinkedList<>();
        final Route route11 = new Route();
        route11.addSource(SOURCE_REGISTRY.get("ro1").get("so_s14_t2"));
        route11.addSection(SECTION_REGISTRY.get("ro1").get("s14_t2"));
        route11.addSection(SECTION_REGISTRY.get("ro1").get("s19_t2"));
        route11.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route11.addSection(SECTION_REGISTRY.get("ro1").get("s4_t1"));
        route11.addSection(SINK_REGISTRY.get("ro1").get("sk_s4_t1"));
        so_s14_t2_routes.add(route11);
        // route so_s14_t2 --> ro1 sk_s9_t1
        final Route route12 = new Route();
        route12.addSource(SOURCE_REGISTRY.get("ro1").get("so_s14_t2"));
        route12.addSection(SECTION_REGISTRY.get("ro1").get("s14_t2"));
        route12.addSection(SECTION_REGISTRY.get("ro1").get("s19_t2"));
        route12.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route12.addSection(SECTION_REGISTRY.get("ro1").get("s7_t1"));
        route12.addSection(SECTION_REGISTRY.get("ro1").get("s8_t1"));
        route12.addSection(SECTION_REGISTRY.get("ro1").get("s9_t1"));
        route12.addSection(SINK_REGISTRY.get("ro1").get("sk_s9_t1"));
        so_s14_t2_routes.add(route12);
        // route so_s14_t2 --> ro1 sk_s9_t2
        final Route route13 = new Route();
        route13.addSource(SOURCE_REGISTRY.get("ro1").get("so_s14_t2"));
        route13.addSection(SECTION_REGISTRY.get("ro1").get("s14_t2"));
        route13.addSection(SECTION_REGISTRY.get("ro1").get("s19_t2"));
        route13.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route13.addSection(SECTION_REGISTRY.get("ro1").get("s7_t1"));
        route13.addSection(SECTION_REGISTRY.get("ro1").get("s8_t1"));
        route13.addSection(SECTION_REGISTRY.get("ro1").get("s9_t2"));
        route13.addSection(SINK_REGISTRY.get("ro1").get("sk_s9_t2"));
        so_s14_t2_routes.add(route13);
        resultMap.put("so_s14_t2", so_s14_t2_routes);

        //all routes from  ro1 so_s18_t1
        // route so_s18_t1 --> is1 sk_s2_t1
        final List<Route> so_s18_t1_routes = new LinkedList<>();
        final Route route14 = new Route();
        route14.addSource(SOURCE_REGISTRY.get("ro1").get("so_s18_t1"));
        route14.addSection(SECTION_REGISTRY.get("ro1").get("s18_t1"));
        route14.addSection(SECTION_REGISTRY.get("ro1").get("s19_t1"));
        route14.addSection(SECTION_REGISTRY.get("ro1").get("s1_t1"));
        route14.addSection(SECTION_REGISTRY.get("is1").get("s2_t1"));
        route14.addSection(SINK_REGISTRY.get("is1").get("sk_s2_t1"));
        so_s18_t1_routes.add(route14);
        // route so_s18_t1 --> ro1 sk_s4_t1
        final Route route15 = new Route();
        route15.addSource(SOURCE_REGISTRY.get("ro1").get("so_s18_t1"));
        route15.addSection(SECTION_REGISTRY.get("ro1").get("s18_t1"));
        route15.addSection(SECTION_REGISTRY.get("ro1").get("s19_t2"));
        route15.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route15.addSection(SECTION_REGISTRY.get("ro1").get("s4_t1"));
        route15.addSection(SINK_REGISTRY.get("ro1").get("sk_s4_t1"));
        so_s18_t1_routes.add(route15);
        // route so_s18_t1 --> ro1 sk_s9_t1
        final Route route16 = new Route();
        route16.addSource(SOURCE_REGISTRY.get("ro1").get("so_s18_t1"));
        route16.addSection(SECTION_REGISTRY.get("ro1").get("s18_t1"));
        route16.addSection(SECTION_REGISTRY.get("ro1").get("s19_t2"));
        route16.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route16.addSection(SECTION_REGISTRY.get("ro1").get("s7_t1"));
        route16.addSection(SECTION_REGISTRY.get("ro1").get("s8_t1"));
        route16.addSection(SECTION_REGISTRY.get("ro1").get("s9_t1"));
        route16.addSection(SINK_REGISTRY.get("ro1").get("sk_s9_t1"));
        so_s18_t1_routes.add(route16);
        // route so_s18_t1 --> ro1 sk_s9_t2
        final Route route17 = new Route();
        route17.addSource(SOURCE_REGISTRY.get("ro1").get("so_s18_t1"));
        route17.addSection(SECTION_REGISTRY.get("ro1").get("s18_t1"));
        route17.addSection(SECTION_REGISTRY.get("ro1").get("s19_t2"));
        route17.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route17.addSection(SECTION_REGISTRY.get("ro1").get("s7_t1"));
        route17.addSection(SECTION_REGISTRY.get("ro1").get("s8_t1"));
        route17.addSection(SECTION_REGISTRY.get("ro1").get("s9_t2"));
        route17.addSection(SINK_REGISTRY.get("ro1").get("sk_s9_t2"));
        so_s18_t1_routes.add(route17);
        // route so_s18_t1 --> ro1 sk_s11_t1
        final Route route18 = new Route();
        route18.addSource(SOURCE_REGISTRY.get("ro1").get("so_s18_t1"));
        route18.addSection(SECTION_REGISTRY.get("ro1").get("s18_t1"));
        route18.addSection(SECTION_REGISTRY.get("ro1").get("s19_t2"));
        route18.addSection(SECTION_REGISTRY.get("ro1").get("s3_t2"));
        route18.addSection(SECTION_REGISTRY.get("ro1").get("s7_t2"));
        route18.addSection(SECTION_REGISTRY.get("ro1").get("s12_t1"));
        route18.addSection(SECTION_REGISTRY.get("ro1").get("s13_t1"));
        route18.addSection(SINK_REGISTRY.get("ro1").get("sk_s13_t1"));
        so_s18_t1_routes.add(route18);
        resultMap.put("so_s18_t1", so_s18_t1_routes);

        //all routes from  is1 so_s4_t1
        // route is1 so_s4_t1 --> ro1 sk_s4_t1
        final List<Route> is1_so_s4_t1_routes = new LinkedList<>();
        final Route route19 = new Route();
        route19.addSource(SOURCE_REGISTRY.get("is1").get("so_s4_t1"));
        route19.addSection(SECTION_REGISTRY.get("is1").get("s4_t1"));
        route19.addSection(SECTION_REGISTRY.get("is1").get("s6_t1"));
        route19.addSection(SECTION_REGISTRY.get("ro1").get("s2_t1"));
        route19.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route19.addSection(SECTION_REGISTRY.get("ro1").get("s4_t1"));
        route19.addSection(SINK_REGISTRY.get("is1").get("sk_s4_t1"));
        is1_so_s4_t1_routes.add(route19);
        // route is1_so_s4_t1 --> ro1 sk_s9_t1
        final Route route20 = new Route();
        route20.addSource(SOURCE_REGISTRY.get("is1").get("so_s4_t1"));
        route20.addSection(SECTION_REGISTRY.get("is1").get("s4_t1"));
        route20.addSection(SECTION_REGISTRY.get("is1").get("s6_t1"));
        route20.addSection(SECTION_REGISTRY.get("ro1").get("s2_t1"));
        route20.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route20.addSection(SECTION_REGISTRY.get("ro1").get("s7_t1"));
        route20.addSection(SECTION_REGISTRY.get("ro1").get("s8_t1"));
        route20.addSection(SECTION_REGISTRY.get("ro1").get("s9_t1"));
        route20.addSection(SINK_REGISTRY.get("ro1").get("sk_s9_t1"));
        is1_so_s4_t1_routes.add(route20);
        // route is1_so_s4_t1 --> ro1 sk_s9_t2
        final Route route21 = new Route();
        route21.addSource(SOURCE_REGISTRY.get("is1").get("so_s4_t1"));
        route21.addSection(SECTION_REGISTRY.get("is1").get("s4_t1"));
        route21.addSection(SECTION_REGISTRY.get("is1").get("s6_t1"));
        route21.addSection(SECTION_REGISTRY.get("ro1").get("s2_t1"));
        route21.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route21.addSection(SECTION_REGISTRY.get("ro1").get("s7_t1"));
        route21.addSection(SECTION_REGISTRY.get("ro1").get("s8_t1"));
        route21.addSection(SECTION_REGISTRY.get("ro1").get("s9_t2"));
        route21.addSection(SINK_REGISTRY.get("ro1").get("sk_s9_t2"));
        is1_so_s4_t1_routes.add(route21);
        // route is1_so_s4_t1 --> ro1 sk_s13_t1
        final Route route22 = new Route();
        route22.addSource(SOURCE_REGISTRY.get("is1").get("so_s4_t1"));
        route22.addSection(SECTION_REGISTRY.get("is1").get("s4_t1"));
        route22.addSection(SECTION_REGISTRY.get("is1").get("s6_t2"));
        route22.addSection(SECTION_REGISTRY.get("ro1").get("s2_t2"));
        route22.addSection(SECTION_REGISTRY.get("ro1").get("s3_t2"));
        route22.addSection(SECTION_REGISTRY.get("ro1").get("s7_t2"));
        route22.addSection(SECTION_REGISTRY.get("ro1").get("s12_t1"));
        route22.addSection(SECTION_REGISTRY.get("ro1").get("s13_t1"));
        route22.addSection(SINK_REGISTRY.get("ro1").get("sk_s13_t1"));
        is1_so_s4_t1_routes.add(route22);
        // route is1_so_s4_t1 --> ro1 sk_s17_t1
        final Route route23 = new Route();
        route23.addSource(SOURCE_REGISTRY.get("is1").get("so_s4_t1"));
        route23.addSection(SECTION_REGISTRY.get("is1").get("s4_t1"));
        route23.addSection(SECTION_REGISTRY.get("is1").get("s6_t2"));
        route23.addSection(SECTION_REGISTRY.get("ro1").get("s2_t2"));
        route23.addSection(SECTION_REGISTRY.get("ro1").get("s3_t2"));
        route23.addSection(SECTION_REGISTRY.get("ro1").get("s7_t2"));
        route23.addSection(SECTION_REGISTRY.get("ro1").get("s12_t1"));
        route23.addSection(SECTION_REGISTRY.get("ro1").get("s16_t1"));
        route23.addSection(SECTION_REGISTRY.get("ro1").get("s17_t1"));
        route23.addSection(SINK_REGISTRY.get("ro1").get("sk_s17_t1"));
        is1_so_s4_t1_routes.add(route23);
        resultMap.put("is1_so_s4_t1", is1_so_s4_t1_routes);

        //all routes from  is1 so_s5_t1
        // route is1 so_s5_t1 --> ro1 sk_s4_t1
        final List<Route> is1_so_s5_t1_routes = new LinkedList<>();
        final Route route24 = new Route();
        route24.addSource(SOURCE_REGISTRY.get("is1").get("so_s5_t1"));
        route24.addSection(SECTION_REGISTRY.get("is1").get("s5_t1"));
        route24.addSection(SECTION_REGISTRY.get("is1").get("s6_t1"));
        route24.addSection(SECTION_REGISTRY.get("ro1").get("s2_t1"));
        route24.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route24.addSection(SECTION_REGISTRY.get("ro1").get("s4_t1"));
        route24.addSection(SINK_REGISTRY.get("ro1").get("sk_s4_t1"));
        is1_so_s5_t1_routes.add(route24);
        // route is1_so_s5_t1 --> ro1 sk_s9_t1
        final Route route25 = new Route();
        route25.addSource(SOURCE_REGISTRY.get("is1").get("so_s5_t1"));
        route25.addSection(SECTION_REGISTRY.get("is1").get("s5_t1"));
        route25.addSection(SECTION_REGISTRY.get("is1").get("s6_t1"));
        route25.addSection(SECTION_REGISTRY.get("ro1").get("s2_t1"));
        route25.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route25.addSection(SECTION_REGISTRY.get("ro1").get("s7_t1"));
        route25.addSection(SECTION_REGISTRY.get("ro1").get("s8_t1"));
        route25.addSection(SECTION_REGISTRY.get("ro1").get("s9_t1"));
        route25.addSection(SINK_REGISTRY.get("ro1").get("sk_s9_t1"));
        is1_so_s5_t1_routes.add(route25);
        // route is1_so_s5_t1 --> ro1 sk_s9_t2
        final Route route26 = new Route();
        route26.addSource(SOURCE_REGISTRY.get("is1").get("so_s5_t1"));
        route26.addSection(SECTION_REGISTRY.get("is1").get("s5_t1"));
        route26.addSection(SECTION_REGISTRY.get("is1").get("s6_t1"));
        route26.addSection(SECTION_REGISTRY.get("ro1").get("s2_t1"));
        route26.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route26.addSection(SECTION_REGISTRY.get("ro1").get("s7_t1"));
        route26.addSection(SECTION_REGISTRY.get("ro1").get("s8_t1"));
        route26.addSection(SECTION_REGISTRY.get("ro1").get("s9_t2"));
        route26.addSection(SINK_REGISTRY.get("ro1").get("sk_s9_t2"));
        is1_so_s5_t1_routes.add(route26);
        // route is1_so_s5_t1 --> ro1 sk_s13_t1
        final Route route27 = new Route();
        route27.addSource(SOURCE_REGISTRY.get("is1").get("so_s5_t1"));
        route27.addSection(SECTION_REGISTRY.get("is1").get("s5_t1"));
        route27.addSection(SECTION_REGISTRY.get("is1").get("s6_t2"));
        route27.addSection(SECTION_REGISTRY.get("ro1").get("s2_t2"));
        route27.addSection(SECTION_REGISTRY.get("ro1").get("s3_t2"));
        route27.addSection(SECTION_REGISTRY.get("ro1").get("s7_t2"));
        route27.addSection(SECTION_REGISTRY.get("ro1").get("s12_t1"));
        route27.addSection(SECTION_REGISTRY.get("ro1").get("s13_t1"));
        route27.addSection(SINK_REGISTRY.get("ro1").get("sk_s13_t1"));
        is1_so_s5_t1_routes.add(route27);
        // route is1_so_s5_t1 --> ro1 sk_s17_t1
        final Route route28 = new Route();
        route28.addSource(SOURCE_REGISTRY.get("is1").get("so_s5_t1"));
        route28.addSection(SECTION_REGISTRY.get("is1").get("s5_t1"));
        route28.addSection(SECTION_REGISTRY.get("is1").get("s6_t2"));
        route28.addSection(SECTION_REGISTRY.get("ro1").get("s2_t2"));
        route28.addSection(SECTION_REGISTRY.get("ro1").get("s3_t2"));
        route28.addSection(SECTION_REGISTRY.get("ro1").get("s7_t2"));
        route28.addSection(SECTION_REGISTRY.get("ro1").get("s12_t1"));
        route28.addSection(SECTION_REGISTRY.get("ro1").get("s16_t1"));
        route28.addSection(SECTION_REGISTRY.get("ro1").get("s17_t1"));
        route28.addSection(SINK_REGISTRY.get("ro1").get("sk_s17_t1"));
        is1_so_s5_t1_routes.add(route28);
        resultMap.put("is1_so_s5_t1", is1_so_s5_t1_routes);

        //all routes from  is1 so_s3_t4
        // route is1_so_s3_t4 --> ro1 sk_s13_t1
        final List<Route> is1_so_s3_t4_routes = new LinkedList<>();
        final Route route29 = new Route();
        route29.addSource(SOURCE_REGISTRY.get("is1").get("so_s3_t4"));
        route29.addSection(SECTION_REGISTRY.get("is1").get("s3_t4"));
        route29.addSection(SECTION_REGISTRY.get("is1").get("s6_t2"));
        route29.addSection(SECTION_REGISTRY.get("ro1").get("s2_t2"));
        route29.addSection(SECTION_REGISTRY.get("ro1").get("s3_t2"));
        route29.addSection(SECTION_REGISTRY.get("ro1").get("s7_t2"));
        route29.addSection(SECTION_REGISTRY.get("ro1").get("s12_t1"));
        route29.addSection(SECTION_REGISTRY.get("ro1").get("s13_t1"));
        route29.addSection(SINK_REGISTRY.get("ro1").get("sk_s13_t1"));
        is1_so_s3_t4_routes.add(route29);
        // route is1_so_s3_t4 --> ro1 sk_s17_t1
        final Route route30 = new Route();
        route30.addSource(SOURCE_REGISTRY.get("is1").get("so_s3_t4"));
        route30.addSection(SECTION_REGISTRY.get("is1").get("s3_t4"));
        route30.addSection(SECTION_REGISTRY.get("is1").get("s6_t2"));
        route30.addSection(SECTION_REGISTRY.get("ro1").get("s2_t2"));
        route30.addSection(SECTION_REGISTRY.get("ro1").get("s3_t2"));
        route30.addSection(SECTION_REGISTRY.get("ro1").get("s7_t2"));
        route30.addSection(SECTION_REGISTRY.get("ro1").get("s12_t1"));
        route30.addSection(SECTION_REGISTRY.get("ro1").get("s16_t1"));
        route30.addSection(SECTION_REGISTRY.get("ro1").get("s17_t1"));
        route30.addSection(SINK_REGISTRY.get("ro1").get("sk_s17_t1"));
        is1_so_s3_t4_routes.add(route30);
        resultMap.put("is1_so_s3_t4", is1_so_s3_t4_routes);

        //all routes from  is1 so_s3_t3
        // route is1 so_s3_t3 --> ro1 sk_s4_t1
        final List<Route> is1_so_s3_t3_routes = new LinkedList<>();
        final Route route31 = new Route();
        route31.addSource(SOURCE_REGISTRY.get("is1").get("so_s3_t3"));
        route31.addSection(SECTION_REGISTRY.get("is1").get("s3_t3"));
        route31.addSection(SECTION_REGISTRY.get("is1").get("s6_t1"));
        route31.addSection(SECTION_REGISTRY.get("ro1").get("s2_t1"));
        route31.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route31.addSection(SECTION_REGISTRY.get("ro1").get("s4_t1"));
        route31.addSection(SINK_REGISTRY.get("ro1").get("sk_s4_t1"));
        is1_so_s3_t3_routes.add(route31);
        // route is1_so_s3_t3 --> ro1 sk_s9_t1
        final Route route32 = new Route();
        route32.addSource(SOURCE_REGISTRY.get("is1").get("so_s3_t3"));
        route32.addSection(SECTION_REGISTRY.get("is1").get("s3_t3"));
        route32.addSection(SECTION_REGISTRY.get("is1").get("s6_t1"));
        route32.addSection(SECTION_REGISTRY.get("ro1").get("s2_t1"));
        route32.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route32.addSection(SECTION_REGISTRY.get("ro1").get("s7_t1"));
        route32.addSection(SECTION_REGISTRY.get("ro1").get("s8_t1"));
        route32.addSection(SECTION_REGISTRY.get("ro1").get("s9_t1"));
        route32.addSection(SINK_REGISTRY.get("ro1").get("sk_s9_t1"));
        is1_so_s3_t3_routes.add(route32);
        // route is1_so_s3_t3 --> ro1 sk_s9_t2
        final Route route33 = new Route();
        route33.addSource(SOURCE_REGISTRY.get("is1").get("so_s3_t3"));
        route33.addSection(SECTION_REGISTRY.get("is1").get("s3_t3"));
        route33.addSection(SECTION_REGISTRY.get("is1").get("s6_t1"));
        route33.addSection(SECTION_REGISTRY.get("ro1").get("s2_t1"));
        route33.addSection(SECTION_REGISTRY.get("ro1").get("s3_t1"));
        route33.addSection(SECTION_REGISTRY.get("ro1").get("s7_t1"));
        route33.addSection(SECTION_REGISTRY.get("ro1").get("s8_t1"));
        route33.addSection(SECTION_REGISTRY.get("ro1").get("s9_t2"));
        route33.addSection(SINK_REGISTRY.get("ro1").get("sk_s9_t2"));
        is1_so_s3_t3_routes.add(route33);
        resultMap.put("Is1_so_s3_t3", is1_so_s3_t3_routes);

        return resultMap;
    }
}
