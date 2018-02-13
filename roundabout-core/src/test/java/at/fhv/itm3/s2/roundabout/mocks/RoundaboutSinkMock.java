package at.fhv.itm3.s2.roundabout.mocks;

import at.fhv.itm3.s2.roundabout.api.entity.ICar;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutSink;
import desmoj.core.simulator.Model;

import java.util.LinkedList;

public class RoundaboutSinkMock extends RoundaboutSink {

    private LinkedList<ICar> enteredCars;

    public RoundaboutSinkMock(Model model, String s, boolean b) {
        super(model, s, b);
        enteredCars = new LinkedList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCar(ICar iCar) {
        enteredCars.addLast(iCar);
        super.addCar(iCar);
    }

    public LinkedList<ICar> getEnteredCars() {
        return enteredCars;
    }
}
