package at.fhv.itm3.s2.roundabout.util.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "roundabout")
public class RoundAboutConfig {
    private Parameters parameters;
    private Structure structure;

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }
}
