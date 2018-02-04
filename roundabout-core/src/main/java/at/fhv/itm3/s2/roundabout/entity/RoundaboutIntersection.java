package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.intersection.Intersection;
import desmoj.core.simulator.Model;

public class RoundaboutIntersection extends Intersection implements IConsumer {

    public RoundaboutIntersection(Model owner, String name, boolean showInTrace, int size) {
        super(owner, name, showInTrace, size);
    }

    @Override
    public void carEnter(Car car, int inDirection) {

        try {
            // notify last section that car was successfully delivered
            // CarDeliveredEvent is never used, so it can be null
            inList[inDirection].carDelivered(null, car, true);

            // Get outgoing direction
            int outIndex = car.getNextDirection();

            // Statistics
            car.enterIntersection();

            if (queues[inDirection][outIndex].isEmpty() && controller.canDrive(inDirection, outIndex)) {
                // If queue is empty and green for this direction: Drive through --> schedule CarDeparture-Event
                drive(car, inDirection);
            } else {
                // Otherwise queue car
                queues[inDirection][outIndex].insert(car);
                if (!car.isWaitingInIntersection()) {
                    car.startWaiting();
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Given inDirection is not available on this intersection");
        }

    }

    @Override
    public void carEnter(Car car) {
        // should never be used
        throw new IllegalStateException("RoundaboutIntersection.carEnter(Car car) should never be used");
    }

    public double getTimeToTraverseIntersection(int inDirection, int outDirection) {
        try {
            return traverseTimes[inDirection][outDirection];
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Given inDirection and/or outDirection is not available on this intersection");
        }

    }
}
