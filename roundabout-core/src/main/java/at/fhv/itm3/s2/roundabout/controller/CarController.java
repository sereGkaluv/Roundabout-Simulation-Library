package at.fhv.itm3.s2.roundabout.controller;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutCar;

import java.util.HashMap;
import java.util.Map;

public class CarController {

    private static Map<Car, ICar> carToICarMap = new HashMap<>();
    private static Map<ICar, Car> iCarToCarMap = new HashMap<>();

    public static void addCarMapping(Car car, ICar iCar) {
        carToICarMap.put(car, iCar);
        iCarToCarMap.put(iCar, car);
    }

    public static ICar getICar(Car car) {
        if (!carToICarMap.containsKey(car)) {
            throw new IllegalArgumentException("carToICarMap does not contain car");
        }

        return carToICarMap.get(car);
    }

    public static Car getCar(ICar car) {
        if (!iCarToCarMap.containsKey(car)) {
            throw new IllegalArgumentException("iCarToCarMap does not contain car");
        }

        return iCarToCarMap.get(car);
    }
}
