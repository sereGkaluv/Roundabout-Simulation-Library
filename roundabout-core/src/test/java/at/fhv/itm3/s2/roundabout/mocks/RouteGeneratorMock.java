package at.fhv.itm3.s2.roundabout.mocks;

import at.fhv.itm14.trafsim.model.ModelFactory;
import at.fhv.itm14.trafsim.model.entities.AbstractConsumer;
import at.fhv.itm14.trafsim.model.entities.AbstractProSumer;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.IProducer;
import at.fhv.itm14.trafsim.model.entities.intersection.FixedCirculationController;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.api.entity.AbstractSource;
import at.fhv.itm3.s2.roundabout.api.entity.IRoute;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.controller.IntersectionController;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutIntersection;
import at.fhv.itm3.s2.roundabout.entity.Route;
import at.fhv.itm3.s2.roundabout.entity.StreetConnector;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static at.fhv.itm3.s2.roundabout.mocks.RouteType.*;

public class RouteGeneratorMock {

    private Map<RouteType, IRoute> routes;
    private RoundaboutSimulationModel model;

    public RouteGeneratorMock(RoundaboutSimulationModel model) {
        routes = new HashMap<>();
        this.model = model;

        initializeRouteWithTwoStreetSectionsOneCar();
        initializeRouteWithTwoStreetSectionsTwoCars();
        initializeRouteWithIntersectionOneCar();
        initializeRouteWithIntersectionTwoCars();
    }

    public IRoute getRoute(RouteType type) {
        switch (type) {
            case TWO_STREETSECTIONS_ONE_CAR:
                return routes.get(TWO_STREETSECTIONS_ONE_CAR);
            case TWO_STREETSECTIONS_TWO_CARS:
                return routes.get(TWO_STREETSECTIONS_TWO_CARS);
            case STREETSECTION_INTERSECTION_STREETSECTION_ONE_CAR:
                return routes.get(STREETSECTION_INTERSECTION_STREETSECTION_ONE_CAR);
            case STREETSECTION_INTERSECTION_STREETSECTION_TWO_CARS:
                return routes.get(STREETSECTION_INTERSECTION_STREETSECTION_TWO_CARS);
        }
        return null;
    }

    private void initializeRouteWithTwoStreetSectionsOneCar() {
        routes.put(
                TWO_STREETSECTIONS_ONE_CAR,
                initializeRouteWithTwoStreetSections(1)
        );
    }

    private void initializeRouteWithTwoStreetSectionsTwoCars() {
        routes.put(
                TWO_STREETSECTIONS_TWO_CARS,
                initializeRouteWithTwoStreetSections(2)
        );
    }

    private IRoute initializeRouteWithTwoStreetSections(int remainingCarsToGenerate) {

        // INITIALIZE ROUTE WITH TWO STREETSECTIONS
        // initialize streets and sink
        Street street1_1 = new StreetSection(10.0, model, "", false);
        Street street1_2 = new StreetSection(10.0, model, "", false);
        RoundaboutSink roundaboutSink1 = new RoundaboutSink(model, "", false);

        // initialize connectors
        Set<IProducer> prevStreetsForConnector1_1 = new HashSet<>();
        prevStreetsForConnector1_1.add(street1_1);

        Set<IConsumer> nextStreetsForConnector1_1 = new HashSet<>();
        nextStreetsForConnector1_1.add(street1_2);

        StreetConnector connector1_1 = new StreetConnector(prevStreetsForConnector1_1, nextStreetsForConnector1_1);
        street1_1.setNextStreetConnector(connector1_1);
        street1_2.setPreviousStreetConnector(connector1_1);

        Set<IProducer> prevStreetsForConnector1_2 = new HashSet<>();
        prevStreetsForConnector1_2.add(street1_2);

        Set<IConsumer> nextStreetsForConnector1_2 = new HashSet<>();
        nextStreetsForConnector1_2.add(roundaboutSink1);

        StreetConnector connector1_2 = new StreetConnector(prevStreetsForConnector1_2, nextStreetsForConnector1_2);
        street1_2.setNextStreetConnector(connector1_2);
        roundaboutSink1.setPreviousStreetConnector(connector1_2);

        // initialize source and route
        AbstractSource source1 = new RoundaboutSourceMock(
                model,
                "",
                false,
                street1_1,
                remainingCarsToGenerate,
                this,
                RouteType.TWO_STREETSECTIONS_TWO_CARS
        );

        IRoute route1 = new Route();
        route1.addSource(source1);
        route1.addSection(street1_1);
        route1.addSection(street1_2);
        route1.addSection(roundaboutSink1);

        return route1;
    }

