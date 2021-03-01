package com.sptest.service;

import java.util.*;

import com.sptest.pojo.kScores;
import net.sf.javaml.clustering.*;
import net.sf.javaml.clustering.evaluation.AICScore;
import net.sf.javaml.clustering.evaluation.BICScore;
import net.sf.javaml.clustering.evaluation.ClusterEvaluation;
import net.sf.javaml.clustering.evaluation.SumOfSquaredErrors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.*;
import org.springframework.web.bind.annotation.RequestParam;

abstract public class clusterCompute {
    public static int[] compute(List<List<String>> data, int k, int[] index){
        int rowNum = data.size();
        int colNum = index.length;
        float[][] dataArray = new float[rowNum][colNum];
        float[][] meansArray = new float[k][colNum];
        int[] result = new int[rowNum];

        for(int i =0; i<rowNum;i++){
            for (int j=0;j<colNum;j++){
                dataArray[i][j] = Float.parseFloat(data.get(i).get(index[j]));
            }
        }

        Random rand = new Random();

        //取初始簇中心：方法一
//        Vector<Integer> vector = new Vector<>();
//        int count = 0;
//        while (count<k){
//            int number = rand.nextInt(rowNum);
//            if(!vector.contains(number)){
//                vector.add(number);
//                meansArray[count] = dataArray[number];
//                count++;
//            }
//        }

        //取初始簇中心：方法二
        int remainIndex;
        int remainNum;
        List<Integer> remain = new ArrayList<>();
        for(int i=0;i<rowNum;i++){
            remain.add(i);
        }
        remainNum = remain.size();
        int number;
        for(int i=0;i<k;i++){
            remainIndex = rand.nextInt(remainNum);
            number = remain.get(remainIndex);
            meansArray[i] = dataArray[number];
            remain.remove(remainIndex);
            remainNum--;
        }

        //取初始簇中心：方法三：强制均匀
//        int meanClusterNum = (int)(rowNum/k);
//        int baseIndex = 0;
//        int number;
//        for(int i=0;i<k;i++){
//            number = rand.nextInt(meanClusterNum) + baseIndex;
//            baseIndex+=meanClusterNum;
//            meansArray[i] = dataArray[number];
//        }


        float changeSum;
        int near;
        float distance;
        float tempDistance;
        float[][] tempSum = new float[k][colNum];
        for(int i=0;i<k;i++){
            for(int j = 0;j<colNum;j++){
                tempSum[i][j] = 0;
            }
        }
        int[] pointNum = new int[k];
        for(int i=0;i<k;i++){
            pointNum[i]=0;
        }
        float[][] newMeans = new float[k][colNum];
        while(true){
            changeSum = 0;
            for(int i=0;i<rowNum;i++){
                near = 0;
                distance = Float.MAX_VALUE;
                for(int j=0;j<k;j++){
                    tempDistance = computeDistance(dataArray[i],meansArray[j],colNum);
                    if(tempDistance<distance){
                        distance = tempDistance;
                        near = j;
                    }
                }
                result[i] = near;
            }

            for(int i=0;i<rowNum;i++){
                pointNum[result[i]]++;
                for(int j=0;j<colNum;j++){
                    tempSum[result[i]][j]+=dataArray[i][j];
                }
            }

            for(int i = 0;i<k;i++){
                for(int j=0;j<colNum;j++){
                    newMeans[i][j] = tempSum[i][j]/pointNum[i];
                }
            }

            for(int i=0;i<k;i++){
                changeSum+=computeDistance(meansArray[i],newMeans[i],colNum);
                meansArray[i] = newMeans[i];
            }

            if(changeSum==0){
                break;
            }
        }

        return result;

    }

    public static float computeDistance(float[] a, float[] b, int length){
        float dist = 0;
        for(int i=0;i<length;i++){
            dist+=(a[i]-b[i])*(a[i]-b[i]);
        }
        return dist;
    }

//    public static int[] KMeansJavaML(List<List<String>> data, int k, int[] index){
//        int rowNum = data.size();
//        int colNum = index.length;
//        double[] dataArray = new double[colNum];
//        int[] result = new int[rowNum];
//
//        HashMap<Instance,Integer> map = new HashMap<>();
//
//        Dataset rawData = new DefaultDataset();
//
//        for(int i =0; i<rowNum;i++){
//            for (int j=0;j<colNum;j++){
//                dataArray[j] = Double.parseDouble(data.get(i).get(index[j]));
//            }
//            Instance instance = new DenseInstance(dataArray);
//            rawData.add(instance);
//            map.put(instance,i);
//        }
//
//        Clusterer km = new KMeans(k);
//        Dataset[] clusters = km.cluster(rawData);
//
//        int numClusters = clusters.length;
//        for(int i=0;i<numClusters;i++){
//            for (Instance instance : clusters[i]) {
//                result[map.get(instance)] = i;
//            }
//        }
//
////        for (int i : result) {
////            System.out.println(i);
////        }
//
//        return result;
//    }

