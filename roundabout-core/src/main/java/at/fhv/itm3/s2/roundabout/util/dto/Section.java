package at.fhv.itm3.s2.roundabout.util.dto;

import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;

import javax.xml.bind.annotation.XmlAttribute;

public class Section implements IDTO {
    private String id;
    private Double length;
    private Integer order;
    private Boolean isTrafficLightActive;

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    @XmlAttribute
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @XmlAttribute
    public Boolean getTrafficLightActive() {
        return isTrafficLightActive;
    }

    public void setTrafficLightActive(Boolean trafficLightActive) {
        isTrafficLightActive = trafficLightActive;
    }
}
