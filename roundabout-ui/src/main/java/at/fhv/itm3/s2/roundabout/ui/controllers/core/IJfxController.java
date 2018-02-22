package at.fhv.itm3.s2.roundabout.ui.controllers.core;

import javafx.fxml.Initializable;

import java.util.function.Consumer;

/**
 * ViewController interface.
 * Implementing this interface helps to produce more flexible and reusable views.
 */
public interface IJfxController extends Initializable {
    /**
     * This method gives a possibility to register dispose function for view
     * so it can close itself, no relation to the "parent controller" is required.
     * Dispose function should be set from parent controller.
     *
     * @param disposeFunction Function that accepts "controller class that should be
     *                        disposed"(child controller) as an argument. In the body
     *                        of the function all the dependencies to the child controller
     *                        should be removed / unbounded from parent controller.
     *
     *                        e.g. Parent controller contains private variable which stores
     *                        reference to the child controller.
     */
    void setDisposeFunction(Consumer<IJfxController> disposeFunction);

    /**
     * Disposes {@code this} view if disposeFunction was registered and {@code this} view
     * is a child of the different view.
     */
    void dispose();
}