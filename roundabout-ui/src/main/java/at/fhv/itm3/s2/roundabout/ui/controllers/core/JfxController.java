package at.fhv.itm3.s2.roundabout.ui.controllers.core;

import java.util.function.Consumer;

public abstract class JfxController implements IJfxController {
    private Consumer<IJfxController> disposeFunction;

    @Override
    public void setDisposeFunction(Consumer<IJfxController> disposeFunction) {
        this.disposeFunction = disposeFunction;
    }

    @Override
    public void dispose() {
        if (disposeFunction != null) disposeFunction.accept(this);
    }
}