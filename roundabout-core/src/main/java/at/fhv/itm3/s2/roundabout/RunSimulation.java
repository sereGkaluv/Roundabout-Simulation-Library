package at.fhv.itm3.s2.roundabout;

import at.fhv.itm14.trafsim.persistence.model.ScenarioDTO;
import at.fhv.itm14.trafsim.util.ScenarioReadException;
import at.fhv.itm14.trafsim.util.XMLParser;
import at.fhv.itm3.s2.roundabout.util.ConfigParser;
import at.fhv.itm3.s2.roundabout.util.ConfigParserException;
import at.fhv.itm3.s2.roundabout.util.ILogger;
import at.fhv.itm3.s2.roundabout.util.dto.RoundAboutConfig;

public class RunSimulation implements ILogger {
    private static final String DEFAULT_ROUNDABOUT_CONFIG_FILENAME = "roundabout.xml";
    private static final String DEFAULT_TRAFSIM_CONFIG_FILENAME = "trafsim.xml";

    public static void main(String[] args){
        String roundaboutConfigFileName = getArgOrDefault(args, 0, DEFAULT_ROUNDABOUT_CONFIG_FILENAME);
        try {
            RoundAboutConfig roundAboutConfig = ConfigParser.loadConfig(roundaboutConfigFileName);
        } catch (ConfigParserException e) {
            log.error(e);
        }

        String trafsimConfigFileName = getArgOrDefault(args, 0, DEFAULT_TRAFSIM_CONFIG_FILENAME);
        ScenarioDTO trafsimScenario;
        try {
            trafsimScenario = XMLParser.loadScenario(trafsimConfigFileName);
        } catch (ScenarioReadException e) {
            log.error(e);
        }
    }

    private static String getArgOrDefault(String[] array, int index, String defaultValue){
        if(array.length>index){
            return array[index];
        }
        return defaultValue;
    }
}
