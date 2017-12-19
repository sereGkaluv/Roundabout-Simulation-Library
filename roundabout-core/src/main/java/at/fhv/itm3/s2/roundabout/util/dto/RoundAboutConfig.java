package at.fhv.itm3.s2.roundabout.util.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(
        name = "roundabout"
)
public class RoundAboutConfig {
    private List<Parameter> parameters;
    private List<Sections> structure;

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<Sections> getStructure() {
        return structure;
    }

    public void setStructure(List<Sections> structure) {
        this.structure = structure;
    }
}
