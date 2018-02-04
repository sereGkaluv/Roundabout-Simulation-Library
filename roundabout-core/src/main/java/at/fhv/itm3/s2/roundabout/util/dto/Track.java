package at.fhv.itm3.s2.roundabout.util.dto;

import at.fhv.itm3.s2.roundabout.api.entity.ConsumerType;
import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;

import javax.xml.bind.annotation.XmlAttribute;

public class Track implements IDTO {

    private Long order;

    private String fromComponentId;
    private String fromSectionId;
    private ConsumerType fromSectionType;

    private String toComponentId;
    private String toSectionId;
    private ConsumerType toSectionType;

    @XmlAttribute
    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    @XmlAttribute
    public String getFromComponentId() {
        return fromComponentId;
    }

    public void setFromComponentId(String fromComponentId) {
        this.fromComponentId = fromComponentId;
    }

    @XmlAttribute
    public String getFromSectionId() {
        return fromSectionId;
    }

    public void setFromSectionId(String fromSectionId) {
        this.fromSectionId = fromSectionId;
    }

    @XmlAttribute
    public ConsumerType getFromSectionType() {
        return fromSectionType;
    }

    public void setFromSectionType(ConsumerType fromSectionType) {
        this.fromSectionType = fromSectionType;
    }

    @XmlAttribute
    public String getToComponentId() {
        return toComponentId;
    }

    public void setToComponentId(String toComponentId) {
        this.toComponentId = toComponentId;
    }

    @XmlAttribute
    public String getToSectionId() {
        return toSectionId;
    }

    public void setToSectionId(String toSectionId) {
        this.toSectionId = toSectionId;
    }

    @XmlAttribute
    public ConsumerType getToSectionType() {
        return toSectionType;
    }

    public void setToSectionType(ConsumerType toSectionType) {
        this.toSectionType = toSectionType;
    }
}
