package at.fhv.itm3.s2.roundabout.api.entity;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TrafficLightTest {

    @Test(expected = IllegalStateException.class)
    public void changeStateOfInactiveTrafficLight() {
        TrafficLight trafficLight = new TrafficLight(false);
        trafficLight.setFreeToGo(true);
    }

    @Test
    public void initialState() {
        TrafficLight trafficLight = new TrafficLight(true);
        assertTrue(trafficLight.isFreeToGo());
    }

    @Test
    public void setState() {
        TrafficLight trafficLight = new TrafficLight(true);
        trafficLight.setFreeToGo(false);
        assertFalse(trafficLight.isFreeToGo());
    }

    @Test
    public void isNotActive() {
        TrafficLight trafficLight = new TrafficLight(false);
        assertFalse(trafficLight.isActive());
    }
}
