package io.demo.danggn;

import java.util.ArrayList;
import java.util.List;

public class MovingAverage {
    private List<Float> arr = new ArrayList<>();
    private int maxCount = 5;
    private float bias = 5.0f;
    private float weight = 0.1f;

    public void append(float element) {
        arr.add(element);
        if (arr.size() > maxCount) {
            arr.removeFirst();
        }
    }

    public float getAverageValue() {
        if (arr.isEmpty()) {
            return 0f;
        }
        float sum = 0f;
        for (float value : arr) {
            sum += value * weight;
            weight += 0.1f;
        }
        float average = sum / arr.size() + bias;
        return String.format("%.4f", average).isEmpty() ? 0f : Float.parseFloat(String.format("%.4f", average));
    }
}
