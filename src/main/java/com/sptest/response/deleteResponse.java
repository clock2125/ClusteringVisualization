package com.sptest.response;

import java.util.List;

public class deleteResponse {

    List<double[]> mapData;
    int[] itemNum;

    public List<double[]> getMapData() {
        return mapData;
    }

    public void setMapData(List<double[]> mapData) {
        this.mapData = mapData;
    }

    public int[] getItemNum() {
        return itemNum;
    }

    public void setItemNum(int[] itemNum) {
        this.itemNum = itemNum;
    }

    public deleteResponse(List<double[]> mapData, int[] itemNum) {
        this.mapData = mapData;
        this.itemNum = itemNum;
    }
}
