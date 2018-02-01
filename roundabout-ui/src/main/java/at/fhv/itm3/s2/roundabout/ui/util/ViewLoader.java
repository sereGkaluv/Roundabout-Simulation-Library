package at.fhv.itm3.s2.roundabout.ui.util;


import at.fhv.itm3.s2.roundabout.ui.controllers.MainViewController;
import at.fhv.itm3.s2.roundabout.ui.controllers.StatsViewController;
import at.fhv.itm3.s2.roundabout.ui.controllers.core.IJfxController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class which helps to manage JavaFX view and their controllers.
 * @param <T> controllers class. Is required for controllers instantiation and
 * identification of the required {@code fxml} file.
 */
public class ViewLoader<T extends IJfxController> {
    private static Map<Class<? extends IJfxController>, String> viewPaths;

    /*
      Static Map for all registered {@code fxml} files and their controllers.
     */
    static {
        viewPaths = new HashMap<>();

        //All UI-Controller relations should be defined here.
        viewPaths.put(MainViewController.class, "/at/fhv/itm3/s2/roundabout/ui/views/main.fxml");
        viewPaths.put(StatsViewController.class, "/at/fhv/itm3/s2/roundabout/ui/views/stats.fxml");
    }

    private FXMLLoader loader;

    /**
     * Default constructor for this utility class.
     * @param controllerClass controllers class of the requested view.
     */
    private ViewLoader(Class<T> controllerClass) {
        String path = viewPaths.get(controllerClass);
        assert(path != null);
        URL viewResource = getClass().getResource(path);
        assert(viewResource != null);

        loader = new FXMLLoader(viewResource);
    }

    /**
     * Returns loaded view as a {@code Node}}.
     * @return loaded {@code Node}.
     */
    public Node loadNode() {
        try {
            return loader.load();
        } catch (IOException e) {
            throw new ViewLoaderException(e);
        }
    }

    /**
     * Returns loaded controllers class.
     * @return controllers class.
     */
    public T getController() {
        return loader.getController();
    }

    /**
     * Returns new ViewLoader instance. ViewLoader provides a functionality
     * to load the View by its controllers class and the instance of the
     * controllers class itself.
     * @param controllerClass controllers class of the requested view.
     * @param <T> controllers which is an instance of IJfxController.
     * @return new ViewLoader instance for requested controllers class.
     */
    public static <T extends IJfxController> ViewLoader<T> loadView(Class<T> controllerClass) {
        return new ViewLoader<>(controllerClass);
    }

    /**
     * ViewLoaderException class which is used (returned) to indicate problem on
     * the view-loading stage.
     */
    private static class ViewLoaderException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        /**
         * Constructor which wraps the reason exception.
         * @param e exception to e wrapped.
         */
        ViewLoaderException(Exception e) {
            super(e);
        }
    }
}