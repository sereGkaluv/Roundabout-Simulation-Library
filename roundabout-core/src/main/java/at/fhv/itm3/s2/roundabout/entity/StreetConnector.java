package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.ConsumerType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StreetConnector implements IStreetConnector {
    private List<IConsumer> nextSections;
    private List<IConsumer> previousSections;
    private Map<IConsumer, List<IConsumer>> nextSectionsOnTrackMap;
    private Map<IConsumer, List<IConsumer>> previousSectionsOnTrackMap;
    private Map<IConsumer, ConsumerType> streetTypeMap;

    public StreetConnector(List<IConsumer> previousSections, List<IConsumer> nextSections){
        this.previousSections = previousSections;
        this.nextSections = nextSections;
        this.nextSectionsOnTrackMap = new HashMap<>();
        this.previousSectionsOnTrackMap = new HashMap<>();
        this.streetTypeMap = new HashMap<>();
    }

    @Override
    public List<IConsumer> getNextConsumers() {
        return nextSections;
    }

    @Override
    public List<IConsumer> getPreviousConsumers() {
        return previousSections;
    }

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

    @Override
    public boolean isNextConsumerOnSameTrackAsCurrent(IConsumer currentConsumer, IConsumer nextConsumer) {
        if (!this.previousSectionsOnTrackMap.containsKey(nextConsumer) || !this.nextSectionsOnTrackMap.containsKey(currentConsumer)) {
            throw new IllegalArgumentException("There are no tracks defined for the given streets");
        }
        List<IConsumer> nextStreetsOnTrack = this.nextSectionsOnTrackMap.get(currentConsumer);

        for (IConsumer street: nextStreetsOnTrack) {
            if (street == nextConsumer) {
                return true;
            }
        }
        return false;
    }

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

    @Override
    public ConsumerType getTypeOfConsumer(IConsumer consumer) {
        if (!streetTypeMap.containsKey(consumer)) {
            throw new IllegalArgumentException("There is no entry for this street");
        }
        return streetTypeMap.get(consumer);
    }
}
