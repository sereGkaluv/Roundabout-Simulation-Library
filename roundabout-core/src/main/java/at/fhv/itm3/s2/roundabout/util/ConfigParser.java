package at.fhv.itm3.s2.roundabout.util;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.util.dto.RoundAboutConfig;

import javax.xml.bind.JAXB;
import java.io.File;

public class ConfigParser {
    private String filename;

    public ConfigParser(String filename) {
        this.filename = filename;
    }

    public static RoundAboutConfig loadConfig(String filename) throws ConfigParserException {
        File configFile = new File(filename);
        if(!configFile.exists()){
            throw new ConfigParserException("no such config file " + filename);
        }
        return JAXB.unmarshal(configFile, RoundAboutConfig.class);
    }

    public RoundaboutSimulationModel generateModel() throws ConfigParserException {
        RoundAboutConfig roundAboutConfig = loadConfig(filename);

        RoundaboutSimulationModel model = new RoundaboutSimulationModel();

        return model;
    }
}
