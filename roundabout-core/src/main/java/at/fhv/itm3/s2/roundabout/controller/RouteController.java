package at.fhv.itm3.s2.roundabout.controller;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.ISource;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.entity.Route;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import desmoj.core.simulator.Model;

import java.util.*;

public class RouteController {

    private final RoundaboutSimulationModel model;
    private static RouteController instance;

    private Map<ISource, List<IRoute>> routes;
    private List<ISource> sources;

    /**
     * Returns a singleton of {@link RouteController}.
     *
     * @param model the model the RouteController and its {@link Street}s are part of.
     * @return the singleton RouteController object.
     */
    public static RouteController getInstance(RoundaboutSimulationModel model) {
        if (instance == null) {
            instance = new RouteController(model);
        }
        return instance;
    }

    /**
     * Private constructor for {@link RouteController}. Use getInstance(...) instead.
     *
     * @param model the model the {@link RouteController} and its {@link Street}s are part of.
     * @throws IllegalArgumentException when the given model is not of type {@link RoundaboutSimulationModel}.
     */
    private RouteController(Model model)
    throws IllegalArgumentException {
        if (model != null && model instanceof RoundaboutSimulationModel) {
            this.model = (RoundaboutSimulationModel) model;
        } else {
            throw new IllegalArgumentException("No suitable model given over.");
        }

        this.routes = new HashMap<>();
        this.sources = new LinkedList<>();

        initializeRoutes();
    }

    /**
     * Generates a new route a car could take.
     *
     * @return a generated route as {@link IRoute} object.
     */
//    public IRoute generateNewRoute() {
//        // TODO: implement
//        Route route = new Route();
//        route.addSection(new StreetSection(10.0, model, "", false));
//        return route;
//    }

    public IRoute getRandomRoute() {
        int randNr = new Random().nextInt(this.routes.size());
        ISource source =  this.sources.get(randNr);

        List<IRoute> routes = this.routes.get(source);

        randNr = new Random().nextInt(routes.size());
        return routes.get(randNr);
    }

    private void initializeRoutes() {

    }
}
