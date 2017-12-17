package mocks;

import at.fhv.itm14.trafsim.model.entities.OneWayStreet;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.RoundaboutSource;
import at.fhv.itm3.s2.roundabout.Sink;
import at.fhv.itm3.s2.roundabout.adapter.SourceAdapter;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.adapter.OneWayStreetAdapter;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
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

    private IRoute getRouteWithTwoStreetSections() {
        return routes.get(0);
    }

    private IRoute getRouteWithOneWayStreetAndStreetSection() {
        return routes.get(1);
    }

    public IRoute getRoute(RouteType type) {
        switch (type) {
            case TWO_STREETSECTIONS:
                return getRouteWithTwoStreetSections();
            case ONE_ONEWAYSTREET_ONE_STREETSECTION:
                return getRouteWithOneWayStreetAndStreetSection();
            case ONE_STREETSECTION_ONE_ONEWAYSTREET:
                return null;
        }
        return null;
    }

    private void initializeRoutes(RoundaboutSimulationModel model) {

        // INITIALIZE ROUTE WITH TWO STREETSECTIONS
        // initialize streets and sink
        Street street1_1 = new StreetSection(10.0, model, "", false);
        Street street1_2 = new StreetSection(10.0, model, "", false);
        Sink sink1 = new Sink(model, "", false);

        // initialize connectors
        Set<Street> prevStreetsForConnector1_1 = new HashSet<>();
        prevStreetsForConnector1_1.add(street1_1);

        Set<Street> nextStreetsForConnector1_1 = new HashSet<>();
        nextStreetsForConnector1_1.add(street1_2);

        StreetConnector connector1_1 = new StreetConnector(prevStreetsForConnector1_1, nextStreetsForConnector1_1);
        street1_1.setNextStreetConnector(connector1_1);
        street1_2.setPreviousStreetConnector(connector1_1);

        Set<Street> prevStreetsForConnector1_2 = new HashSet<>();
        prevStreetsForConnector1_2.add(street1_2);

        Set<Street> nextStreetsForConnector1_2 = new HashSet<>();
        nextStreetsForConnector1_2.add(sink1);

        StreetConnector connector1_2 = new StreetConnector(prevStreetsForConnector1_2, nextStreetsForConnector1_2);
        street1_2.setNextStreetConnector(connector1_2);
        sink1.setPreviousStreetConnector(connector1_2);

        // initialize source and route
        AbstractSource source1 = new RoundaboutSourceMock(model, "", false, street1_1, 2, this, RouteType.TWO_STREETSECTIONS);

        IRoute route1 = new Route();
        route1.addSource(source1);
        route1.addSection(street1_1);
        route1.addSection(street1_2);
        route1.addSection(sink1);

        routes.put(0, route1);


        // INITIALIZE ROUTE WITH ONEWAYSTREET AND STREETSECTION
        // initialize streets and sink
        Street street2_2 = new StreetSection(10.0, model, "", false);
        OneWayStreet oneWayStreet2_1 = new OneWayStreet(model, "", false, 2, street2_2, 10);
        Street street2_1 = new OneWayStreetAdapter(oneWayStreet2_1, model, "", false);

        Sink sink2 = new Sink(model, "", false);

        // initialize connectors
        Set<Street> prevStreetsForConnector2_1 = new HashSet<>();
        prevStreetsForConnector2_1.add(street2_1);

        Set<Street> nextStreetsForConnector2_1 = new HashSet<>();
        nextStreetsForConnector2_1.add(street2_2);

        StreetConnector connector2_1 = new StreetConnector(prevStreetsForConnector2_1, nextStreetsForConnector2_1);
        street2_1.setNextStreetConnector(connector2_1);
        street2_2.setPreviousStreetConnector(connector2_1);

        Set<Street> prevStreetsForConnector2_2 = new HashSet<>();
        prevStreetsForConnector2_2.add(street2_2);

        Set<Street> nextStreetsForConnector2_2 = new HashSet<>();
        nextStreetsForConnector2_2.add(sink2);

        StreetConnector connector2_2 = new StreetConnector(prevStreetsForConnector2_2, nextStreetsForConnector2_2);
        street2_2.setNextStreetConnector(connector2_2);
        sink2.setPreviousStreetConnector(connector2_2);

        // initialize source and route
        AbstractSource source2 = new RoundaboutSourceMock(model, "", false, street2_1, 2, this, RouteType.ONE_ONEWAYSTREET_ONE_STREETSECTION);

        IRoute route2 = new Route();
        route2.addSource(source2);
        route2.addSection(street2_1);
        route2.addSection(street2_2);
        route2.addSection(sink2);

        routes.put(1, route2);
    }
}
