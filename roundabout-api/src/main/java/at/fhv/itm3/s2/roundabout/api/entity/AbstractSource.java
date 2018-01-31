package at.fhv.itm3.s2.roundabout.api.entity;

import at.fhv.itm14.trafsim.model.entities.AbstractProducer;
import desmoj.core.simulator.Model;

public abstract class AbstractSource extends AbstractProducer {
    public AbstractSource(Model model, String string, boolean bln) {
        super(model, string, bln);
    }

    /**
     * Starts the generation of cars by scheduling the first CarGenerateEvent.
     */
    public abstract void startGeneratingCars(double afterModelTimeUnits);

    /**
     * Returns the street to which the newly generated cars are added.
     *
     * @return the connected street as {@link Street}.
     */
    public abstract Street getConnectedStreet();
}
