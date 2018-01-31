package at.fhv.itm3.s2.roundabout.ui.controller;

import at.fhv.itm3.s2.roundabout.api.entity.ConsumerType;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutSink;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutSource;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
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

    //Sink Minimum Roundabout ~ minimum waiting time
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


    //Sink Average Roundabout ~ average waiting time
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

    //Sink Maximum Roundabout ~ maximum waiting time
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


    //Sink Minimum Roundabout ~ minimum wainting time
    @FXML
    private Label lbl_S2_t1_CO_SK_MIN_s1;
    @FXML
    private Label lbl_S3_t1_CO_SK_MIN_s1;
    @FXML
    private Label lbl_S4_t1_CO_SK_MIN_s1;


    //Sink Average Roundabout ~ average waiting time
    @FXML
    private Label lbl_S3_t1_CO_SK_AVG_s1;
    @FXML
    private Label lbl_S4_t1_CO_SK_AVG_s1;
    @FXML
    private Label lbl_S2_t1_CO_SK_AVG_s1;


    //Sink Maximum Roundabout ~ maximum waiting time
    @FXML
    private Label lbl_S2_t1_CO_SK_MAX_s1;
    @FXML
    private Label lbl_S3_t1_CO_SK_MAX_s1;
    @FXML
    private Label lbl_S4_t1_CO_SK_MAX_s1;


    @FXML
    private void initialize() {
    }

    public void initSourceObservers(BiFunction<String, String, RoundaboutSource> sResolver) {
    }

    public void initStreetSectionObservers(BiFunction<String, String, StreetSection> sResolver) {
        addIsPsStreetLabels(sResolver.apply("ro1", "s2_t2"), lbl_s2_t2_is_ro1, lbl_s2_t2_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s2_t1"), lbl_s2_t1_is_ro1, lbl_s2_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s3_t1"), lbl_s3_t1_is_ro1, lbl_s3_t1_ps_ro1);
        addIsPsStreetLabels(sResolver.apply("ro1", "s3_t2"), lbl_s3_t2_is_ro1, lbl_s3_t2_ps_ro1);
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

        // Component id, section id. for intersection
        /*addIsPsStreetLabels(sResolver.apply("is1", "s3_t4"), lbl_s3_t4_is_s1, lbl_s3_t4_ps_s1);
        addIsPsStreetLabels(sResolver.apply("is1", "s3_t3"), lbl_s3_t3_is_s1, lbl_s3_t3_ps_s1);
        addIsPsStreetLabels(sResolver.apply("is1", "s4_t1"), lbl_s4_t1_is_s1, lbl_s4_t1_ps_s1);
        addIsPsStreetLabels(sResolver.apply("is1", "s5_t1"), lbl_s5_t1_is_s1, lbl_s5_t1_ps_s1);
        addIsPsStreetLabels(sResolver.apply("is1", "s6_t1"), lbl_s6_t1_is_s1, lbl_s6_t1_ps_s1);
        addIsPsStreetLabels(sResolver.apply("is1", "s6_t2"), lbl_s6_t2_is_s1, lbl_s6_t2_ps_s1);
        addIsPsStreetLabels(sResolver.apply("is1", "s2_t1"), lbl_s2_t1_is_s1, lbl_s2_t1_ps_s1);
        addIsPsStreetLabels(sResolver.apply("is1", "s2_t2"), lbl_s2_t2_is_s1, lbl_s2_t2_ps_s1);*/
    }

    public void initSinkObservers(BiFunction<String, String, RoundaboutSink> sResolver) {
        addMinAvgMaxSinkLabels(sResolver.apply("is1", "s4_t1"), lbl_S4_t1_CO_SK_MIN_s1, lbl_S4_t1_CO_SK_AVG_s1, lbl_S4_t1_CO_SK_MAX_s1);
    }

    private void addIsPsStreetLabels(final Street street, Label isLabel, Label psLabel) {
        addIsPsStreetObservers(street, isLabel::setText, psLabel::setText);
    }

    private void addMinAvgMaxSinkLabels(final RoundaboutSink sink, Label minLabel, Label avgLabel, Label maxLabel) {
        addMinAvgMaxObservers(sink, minLabel::setText, avgLabel::setText, maxLabel::setText);
    }

    private void addMinAvgMaxObservers(final RoundaboutSink sink, Consumer<String> minConsumer, Consumer<String> avgMinConsumer, Consumer<String> maxConsumer) {
        //TODO
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
