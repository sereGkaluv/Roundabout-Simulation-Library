package at.fhv.itm3.s2.roundabout.entity;

import desmoj.core.simulator.Entity;
import desmoj.core.simulator.Model;

public class StreetSection extends Entity {

    public StreetSection(Model model, String s, boolean b) {
        super(model, s, b);
    }

    public boolean carCouldEnterNextSection() {
        // car knows its route (next section)

        // update position in next section
        // if is enough space in next section
        // update pos in previous roundabout section
        // if no car entering from previous roundabout section && enough space in previous section
        return false;
    }
}