    private void initializeRouteWithIntersectionOneCar() {
        routes.put(
                STREETSECTION_INTERSECTION_STREETSECTION_ONE_CAR,
                initializeRouteWithIntersection(1)
        );
    }

    private void initializeRouteWithIntersectionTwoCars() {
        routes.put(
                STREETSECTION_INTERSECTION_STREETSECTION_TWO_CARS,
                initializeRouteWithIntersection(2)
        );
    }

    private IRoute initializeRouteWithIntersection(int remainingCarsToGenerate) {
        float turnaroundTime = 60;
        float[] phaseShiftTimes = new float[]{0.0F, 10.0F, 20.0F};
        double intersectionTraverseTime = 5.0;
        double accelerationTime = 2.0;
        double yellowDuration = turnaroundTime / 8;
        double greenDuration = turnaroundTime / 2 - yellowDuration;
        int outDirection = 1;
        int inDirection = 0;

        // initialize intersection
        RoundaboutIntersection intersection = new RoundaboutIntersection(model, "", false, 2);
        intersection.setServiceDelay(accelerationTime);
        FixedCirculationController ic = ModelFactory.getInstance(model).createOneWayController(
                intersection,
                greenDuration,
                yellowDuration,
                phaseShiftTimes[0]
        );
        intersection.attachController(ic);

        // initialize streets
        AbstractProSumer street1 = new StreetSection(10.0, model, "", false);
        AbstractProSumer street2 = new StreetSection(10.0, model, "", false);

        // initialize roundaboutSink
        RoundaboutSink roundaboutSink = new RoundaboutSink(model, "", false);

        // initialize source
        AbstractSource source = new RoundaboutSourceMock(
                model,
                "",
                false,
                (StreetSection) street1,
                remainingCarsToGenerate,
                this,
                STREETSECTION_INTERSECTION_STREETSECTION_TWO_CARS
        );

        // connect streets with intersection
        IntersectionController.getInstance().setIntersectionInDirectionMapping(intersection, street1, inDirection);
        IntersectionController.getInstance().setIntersectionOutDirectionMapping(intersection, street2, outDirection);
        intersection.attachProducer(inDirection, street1.toProducer());
        intersection.attachConsumer(outDirection, street2.toConsumer());
        intersection.createConnectionQueue(
                street1.toProducer(),
                new AbstractConsumer[]{street2.toConsumer()},
                new double[]{intersectionTraverseTime},
                new double[]{1.0}
        );

        // initialize connectors
        Set<IProducer> prevStreetsForConnector1 = new HashSet<>();
        prevStreetsForConnector1.add(street2);

        Set<IConsumer> nextStreetsForConnector1 = new HashSet<>();
        nextStreetsForConnector1.add(roundaboutSink);

        StreetConnector connector1 = new StreetConnector(prevStreetsForConnector1, nextStreetsForConnector1);
        ((StreetSection)street2).setNextStreetConnector(connector1);
        roundaboutSink.setPreviousStreetConnector(connector1);

        // initialize route
        IRoute route = new Route();
        route.addSource(source);
        route.addSection(street1);
        route.addSection(intersection);
        route.addSection(street2);
        route.addSection(roundaboutSink);

        return route;
    }
}
