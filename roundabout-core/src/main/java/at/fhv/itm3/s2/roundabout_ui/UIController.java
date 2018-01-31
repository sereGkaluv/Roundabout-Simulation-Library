package at.fhv.itm3.s2.roundabout_ui;

import at.fhv.itm3.s2.roundabout.api.entity.Street;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class UIController {

    //Roundabout
    //in section roundabout ~ number of cars in section
    @FXML
    private Label lbl_s1_t1_is_ro1;
    @FXML
    private Label lbl_s2_t2_is_ro1;
    @FXML
    private Label lbl_s2_t1_is_ro1;
    @FXML
    private Label lbl_s3_t1_is_ro1;
    @FXML
    private Label lbl_s3_t2_is_ro1;
    @FXML
    private Label lbl_s4_t1_is_ro1;
    @FXML
    private Label lbl_s5_t1_is_ro1;
    @FXML
    private Label lbl_s6_t1_is_ro1;
    @FXML
    private Label lbl_s7_t1_is_ro1;
    @FXML
    private Label lbl_s7_t2_is_ro1;
    @FXML
    private Label lbl_s8_t1_is_ro1;
    @FXML
    private Label lbl_s9_t1_is_ro1;
    @FXML
    private Label lbl_s9_t2_is_ro1;
    @FXML
    private Label lbl_s10_t1_is_ro1;
    @FXML
    private Label lbl_s10_t2_is_ro1;
    @FXML
    private Label lbl_s11_t1_is_ro1;
    @FXML
    private Label lbl_s12_t1_is_ro1;
    @FXML
    private Label lbl_s12_t2_is_ro1;
    @FXML
    private Label lbl_s13_t1_is_ro1;
    @FXML
    private Label lbl_s14_t1_is_ro1;
    @FXML
    private Label lbl_s14_t2_is_ro1;
    @FXML
    private Label lbl_s15_t1_is_ro1;
    @FXML
    private Label lbl_s16_t1_is_ro1;
    @FXML
    private Label lbl_s16_t2_is_ro1;
    @FXML
    private Label lbl_s17_t1_is_ro1;
    @FXML
    private Label lbl_s18_t1_is_ro1;
    @FXML
    private Label lbl_s19_t1_is_ro1;
    @FXML
    private Label lbl_s19_t2_is_ro1;

    //passed section roundabout ~ total number of cars, which passed this section
    @FXML
    private Label lbl_s1_t1_ps_ro1;
    @FXML
    private Label lbl_s2_t2_ps_ro1;
    @FXML
    private Label lbl_s2_t1_ps_ro1;
    @FXML
    private Label lbl_s3_t1_ps_ro1;
    @FXML
    private Label lbl_s3_t2_ps_ro1;
    @FXML
    private Label lbl_s4_t1_ps_ro1;
    @FXML
    private Label lbl_s5_t1_ps_ro1;
    @FXML
    private Label lbl_s6_t1_ps_ro1;
    @FXML
    private Label lbl_s7_t1_ps_ro1;
    @FXML
    private Label lbl_s7_t2_ps_ro1;
    @FXML
    private Label lbl_s8_t1_ps_ro1;
    @FXML
    private Label lbl_s9_t1_ps_ro1;
    @FXML
    private Label lbl_s9_t2_ps_ro1;
    @FXML
    private Label lbl_s10_t1_ps_ro1;
    @FXML
    private Label lbl_s10_t2_ps_ro1;
    @FXML
    private Label lbl_s11_t1_ps_ro1;
    @FXML
    private Label lbl_s12_t1_ps_ro1;
    @FXML
    private Label lbl_s12_t2_ps_ro1;
    @FXML
    private Label lbl_s13_t1_ps_ro1;
    @FXML
    private Label lbl_s14_t1_ps_ro1;
    @FXML
    private Label lbl_s14_t2_ps_ro1;
    @FXML
    private Label lbl_s15_t1_ps_ro1;
    @FXML
    private Label lbl_s16_t1_ps_ro1;
    @FXML
    private Label lbl_s16_t2_ps_ro1;
    @FXML
    private Label lbl_s17_t1_ps_ro1;
    @FXML
    private Label lbl_s18_t1_ps_ro1;
    @FXML
    private Label lbl_s19_t1_ps_ro1;
    @FXML
    private Label lbl_s19_t2_ps_ro1;

    //Sink Minimum Roundabout
    @FXML
    private Label lbl_s17_t1_CO_SK_MIN_ro1;
    @FXML
    private Label lbl_s4_t1_CO_SK_MIN_ro1;
    @FXML
    private Label lbl_s6_t1_CO_SK_MIN_ro1;
    @FXML
    private Label lbl_s9_t1_CO_SK_MIN_ro1;
    @FXML
    private Label lbl_s9_t2_CO_SK_MIN_ro1;
    @FXML
    private Label lbl_s11_t1_CO_SK_MIN_ro1;
    @FXML
    private Label lbl_s13_t1_CO_SK_MIN_ro1;
    @FXML
    private Label lbl_s15_t1_CO_SK_MIN_ro1;


    //Sink Average Roundabout
    @FXML
    private Label lbl_s17_t1_CO_SK_AVG_ro1;
    @FXML
    private Label lbl_s4_t1_CO_SK_AVG_ro1;
    @FXML
    private Label lbl_s6_t1_CO_SK_AVG_ro1;
    @FXML
    private Label lbl_s9_t1_CO_SK_AVG_ro1;
    @FXML
    private Label lbl_s9_t2_CO_SK_AVG_ro1;
    @FXML
    private Label lbl_s11_t1_CO_SK_AVG_ro1;
    @FXML
    private Label lbl_s13_t1_CO_SK_AVG_ro1;
    @FXML
    private Label lbl_s15_t1_CO_SK_AVG_ro1;

    //Sink Maximum Roundabout
    @FXML
    private Label lbl_s17_t1_CO_SK_MAX_ro1;
    @FXML
    private Label lbl_s4_t1_CO_SK_MAX_ro1;
    @FXML
    private Label lbl_s6_t1_CO_SK_MAX_ro1;
    @FXML
    private Label lbl_s9_t1_CO_SK_MAX_ro1;
    @FXML
    private Label lbl_s9_t2_CO_SK_MAX_ro1;
    @FXML
    private Label lbl_s11_t1_CO_SK_MAX_ro1;
    @FXML
    private Label lbl_s13_t1_CO_SK_MAX_ro1;
    @FXML
    private Label lbl_s15_t1_CO_SK_MAX_ro1;

    //Intersection
    //in section intersection ~ number of cars in section
    @FXML
    private Label lbl_s3_t4_is_s1;
    @FXML
    private Label lbl_s3_t3_is_s1;
    @FXML
    private Label lbl_s4_t1_is_s1;
    @FXML
    private Label lbl_s5_t1_is_s1;
    @FXML
    private Label lbl_s6_t1_is_s1;
    @FXML
    private Label lbl_s6_t2_is_s1;
    @FXML
    private Label lbl_s2_t1_is_s1;
    @FXML
    private Label lbl_s2_t2_is_s1;

    //passed section intersection ~ total number of cars, which passed this section
    @FXML
    private Label lbl_s3_t4_ps_s1;
    @FXML
    private Label lbl_s3_t3_ps_s1;
    @FXML
    private Label lbl_s4_t1_ps_s1;
    @FXML
    private Label lbl_s5_t1_ps_s1;
    @FXML
    private Label lbl_s6_t1_ps_s1;
    @FXML
    private Label lbl_s6_t2_ps_s1;
    @FXML
    private Label lbl_s2_t1_ps_s1;
    @FXML
    private Label lbl_s2_t2_ps_s1;


    //Sink Minimum Roundabout
    @FXML
    private Label lbl_S2_t1_CO_SK_MIN_s1;
    @FXML
    private Label lbl_S3_t1_CO_SK_MIN_s1;
    @FXML
    private Label lbl_S4_t1_CO_SK_MIN_s1;


    //Sink Average Roundabout
    @FXML
    private Label lbl_S3_t1_CO_SK_AVG_s1;
    @FXML
    private Label lbl_S4_t1_CO_SK_AVG_s1;
    @FXML
    private Label lbl_S2_t1_CO_SK_AVG_s1;


    //Sink Maximum Roundabout
    @FXML
    private Label lbl_S2_t1_CO_SK_MAX_s1;
    @FXML
    private Label lbl_S3_t1_CO_SK_MAX_s1;
    @FXML
    private Label lbl_S4_t1_CO_SK_MAX_s1;


    @FXML
    private void initialize() {
    }

    public void initSectionObservers(BiFunction<String, String, Street> sResolver) {
        // Component id, section id.
        addIsPsStreetLabels(sResolver.apply("ro1", "s1_t1"), lbl_s1_t1_is_ro1, lbl_s1_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s2_t2"), lbl_s2_t2_is_ro1, lbl_s2_t2_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s2_t1"), lbl_s2_t1_is_ro1, lbl_s2_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s3_t1"), lbl_s3_t1_is_ro1, lbl_s3_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "t2_ps"), lbl_s3_t2_is_ro1, lbl_s3_t2_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s4_t1"), lbl_s4_t1_is_ro1, lbl_s4_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s5_t1"), lbl_s5_t1_is_ro1, lbl_s5_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s6_t1"), lbl_s6_t1_is_ro1, lbl_s6_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s7_t1"), lbl_s7_t1_is_ro1, lbl_s7_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s7_t2"), lbl_s7_t2_is_ro1, lbl_s7_t2_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s8_t1"), lbl_s8_t1_is_ro1, lbl_s8_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s9_t1"), lbl_s9_t1_is_ro1, lbl_s9_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s9_t2"), lbl_s9_t2_is_ro1, lbl_s9_t2_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s10_t1"), lbl_s10_t1_is_ro1, lbl_s10_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s10_t2"), lbl_s10_t2_is_ro1, lbl_s10_t2_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s11_t1"), lbl_s11_t1_is_ro1, lbl_s11_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s12_t1"), lbl_s12_t1_is_ro1, lbl_s12_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s12_t2"), lbl_s12_t2_is_ro1, lbl_s12_t2_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s13_t1"), lbl_s13_t1_is_ro1, lbl_s13_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s14_t1"), lbl_s14_t1_is_ro1, lbl_s14_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s14_t2"), lbl_s14_t2_is_ro1, lbl_s14_t2_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s15_t1"), lbl_s15_t1_is_ro1, lbl_s15_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s16_t1"), lbl_s16_t1_is_ro1, lbl_s16_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s16_t2"), lbl_s16_t2_is_ro1, lbl_s16_t2_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s17_t1"), lbl_s17_t1_is_ro1, lbl_s17_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s18_t1"), lbl_s18_t1_is_ro1, lbl_s18_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s19_t1"), lbl_s19_t1_is_ro1, lbl_s19_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s19_t2"), lbl_s19_t2_is_ro1, lbl_s19_t2_ps_ro1);

    }

    private void addIsPsStreetLabels(final Street street, Label isLabel, Label psLabel) {
        addIsPsStreetObservers(street, isLabel::setText, psLabel::setText);
    }

    private void addIsPsStreetObservers(final Street street, Consumer<String> isConsumer, Consumer<String> psConsumer) {
        street.addObserver((o, arg) -> {
            final String rawValue = toStringOrEmpty(arg);
            psConsumer.accept(rawValue);

            final long is_counter = Math.max(street.getNrOfEnteredCars() - Long.valueOf(rawValue), 0);
            isConsumer.accept(toStringOrEmpty(is_counter));
        });
    }

    private String toStringOrEmpty(Object value) {
        return value != null ? value.toString() : "0";
    }
}
