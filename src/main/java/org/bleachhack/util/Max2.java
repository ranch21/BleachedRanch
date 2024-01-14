package org.bleachhack.util;


import static java.lang.Math.min;
import static java.lang.Math.max;

public class Max2 {

    public static double max2 (double number, double limit) {
        double max = min(number, limit);

        return max(max, -limit);
    }
}
