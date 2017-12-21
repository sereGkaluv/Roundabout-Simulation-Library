package at.fhv.itm3.s2.roundabout.api.entity;

import desmoj.core.simulator.Model;

import java.util.Map;
import java.util.Set;

public interface IRoundaboutStructure {

    void addStreetConnector(IStreetConnector streetConnector);

    void addRoute(IRoute route);

    void addStreet(Street street);

    void addParameter(String key, String value);

    Set<IStreetConnector> getStreetConnectors();

    Set<IRoute> getRoutes();

    Set<Street> getStreets();

    Map<String, String> getParameters();

    Model getModel();
}
