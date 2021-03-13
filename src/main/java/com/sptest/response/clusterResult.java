package com.sptest.response;

import java.util.List;

public class clusterResult {
    int[] result;
    List<double[]> mapData;
    int[] itemNum;
    int clusterNum;

    public int getClusterNum() {
        return clusterNum;
    }

    public void setClusterNum(int clusterNum) {
        this.clusterNum = clusterNum;
    }

    public clusterResult(int[] result, List<double[]> mapData, int[] itemNum, int clusterNum) {
        this.result = result;
        this.mapData = mapData;
        this.itemNum = itemNum;
        this.clusterNum = clusterNum;
    }

    public int[] getResult() {
        return result;
    }

    public void setResult(int[] result) {
        this.result = result;
    }

    public clusterResult(int[] result, List<double[]> mapData, int[] itemNum) {
        this.result = result;
        this.mapData = mapData;
        this.itemNum = itemNum;
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
}
