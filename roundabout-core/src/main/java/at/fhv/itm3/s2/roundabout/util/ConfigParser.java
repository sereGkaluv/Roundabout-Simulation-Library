package at.fhv.itm3.s2.roundabout.util;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.IRoundaboutStructure;
import at.fhv.itm3.s2.roundabout.entity.*;
import at.fhv.itm3.s2.roundabout.util.dto.*;
import desmoj.core.simulator.Experiment;

import javax.xml.bind.JAXB;
import java.io.File;
import java.util.*;

public class ConfigParser {
    private String filename;

    public ConfigParser(String filename) {
        this.filename = filename;
    }

    public RoundAboutConfig loadConfig() throws ConfigParserException {
        File configFile = new File(filename);
        if (!configFile.exists()) {
            throw new ConfigParserException("no such config file " + filename);
        }
        return JAXB.unmarshal(configFile, RoundAboutConfig.class);
    }

    public IRoundaboutStructure generateRoundaboutStructure(RoundAboutConfig roundAboutConfig, Experiment experiment) throws ConfigParserException {
        RoundaboutSimulationModel model = new RoundaboutSimulationModel(null, roundAboutConfig.getRoundabout().getName(), false, false);
        model.connectToExperiment(experiment);

        IRoundaboutStructure roundaboutStructure = new RoundaboutStructure(model);

        generateParameters(roundaboutStructure, roundAboutConfig);

        HashMap<String, List<IConsumer>> previousStreetSectionsMap = new HashMap<>();
        HashMap<String, List<IConsumer>> nextStreetSectionsMap = new HashMap<>();

        for (Section section : roundAboutConfig.getRoundabout().getSections().getSection()) {
            String startConnectorKey = section.getPrevious() + section.getId();
            String endConnectorKey = section.getId() + section.getNext();
            List<IConsumer> startPreviousStreetSections = previousStreetSectionsMap.get(startConnectorKey);
            List<IConsumer> startNextStreetSections = nextStreetSectionsMap.get(startConnectorKey);
            List<IConsumer> endPreviousStreetSections = previousStreetSectionsMap.get(endConnectorKey);
            List<IConsumer> endNextStreetSections = nextStreetSectionsMap.get(endConnectorKey);

            if (startPreviousStreetSections == null) {
                startPreviousStreetSections = new LinkedList<>();
            }
            if (startNextStreetSections == null) {
                startNextStreetSections = new LinkedList<>();
            }
            if (endPreviousStreetSections == null) {
                endPreviousStreetSections = new LinkedList<>();
            }
            if (endNextStreetSections == null) {
                endNextStreetSections = new LinkedList<>();
            }

            generateEntries(roundaboutStructure, startPreviousStreetSections, section);
            generateTracks(roundaboutStructure, startNextStreetSections, endPreviousStreetSections, section);
            generateExit(roundaboutStructure, endNextStreetSections, section);

            previousStreetSectionsMap.put(startConnectorKey, startPreviousStreetSections);
            previousStreetSectionsMap.put(endConnectorKey, endPreviousStreetSections);
            nextStreetSectionsMap.put(startConnectorKey, startNextStreetSections);
            nextStreetSectionsMap.put(endConnectorKey, endNextStreetSections);
        }

        for (Section section : roundAboutConfig.getRoundabout().getSections().getSection()) {
            String connectorKey = section.getPrevious() + section.getId();
            List<IConsumer> previousStreetSections = previousStreetSectionsMap.get(connectorKey);
            List<IConsumer> nextStreetSections = nextStreetSectionsMap.get(connectorKey);
            roundaboutStructure.addStreetConnector(new StreetConnector(previousStreetSections, nextStreetSections));
        }

        //TODO generate tracks

        return roundaboutStructure;
    }

    private void generateParameters(IRoundaboutStructure roundaboutStructure, RoundAboutConfig roundaboutConfig) {
        Iterator<Parameter> keyIterator = roundaboutConfig.getRoundabout().getParameters().getParameter().iterator();
        while (keyIterator.hasNext()) {
            Parameter parameter = keyIterator.next();
            roundaboutStructure.addParameter(parameter.getName(), parameter.getValue());
        }
    }

    private void generateExit(IRoundaboutStructure structure, List<IConsumer> endNextSections, Section section) throws ConfigParserException {
        if (section.getExit() != null) {
            if (section.getExit().getConnectorId() != null) {
                //TODO connection to trafsim intersection
            } else {
                RoundaboutSink roundaboutSink = new RoundaboutSink(structure.getModel(), "exit from section with id " + section.getId(), false);
                endNextSections.add(roundaboutSink);

                structure.addStreet(roundaboutSink);
            }
        } else {
            throw new ConfigParserException("no exit defined for section with id " + section.getId());
        }
    }

    private void generateEntries(IRoundaboutStructure structure, List<IConsumer> startPreviousSections, Section section) throws ConfigParserException {
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
                StreetSection entryStreet = new StreetSection(length, structure.getModel(), "entry street to section with id " + section.getId(), false);
                RoundaboutSource source = new RoundaboutSource(structure.getModel(), "entry source to section with id " + section.getId(), false, entryStreet);
                startPreviousSections.add(entryStreet);

                structure.addStreet(entryStreet);
            }
        }
    }

    private void generateTracks(IRoundaboutStructure structure, List<IConsumer> startNextSections, List<IConsumer> endPreviousSections, Section section) throws ConfigParserException {
        for (Track track : section.getTrack()) {
            double length;
            try {
                length = Double.valueOf(track.getLength());
            } catch (NumberFormatException e) {
                throw new ConfigParserException("attribute length of track with id " + track.getId() + " in section with id " + section.getId() + " not parseable, error: " + e.getMessage());
            }

            StreetSection streetSection = new StreetSection(length, structure.getModel(), "track with id " + track.getId() + " in section with id " + section.getId(), false);
            startNextSections.add(streetSection);
            endPreviousSections.add(streetSection);

            structure.addStreet(streetSection);
        }
    }
}
