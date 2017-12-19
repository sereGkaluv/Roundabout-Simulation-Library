package at.fhv.itm3.s2.roundabout.util;

import at.fhv.itm3.s2.roundabout.util.dto.RoundAboutConfig;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

public class ConfigParserTest {
    @Test
    public void configParserTest_parseExampleXml() throws ConfigParserException {
        URL path = getClass().getClassLoader().getResource("test/roundabout.xml");
        RoundAboutConfig roundAboutConfig = ConfigParser.loadConfig(path.getPath());

        assertNotNull("parameters attribute not null",roundAboutConfig.getParameters());
        assertEquals("5 parameters loaded",5,roundAboutConfig.getParameters().getParameter().size());

        assertNotNull("structure attribute not null",roundAboutConfig.getStructure());
        assertEquals("5 sections loaded in structure",5,roundAboutConfig.getStructure().getSections().size());
    }

    @Test(expected = ConfigParserException.class)
    public void configParserTest_fileNotFound() throws ConfigParserException {
        String file = "not_exists.xml";
        ConfigParser.loadConfig(file);
    }
}
