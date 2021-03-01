package com.sptest.service;

import com.sptest.pojo.attrStatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class attrStatisticCompute {
    public static attrStatistics compute(List<List<String>> data, int index){
        int rowNum = data.size();

        final int disNum = 10;

        float mean;
        float mid;
        float max = -Float.MAX_VALUE;
        float min = Float.MAX_VALUE;
        float var;
        float std;

        int[] distribution = new int[disNum];

        float[] col = new float[rowNum];
        int i;
        for(i=0;i<disNum;i++){
            distribution[i] = 0;
        }
        float sum = 0;
        float varSum = 0;
        float tempData;
        for(i=0;i<rowNum;i++){
            tempData = Float.parseFloat(data.get(i).get(index));
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

        std = (float) Math.sqrt(var);

        Arrays.sort(col);
        int midPlace = rowNum/2;
        mid = col[midPlace];

        int j;
        i = 0;
        float step = (max-min)/disNum;
        float board = min+step;
        for(j=0;j<rowNum&&i<disNum;j++){
            if(col[j]<=board){
                distribution[i]++;
            }
            else{
                board+=step;
                i++;
            }
        }



        return new attrStatistics(mean,mid,max,min,var,std,distribution);
    }
}
