package com.sptest.response;

import java.util.List;

public class globalCovResponse {
    List<String> nameLabel;
    double[] globalCovList;

    public List<String> getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(List<String> nameLabel) {
        this.nameLabel = nameLabel;
    }

    public double[] getGlobalCovList() {
        return globalCovList;
    }

    public void setGlobalCovList(double[] globalCovList) {
        this.globalCovList = globalCovList;
    }

    public globalCovResponse(List<String> nameLabel, double[] globalCovList) {
        this.nameLabel = nameLabel;
        this.globalCovList = globalCovList;
    }
}
