package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.*;
import desmoj.core.simulator.Model;

import java.util.*;

public class RoundaboutStructure implements IRoundaboutStructure {
    private final Model model;
    private Set<IStreetConnector> connectors;
    private Set<IRoute> routes;
    private Set<Street> streets;
    private Map<String, String> parameters;
    private Set<AbstractSource> sources;
    private Set<AbstractSink> sinks;
    private Set<Street> roundaboutInlets;

    public RoundaboutStructure(Model model) {
        this(model, new HashMap<>());
    }

    public RoundaboutStructure(Model model, Map<String, String> parameters) {
        this.model = model;
        this.connectors = new HashSet<>();
        this.routes = new HashSet<>();
        this.streets = new HashSet<>();
        this.parameters = parameters;
        this.sources = new HashSet<>();
        this.sinks = new HashSet<>();
        this.roundaboutInlets = new HashSet<>();
    }

    @Override
    public void addStreetConnector(IStreetConnector streetConnector) {
        connectors.add(streetConnector);
    }

    @Override
    public void addRoute(IRoute route) {
        routes.add(route);
    }

    @Override
    public void addParameter(String key, String value) {
        if (!key.isEmpty() && !value.isEmpty() && !parameters.containsKey(key)) {
            parameters.put(key, value);
        }
    }

    @Override
    public void addStreet(Street street) {
        streets.add(street);
        if (street.getNextStreetConnector().getTypeOfConsumer(street) == ConsumerType.ROUNDABOUT_INLET) {
            roundaboutInlets.add(street);
        }
    }

    @Override
    public void addSink(AbstractSink sink) {
        sinks.add(sink);
    }

    @Override
    public void addSource(AbstractSource source) {
        sources.add(source);
    }

    @Override
    public Set<IStreetConnector> getStreetConnectors() {
        return Collections.unmodifiableSet(connectors);
    }

    @Override
    public Set<IRoute> getRoutes() {
        return Collections.unmodifiableSet(routes);
    }

    @Override
    public Set<Street> getStreets() {
        return Collections.unmodifiableSet(streets);
    }

    @Override
    public Set<AbstractSink> getSinks() {
        return sinks;
    }

    @Override
    public Set<AbstractSource> getSources() {
        return sources;
    }

    @Override
    public Set<Street> getRoundaboutInlets() {
        return roundaboutInlets;
    }

    @Override
    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    @Override
    public String getParameter(String key) {
        return parameters.get(key);
    }

    @Override
    public Model getModel() {
        return model;
    }
}
