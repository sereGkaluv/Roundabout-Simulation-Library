package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;

public class DriverBehaviour implements IDriverBehaviour {

    private double speed;
    private double minDistanceToNextCar;
    private double maxDistanceToNextCar;
    private double mergeFactor;

    public DriverBehaviour(){}

    public DriverBehaviour(double speed, double minDistanceToNextCar, double maxDistanceToNextCar, double mergeFactor){
        this.speed = speed;
        this.minDistanceToNextCar = minDistanceToNextCar;
        this.maxDistanceToNextCar = maxDistanceToNextCar;
        this.mergeFactor = mergeFactor;
    }


    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        if (speed >= 0) {
            this.speed = speed;
        } else {
            throw new IllegalArgumentException("speed should be greater or equal than 0");
        }
    }

    public double getMinDistanceToNextCar() {
        return minDistanceToNextCar;
    }

    public void setMinDistanceToNextCar(double minDistanceToNextCar) {
        if(minDistanceToNextCar > 0){
            this.minDistanceToNextCar = minDistanceToNextCar;
        } else {
            throw new IllegalArgumentException("min distance must be positive");
        }
    }

    public double getMaxDistanceToNextCar() {
        return maxDistanceToNextCar;
    }

    public void setMaxDistanceToNextCar(double maxDistanceToNextCar) {
        if(maxDistanceToNextCar > 0){
            this.maxDistanceToNextCar = maxDistanceToNextCar;
        } else {
            throw new IllegalArgumentException("max distance must be positive");
        }
    }

    public double getMergeFactor() {
        return mergeFactor;
    }

    public void setMergeFactor(double mergeFactor) {
        this.mergeFactor = mergeFactor;
    }

}
