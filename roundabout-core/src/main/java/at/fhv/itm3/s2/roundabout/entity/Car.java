package at.fhv.itm3.s2.roundabout.entity;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.api.entity.IDriverBehaviour;
import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collections;
import java.util.List;

public class Car implements ICar {

    private final double length;
    private final IDriverBehaviour driverBehaviour;
    private final List<IStreetSection> route;

    private double lastUpdateTimestamp;
    private IStreetSection currentSection;


    public Car(double length, IDriverBehaviour driverBehaviour, List<IStreetSection> route) {
        this.length = length;
        this.driverBehaviour = driverBehaviour;
        this.route = route;

        this.setLastUpdateTime(0);
        this.setCurrentSection(!route.isEmpty() ? route.get(0) : null);
    }

    @Override
    public double getLastUpdateTime() {
        return lastUpdateTimestamp;
    }

    @Override
    public void setLastUpdateTime(double lastUpdateTimestamp)
    throws IllegalArgumentException {
        if(lastUpdateTimestamp > 0){
            this.lastUpdateTimestamp = lastUpdateTimestamp;
        } else {
            throw new IllegalArgumentException("last update timestamp must be greater than 0.");
        }
    }

    @Override
    public IDriverBehaviour getDriverBehaviour() {
        return driverBehaviour;
    }

    @Override
    public IStreetSection getNextStreetSection() {
        throw new NotImplementedException();
    }

    public double getLength() {
        return length;
    }

    @Override
    public IStreetSection getDestination() {
        return !route.isEmpty() ? route.get(route.size() - 1) : null;
    }

    @Override
    public List<IStreetSection> getRoute() {
        return Collections.unmodifiableList(route);
    }

    @Override
    public IStreetSection getCurrentSection() {
        return currentSection;
    }

    @Override
    public void setCurrentSection(IStreetSection currentSection)
    throws IllegalArgumentException {
        if (route.contains(currentSection) && route.indexOf(currentSection) >= route.indexOf(this.currentSection)) {
            this.currentSection = currentSection;
        } else {
            throw new IllegalArgumentException("actual street section must be in route and must follow last section");
        }
    }
}
