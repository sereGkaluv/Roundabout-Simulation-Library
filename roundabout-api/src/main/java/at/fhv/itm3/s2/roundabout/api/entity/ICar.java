package at.fhv.itm3.s2.roundabout.api.entity;

public interface ICar {
    IStreetSection getNextStreetSection();

    double getLength();
}
