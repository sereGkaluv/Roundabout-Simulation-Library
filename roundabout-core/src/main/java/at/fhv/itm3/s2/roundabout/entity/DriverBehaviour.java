package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;

public class DriverBehaviour implements IDriverBehaviour {

    private double speed;
    private double minDistanceToNextCar;
    private double maxDistanceToNextCar;
    private double behaviourFactor;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getMinDistanceToNextCar() {
        return minDistanceToNextCar;
    }

    public void setMinDistanceToNextCar(double minDistanceToNextCar) {
        this.minDistanceToNextCar = minDistanceToNextCar;
    }

    public double getMaxDistanceToNextCar() {
        return maxDistanceToNextCar;
    }

    public void setMaxDistanceToNextCar(double maxDistanceToNextCar) {
        this.maxDistanceToNextCar = maxDistanceToNextCar;
    }

    public double getBehaviourFactor() {
        return behaviourFactor;
    }

    public void setBehaviourFactor(double behaviourFactor) {
        this.behaviourFactor = behaviourFactor;
    }
}
