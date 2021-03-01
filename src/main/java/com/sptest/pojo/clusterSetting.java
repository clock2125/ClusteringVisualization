package com.sptest.pojo;

import java.util.Arrays;

public class clusterSetting {
    String clusterAlgValue;
    int KValue;
    double epsilon;
    int minpoints;
    int iterations;
    String distanceMeasure;
    int[] selectedIndex;

    public clusterSetting(String clusterAlgValue, int KValue, double epsilon, int minpoints, int iterations, String distanceMeasure, int[] selectedIndex) {
        this.clusterAlgValue = clusterAlgValue;
        this.KValue = KValue;
        this.epsilon = epsilon;
        this.minpoints = minpoints;
        this.iterations = iterations;
        this.distanceMeasure = distanceMeasure;
        this.selectedIndex = selectedIndex;
    }

    public String getClusterAlgValue() {
        return clusterAlgValue;
    }

    public void setClusterAlgValue(String clusterAlgValue) {
        this.clusterAlgValue = clusterAlgValue;
    }

    public int getKValue() {
        return KValue;
    }

    public void setKValue(int KValue) {
        this.KValue = KValue;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    @Override
    public String toString() {
        return "clusterSetting{" +
                "clusterAlgValue='" + clusterAlgValue + '\'' +
                ", KValue=" + KValue +
                ", epsilon=" + epsilon +
                ", minpoints=" + minpoints +
                ", iterations=" + iterations +
                ", distanceMeasure='" + distanceMeasure + '\'' +
                ", selectedIndex=" + Arrays.toString(selectedIndex) +
                '}';
    }

    public int getMinpoints() {
        return minpoints;
    }

    public void setMinpoints(int minpoints) {
        this.minpoints = minpoints;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public String getDistanceMeasure() {
        return distanceMeasure;
    }

    public void setDistanceMeasure(String distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    public int[] getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int[] selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
