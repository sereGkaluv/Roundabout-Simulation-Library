package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.intersection.Intersection;
import desmoj.core.simulator.Model;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;

public class RoundaboutIntersection extends Intersection implements IConsumer {

    public RoundaboutIntersection(Model owner, String name, boolean showInTrace, int size) {
        super(owner, name, showInTrace, size);
    }

    @Override
    public void carEnter(Car car, int inDirection) {
        // Get outgoing direction
        int outIndex = car.getNextDirection();

        // Statisitics
        car.enterIntersection();

        if (queues[inDirection][outIndex].isEmpty() && controller.canDrive(inDirection, outIndex)) {
            // If queue is empty and green for this direction: Drive through --> schedule CarDeparture-Event
            drive(car, inDirection);
        } else {
            // Otherwise queue car
            queues[inDirection][outIndex].insert(car);
            car.startWaiting();
        }
    }

    @Override
    public void carEnter(Car car) {
        // should never be used
        throw new NotImplementedException();
    }

    public double getTimeToTraverseIntersection(int inDirection, int outDirection) {
        return traverseTimes[inDirection][outDirection];
    }
}
