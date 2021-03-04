package com.sptest.response;

import java.util.List;

public class clusterResult {
    int[] result;
    List<double[]> mapData;
    int[] itemNum;
    List<String> label;



    public int[] getResult() {
        return result;
    }

    public void setResult(int[] result) {
        this.result = result;
    }

    public clusterResult(int[] result, List<double[]> mapData, int[] itemNum, List<String> label) {
        this.result = result;
        this.mapData = mapData;
        this.itemNum = itemNum;
        this.label = label;
    }

    public int[] getItemNum() {
        return itemNum;
    }

    public List<double[]> getMapData() {
        return mapData;
    }

    public void setMapData(List<double[]> mapData) {
        this.mapData = mapData;
    }

    public void setItemNum(int[] itemNum) {
        this.itemNum = itemNum;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }
}
