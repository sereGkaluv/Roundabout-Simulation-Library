package at.fhv.itm3.s2.roundabout.util.dto;

import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;

import java.util.List;

public class Components implements IDTO {
    private List<Component> componentList;

    public List<Component> getComponent() {
        return componentList;
    }

    public void setComponent(List<Component> componentList) {
        this.componentList = componentList;
    }
}
