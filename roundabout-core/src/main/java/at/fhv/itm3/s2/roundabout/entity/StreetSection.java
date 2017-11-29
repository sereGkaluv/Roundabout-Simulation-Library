package at.fhv.itm3.s2.roundabout.entity;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class StreetSection extends Entity {


    public StreetSection(Model model, String s, boolean b) {
        super(model, s, b);
    }

    public void updateAllCarsPositions() {

    }

    public boolean isFirstCarOnExitPoint() {
        return false;
    }

    /**
     * Checks, if first car in street section is able to enter the next section, depending on its predefined route.
     *
     * @return true = car can enter next section, false = car can not enter next section
     */
    public boolean firstCarCouldEnterNextSection() {
        // car knows its route (next section)

        // update position in next section
        // if is enough space in next section
        // update pos in previous roundabout section
        // if no car entering from previous roundabout section && enough space in previous section

        if (isFirstCarOnExitPoint()) {
            return true;
        }

        return false;
    }

    public void moveFirstCarToNextSection() {

    }
}
