package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetConnector;
import at.fhv.itm3.s2.roundabout.api.entity.StreetType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StreetConnector implements IStreetConnector {
    private List<IConsumer> nextSections;
    private List<IConsumer> previousSections;
    private Map<IConsumer, List<IConsumer>> nextSectionsOnTrackMap;
    private Map<IConsumer, List<IConsumer>> previousSectionsOnTrackMap;
    private Map<IConsumer, StreetType> streetTypeMap;

    public StreetConnector(List<IConsumer> previousSections, List<IConsumer> nextSections){
        this.previousSections = previousSections;
        this.nextSections = nextSections;
        this.nextSectionsOnTrackMap = new HashMap<>();
        this.previousSectionsOnTrackMap = new HashMap<>();
        this.streetTypeMap = new HashMap<>();
    }

    @Override
    public List<IConsumer> getNextSections() {
        return nextSections;
    }

    @Override
    public List<IConsumer> getPreviousSections() {
        return previousSections;
    }

    @Override
    public List<IConsumer> getPreviousSections(StreetType streetType) {
        List<IConsumer> streetsToReturn = new LinkedList<>();
        for (IConsumer nextSection : nextSections) {
            if (streetTypeMap.get(nextSection) == streetType) {
                streetsToReturn.add(nextSection);
            }
        }
        return streetsToReturn;
    }

    @Override
    public List<IConsumer> getPreviousTrackSections(IConsumer street, StreetType streetType) {
        if (!this.previousSectionsOnTrackMap.containsKey(street)) {
            throw new IllegalArgumentException("There is no track defined for this street");
        }
        List<IConsumer> streetsToReturn = new LinkedList<>();
        for (IConsumer prevStreet: this.previousSectionsOnTrackMap.get(street)) {
            if (streetTypeMap.get(prevStreet) == streetType) {
                streetsToReturn.add(prevStreet);
            }
        }
        return streetsToReturn;
    }

    @Override
    public boolean isNextStreetOnSameTrackAsCurrent(IConsumer currentStreet, IConsumer nextStreet) {
        if (!this.previousSectionsOnTrackMap.containsKey(nextStreet) || !this.nextSectionsOnTrackMap.containsKey(currentStreet)) {
            throw new IllegalArgumentException("There are no tracks defined for the given streets");
        }
        List<IConsumer> nextStreetsOnTrack = this.nextSectionsOnTrackMap.get(currentStreet);

        for (IConsumer street: nextStreetsOnTrack) {
            if (street == nextStreet) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void initializeTrack(IConsumer firstStreet, StreetType streetTypeOfFirstStreet, IConsumer secondStreet, StreetType streetTypeOfSecondStreet) {
        if (!this.nextSectionsOnTrackMap.containsKey(firstStreet)) {
            this.nextSectionsOnTrackMap.put(firstStreet, new LinkedList<>());
        }
        List<IConsumer> nextSections = this.nextSectionsOnTrackMap.get(firstStreet);
        nextSections.add(secondStreet);

        if (!this.previousSectionsOnTrackMap.containsKey(secondStreet)) {
            this.previousSectionsOnTrackMap.put(secondStreet, new LinkedList<>());
        }
        List<IConsumer> previousSections = this.previousSectionsOnTrackMap.get(secondStreet);
        previousSections.add(firstStreet);

        streetTypeMap.put(firstStreet, streetTypeOfFirstStreet);
        streetTypeMap.put(secondStreet, streetTypeOfSecondStreet);
    }

    @Override
    public StreetType getTypeOfStreet(IConsumer street) {
        if (!streetTypeMap.containsKey(street)) {
            throw new IllegalArgumentException("There is no entry for this street");
        }
        return streetTypeMap.get(street);
    }
}
