package com.sptest.pojo;

import java.util.List;

public class attrStatistics {
    float mean;
    float mid;
    float max;
    float min;
    float var;
    float std;

    int[] distribution;

    public attrStatistics(float mean, float mid, float max, float min, float var, float std, int[] distribution) {
        this.mean = mean;
        this.mid = mid;
        this.max = max;
        this.min = min;
        this.var = var;
        this.std = std;
        this.distribution = distribution;
    }

    public float getMid() {
        return mid;
    }

    public void setMid(float mid) {
        this.mid = mid;
    }

    public float getVar() {
        return var;
    }

    public void setVar(float var) {
        this.var = var;
    }

    public float getStd() {
        return std;
    }

    public void setStd(float std) {
        this.std = std;
    }

    public int[] getDistribution() {
        return distribution;
    }

    public void setDistribution(int[] distribution) {
        this.distribution = distribution;
    }

    public attrStatistics() {
    }

    public float getMean() {
        return mean;
    }

    public void setMean(float mean) {
        this.mean = mean;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }
}
