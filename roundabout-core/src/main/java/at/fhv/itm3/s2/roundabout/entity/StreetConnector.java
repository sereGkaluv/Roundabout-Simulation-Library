package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.AbstractConsumer;
import at.fhv.itm14.trafsim.model.entities.AbstractProducer;
import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.IProducer;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.Street;

import java.util.Set;

public class StreetConnector implements IStreetConnector {
    private Set<IConsumer> nextSections;
    private Set<IProducer> previousSections;

    public StreetConnector(Set<IProducer> previousSections, Set<IConsumer> nextSections){
        this.previousSections = previousSections;
        this.nextSections = nextSections;
    }

    @Override
    public Set<IConsumer> getNextSections() {
        return nextSections;
    }

    @Override
    public Set<IProducer> getPreviousSections() {
        return previousSections;
    }
}
