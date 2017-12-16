package mocks;

import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.Sink;
import at.fhv.itm3.s2.roundabout.Source;
import at.fhv.itm3.s2.roundabout.adapter.Street;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.IStreet;
import at.fhv.itm3.s2.roundabout.entity.Route;
import at.fhv.itm3.s2.roundabout.entity.StreetConnector;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RouteGenerator {

    private Map<Integer, IRoute> routes;

    public RouteGenerator(RoundaboutSimulationModel model) {
        routes = new HashMap<>();

        initializeRoutes(model);
    }

    public IRoute getRouteByNumberOfStreets(int nrOfStreetsInRoute) {
        return routes.get(nrOfStreetsInRoute);
    }

    private void initializeRoutes(RoundaboutSimulationModel model) {
        // initialize streets and sink
        Street street1 = new StreetSection(10.0, model, "", false);
        Street street2 = new StreetSection(10.0, model, "", false);
        Sink sink = new Sink(model, "", false);

        // initialize connectors
        Set<IStreet> prevStreetsForConnector1 = new HashSet<>();
        prevStreetsForConnector1.add(street1);

        Set<IStreet> nextStreetsForConnector1 = new HashSet<>();
        nextStreetsForConnector1.add(street2);

        StreetConnector connector1 = new StreetConnector(prevStreetsForConnector1, nextStreetsForConnector1);
        street1.setNextStreetConnector(connector1);
        street2.setPreviousStreetConnector(connector1);

        Set<IStreet> prevStreetsForConnector2 = new HashSet<>();
        prevStreetsForConnector2.add(street2);

        Set<IStreet> nextStreetsForConnector2 = new HashSet<>();
        nextStreetsForConnector2.add(sink);

        StreetConnector connector2 = new StreetConnector(prevStreetsForConnector2, nextStreetsForConnector2);
        street2.setNextStreetConnector(connector2);
        sink.setPreviousStreetConnector(connector2);

        // initialize sink and source
        Source source = new Source(street1, model);

        IRoute route = new Route();
        route.addSource(source);
        route.addSection(street1);
        route.addSection(street2);
        route.addSection(sink);

        routes.put(2, route);
    }
}
