package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;

public class DriverBehaviour implements IDriverBehaviour {

    private double speed;
    private double minDistanceToNextCar;
    private double maxDistanceToNextCar;
    private double mergeFactor;
    private double accelerationFactor;

    public DriverBehaviour(double speed, double minDistanceToNextCar, double maxDistanceToNextCar, double mergeFactor, double accelerationFactor)
    throws IllegalArgumentException {
        setSpeed(speed);
        this.minDistanceToNextCar = minDistanceToNextCar;
        this.maxDistanceToNextCar = maxDistanceToNextCar;
        this.mergeFactor = mergeFactor;
        this.accelerationFactor = accelerationFactor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getSpeed() {
        return speed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSpeed(double speed)
    throws IllegalArgumentException {
        if (speed >= 0) {
            this.speed = speed;
        } else {
            throw new IllegalArgumentException("Speed should be greater or equal than 0");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getMinDistanceToNextCar() {
        return minDistanceToNextCar;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMinDistanceToNextCar(double minDistanceToNextCar)
    throws IllegalArgumentException {
        if (minDistanceToNextCar > 0) {
            this.minDistanceToNextCar = minDistanceToNextCar;
        } else {
            throw new IllegalArgumentException("Min distance must be positive");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getMaxDistanceToNextCar() {
        return maxDistanceToNextCar;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMaxDistanceToNextCar(double maxDistanceToNextCar)
    throws IllegalArgumentException {
        if (maxDistanceToNextCar > 0) {
            this.maxDistanceToNextCar = maxDistanceToNextCar;
        } else {
            throw new IllegalArgumentException("Max distance must be positive");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getMergeFactor() {
        return mergeFactor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMergeFactor(double mergeFactor) {
        this.mergeFactor = mergeFactor;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public double getAccelerationFactor() {
        return accelerationFactor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAccelerationFactor(double accelerationFactor) {
        this.accelerationFactor = accelerationFactor;
    }
}
