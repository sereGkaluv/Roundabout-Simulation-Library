package at.fhv.itm3.s2.roundabout.util.dto;

import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;

import java.util.List;

public class Components implements IDTO {
    private List<Component> componentList;
    private Connectors connectors;
    private Routes routes;

    public List<Component> getComponent() {
        return componentList;
    }

    public void setComponent(List<Component> componentList) {
        this.componentList = componentList;
    }

    public Connectors getConnectors() {
        return connectors;
    }

    public void setConnectors(Connectors connectors) {
        this.connectors = connectors;
    }

    public Routes getRoutes() {
        return routes;
    }

    public void setRoutes(Routes routes) {
        this.routes = routes;
    }
}
