package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.ConsumerType;
import at.fhv.itm3.s2.roundabout.api.entity.Street;

import java.util.*;

public class StreetConnector implements IStreetConnector {

    private final String id;
    private final Collection<IConsumer> nextSections;
    private final Collection<IConsumer> previousSections;
    private final Map<IConsumer, List<IConsumer>> nextSectionsOnTrackMap;
    private final Map<IConsumer, List<IConsumer>> previousSectionsOnTrackMap;
    private final Map<IConsumer, ConsumerType> streetTypeMap;

    public StreetConnector(Collection<IConsumer> previousSections, Collection<IConsumer> nextSections){
        this(UUID.randomUUID().toString(), previousSections, nextSections);
    }

    public StreetConnector(String id, Collection<IConsumer> previousSections, Collection<IConsumer> nextSections) {
        this.id = id;

        this.previousSections = previousSections;
        if (previousSections != null && !previousSections.isEmpty()) {
            previousSections.forEach(s -> {if (s instanceof Street)((Street) s).setNextStreetConnector(this);});
        }

        this.nextSections = nextSections;
        if (nextSections != null && !nextSections.isEmpty()) {
            nextSections.forEach(s -> {if (s instanceof Street)((Street) s).setPreviousStreetConnector(this);});
        }

        this.nextSectionsOnTrackMap = new HashMap<>();
        this.previousSectionsOnTrackMap = new HashMap<>();
        this.streetTypeMap = new HashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<IConsumer> getNextConsumers() {
        return nextSections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<IConsumer> getPreviousConsumers() {
        return previousSections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IConsumer> getPreviousConsumers(ConsumerType consumerType) {
        List<IConsumer> streetsToReturn = new LinkedList<>();
        for (IConsumer nextSection : nextSections) {
            if (streetTypeMap.get(nextSection) == consumerType) {
                streetsToReturn.add(nextSection);
            }
        }
        return streetsToReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IConsumer> getPreviousTrackConsumers(IConsumer consumer, ConsumerType consumerType) {
        if (!this.previousSectionsOnTrackMap.containsKey(consumer)) {
            throw new IllegalArgumentException("There is no track defined for this street");
        }
        List<IConsumer> streetsToReturn = new LinkedList<>();
        for (IConsumer prevStreet: this.previousSectionsOnTrackMap.get(consumer)) {
            if (streetTypeMap.get(prevStreet) == consumerType) {
                streetsToReturn.add(prevStreet);
            }
        }
        return streetsToReturn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNextConsumerOnSameTrackAsCurrent(IConsumer currentConsumer, IConsumer nextConsumer) {
        if (!this.previousSectionsOnTrackMap.containsKey(nextConsumer) || !this.nextSectionsOnTrackMap.containsKey(currentConsumer)) {
            throw new IllegalArgumentException(String.format(
                "Connector: %s | There are no tracks defined for the given streets: \"%s\" |--> \"%s\"",
                getId(),
                currentConsumer,
                nextConsumer
            ));
        }
        List<IConsumer> nextStreetsOnTrack = this.nextSectionsOnTrackMap.get(currentConsumer);

        for (IConsumer street : nextStreetsOnTrack) {
            if (street == nextConsumer) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeTrack(IConsumer firstConsumer, ConsumerType consumerTypeOfFirstConsumer, IConsumer secondConsumer, ConsumerType consumerTypeOfSecondConsumer) {
        if (!this.nextSectionsOnTrackMap.containsKey(firstConsumer)) {
            this.nextSectionsOnTrackMap.put(firstConsumer, new LinkedList<>());
        }
        List<IConsumer> nextSections = this.nextSectionsOnTrackMap.get(firstConsumer);
        nextSections.add(secondConsumer);

        if (!this.previousSectionsOnTrackMap.containsKey(secondConsumer)) {
            this.previousSectionsOnTrackMap.put(secondConsumer, new LinkedList<>());
        }
        List<IConsumer> previousSections = this.previousSectionsOnTrackMap.get(secondConsumer);
        previousSections.add(firstConsumer);

        streetTypeMap.put(firstConsumer, consumerTypeOfFirstConsumer);
        streetTypeMap.put(secondConsumer, consumerTypeOfSecondConsumer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConsumerType getTypeOfConsumer(IConsumer consumer) {
        if (!streetTypeMap.containsKey(consumer)) {
            throw new IllegalArgumentException("There is no entry for this street");
        }
        return streetTypeMap.get(consumer);
    }
}
