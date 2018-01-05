package at.fhv.itm3.s2.roundabout.api.entity;

import at.fhv.itm14.trafsim.model.entities.AbstractConsumer;
import at.fhv.itm14.trafsim.model.entities.AbstractProducer;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.IProducer;

import java.util.Set;

public interface IStreetConnector {

    /**
     * Gets the next connected {@link Street}s, which are accessible via this connector.
     *
     * @return The further connected sections in form of {@link Set< Street >}.
     */
    Set<IConsumer> getNextSections();

    /**
     * Gets the previous connected {@link Street}s, which are accessible through this connector.
     *
     * @return The previous connected sections in form of {@link Set< Street >}.
     */
    Set<IProducer> getPreviousSections();
}
