package at.fhv.itm3.s2.roundabout.api.entity;

public interface StreetSection {
    void updateAllCarsPositions();

    boolean isFirstCarOnExitPoint();

    boolean carCouldEnterNextSection();

    void moveFirstCarToNextSection();
}
