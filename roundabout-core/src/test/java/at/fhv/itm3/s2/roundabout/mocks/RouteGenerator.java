package at.fhv.itm3.s2.roundabout.mocks;

import at.fhv.itm14.trafsim.model.entities.OneWayStreet;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.Sink;
import at.fhv.itm3.s2.roundabout.adapter.OneWayStreetAdapter;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
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

    private IRoute getRouteWithOneStreetSectionAndOneOneWayStreet() { return routes.get(2); }

    private IRoute getRouteWithTwoOneWayStreets() { return routes.get(3); }

    private IRoute getRouteWithTwoStreetSectionsOneCar() { return routes.get(4); }

    public IRoute getRoute(RouteType type) {
        switch (type) {
            case TWO_STREETSECTIONS:
                return getRouteWithTwoStreetSections();
            case ONE_ONEWAYSTREET_ONE_STREETSECTION:
                return getRouteWithOneWayStreetAndStreetSection();
            case ONE_STREETSECTION_ONE_ONEWAYSTREET:
                return getRouteWithOneStreetSectionAndOneOneWayStreet();
            case TWO_ONEWAYSTREETS:
                return getRouteWithTwoOneWayStreets();
            case TWO_STREETSECTIONS_ONE_CAR:
                return getRouteWithTwoStreetSectionsOneCar();
        }
        return null;
    }

    private void initializeRoutes(RoundaboutSimulationModel model) {
        // INITIALIZE ROUTE WITH TWO STREETSECTIONS
        initRouteWithTwoStreetSections(model);

        // INITIALIZE ROUTE WITH ONEWAYSTREET AND STREETSECTION
        initRouteWithOneWayStreetAndStreetSection(model);

        // INITIALIZE ROUTE WITH STREETSECTION AND ONEWAYSTREET
        initRouteWithOneStreetSectionAndOneOneWayStreet(model);

        // INITIALIZE ROUTE WITH TWO ONEWAYSTREETS
        initRouteWithTwoOneWayStreets(model);

        // init route with two streetsections and one car
        initRouteWithTwoStreetSectionsTwoCars(model);

    }

    private void initRouteWithTwoStreetSectionsTwoCars(RoundaboutSimulationModel model) {
        // initialize streets and sink
        Street street1 = new StreetSection(10.0, model, "", false);
        Street street2 = new StreetSection(10.0, model, "", false);
        Sink sink = new Sink(model, "", false);

        // initialize connectors
        Set<Street> prevStreetsForConnector1 = new HashSet<>();
        prevStreetsForConnector1.add(street1);

        Set<Street> nextStreetsForConnector1 = new HashSet<>();
        nextStreetsForConnector1.add(street2);

        StreetConnector connector1 = new StreetConnector(prevStreetsForConnector1, nextStreetsForConnector1);
        street1.setNextStreetConnector(connector1);
        street2.setPreviousStreetConnector(connector1);

        Set<Street> prevStreetsForConnector2 = new HashSet<>();
        prevStreetsForConnector2.add(street2);

        Set<Street> nextStreetsForConnector2 = new HashSet<>();
        nextStreetsForConnector2.add(sink);

        StreetConnector connector2 = new StreetConnector(prevStreetsForConnector2, nextStreetsForConnector2);
        street2.setNextStreetConnector(connector2);
        sink.setPreviousStreetConnector(connector2);

        // initialize source and route
        AbstractSource source = new RoundaboutSourceMock(model, "", false, street1, 1, this, RouteType.TWO_STREETSECTIONS_ONE_CAR);

        IRoute route = new Route();
        route.addSource(source);
        route.addSection(street1);
        route.addSection(street2);
        route.addSection(sink);

        routes.put(4, route);
    }

    private void initRouteWithTwoOneWayStreets(RoundaboutSimulationModel model) {
        // initialize streets and sink
        Sink sink = new Sink(model, "", false);
        OneWayStreet oneWayStreet2 = new OneWayStreet(model, "", false, 2, sink, 10);
        Street street2 = new OneWayStreetAdapter(oneWayStreet2, model, "", false);
        OneWayStreet oneWayStreet1 = new OneWayStreet(model, "", false, 2, street2, 10);
        Street street1 = new OneWayStreetAdapter(oneWayStreet1, model, "", false);

        // initialize connectors
        Set<Street> prevStreetsForConnector1 = new HashSet<>();
        prevStreetsForConnector1.add(street1);

        Set<Street> nextStreetsForConnector1 = new HashSet<>();
        nextStreetsForConnector1.add(street2);

        StreetConnector connector1 = new StreetConnector(prevStreetsForConnector1, nextStreetsForConnector1);
        street1.setNextStreetConnector(connector1);
        street2.setPreviousStreetConnector(connector1);

        Set<Street> prevStreetsForConnector2 = new HashSet<>();
        prevStreetsForConnector2.add(street2);

        Set<Street> nextStreetsForConnector2 = new HashSet<>();
        nextStreetsForConnector2.add(sink);

        StreetConnector connector2 = new StreetConnector(prevStreetsForConnector2, nextStreetsForConnector2);
        street2.setNextStreetConnector(connector2);
        sink.setPreviousStreetConnector(connector2);

        // initialize source and route
        AbstractSource source = new RoundaboutSourceMock(model, "", false, street1, 2, this, RouteType.TWO_STREETSECTIONS_ONE_CAR);

        IRoute route = new Route();
        route.addSource(source);
        route.addSection(street1);
        route.addSection(street2);
        route.addSection(sink);

        routes.put(3, route);
    }

    private void initRouteWithOneStreetSectionAndOneOneWayStreet(RoundaboutSimulationModel model) {
        // initialize streets and sink
        Sink sink = new Sink(model, "", false);
        Street street1 = new StreetSection(10.0, model, "", false);
        OneWayStreet oneWayStreet3_2 = new OneWayStreet(model, "", false, 2, sink, 10);
        Street street2 = new OneWayStreetAdapter(oneWayStreet3_2, model, "", false);

        // initialize connectors
        Set<Street> prevStreetsForConnector1 = new HashSet<>();
        prevStreetsForConnector1.add(street1);

        Set<Street> nextStreetsForConnector1 = new HashSet<>();
        nextStreetsForConnector1.add(street2);

        StreetConnector connector1 = new StreetConnector(prevStreetsForConnector1, nextStreetsForConnector1);
        street1.setNextStreetConnector(connector1);
        street2.setPreviousStreetConnector(connector1);

        Set<Street> prevStreetsForConnector2 = new HashSet<>();
        prevStreetsForConnector2.add(street2);

        Set<Street> nextStreetsForConnector2 = new HashSet<>();
        nextStreetsForConnector2.add(sink);

        StreetConnector connector2 = new StreetConnector(prevStreetsForConnector2, nextStreetsForConnector2);
        street2.setNextStreetConnector(connector2);
        sink.setPreviousStreetConnector(connector2);

        // initialize source and route
        AbstractSource source = new RoundaboutSourceMock(model, "", false, street1, 2, this, RouteType.ONE_STREETSECTION_ONE_ONEWAYSTREET);

        IRoute route = new Route();
        route.addSource(source);
        route.addSection(street1);
        route.addSection(street2);
        route.addSection(sink);

        routes.put(2, route);
    }

    private void initRouteWithOneWayStreetAndStreetSection(RoundaboutSimulationModel model) {
        // initialize streets and sink
        Street street2 = new StreetSection(10.0, model, "", false);
        OneWayStreet oneWayStreet2_1 = new OneWayStreet(model, "", false, 2, street2, 10);
        Street street1 = new OneWayStreetAdapter(oneWayStreet2_1, model, "", false);

        Sink sink = new Sink(model, "", false);

        // initialize connectors
        Set<Street> prevStreetsForConnector1 = new HashSet<>();
        prevStreetsForConnector1.add(street1);

        Set<Street> nextStreetsForConnector1 = new HashSet<>();
        nextStreetsForConnector1.add(street2);

        StreetConnector connector1 = new StreetConnector(prevStreetsForConnector1, nextStreetsForConnector1);
        street1.setNextStreetConnector(connector1);
        street2.setPreviousStreetConnector(connector1);

        Set<Street> prevStreetsForConnector2 = new HashSet<>();
        prevStreetsForConnector2.add(street2);

        Set<Street> nextStreetsForConnector2 = new HashSet<>();
        nextStreetsForConnector2.add(sink);

        StreetConnector connector2 = new StreetConnector(prevStreetsForConnector2, nextStreetsForConnector2);
        street2.setNextStreetConnector(connector2);
        sink.setPreviousStreetConnector(connector2);

        // initialize source and route
        AbstractSource source = new RoundaboutSourceMock(model, "", false, street1, 2, this, RouteType.ONE_ONEWAYSTREET_ONE_STREETSECTION);

        IRoute route = new Route();
        route.addSource(source);
        route.addSection(street1);
        route.addSection(street2);
        route.addSection(sink);

        routes.put(1, route);
    }

    private void initRouteWithTwoStreetSections(RoundaboutSimulationModel model) {
        // initialize streets and sink
        Street street1 = new StreetSection(10.0, model, "", false);
        Street street2 = new StreetSection(10.0, model, "", false);
        Sink sink = new Sink(model, "", false);

        // initialize connectors
        Set<Street> prevStreetsForConnector1 = new HashSet<>();
        prevStreetsForConnector1.add(street1);

        Set<Street> nextStreetsForConnector1 = new HashSet<>();
        nextStreetsForConnector1.add(street2);

        StreetConnector connector1 = new StreetConnector(prevStreetsForConnector1, nextStreetsForConnector1);
        street1.setNextStreetConnector(connector1);
        street2.setPreviousStreetConnector(connector1);

        Set<Street> prevStreetsForConnector2 = new HashSet<>();
        prevStreetsForConnector2.add(street2);

        Set<Street> nextStreetsForConnector2 = new HashSet<>();
        nextStreetsForConnector2.add(sink);

        StreetConnector connector2 = new StreetConnector(prevStreetsForConnector2, nextStreetsForConnector2);
        street2.setNextStreetConnector(connector2);
        sink.setPreviousStreetConnector(connector2);

        // initialize source and route
        AbstractSource source = new RoundaboutSourceMock(model, "", false, street1, 2, this, RouteType.TWO_STREETSECTIONS);

        IRoute route = new Route();
        route.addSource(source);
        route.addSection(street1);
        route.addSection(street2);
        route.addSection(sink);

        routes.put(0, route);
    }
}
