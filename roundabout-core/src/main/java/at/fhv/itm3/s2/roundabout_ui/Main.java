package at.fhv.itm3.s2.roundabout_ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pane root = fxmlLoader.load(getClass().getResource("/fxml/ui_roundabout_south_v2.fxml").openStream());

        UIController uiController = fxmlLoader.getController();
        //uiController.initSectionObservers(); //TODO inplement
        Scene scene = new Scene(root);

        stage.setTitle("Roundabout Dornbirn South Simulation");
        stage.setScene(scene);
        stage.show();
    }
}
