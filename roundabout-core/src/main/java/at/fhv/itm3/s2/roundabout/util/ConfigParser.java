package at.fhv.itm3.s2.roundabout.util;

import at.fhv.itm14.trafsim.model.ModelFactory;
import at.fhv.itm14.trafsim.model.entities.AbstractConsumer;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.intersection.FixedCirculationController;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.*;
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

    private static final Map<String, Map<String, RoundaboutSource>> SOURCE_REGISTRY = new HashMap<>(); // component ID, Section ID, Section
    private static final Map<String, Map<String, RoundaboutSink>> SINK_REGISTRY = new HashMap<>();
    private static final Map<String, Map<String, StreetSection>> SECTION_REGISTRY = new HashMap<>();
    private static final Comparator<Track> TRACK_COMPARATOR = Comparator.comparingLong(Track::getOrder);
    private static final Function<Connector, List<Track>> SORTED_TRACK_EXTRACTOR = co -> co.getTrack().stream().sorted(TRACK_COMPARATOR).collect(Collectors.toList());

    private String filename;
    private Map<Street, Map<Street, IRoute>> routes; // Source, Sink and Route

    public ConfigParser(String filename) {
        this.filename = filename;
    }

    public static void main(String[] args) throws ConfigParserException {
        ModelConfig c = new ConfigParser("C:\\Users\\sereGkaluv\\IdeaProjects\\Roundabout-Simulation-Library\\roundabout-core\\src\\main\\resources\\test\\roundabout.xml").loadConfig();
        System.out.println(c);
    }

    public ModelConfig loadConfig() throws ConfigParserException {
        File configFile = new File(filename);
        if (!configFile.exists()) {
            throw new ConfigParserException("No such config file " + filename);
        }
        return JAXB.unmarshal(configFile, ModelConfig.class);
    }

    public IModelStructure generateRoundaboutStructure(ModelConfig modelConfig, Experiment experiment) throws ConfigParserException {
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

        IModelStructure modelStructure = new ModelStructure(model, parameters);

        handleComponents(modelStructure, modelConfig.getComponents());
        handleConnectors(null, modelConfig.getConnectors()); // connect the different components(roundabouts and intersections)

        inizializeRoutes(modelConfig, modelStructure);

        // just verify the routes
        int cnt = 0;
        for (Street i : modelStructure.getRoutes().keySet()){
            System.out.print("Path " + cnt + " with Start " + i);
            for (Street j : modelStructure.getRoutes().get(i).keySet()){
                System.out.print(" " + modelStructure.getRoutes().get(i).get(j) );
            }
        }
        return modelStructure;
    }

    void inizializeRoutes(ModelConfig modelConfig, IModelStructure modelStructure){
        for( Component componentIt : modelConfig.getComponents().getComponent()){ //Iterate through Roundabouts and Intersections
            //start from every possible source
            for (Source sourceIt : componentIt.getSources().getSource()) {
                String currentStreetSectionID = sourceIt.getSectionId();
                IRoute currentPath = new Route();
                DepthFirstSearch(currentStreetSectionID, currentPath, componentIt, modelStructure);
            }
        }
    }

    void DepthFirstSearch(String currentStreetID, IRoute currentPath, Component component, IModelStructure modelStructure){

        // solely store if the start-street section is not stored
        Street currentStreet = modelStructure.getStreetFromID(currentStreetID);
        if(currentPath.isEmpty()){
            currentPath.addSection(currentStreet);
        }

        boolean endOfPath = true;
        // check each connector
        for(Connector connectorIt : component.getConnectors().getConnector()){
            IRoute currentPathTmp = currentPath;
            for(Track trackIt : connectorIt.getTrack()){
                if(trackIt.getFromSectionId().equals(currentStreetID)){
                    String nextStreetID = trackIt.getToSectionId();
                    Street nextStreet = modelStructure.getStreetFromID(nextStreetID);
                    if(!currentPathTmp.contains(nextStreet)){ // do not loop
                        endOfPath = false;
                        currentPathTmp.addSection(nextStreet);
                        DepthFirstSearch(nextStreetID, currentPathTmp, component, modelStructure);
                    }
                }
            }
        }
        if(endOfPath) {
            Street firstStreet;
            if (currentPath.getStartSection() instanceof Street) {
                firstStreet = (Street) currentPath.getStartSection();
            } else {
                throw new IllegalArgumentException("Section can not be converted to Street");
            }
            modelStructure.addRoute(firstStreet, currentStreet, currentPath);
        }
        return;
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
        handleSources(
            roundaboutComponent.getId(),
            roundaboutComponent.getSources(),
            model
        );

        handleSinks(
            roundaboutComponent.getId(),
            roundaboutComponent.getSinks(),
            model
        );

        handleSections(
            roundaboutComponent.getId(),
            roundaboutComponent.getSections(),
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
        handleSources(
            intersectionComponent.getId(),
            intersectionComponent.getSources(),
            model
        );

        handleSinks(
            intersectionComponent.getId(),
            intersectionComponent.getSinks(),
            model
        );

        handleSections(
            intersectionComponent.getId(),
            intersectionComponent.getSections(),
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
                final StreetSection fromSection = resolveSection(intersectionComponent.getId(), fromSectionId);
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
                final StreetSection toSection = resolveSection(intersectionComponent.getId(), toSectionId);
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

                intersection.createConnectionQueue(
                    fromSection.toProducer(),
                    new AbstractConsumer[]{toSection.toConsumer()},
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
                    final StreetSection fromSection = resolveSection(fromComponentId, track.getFromSectionId());
                    if (!previousSections.contains(fromSection)) previousSections.add(fromSection);

                    final String toComponentId = track.getToComponentId() != null ? track.getToComponentId() : scopeComponentId;
                    final StreetSection toSection = resolveSection(toComponentId, track.getToSectionId());
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

    private StreetSection resolveSection(String componentId, String sectionId) {
        return SECTION_REGISTRY.containsKey(componentId) ? SECTION_REGISTRY.get(componentId).get(sectionId) : null;
    }

    private <K, V, R> R extractParameter(Function<K, V> supplier, Function<V, R> converter, K constant) {
        return converter.apply(supplier.apply(constant));
    }
}
