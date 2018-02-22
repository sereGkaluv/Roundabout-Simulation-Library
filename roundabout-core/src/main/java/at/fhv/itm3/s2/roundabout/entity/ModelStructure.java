package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.intersection.Intersection;
import at.fhv.itm3.s2.roundabout.api.entity.*;
import desmoj.core.simulator.Model;

import java.util.*;

public class ModelStructure implements IModelStructure {
    private final Model model;
    private Set<IStreetConnector> connectors;
    private Map<AbstractSource, List<IRoute>> routes;
    private Set<Intersection> intersections;
    private Set<Street> streets;
    private Map<String, String> parameters;
    private Set<AbstractSource> sources;
    private Set<AbstractSink> sinks;
    private Set<Street> roundaboutInlets;

    public ModelStructure(Model model) {
        this(model, new HashMap<>());
    }

    public ModelStructure(Model model, Map<String, String> parameters) {
        this.model = model;
        this.connectors = new HashSet<>();
        this.routes = new HashMap<>();
        this.intersections = new HashSet<>();
        this.streets = new HashSet<>();
        this.parameters = parameters;
        this.sources = new HashSet<>();
        this.sinks = new HashSet<>();
        this.roundaboutInlets = new HashSet<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addStreetConnectors(Collection<? extends IStreetConnector> streetConnectors) {
        this.connectors.addAll(streetConnectors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRoutes(Collection<? extends IRoute> routes) {
        routes.forEach(route -> {
            List<IRoute> routeList = this.routes.get(route.getSource());
            if (routeList == null) {
                routeList = new ArrayList<>();
            }

            routeList.add(route);

            this.routes.put(route.getSource(), routeList);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addIntersections(Collection<? extends Intersection> intersections) {
        this.intersections.addAll(intersections);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addParameter(String key, String value) {
        if (!key.isEmpty() && !value.isEmpty() && !parameters.containsKey(key)) {
            parameters.put(key, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addStreets(Collection<? extends Street> streets) {
        this.streets.addAll(streets);

        for (Street street : streets) {
            if (street.getNextStreetConnector() != null && street.getNextStreetConnector().getTypeOfConsumer(street) == ConsumerType.ROUNDABOUT_INLET) {
                roundaboutInlets.add(street);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSinks(Collection<? extends AbstractSink> sinks) {
        this.sinks.addAll(sinks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSources(Collection<? extends AbstractSource> sources) {
        this.sources.addAll(sources);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<IStreetConnector> getStreetConnectors() {
        return Collections.unmodifiableSet(connectors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<AbstractSource, List<IRoute>> getRoutes() {
        return Collections.unmodifiableMap(routes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Intersection> getIntersections() {
        return Collections.unmodifiableSet(intersections);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Street> getStreets() {
        return Collections.unmodifiableSet(streets);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<AbstractSink> getSinks() {
        return sinks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<AbstractSource> getSources() {
        return sources;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Street> getRoundaboutInlets() {
        return roundaboutInlets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParameter(String key) {
        return parameters.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Model getModel() {
        return model;
    }
}
