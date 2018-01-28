package at.fhv.itm3.s2.roundabout.mocks;

public enum RouteType {
    TWO_STREETSECTIONS_ONE_CAR(1),
    TWO_STREETSECTIONS_TWO_CARS(2),
    STREETSECTION_INTERSECTION_STREETSECTION_ONE_CAR(1),
    STREETSECTION_INTERSECTION_STREETSECTION_TWO_CARS(2),
    ONE_CAR_STAYS_ON_TRACK(1),
    ONE_CAR_CHANGES_TRACK(1),
    TWO_STREETSECTIONS_ONE_STREETSECTIONMOCK_TWO_CARS(2),
    STREETSECTION_INTERSECTION_STREETSECTIONMOCK_TEN_CARS(10),
    STOPS_FOUR_CARS_CHANGE_TRACK(4),
    STOPS_FOUR_CARS_STAY_ON_TRACK(4);

    private final int carsToGenerate;

    RouteType(int carsToGenerate) {
        this.carsToGenerate = carsToGenerate;
    }

    public int getCarsToGenerate() {
        return carsToGenerate;
    }
}
