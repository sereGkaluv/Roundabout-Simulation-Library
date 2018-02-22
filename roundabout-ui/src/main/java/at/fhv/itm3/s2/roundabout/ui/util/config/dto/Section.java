package at.fhv.itm3.s2.roundabout.ui.util.config.dto;

import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;

public class Section implements IDTO {
    private String componentId;
    private String id;
    private List<Checkpoint> checkpointList;

    @XmlAttribute
    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Checkpoint> getCheckpoint() {
        return checkpointList;
    }

    public void setCheckpoint(List<Checkpoint> checkpointList) {
        this.checkpointList = checkpointList;
    }
}
