package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import org.junit.jupiter.api.Test;

public class StreetSectionTest {
    @Test
    public void firstCarCouldEnterNextSection() throws Exception {

    }

    @Test
    public void isEnoughSpace() throws Exception {
        IStreetSection streetSection = new StreetSection(9.7, null, null, null, null, false);
        ICar firstCar = new Car(); // TODO entity missing
        streetSection.addCar(firstCar);
        // TODO implement
    }

}