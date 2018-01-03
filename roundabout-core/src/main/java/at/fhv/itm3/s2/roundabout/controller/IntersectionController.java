package at.fhv.itm3.s2.roundabout.controller;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.intersection.Intersection;


import java.util.HashMap;
import java.util.Map;

public class IntersectionController {

    private Map<Intersection, Map<IConsumer, Integer>> intersectionsInDirectionsMap = new HashMap<>();
    private Map<Intersection, Map<IConsumer, Integer>> intersectionsOutDirectionsMap = new HashMap<>();

    private static IntersectionController instance;

    /**
     * Returns a singleton of {@link RouteController}.
     *
     * @return the singleton RouteController object.
     */
    public static IntersectionController getInstance() {
        if (instance == null) {
            instance = new IntersectionController();
        }
        return instance;
    }

    private IntersectionController() {

    }


    public void setIntersectionInDirectionMapping(Intersection intersection, IConsumer consumer, int direction) {
        Map<IConsumer, Integer> map = null;
        if (!intersectionsInDirectionsMap.containsKey(intersection)) {
            intersectionsInDirectionsMap.put(intersection, new HashMap<>());
        }
        map = intersectionsInDirectionsMap.get(intersection);
        map.put(consumer, direction);
    }

    public void setIntersectionOutDirectionMapping(Intersection intersection, IConsumer consumer, int direction) {
        Map<IConsumer, Integer> map = null;
        if (!intersectionsOutDirectionsMap.containsKey(intersection)) {
            intersectionsOutDirectionsMap.put(intersection, new HashMap<>());
        }
        map = intersectionsOutDirectionsMap.get(intersection);
        map.put(consumer, direction);
    }

    public int getInDirectionOfIConsumer(Intersection intersection, IConsumer consumer) {
        if (!intersectionsInDirectionsMap.containsKey(intersection)) {
            throw new IllegalArgumentException("There is no mapping for this intersection in the in-direction-map of the intersection");
        }
        Map<IConsumer, Integer> map = intersectionsInDirectionsMap.get(intersection);
        if (!map.containsKey(consumer)) {
            throw new IllegalArgumentException("There is no mapping for this consumer in the in-direction-map of the intersection");
        }
        return map.get(consumer);
    }

    public int getOutDirectionOfIConsumer(Intersection intersection, IConsumer consumer) {
        if (!intersectionsOutDirectionsMap.containsKey(intersection)) {
            throw new IllegalArgumentException("There is no mapping for this intersection in the in-direction-map of the intersection");
        }
        Map<IConsumer, Integer> map = intersectionsOutDirectionsMap.get(intersection);
        if (!map.containsKey(consumer)) {
            throw new IllegalArgumentException("There is no mapping for this consumer in the in-direction-map of the intersection");
        }
        return map.get(consumer);
    }
}
