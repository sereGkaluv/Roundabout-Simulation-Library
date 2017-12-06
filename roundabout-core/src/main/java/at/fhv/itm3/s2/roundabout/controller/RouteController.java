package at.fhv.itm3.s2.roundabout.controller;

import at.fhv.itm3.s2.roundabout.RoundaboutModel;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.entity.Route;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import desmoj.core.simulator.Model;

public class RouteController {

    private final RoundaboutModel model;
    private static RouteController instance;

    public static RouteController getInstance(RoundaboutModel model) {
        if (instance == null) {
            instance = new RouteController(model);
        }
        return instance;
    }

    public RouteController(Model model)
    throws IllegalArgumentException {
        if (model != null && model instanceof RoundaboutModel) {
            this.model = (RoundaboutModel) model;
        } else {
            throw new IllegalArgumentException("Not suitable model.");
        }
    }

    public IRoute generateNewRoute() {
        // TODO: implement
        Route route = new Route();
        route.addSection(new StreetSection(1, null, null, model, null, false));
        return route;
    }
}
