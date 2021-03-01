package com.sptest.pojo;

import java.util.List;

public class csvData {

    List<List<String>> data;
    int[] head;

    public csvData(List<List<String>> data, int[] head) {
        this.data = data;
        this.head = head;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }

    public int[] getHead() {
        return head;
    }

    public void setHead(int[] head) {
        this.head = head;
    }
}
