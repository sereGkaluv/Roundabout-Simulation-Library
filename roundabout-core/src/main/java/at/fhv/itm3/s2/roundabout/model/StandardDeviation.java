package at.fhv.itm3.s2.roundabout.model;

import java.util.LinkedList;
import java.util.List;

public class StandardDeviation {

    private final double variance;
    private final double lowerRatio;
    private final double upperRatio;

    private StandardDeviation(double variance, double lowerRatio, double upperRatio) {
        this.variance = variance;
        this.lowerRatio = lowerRatio;
        this.upperRatio = upperRatio;
    }

    public static StandardDeviation calculate(double min, double max, double expectation, double stepSize) {
        final List<Double> listTmp = new LinkedList<>();
        double mean = 0.0;

        for(double curLength = min; curLength <= max; curLength += stepSize) {
            listTmp.add(curLength);
            mean += curLength;
        }
        mean /= listTmp.size();

        double variancePartSum = 0.0;
        for(double curVal : listTmp)  {
            curVal = Math.pow(curVal - mean, 2); // Preparation vor variance calculation
            variancePartSum += curVal;
        }

        final double lowerRatio = 1 / (max - min) * (expectation - min); // Ratio for the smaller values
        final double upperRatio = 1 - lowerRatio;
        final double variance = Math.sqrt(variancePartSum / listTmp.size());

        return new StandardDeviation(variance, lowerRatio, upperRatio);
    }

    public double getVariance() {
        return variance;
    }

    public double getLowerRatio() {
        return lowerRatio;
    }

    public double getUpperRatio() {
        return upperRatio;
    }

    public double getLeft() {
        return variance * lowerRatio;
    }

    public double getRight() {
        return variance * upperRatio;
    }
}
