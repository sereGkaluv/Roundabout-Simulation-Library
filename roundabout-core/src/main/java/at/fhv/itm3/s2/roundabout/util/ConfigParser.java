package at.fhv.itm3.s2.roundabout.util;

import at.fhv.itm14.trafsim.model.ModelFactory;
import at.fhv.itm14.trafsim.model.entities.AbstractConsumer;
import at.fhv.itm14.trafsim.model.entities.AbstractProSumer;
import at.fhv.itm14.trafsim.model.entities.AbstractProducer;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.intersection.FixedCirculationController;
import at.fhv.itm14.trafsim.model.entities.intersection.IntersectionController;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.IRoundaboutStructure;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.entity.*;
import at.fhv.itm3.s2.roundabout.util.dto.*;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;

import javax.xml.bind.JAXB;
import java.io.File;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class ConfigParser {
    private static final String INTERSECTION_SIZE = "INTERSECTION_SIZE";
    private static final String INTERSECTION_SERVICE_DELAY = "INTERSECTION_SERVICE_DELAY";
    private static final String CONTROLLER_GREEN_DURATION = "CONTROLLER_GREEN_DURATION";
    private static final String CONTROLLER_YELLOW_DURATION = "CONTROLLER_YELLOW_DURATION";
    private static final String CONTROLLER_PHASE_SHIFT_TIME = "CONTROLLER_PHASE_SHIFT_TIME";

    private static final String CONNECTOR_KEY_FORMAT = "%s → %s";

    private static final Map<String, Map<String, RoundaboutSource>> SOURCE_REGISTRY = new HashMap<>();
    private static final Map<String, Map<String, RoundaboutSink>> SINK_REGISTRY = new HashMap<>();
    private static final Map<String, Map<String, StreetSection>> SECTION_REGISTRY = new HashMap<>();

    private String filename;

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

    public IRoundaboutStructure generateRoundaboutStructure(ModelConfig modelConfig, Experiment experiment) throws ConfigParserException {
        RoundaboutSimulationModel model = new RoundaboutSimulationModel(null, modelConfig.getName(), false, false);
        model.connectToExperiment(experiment);

        IRoundaboutStructure roundaboutStructure = new RoundaboutStructure(model);
        handleParameters(roundaboutStructure, modelConfig.getParameters());

        handleComponents(roundaboutStructure, modelConfig.getComponents());
//
//        Map<String, IConsumer> componentsMap = new HashMap<>();
//
//        RoundaboutIntersection intersection = new RoundaboutIntersection();
//        Map<String, IConsumer> sectionsMap = new HashMap<>();
//        Map<String, Map<String, IConsumer>> sectionTracksMap = new HashMap<>();
//
//        handleComponents(roundaboutStructure, modelConfig)
//
//
//        HashMap<String, List<IConsumer>> previousStreetSectionsMap = new HashMap<>();
//        HashMap<String, List<IConsumer>> nextStreetSectionsMap = new HashMap<>();
//
//        for (Section section : modelConfig.getRoundabout().getSections().getSection()) {
//            String startConnectorKey = section.getPrevious() + section.getId();
//            String endConnectorKey = section.getId() + section.getNext();
//            List<IConsumer> startPreviousStreetSections = previousStreetSectionsMap.get(startConnectorKey);
//            List<IConsumer> startNextStreetSections = nextStreetSectionsMap.get(startConnectorKey);
//            List<IConsumer> endPreviousStreetSections = previousStreetSectionsMap.get(endConnectorKey);
//            List<IConsumer> endNextStreetSections = nextStreetSectionsMap.get(endConnectorKey);
//
//            if (startPreviousStreetSections == null) {
//                startPreviousStreetSections = new LinkedList<>();
//            }
//            if (startNextStreetSections == null) {
//                startNextStreetSections = new LinkedList<>();
//            }
//            if (endPreviousStreetSections == null) {
//                endPreviousStreetSections = new LinkedList<>();
//            }
//            if (endNextStreetSections == null) {
//                endNextStreetSections = new LinkedList<>();
//            }
//
//            generateEntries(roundaboutStructure, startPreviousStreetSections, section);
//            generateTracks(roundaboutStructure, startNextStreetSections, endPreviousStreetSections, section);
//            generateExit(roundaboutStructure, endNextStreetSections, section);
//
//            previousStreetSectionsMap.put(startConnectorKey, startPreviousStreetSections);
//            previousStreetSectionsMap.put(endConnectorKey, endPreviousStreetSections);
//            nextStreetSectionsMap.put(startConnectorKey, startNextStreetSections);
//            nextStreetSectionsMap.put(endConnectorKey, endNextStreetSections);
//        }
//
//        for (Section section : modelConfig.getRoundabout().getSections().getSection()) {
//            String connectorKey = section.getPrevious() + section.getId();
//            List<IConsumer> previousStreetSections = previousStreetSectionsMap.get(connectorKey);
//            List<IConsumer> nextStreetSections = nextStreetSectionsMap.get(connectorKey);
//            roundaboutStructure.addStreetConnector(new StreetConnector(previousStreetSections, nextStreetSections));
//        }
//
//        //TODO generate tracks
//
        return roundaboutStructure;
    }

    private void handleParameters(IRoundaboutStructure roundaboutStructure, Parameters parameters) {
        for (Parameter parameter : parameters.getParameter()) {
            roundaboutStructure.addParameter(parameter.getName(), parameter.getValue());
        }
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

    void handleRoundabout(IRoundaboutStructure roundaboutStructure, Component roundaboutComponent) {
//        final Map<String, String> parameters = roundaboutComponent.getParameters().getParameter().stream().collect(toMap(
//            Parameter::getName, Parameter::getValue
//        ));
//
//        final Model model = roundaboutStructure.getModel();
//        final Map<String, StreetSection> sections = handleSections(roundaboutComponent.getSections(), model);
//        final Map<String, RoundaboutSource> sources = handleSources(roundaboutComponent.getSources(), model, sections);
//        final Map<String, RoundaboutSink> sinks = handleSinks(roundaboutComponent.getSinks(), model);
//
//        final Map<String, StreetConnector> connectors = handleConnectors(roundaboutComponent.getConnectors(), model);
//
//        final Map<String, StreetConnector> connectorsMap = new HashMap<>();
//        for (Connector connector : roundaboutComponent.getConnectors().getConnector()) {
//            final FromConnector fromConnector = connector.getFrom();
//            final AbstractProSumer fromTrack = sections.get(fromConnector.getSectionId()).get(fromConnector.getTrackId());
//
//            final ToConnector toConnector = connector.getTo();
//            final AbstractProSumer toTrack = sections.get(toConnector.getSectionId()).get(toConnector.getTrackId());
//
//            final String connectorKey = toConnector.getSectionId() + toConnector.getTrackId();
//            if (!connectorsMap.containsKey(connectorKey)) {
//                final List<IConsumer> nextConsumers = new LinkedList<>();
//                nextConsumers.add(toTrack);
//
//                connectorsMap.put(connectorKey, new StreetConnector(new LinkedList<>(), nextConsumers));
//            }
//
//            final StreetConnector streetConnector = connectorsMap.get(connectorKey);
//            streetConnector.getPreviousConsumers().add(fromTrack);
//        }
    }

    RoundaboutIntersection handleIntersection(IRoundaboutStructure roundaboutStructure, Component intersectionComponent) {
        final Map<String, String> parameters = intersectionComponent.getParameters().getParameter().stream().collect(toMap(
            Parameter::getName, Parameter::getValue
        ));

        final Model model = roundaboutStructure.getModel();
 //       final Map<String, StreetSection> sections = handleSections(intersectionComponent.getSections(), model);

        final RoundaboutIntersection intersection = new RoundaboutIntersection(
            model,
            intersectionComponent.getName(),
            false,
            Integer.parseInt(parameters.get(INTERSECTION_SIZE))
        );
//
//        intersection.setServiceDelay(Float.valueOf(parameters.get(INTERSECTION_SERVICE_DELAY)));
//        final FixedCirculationController ic = ModelFactory.getInstance(model).createOneWayController(
//            intersection,
//            Float.valueOf(parameters.get(CONTROLLER_GREEN_DURATION)),
//            Float.valueOf(parameters.get(CONTROLLER_YELLOW_DURATION)),
//            Float.valueOf(parameters.get(CONTROLLER_PHASE_SHIFT_TIME))
//        );
//        intersection.attachController(ic);
//
//        final IntersectionController intersectionController = IntersectionController.getInstance();
//        for (Connector connector : intersectionComponent.getConnectors().getConnector()) {
//            final FromConnector fromConnector = connector.getFrom();
//            final int outDirection = Integer.valueOf(fromConnector.getSectionId() + fromConnector.getTrackId());
//            final AbstractProSumer fromTrack = sections.get(fromConnector.getSectionId()).get(fromConnector.getTrackId());
//            final AbstractProducer producer = fromTrack.toProducer();
//            intersectionController.setIntersectionOutDirectionMapping(intersection, fromTrack, outDirection);
//            intersection.attachProducer(outDirection, producer);
//
//            final ToConnector toConnector = connector.getTo();
//            final int inDirection = Integer.valueOf(toConnector.getSectionId() + toConnector.getTrackId());
//            final AbstractProSumer toTrack = sections.get(toConnector.getSectionId()).get(toConnector.getTrackId());
//            final AbstractConsumer consumer = toTrack.toConsumer();
//            intersectionController.setIntersectionInDirectionMapping(intersection, toTrack, inDirection);
//            intersection.attachConsumer(inDirection, consumer);
//
//            intersection.createConnectionQueue(
//                producer,
//                new AbstractConsumer[]{consumer},
//                new double[]{connector.getTraverseTime()},
//                new double[]{connector.getProbability()}
//            );
//        }
//
        return intersection;
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

    private Map<String, StreetConnector> handleConnectors(String scopeComponentId, Connectors connectors, Model model) {
        final Comparator<Track> trackComparator = Comparator.comparingLong(Track::getOrder);
        return connectors.getConnector().stream().collect(toMap(
            Connector::getId,
            co -> {
                final List<IConsumer> previousSections = new LinkedList<>();
                final List<IConsumer> nextSections = new LinkedList<>();

                final List<Consumer<StreetConnector>> trackInitializers = new LinkedList<>();

                final List<Track> trackList = co.getTrack().stream().sorted(trackComparator).collect(Collectors.toList());
                for (Track track : trackList) {
                    final String fromComponentId = track.getFromComponentId() != null ? track.getFromComponentId() : scopeComponentId;
                    final StreetSection fromSection = resolveSection(fromComponentId, track.getFromSectionId());
                    previousSections.add(fromSection);

                    final String toComponentId = track.getToComponentId() != null ? track.getToComponentId() : scopeComponentId;
                    final StreetSection toSection = resolveSection(toComponentId, track.getToSectionId());
                    nextSections.add(toSection);

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
//
//
//    private void generateSinks(IRoundaboutStructure structure, List<IConsumer> endNextSections, Section section) throws ConfigParserException {
//        if (section.getExit() != null) {
//            if (section.getExit().getConnectorId() != null) {
//                //TODO connection to trafsim intersection
//            } else {
//                RoundaboutSink roundaboutSink = new RoundaboutSink(structure.getModel(), "exit from section with id " + section.getId(), false);
//                endNextSections.add(roundaboutSink);
//
//                structure.addStreet(roundaboutSink);
//            }
//        } else {
//            throw new ConfigParserException("no exit defined for section with id " + section.getId());
//        }
//    }
//
//    private void generateSources(IRoundaboutStructure structure, List<IConsumer> startPreviousSections, Section section) throws ConfigParserException {
//        for (Entry entry : section.getEntry()) {
//            if (entry.getConnectorId() != null) {
//                //TODO connection from trafsim intersection
//            } else {
//                double length;
//                try {
//                    length = Double.valueOf(entry.getLength());
//                } catch (NumberFormatException e) {
//                    throw new ConfigParserException("attribute length of entry in section with id " + section.getId() + " not parseable, error: " + e.getMessage());
//                }
//                StreetSection entryStreet = new StreetSection(length, structure.getModel(), "entry street to section with id " + section.getId(), false);
//                RoundaboutSource source = new RoundaboutSource(structure.getModel(), "entry source to section with id " + section.getId(), false, entryStreet);
//                startPreviousSections.add(entryStreet);
//
//                structure.addStreet(entryStreet);
//            }
//        }
//    }
//
//    private StreetSection handleTrack(Track track, String sectionId, Model model) {
//        double length = track.getLength() != null ? track.getLength() : 0;
//        return new StreetSection(
//            track.getId(),
//            length,
//            model,
//            "track with id " + track.getId() + " in section with id " + sectionId,
//            false
//        );
//    }
//
//    private void generateTracks(
//        IRoundaboutStructure structure,
//        List<IConsumer> startNextSections,
//        List<IConsumer> endPreviousSections,
//        Section section
//    ) {
//        for (Track track : section.getTrack()) {
//            StreetSection streetSection = handleTrack(track, section.getId(), structure.getModel());
//
//            startNextSections.add(streetSection);
//            endPreviousSections.add(streetSection);
//
//            structure.addStreet(streetSection);
//        }
//    }
//
}
