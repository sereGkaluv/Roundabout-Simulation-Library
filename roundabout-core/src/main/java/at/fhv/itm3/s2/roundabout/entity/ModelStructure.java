package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.*;
import desmoj.core.simulator.Model;

import java.util.*;

public class ModelStructure implements IModelStructure {
    private final Model model;
    private Set<IStreetConnector> connectors;
    private Map<Street, Map<Street, IRoute>> routes; // Source, Sink and Route
    private Set<Street> streets;
    private Map<String, String> parameters;
    private Set<AbstractSource> sources;
    private Set<AbstractSink> sinks;

    public ModelStructure(Model model) {
        this(model, new HashMap<>());
    }

    public ModelStructure(Model model, Map<String, String> parameters) {
        this.model = model;
        this.connectors = new HashSet<>();
        this.routes = new HashMap<>();
        this.streets = new HashSet<>();
        this.parameters = parameters;
        this.sources = new HashSet<>();
        this.sinks = new HashSet<>();
    }

    @Override
    public void addStreetConnector(IStreetConnector streetConnector) {
        connectors.add(streetConnector);
    }

    @Override
    public void addRoute(Street source , Street sink, IRoute route) {
        Map<Street, IRoute> tmpStartEndStreet = new HashMap<>();
        tmpStartEndStreet.put(sink, route);
        routes.put(source, tmpStartEndStreet);
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
    public Map<Street, Map<Street, IRoute>> getRoutes() {
        return routes;
    }

    @Override
    public IRoute getRoute(Street start, Street destination){
        return routes.get(start).get(destination);
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

    public Street getStreetFromID (String ID){
        for(Street streetIt : streets){
            if(streetIt.getId().equals(ID)) return streetIt;
        }
        throw new IllegalArgumentException( ID + " is not a legit Streed ID Name.");
    }
}