    public static int[] clusteringJavaML(List<List<String>> data, String clusterAlg, int kValue,
                                         double epsilon, int minpoints,
                                         int iterations, String dmString,
                                         int[] selectedIndex, int repeats,
                                         String clusterEvaluationString){
        int rowNum = data.size();
        int colNum = selectedIndex.length;
        double[] dataArray = new double[colNum];
        int[] result = new int[rowNum];

        HashMap<Instance,Integer> map = new HashMap<>();

        Dataset rawData = new DefaultDataset();

        for(int i =0; i<rowNum;i++){
            for (int j=0;j<colNum;j++){
                dataArray[j] = Double.parseDouble(data.get(i).get(selectedIndex[j]));
            }
            Instance instance = new DenseInstance(dataArray);
            rawData.add(instance);
            map.put(instance,i);
        }

        Dataset[] clusters;
        DistanceMeasure dm;
        Clusterer clusterer = null;

        switch (dmString){
            case "Euclidean":{
                dm = new EuclideanDistance();
                break;
            }
            case "Minkowski":{
                dm = new MinkowskiDistance();
                break;
            }
            case "Norm":{
                dm = new NormDistance();
                break;
            }
            case "Manhattan":{
                dm = new ManhattanDistance();
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + dmString);
        }

        if(clusterAlg.equals("KMeans")){
            System.out.println("this is KMeans");
            clusterer = new KMeans(kValue,iterations,dm);
        }else if(clusterAlg.equals("KMedoids")){
            System.out.println("this is KMedoids");
            clusterer = new KMedoids(kValue,iterations,dm);
        }else if(clusterAlg.equals("DBSC")){
            System.out.println("this is DBSC");
            clusterer = new DensityBasedSpatialClustering(epsilon,minpoints,dm);
        }else if(clusterAlg.equals("MultiKMeans")){
            System.out.println("this is MultiKMeans");
            ClusterEvaluation clusterEvaluation;
            switch (clusterEvaluationString){
                case "AICScore":{
                    clusterEvaluation = new AICScore();
                }
                case "BICScore":{
                    clusterEvaluation = new BICScore();
                }
                case "SOSE":{
                    clusterEvaluation = new SumOfSquaredErrors();
                }
                break;
                default:
                    throw new IllegalStateException("Unexpected value: " + clusterEvaluationString);
            }
            clusterer = new MultiKMeans(kValue,iterations,repeats,dm,clusterEvaluation);
        }

        if(clusterer!=null){
            clusters = clusterer.cluster(rawData);
            int numClusters = clusters.length;
            for(int i=0;i<numClusters;i++){
                for (Instance instance : clusters[i]) {
                    result[map.get(instance)] = i;
                }
            }
        }

        return result;
    }

    public static kScores getKScores(List<List<String>> data, String clusterAlg, int[] kList,
                                     int iterations, String dmString,
                                     int[] selectedIndex, int repeats,
                                     String clusterEvaluationString){
        int rowNum = data.size();
        int colNum = selectedIndex.length;
        double[] dataArray = new double[colNum];
        int[] result = new int[rowNum];

        Dataset rawData = new DefaultDataset();

        for (List<String> datum : data) {
            for (int j = 0; j < colNum; j++) {
                dataArray[j] = Double.parseDouble(datum.get(selectedIndex[j]));
            }
            Instance instance = new DenseInstance(dataArray);
            rawData.add(instance);
        }

        Dataset[] clusters;
        DistanceMeasure dm;
        Clusterer clusterer = null;

        switch (dmString){
            case "Euclidean":{
                dm = new EuclideanDistance();
                break;
            }
            case "Minkowski":{
                dm = new MinkowskiDistance();
                break;
            }
            case "Norm":{
                dm = new NormDistance();
                break;
            }
            case "Manhattan":{
                dm = new ManhattanDistance();
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + dmString);
        }

        kScores scores;
        int kNum = kList.length;
        double[] AICScores = new double[kNum];
        double[] BICScores = new double[kNum];
        AICScore aic = new AICScore();
        BICScore bic = new BICScore();

        switch (clusterAlg) {
            case "KMeans":
                System.out.println("this is KMeans");

                for (int i = 0; i < kNum; i++) {
                    clusterer = new KMeans(kList[i], iterations, dm);
                    clusters = clusterer.cluster(rawData);
                    AICScores[i] = aic.score(clusters);
                    BICScores[i] = bic.score(clusters);
                }

                break;
            case "KMedoids":
                System.out.println("this is KMedoids");
                for (int i = 0; i < kNum; i++) {
                    clusterer = new KMedoids(kList[i], iterations, dm);
                    clusters = clusterer.cluster(rawData);
                    AICScores[i] = aic.score(clusters);
                    BICScores[i] = bic.score(clusters);
                }
                break;
            case "MultiKMeans":
                System.out.println("this is MultiKMeans");
                ClusterEvaluation clusterEvaluation;
                switch (clusterEvaluationString) {
                    case "AICScore": {
                        clusterEvaluation = new AICScore();
                    }
                    case "BICScore": {
                        clusterEvaluation = new BICScore();
                    }
                    case "SOSE": {
                        clusterEvaluation = new SumOfSquaredErrors();
                    }
                    break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + clusterEvaluationString);
                }
                for (int i = 0; i < kNum; i++) {
                    clusterer = new MultiKMeans(kList[i], iterations, repeats, dm, clusterEvaluation);
                    clusters = clusterer.cluster(rawData);
                    AICScores[i] = aic.score(clusters);
                    BICScores[i] = bic.score(clusters);
                }
                break;
        }

        System.out.println("AICScore:");
        for (double aicScore : AICScores) {
            System.out.println(aicScore);
        }
        System.out.println("BICScore:");
        for (double bicScore : BICScores) {
            System.out.println(bicScore);
        }
        scores = new kScores(AICScores,BICScores);
        return scores;
    }
}
