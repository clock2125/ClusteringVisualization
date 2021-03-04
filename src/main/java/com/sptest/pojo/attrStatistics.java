package com.sptest.pojo;

import java.util.List;

public class attrStatistics {
    double mean;
    double mid;
    double max;
    double min;
    double var;
    double std;

    int[] distribution;

    double[] covDataList;
    List<String> covLabelList;

    public attrStatistics(double mean, double mid, double max, double min, double var, double std, int[] distribution, double[] covDataList, List<String> covLabelList) {
        this.mean = mean;
        this.mid = mid;
        this.max = max;
        this.min = min;
        this.var = var;
        this.std = std;
        this.distribution = distribution;
        this.covDataList = covDataList;
        this.covLabelList = covLabelList;
    }

    public attrStatistics(double mean, double mid, double max, double min, double var, double std, int[] distribution, double[] covDataList) {
        this.mean = mean;
        this.mid = mid;
        this.max = max;
        this.min = min;
        this.var = var;
        this.std = std;
        this.distribution = distribution;
        this.covDataList = covDataList;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getMid() {
        return mid;
    }

    public void setMid(double mid) {
        this.mid = mid;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getVar() {
        return var;
    }

    public void setVar(double var) {
        this.var = var;
    }

    public double getStd() {
        return std;
    }

    public void setStd(double std) {
        this.std = std;
    }

    public int[] getDistribution() {
        return distribution;
    }

    public void setDistribution(int[] distribution) {
        this.distribution = distribution;
    }

    public double[] getCovDataList() {
        return covDataList;
    }

    public void setCovDataList(double[] covDataList) {
        this.covDataList = covDataList;
    }

    public List<String> getCovLabelList() {
        return covLabelList;
    }

    public void setCovLabelList(List<String> covLabelList) {
        this.covLabelList = covLabelList;
    }
}
