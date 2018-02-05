package at.fhv.itm3.s2.roundabout.util;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutStructure;
import at.fhv.itm3.s2.roundabout.util.dto.ModelConfig;
import at.fhv.itm3.s2.roundabout.util.dto.Section;
import desmoj.core.simulator.Experiment;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigParserTest {
    private ModelConfig roundAboutConfig;
    private ConfigParser configParser;
    private Map<String, Double> parameters;

    @Before
    public void setUp() throws ConfigParserException {
        URL path = getClass().getClassLoader().getResource("test/dornbirn-nord.xml");
        assertNotNull(path);

        configParser = new ConfigParser(path.getPath());
        roundAboutConfig = configParser.loadConfig();

        Experiment experiment = new Experiment("Config Parser Test");
        RoundaboutSimulationModel model = new RoundaboutSimulationModel(
                null,
                roundAboutConfig.getName(),
                false,
                false
        );
        model.connectToExperiment(experiment);
        RoundaboutStructure structure = (RoundaboutStructure) configParser.generateRoundaboutStructure(roundAboutConfig, model);
    }

    @Test
    public void configParserTest_parametersModelLoaded() {
        assertNotNull("model parameters not null", roundAboutConfig.getParameters());
        assertEquals("1 model parameter loaded", 1, roundAboutConfig.getParameters().getParameter().size());
    }

    @Test
    public void configParserTest_parametersModelDataLoaded() {
        assertNotNull("parameter name loaded", roundAboutConfig.getParameters().getParameter().get(0).getName());
        assertNotNull("parameter value loaded", roundAboutConfig.getParameters().getParameter().get(0).getValue());
    }

    @Test
    public void configParserTest_componentLoaded() {
        assertNotNull("components loaded", roundAboutConfig.getComponents().getComponent());
        assertEquals("1 component loaded", 1, roundAboutConfig.getComponents().getComponent().size());
    }

    @Test
    public void configParserTest_parametersComponentLoaded() {
        assertNotNull("component parameters not not null", roundAboutConfig.getComponents().getComponent().get(0).getParameters());
        assertEquals("4 component parameters loaded", 4, roundAboutConfig.getComponents().getComponent().get(0).getParameters().getParameter().size());
    }

    @Test
    public void configParserTest_sourcesComponentLoaded() {
        assertNotNull("sources in component not null", roundAboutConfig.getComponents().getComponent().get(0).getSources().getSource());
        assertEquals("4 sources loaded", 4, roundAboutConfig.getComponents().getComponent().get(0).getSources().getSource().size());
    }

    @Test
    public void configParserTest_sinksComponentLoaded() {
        assertNotNull("sinks in component not null", roundAboutConfig.getComponents().getComponent().get(0).getSinks().getSink());
        assertEquals("4 sinks loaded", 4, roundAboutConfig.getComponents().getComponent().get(0).getSinks().getSink().size());
    }

    @Test
    public void configParserTest_sectionsComponentLoaded() {
        assertNotNull("sections in component not null", roundAboutConfig.getComponents().getComponent().get(0).getSections().getSection());
        assertEquals("16 sections loaded", 16, roundAboutConfig.getComponents().getComponent().get(0).getSections().getSection().size());
    }

    @Test
    public void configParserTest_sectionsTrafficLightsLoaded() {
        assertNotNull("sections in component not null", roundAboutConfig.getComponents().getComponent().get(0).getSections().getSection());
        for (Section section : roundAboutConfig.getComponents().getComponent().get(0).getSections().getSection()) {
            if (section.getId().contains("section_a14_inlet") || section.getId().contains("section_achrain_inlet")) {
                section.getTrafficLight().equals("active");
            }
        }
        assertEquals("16 sections loaded", 16, roundAboutConfig.getComponents().getComponent().get(0).getSections().getSection().size());
    }

    @Test
    public void configParserTest_connectorsComponentLoaded() {
        assertNotNull("connectors in component not null", roundAboutConfig.getComponents().getComponent().get(0).getConnectors().getConnector());
        assertEquals("12 connectors loaded", 12, roundAboutConfig.getComponents().getComponent().get(0).getConnectors().getConnector().size());
    }

    @Test
    public void configParserTest_routesComponentLoaded() {
        assertNotNull("routes in component not null", roundAboutConfig.getComponents().getComponent().get(0).getRoutes().getRoute());
        assertEquals("8 routes loaded", 8, roundAboutConfig.getComponents().getComponent().get(0).getRoutes().getRoute().size());
    }
}
