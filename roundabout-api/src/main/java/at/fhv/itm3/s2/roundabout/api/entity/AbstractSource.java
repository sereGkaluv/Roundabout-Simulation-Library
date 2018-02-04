package at.fhv.itm3.s2.roundabout.api.entity;

import at.fhv.itm14.trafsim.model.entities.AbstractProducer;
import desmoj.core.simulator.Model;

import java.util.UUID;

public abstract class AbstractSource extends AbstractProducer {
    private String id;
    private double generatorExpectation;

    public AbstractSource(Double generatorExpectation, Model model, String description, boolean showInTrace) {
        this(UUID.randomUUID().toString(), generatorExpectation, model, description, showInTrace);
    }

    public AbstractSource(String id, Double generatorExpectation, Model model, String description, boolean showInTrace) {
        super(model, description, showInTrace);
        this.id = id;
        this.generatorExpectation = generatorExpectation != null ? generatorExpectation : 1.0;
    }

    /**
     * Get id of this source.
     * @return id value.
     */
    public String getId() {
        return id;
    }

    /**
     * Get generator expectation.
     *
     * @return generator expectation value.
     */
    public double getGeneratorExpectation() {
        return generatorExpectation;
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
