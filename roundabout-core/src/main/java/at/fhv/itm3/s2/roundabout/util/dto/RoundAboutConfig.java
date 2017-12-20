package at.fhv.itm3.s2.roundabout.util.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "model")
public class RoundAboutConfig {
    private Parameters parameters;
    private Roundabout roundabout;

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public Roundabout getRoundabout() {
        return roundabout;
    }

    public void setRoundabout(Roundabout roundabout) {
        this.roundabout = roundabout;
    }

}
