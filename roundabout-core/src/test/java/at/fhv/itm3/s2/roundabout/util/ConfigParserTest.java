package at.fhv.itm3.s2.roundabout.util;

import at.fhv.itm3.s2.roundabout.api.entity.IRoundaboutStructure;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.util.dto.RoundAboutConfig;
import at.fhv.itm3.s2.roundabout.util.dto.Section;
import desmoj.core.simulator.Experiment;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

public class ConfigParserTest {
    private RoundAboutConfig roundAboutConfig;
    private ConfigParser configParser;

    @Before
    public void setUp() throws ConfigParserException {
        URL path = getClass().getClassLoader().getResource("test/roundabout.xml");
        assertNotNull(path);

        configParser = new ConfigParser(path.getPath());
        roundAboutConfig = configParser.loadConfig();
    }

    @Test
    public void configParserTest_parametersLoaded() {
        assertNotNull("parameters attribute not null", roundAboutConfig.getParameters());
        assertEquals("6 parameters loaded", 1, roundAboutConfig.getParameters().getParameter().size());
    }

    @Test
    public void configParserTest_parametersDataLoaded() {
        assertNotNull("parameter name loaded", roundAboutConfig.getParameters().getParameter().get(0).getName());
        assertNotNull("parameter value loaded", roundAboutConfig.getParameters().getParameter().get(0).getValue());
    }

    @Test
    public void configParserTest_roundaboutLoaded() {
        assertNotNull("roundabout loaded", roundAboutConfig.getRoundabout());
    }

    @Test
    public void configParserTest_roundaboutDataLoaded() {
        assertNotNull("roundabout sections loaded", roundAboutConfig.getRoundabout().getSections());
        assertEquals("roundabout correct name", "Kreisverkehr Dornbirn Süd", roundAboutConfig.getRoundabout().getName());
        assertEquals("5 sections loaded in roundabout", 5, roundAboutConfig.getRoundabout().getSections().getSection().size());
    }

    @Test
    public void configParserTest_sectionsDataLoaded() {
        assertNotNull("sections has entry", roundAboutConfig.getRoundabout().getSections().getSection().get(0).getEntry());
        assertEquals("sections has 2 entries", 2, roundAboutConfig.getRoundabout().getSections().getSection().get(0).getEntry().size());
        assertNotNull("sections has out", roundAboutConfig.getRoundabout().getSections().getSection().get(0).getExit());
        assertNotNull("sections has tracks", roundAboutConfig.getRoundabout().getSections().getSection().get(0).getTrack());

        assertNotNull("sections id", roundAboutConfig.getRoundabout().getSections().getSection().get(0).getId());
        assertNotNull("sections previous", roundAboutConfig.getRoundabout().getSections().getSection().get(0).getPrevious());
        assertNotNull("sections next", roundAboutConfig.getRoundabout().getSections().getSection().get(0).getNext());
    }

    @Test
    public void configParserTest_sectionDataLoaded() {
        assertNotNull("section has tracks", roundAboutConfig.getRoundabout().getSections().getSection().get(0).getTrack());
        assertNotEquals("section has tracks", 0, roundAboutConfig.getRoundabout().getSections().getSection().get(0).getTrack().size());
        assertNotNull("track has length", roundAboutConfig.getRoundabout().getSections().getSection().get(0).getTrack().get(0).getLength());
        assertNotNull("track has id", roundAboutConfig.getRoundabout().getSections().getSection().get(0).getTrack().get(0).getId());
    }

    @Test
    public void configParserTest_entryDataLoaded() {
        assertNotNull("entry has trackId", roundAboutConfig.getRoundabout().getSections().getSection().get(0).getEntry().get(0).getTrackId());
        assertNotNull("entry has probability", roundAboutConfig.getRoundabout().getSections().getSection().get(0).getEntry().get(0).getProbability());

        boolean hasStreetId = false;
        for (Section section : roundAboutConfig.getRoundabout().getSections().getSection()) {
            if (section.getEntry().get(0) != null && section.getEntry().get(0).getConnectorId() != null) {
                hasStreetId = true;
            }
        }
        assertTrue("some entry has connectorId", hasStreetId);
    }

    @Test
    public void configParserTest_exitDataLoaded() {
        assertNotNull("exit has trackId", roundAboutConfig.getRoundabout().getSections().getSection().get(0).getExit().getTrackId());
        assertNotNull("exit has probability", roundAboutConfig.getRoundabout().getSections().getSection().get(0).getExit().getProbability());

        boolean hasStreetId = false;
        for (Section section : roundAboutConfig.getRoundabout().getSections().getSection()) {
            if (section.getExit().getConnectorId() != null) {
                hasStreetId = true;
            }
        }
        assertTrue("some exit has connectorId", hasStreetId);
    }

    @Test(expected = ConfigParserException.class)
    public void configParserTest_fileNotFound() throws ConfigParserException {
        String file = "not_exists.xml";
        ConfigParser configParser = new ConfigParser(file);
        roundAboutConfig = configParser.loadConfig();
    }

    @Test
    public void configParserTest_generateModel() throws ConfigParserException {
        Experiment exp = new Experiment("Experiment");
        IRoundaboutStructure roundaboutStructure = configParser.generateStructure(roundAboutConfig, exp);

        assertNotNull("has connectors", roundaboutStructure.getStreetConnectors());
        assertEquals("has 5 street connectors", 5, roundaboutStructure.getStreetConnectors().size());

        assertNotNull("has streets", roundaboutStructure.getStreets());
        assertEquals("has 22 streets", 22, roundaboutStructure.getStreets().size());
    }

    @Test
    public void configParserTest_connectorsHaveData() throws ConfigParserException {
        Experiment exp = new Experiment("Experiment");
        IRoundaboutStructure roundaboutStructure = configParser.generateStructure(roundAboutConfig, exp);

        for (IStreetConnector connector : roundaboutStructure.getStreetConnectors()) {
            assertNotEquals("next street sections not empty", 0, connector.getNextSections().size());
            assertNotEquals("previous street sections not empty", 0, connector.getPreviousSections().size());
        }
    }
}
