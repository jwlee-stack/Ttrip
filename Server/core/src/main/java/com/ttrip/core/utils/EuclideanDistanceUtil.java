package com.ttrip.core.utils;


import com.ttrip.core.entity.survey.Survey;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EuclideanDistanceUtil {
    public final DistanceMeasure euclideanDistance;
    private static final double  MAX_DISTANCE = 2.4; // 9개 항목 전체 일치할 경우
    private static final int  DEFAULT_RATE = 0;

    public EuclideanDistanceUtil() {
        this.euclideanDistance = new EuclideanDistance();
    }
    public int getMatchingRate(Survey s1, Survey s2) throws DimensionMismatchException {
        if (Objects.equals(s1, null) || Objects.equals(s2, null))
            return DEFAULT_RATE;
        double d = euclideanDistance.compute(s1.toVector(), s2.toVector());
        return (int) Math.round((MAX_DISTANCE - d )/ MAX_DISTANCE * 100);
    }
    public int getMatchingRateByVectors(double[] v1, double[] v2) throws DimensionMismatchException {
        if (Objects.equals(v1, null) || Objects.equals(v2, null))
            return DEFAULT_RATE;
        double d = euclideanDistance.compute(v1, v2);
        return (int) Math.round((MAX_DISTANCE - d )/ MAX_DISTANCE * 100);
    }
}
