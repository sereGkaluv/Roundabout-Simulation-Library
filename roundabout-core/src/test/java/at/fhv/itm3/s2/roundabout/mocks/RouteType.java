package at.fhv.itm3.s2.roundabout.mocks;

public enum RouteType {
    TWO_STREETSECTIONS_ONE_CAR(1),
    TWO_STREETSECTIONS_TWO_CARS(2),
    STREETSECTION_INTERSECTION_STREETSECTION_ONE_CAR(1),
    STREETSECTION_INTERSECTION_STREETSECTION_TWO_CARS(2);

    private final int carsToGenerate;

    RouteType(int carsToGenerate) {
        this.carsToGenerate = carsToGenerate;
    }

    public int getCarsToGenerate() {
        return carsToGenerate;
    }
}
