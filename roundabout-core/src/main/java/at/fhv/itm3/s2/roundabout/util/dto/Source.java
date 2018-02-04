package at.fhv.itm3.s2.roundabout.util.dto;

import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;

public class Source implements IDTO {
    private String id;
    private String sectionId;
    private Double generatorExpectation;

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    @XmlAttribute
    public Double getGeneratorExpectation() {
        return generatorExpectation;
    }

    public void setGeneratorExpectation(Double generatorExpectation) {
        this.generatorExpectation = generatorExpectation;
    }
}
