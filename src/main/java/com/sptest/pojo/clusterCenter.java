package com.sptest.pojo;

import java.util.Collection;

public class clusterCenter implements Comparable<clusterCenter>{
    double distance;
    Collection<Double> center;
    int index;
    int itemNum;

    public clusterCenter(Collection<Double> center, int index, int itemNum) {
        this.center = center;
        this.index = index;
        this.itemNum = itemNum;
        distance = 0;
        for (Double aDouble : center) {
            distance+=aDouble;
        }
    }

    public Collection<Double> getCenter() {
        return center;
    }

    public void setCenter(Collection<Double> center) {
        this.center = center;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }


    public clusterCenter(double distance, int index) {
        this.distance = distance;
        this.index = index;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(clusterCenter o) {
        return Double.compare(this.distance, o.distance);
    }
}
