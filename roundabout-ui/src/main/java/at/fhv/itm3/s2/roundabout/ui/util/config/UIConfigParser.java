package at.fhv.itm3.s2.roundabout.ui.util.config;

import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;
import at.fhv.itm3.s2.roundabout.ui.util.config.dto.UIConfig;
import at.fhv.itm3.s2.roundabout.util.ConfigParserException;
import at.fhv.itm3.s2.roundabout.util.dto.ModelConfig;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAttribute;
import java.io.File;

public class UIConfigParser implements IDTO {

    public static UIConfig loadConfig(String filename)
    throws ConfigParserException {
        File configFile = new File(filename);
        if (!configFile.exists()) {
            configFile = new File(UIConfigParser.class.getResource(filename).getPath());
            if (!configFile.exists()) {
                throw new ConfigParserException("No such config file " + filename);
            }
        }
        return JAXB.unmarshal(configFile, UIConfig.class);
    }
}
