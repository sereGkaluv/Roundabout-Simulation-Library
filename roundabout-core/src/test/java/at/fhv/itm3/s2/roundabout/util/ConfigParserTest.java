package at.fhv.itm3.s2.roundabout.util;

import at.fhv.itm3.s2.roundabout.util.dto.RoundAboutConfig;
import at.fhv.itm3.s2.roundabout.util.dto.Sections;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

public class ConfigParserTest {
    private RoundAboutConfig roundAboutConfig;

    @Before
    public void setUp() throws ConfigParserException {
        URL path = getClass().getClassLoader().getResource("test/roundabout.xml");
        assertNotNull(path);

        roundAboutConfig = ConfigParser.loadConfig(path.getPath());
    }

    @Test
    public void configParserTest_parametersLoaded() {
        assertNotNull("parameters attribute not null", roundAboutConfig.getParameters());
        assertEquals("5 parameters loaded", 5, roundAboutConfig.getParameters().getParameter().size());
    }

    @Test
    public void configParserTest_parametersDataLoaded() {
        assertNotNull("parameter name loaded", roundAboutConfig.getParameters().getParameter().get(0).getName());
        assertNotNull("parameter value loaded", roundAboutConfig.getParameters().getParameter().get(0).getValue());
    }

    @Test
    public void configParserTest_structureLoaded() {
        assertNotNull("structure loaded", roundAboutConfig.getStructure());
    }

    @Test
    public void configParserTest_sectionsLoaded() {
        assertEquals("5 sections loaded in structure", 5, roundAboutConfig.getStructure().getSections().size());
    }

    @Test
    public void configParserTest_sectionsDataLoaded() {
        assertNotNull("sections has in", roundAboutConfig.getStructure().getSections().get(0).getIn());
        assertNotNull("sections has 2 in", roundAboutConfig.getStructure().getSections().get(0).getIn().size());
        assertNotNull("sections has out", roundAboutConfig.getStructure().getSections().get(0).getOut());
        assertNotNull("sections has section", roundAboutConfig.getStructure().getSections().get(0).getSection());

        assertNotNull("sections id", roundAboutConfig.getStructure().getSections().get(0).getId());
        assertNotNull("sections previous", roundAboutConfig.getStructure().getSections().get(0).getPrevious());
        assertNotNull("sections next", roundAboutConfig.getStructure().getSections().get(0).getNext());
    }

    @Test
    public void configParserTest_sectionDataLoaded() {
        assertNotNull("sections has section", roundAboutConfig.getStructure().getSections().get(0).getSection());
        assertNotEquals("sections has section", 0, roundAboutConfig.getStructure().getSections().get(0).getSection().size());
        assertNotNull("section has length", roundAboutConfig.getStructure().getSections().get(0).getSection().get(0).getLength());
        assertNotNull("section has trackId", roundAboutConfig.getStructure().getSections().get(0).getSection().get(0).getTrackId());
    }

    @Test
    public void configParserTest_inDataLoaded() {
        assertNotNull("in has trackId", roundAboutConfig.getStructure().getSections().get(0).getIn().get(0).getTrackId());
        assertNotNull("in has probability", roundAboutConfig.getStructure().getSections().get(0).getIn().get(0).getProbability());
    }

    @Test
    public void configParserTest_outDataLoaded() {
        assertNotNull("out has trackId", roundAboutConfig.getStructure().getSections().get(0).getOut().getTrackId());
        assertNotNull("out has probability", roundAboutConfig.getStructure().getSections().get(0).getOut().getProbability());

        boolean hasStreetId = false;
        for(Sections sections : roundAboutConfig.getStructure().getSections()){
            if(sections.getOut().getStreetId()!=null){
                hasStreetId = true;
            }
        }
        assertTrue("some out has streetId", hasStreetId);
    }

    @Test(expected = ConfigParserException.class)
    public void configParserTest_fileNotFound() throws ConfigParserException {
        String file = "not_exists.xml";
        ConfigParser.loadConfig(file);
    }
}
