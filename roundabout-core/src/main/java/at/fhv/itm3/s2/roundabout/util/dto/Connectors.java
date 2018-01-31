package at.fhv.itm3.s2.roundabout.util.dto;

import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;

import java.util.List;

public class Connectors implements IDTO {
    private List<Connector> connectorList;

    public List<Connector> getConnector() {
        return connectorList;
    }

    public void setConnector(List<Connector> connectorList) {
        this.connectorList = connectorList;
    }
}
