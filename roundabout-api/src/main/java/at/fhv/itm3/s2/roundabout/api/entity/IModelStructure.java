package at.fhv.itm3.s2.roundabout.api.entity;

import at.fhv.itm14.trafsim.model.entities.intersection.Intersection;
import desmoj.core.simulator.Model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IModelStructure {

    /**
     * Add a collection of street connectors to the structure.
     * Structure setup needs to be done before.
     *
     * @param streetConnector street connectors to be added
     */
    void addStreetConnectors(Collection<? extends IStreetConnector> streetConnector);

    /**
     * Add a collection of routes to the structure.
     * Structure setup needs to be done before.
     *
     * @param routes routes to be added
     */
    void addRoutes(Collection<? extends IRoute> routes);

    /**
     * Add a collection of intersections to the structure.
     * Structure setup needs to be done before.
     *
     * @param intersections intersections to be added
     */
    void addIntersections(Collection<? extends Intersection> intersections);

    /**
     * Add a collection of streets to the structure.
     * Structure setup needs to be done before.
     *
     * @param streets streets to be added
     */
    void addStreets(Collection<? extends Street> streets);

    /**
     * Adds a collection of sinks to the structure.
     *
     * @param sinks The sinks to be added.
     */
    void addSinks(Collection<? extends AbstractSink> sinks);

    /**
     * Adds a collection of sources to the structure.
     *
     * @param sources The source to be added.
     */
    void addSources(Collection<? extends AbstractSource> sources);

    /**
     * Add a configuration parameter to the structure.
     * Known parameters: //TODO full list of parameters
     *
     * @param key   key of the parameter
     * @param value value of the parameter
     */
    void addParameter(String key, String value);

    /**
     * Get all street connectors of the structure.
     *
     * @return street connectors of structure
     */
    Set<IStreetConnector> getStreetConnectors();

    /**
     * Get all routes of the structure.
     *
     * @return routes of structure
     */
    Map<AbstractSource, List<IRoute>> getRoutes();

    /**
     * Get all intersections of the structure.
     *
     * @return intersections of structure
     */
    Set<Intersection> getIntersections();

    /**
     * Get all streets of the structure.
     *
     * @return streets of structure
     */
    Set<Street> getStreets();

    /**
     * Gets all sinks from the structure.
     */
    Set<AbstractSink> getSinks();

    /**
     * Gets all sources from the structure.
     */
    Set<AbstractSource> getSources();

    /**
     * Gets all roundabout inlets as {@link Street}
     */
    Set<Street> getRoundaboutInlets();

    /**
     * Get all parameters of the structure.
     *
     * @return parameters of structure
     */
    Map<String, String> getParameters();

    /**
     * Returns value associated with the given key.
     *
     * @param key key of the parameter.
     * @return value of the parameter.
     */
    String getParameter(String key);

    /**
     * Get simulation model of structure.
     *
     * @return simulation model of structure
     */
    Model getModel();
}
