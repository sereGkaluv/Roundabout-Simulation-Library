package at.fhv.itm3.s2.roundabout.util.dto;

import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;

import java.util.List;

public class Routes implements IDTO {
    private List<Route> routeList;

    public List<Route> getRoute() {
        return routeList;
    }

    public void setRoute(List<Route> routeList) {
        this.routeList = routeList;
    }
}
