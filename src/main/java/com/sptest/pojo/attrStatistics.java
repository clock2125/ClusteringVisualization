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
}
