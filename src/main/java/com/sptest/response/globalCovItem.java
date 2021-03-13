package com.sptest.response;

public class globalCovItem implements Comparable<globalCovItem>{
    String name;
    double value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public globalCovItem(String name, double value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int compareTo(globalCovItem o) {
        return Double.compare(this.value, o.value);
    }
}
