package at.fhv.itm3.s2.roundabout.api.entity;

import desmoj.core.simulator.Model;

/**
 * Created by Karin on 05.01.2018.
 */
public abstract class AbstractSink extends Street implements ICarCountable {

    public AbstractSink(Model model, String string, boolean bln) {
        super(model, string, bln);
    }
}
