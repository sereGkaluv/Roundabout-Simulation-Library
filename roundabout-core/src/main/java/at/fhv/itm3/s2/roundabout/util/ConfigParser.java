package at.fhv.itm3.s2.roundabout.util;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.RoundaboutSource;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.entity.StreetConnector;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import at.fhv.itm3.s2.roundabout.util.dto.Entry;
import at.fhv.itm3.s2.roundabout.util.dto.RoundAboutConfig;
import at.fhv.itm3.s2.roundabout.util.dto.Section;
import at.fhv.itm3.s2.roundabout.util.dto.Track;
import desmoj.core.simulator.Experiment;

import javax.xml.bind.JAXB;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ConfigParser {
    private String filename;

    public static RoundAboutConfig loadConfig(String filename) throws ConfigParserException {
        File configFile = new File(filename);
        if (!configFile.exists()) {
            throw new ConfigParserException("no such config file " + filename);
        }
        return JAXB.unmarshal(configFile, RoundAboutConfig.class);
    }

    public static RoundaboutSimulationModel generateModel(RoundAboutConfig roundAboutConfig, Experiment experiment) throws ConfigParserException {
        RoundaboutSimulationModel model = new RoundaboutSimulationModel(null, roundAboutConfig.getRoundabout().getName(), false, false);
        model.connectToExperiment(experiment);

        HashMap<String, Set<Street>> previousStreetSectionsMap = new HashMap<String, Set<Street>>();
        HashMap<String, Set<Street>> nextStreetSectionsMap = new HashMap<String, Set<Street>>();

        for (Section section : roundAboutConfig.getRoundabout().getSections().getSection()) {
            String startConnectorKey = section.getPrevious() + section.getId();
            String endConnectorKey = section.getId() + section.getNext();
            Set<Street> startPreviousStreetSections = previousStreetSectionsMap.get(startConnectorKey);
            Set<Street> startNextStreetSections = nextStreetSectionsMap.get(startConnectorKey);
            Set<Street> endPreviousStreetSections = previousStreetSectionsMap.get(endConnectorKey);
            Set<Street> endNextStreetSections = nextStreetSectionsMap.get(endConnectorKey);

            if (startPreviousStreetSections == null) {
                startPreviousStreetSections = new HashSet<Street>();
            }
            if (startNextStreetSections == null) {
                startNextStreetSections = new HashSet<Street>();
            }
            if (endPreviousStreetSections == null) {
                endPreviousStreetSections = new HashSet<Street>();
            }
            if (endNextStreetSections == null) {
                endNextStreetSections = new HashSet<Street>();
            }

            generateEntries(model, startPreviousStreetSections, section);
            generateTracks(model, startNextStreetSections, endPreviousStreetSections, section);
            generateExit(model, endNextStreetSections, section);

            previousStreetSectionsMap.put(startConnectorKey, startPreviousStreetSections);
            previousStreetSectionsMap.put(endConnectorKey, endPreviousStreetSections);
            nextStreetSectionsMap.put(startConnectorKey, startNextStreetSections);
            nextStreetSectionsMap.put(endConnectorKey, endNextStreetSections);
        }

        return model;
    }

    private static void addConnector(Set<StreetConnector> connectors, Set<Street> startPreviousSections, Set<Street> startNextSections, Set<Street> endPreviousSections, Set<Street> endNextSections, int iterationCount, int lastIteration) {
        if (iterationCount == lastIteration) {
            StreetConnector streetConnector = new StreetConnector(startPreviousSections, startNextSections);
            connectors.add(streetConnector);
            StreetConnector streetConnectorEnd = new StreetConnector(endPreviousSections, endNextSections);
            connectors.add(streetConnectorEnd);
        } else {
            StreetConnector streetConnector = new StreetConnector(startPreviousSections, startNextSections);
            connectors.add(streetConnector);
            startNextSections.clear();
            startNextSections.clear();
        }
    }

    private static void generateExit(RoundaboutSimulationModel model, Set<Street> endNextSections, Section section) throws ConfigParserException {
        if (section.getExit() != null) {
            if (section.getExit().getConnectorId() != null) {
                //TODO connection to trafsim intersection
            } else {
                RoundaboutSink roundaboutSink = new RoundaboutSink(model, "exit from section with id " + section.getId(), false);
                endNextSections.add(roundaboutSink);
            }
        } else {
            throw new ConfigParserException("no exit defined for section with id " + section.getId());
        }
    }

    private static void generateEntries(RoundaboutSimulationModel model, Set<Street> startPreviousSections, Section section) throws ConfigParserException {
        for (Entry entry : section.getEntry()) {
            if (entry.getConnectorId() != null) {
                //TODO connection from trafsim intersection
            } else {
                double length;
                try {
                    length = Double.valueOf(entry.getLength());
                } catch (NumberFormatException e) {
                    throw new ConfigParserException("attribute length of entry in section with id " + section.getId() + " not parseable, error: " + e.getMessage());
                }
                StreetSection entryStreet = new StreetSection(length, model, "entry street to section with id " + section.getId(), false);
                RoundaboutSource source = new RoundaboutSource(model, "entry source to section with id " + section.getId(), false, entryStreet);
                startPreviousSections.add(entryStreet);
            }
        }
    }

    private static void generateTracks(RoundaboutSimulationModel model, Set<Street> startNextSections, Set<Street> endPreviousSections, Section section) throws ConfigParserException {
        for (Track track : section.getTrack()) {
            double length;
            try {
                length = Double.valueOf(track.getLength());
            } catch (NumberFormatException e) {
                throw new ConfigParserException("attribute length of track with id " + track.getId() + " in section with id " + section.getId() + " not parseable, error: " + e.getMessage());
            }

            StreetSection streetSection = new StreetSection(length, model, "track with id " + track.getId() + " in section with id " + section.getId(), false);
            startNextSections.add(streetSection);
            endPreviousSections.add(streetSection);
        }
    }
}
