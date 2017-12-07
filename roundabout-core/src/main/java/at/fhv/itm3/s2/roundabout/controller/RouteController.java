package at.fhv.itm3.s2.roundabout.controller;

import at.fhv.itm3.s2.roundabout.RoundaboutModel;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.entity.Route;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import desmoj.core.simulator.Model;

public class RouteController {

    private final RoundaboutModel model;
    private static RouteController instance;

    /**
     * Returns a singleton of RouteController
     *
     * @param model     the model the RouteController and its IStreetSections are part of
     * @return          the singleton RouteController object
     */
    public static RouteController getInstance(RoundaboutModel model) {
        if (instance == null) {
            instance = new RouteController(model);
        }
        return instance;
    }

    /**
     * Private constructor for RouteController. Use getInstance(...) instead
     *
     * @param model                         the model the RouteController and its IStreetSections are part of
     * @throws IllegalArgumentException     when the given model is not of type RoundaboutModel
     */
    private RouteController(Model model)
    throws IllegalArgumentException {
        if (model != null && model instanceof RoundaboutModel) {
            this.model = (RoundaboutModel) model;
        } else {
            throw new IllegalArgumentException("No suitable model given over.");
        }
    }

    /**
     * Generates a new route a car could take
     *
     * @return  a generated route as IRoute object
     */
    public IRoute generateNewRoute() {
        // TODO: implement
        Route route = new Route();
        route.addSection(new StreetSection(1, null, null, model, null, false));
        return route;
    }
}
