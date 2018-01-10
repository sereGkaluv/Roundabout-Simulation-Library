package at.fhv.itm3.s2.roundabout.util.dto;

import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;

public class Connector implements IDTO {
    private Double traverseTime;
    private Float probability;
    private FromConnector fromConnector;
    private ToConnector toConnector;

    @XmlAttribute
    public Double getTraverseTime() {
        return traverseTime;
    }

    public void setTraverseTime(Double traverseTime) {
        this.traverseTime = traverseTime;
    }

    @XmlAttribute
    public Float getProbability() {
        return probability;
    }

    public void setProbability(Float probability) {
        this.probability = probability;
    }

    public FromConnector getFrom() {
        return fromConnector;
    }

    public void setFrom(FromConnector fromConnector) {
        this.fromConnector = fromConnector;
    }

    public ToConnector getTo() {
        return toConnector;
    }

    public void setTo(ToConnector toConnector) {
        this.toConnector = toConnector;
    }
}
