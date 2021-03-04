package com.sptest.service;

import com.sptest.pojo.attrStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class attrStatisticCompute {
    public static attrStatistics compute(List<List<String>> data, int index){
        int rowNum = data.size();
        int colNum = data.get(0).size();

        final int disNum = 10;

        double mean;
        double mid;
        double max = -Float.MAX_VALUE;
        double min = Float.MAX_VALUE;
        double var;
        double std;

        int[] distribution = new int[disNum];

        double[] col = new double[rowNum];
        int i;
        for(i=0;i<disNum;i++){
            distribution[i] = 0;
        }
        double sum = 0;
        double varSum = 0;
        double tempData;
        for(i=0;i<rowNum;i++){
            tempData = Double.parseDouble(data.get(i).get(index));
            col[i] = tempData;
            sum+=tempData;
            varSum+=tempData*tempData;
            if(tempData>max){
                max = tempData;
            }
            if (tempData<min){
                min = tempData;
            }
        }

        mean = sum/rowNum;

        var = varSum/rowNum - mean*mean;

        std =  Math.sqrt(var);

        List<String> covLabelList = new ArrayList<>();
        double[] covDataList = new double[colNum-1];

        double sumY ;
        double meanY;
        double sumY2;
        double meanY2;
        double varY;
        double stdY;
        double sumXY;
        double meanXY;

        int k = 0;
        int j;

        for(i=0;i<colNum;i++){
            sumY = 0;
            sumY2 = 0;
            meanY = 0;
            meanY2 = 0;
            varY = 0;
            stdY = 0;
            sumXY = 0;
            meanXY = 0;
            if(i!=index){
                covLabelList.add("属性"+i);
                for (j = 0;j<rowNum;j++){
                    tempData = Double.parseDouble(data.get(j).get(i));
                    sumXY += tempData*col[j];
                    sumY+=tempData;
                    sumY2+=tempData*tempData;
                }

                meanY = sumY/rowNum;
                meanY2 = sumY2/rowNum;
                meanXY = sumXY/rowNum;
                varY = meanY2 - meanY*meanY;
                stdY = Math.sqrt(varY);

                covDataList[k] = (meanXY-mean*meanY)/(std*stdY);
                k++;

            }
        }

        Arrays.sort(col);
        int midPlace = rowNum/2;
        mid = col[midPlace];

        i = 0;
        double step = (max-min)/disNum;
        double board = min+step;
        for(j=0;j<rowNum&&i<disNum;j++){
            if(col[j]<=board){
                distribution[i]++;
            }
            else{
                board+=step;
                i++;
            }
        }

        return new attrStatistics(mean,mid,max,min,var,std,distribution,covDataList,covLabelList);
    }
}
