package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by manue on 12.12.2017.
 */
public class DriverBehaviourTest {

    @Test
    public void shouldInitializeCorrectly() {
        double speed = 10.0;
        double minDistanceToNextCar = 2.0;
        double maxDistanceToNextCar = 5.0;
        double mergeFactor = 1.5;

        IDriverBehaviour driverBehaviour = new DriverBehaviour(10.0, 2.0,5.0, 1.5, 1);
        Assert.assertEquals(speed, driverBehaviour.getSpeed(), 0.0);
        Assert.assertEquals(minDistanceToNextCar, driverBehaviour.getMinDistanceToNextCar(), 0.0);
        Assert.assertEquals(maxDistanceToNextCar, driverBehaviour.getMaxDistanceToNextCar(), 0.0);
        Assert.assertEquals(mergeFactor, driverBehaviour.getMergeFactor(), 0.0);
    }

    @Test
    public void setSpeedIfGreaterOrEqualsZero() {
        double speed = 50.0;
        IDriverBehaviour driverBehaviour = createDriverBehaviour();
        driverBehaviour.setSpeed(speed);
        Assert.assertEquals(speed, driverBehaviour.getSpeed(), 0.0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIfSpeedIsLessThanZero() {
        IDriverBehaviour driverBehaviour = createDriverBehaviour();
        driverBehaviour.setSpeed(-50);
    }

    @Test
    public void setMinDistanceToNextCarIfGreaterThanZero() {
        double minDistanceToNextCar = 5;
        IDriverBehaviour driverBehaviour = createDriverBehaviour();
        driverBehaviour.setMinDistanceToNextCar(minDistanceToNextCar);
        Assert.assertEquals(minDistanceToNextCar, driverBehaviour.getMinDistanceToNextCar(), 0.0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIfMinDistanceToNextCarIsZero() {
        IDriverBehaviour driverBehaviour = createDriverBehaviour();
        driverBehaviour.setMinDistanceToNextCar(0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIfMinDistanceToNextCarIsLessThanZero() {
        IDriverBehaviour driverBehaviour = createDriverBehaviour();
        driverBehaviour.setMinDistanceToNextCar(-1);
    }

    @Test
    public void setMaxDistanceToNextCarIfGreaterThanZero() {
        double maxDistanceToNextCar = 5;
        IDriverBehaviour driverBehaviour = createDriverBehaviour();
        driverBehaviour.setMaxDistanceToNextCar(maxDistanceToNextCar);
        Assert.assertEquals(maxDistanceToNextCar, driverBehaviour.getMaxDistanceToNextCar(), 0.0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIfMaxDistanceToNextCarIsZero() {
        IDriverBehaviour driverBehaviour = createDriverBehaviour();
        driverBehaviour.setMaxDistanceToNextCar(0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowIfMaxDistanceToNextCarIsLessThanZero() {
        IDriverBehaviour driverBehaviour = createDriverBehaviour();
        driverBehaviour.setMaxDistanceToNextCar(-1);
    }

    private IDriverBehaviour createDriverBehaviour() {
        return  new DriverBehaviour(10.0, 2.0,5.0, 1.5, 1);
    }

}
