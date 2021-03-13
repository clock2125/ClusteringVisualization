package com.sptest.service;

import java.util.*;

import com.sptest.pojo.clusterCenter;
import com.sptest.pojo.kScores;
import com.sptest.response.clusterResult;
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
import net.sf.javaml.filter.normalize.NormalizeMidrange;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

public class clusterCompute {
    Dataset rawData;
    Dataset[] clusters;
    List<clusterCenter> centers;
    int rowNum;
    int colNum;
    HashMap<Integer,Integer> map;

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getColNum() {
        return colNum;
    }

    public void setColNum(int colNum) {
        this.colNum = colNum;
    }

    public clusterCompute(List<List<String>> data, int[] selectedIndex) {
        rowNum = data.size();
        colNum = selectedIndex.length;
        double[] dataArray = new double[colNum];

        map = new HashMap<>();

        rawData = new DefaultDataset();

        for(int i =0; i<rowNum;i++){
            for (int j=0;j<colNum;j++){
                dataArray[j] = Double.parseDouble(data.get(i).get(selectedIndex[j]));
            }
            Instance instance = new DenseInstance(dataArray);
            rawData.add(instance);
            map.put(System.identityHashCode(instance),i);
        }
    }

    public List<clusterCenter> getCenters() {
        return centers;
    }

    public void setCenters(List<clusterCenter> centers) {
        this.centers = centers;
    }

    public Dataset getRawData() {
        return rawData;
    }

    public void setRawData(Dataset rawData) {
        this.rawData = rawData;
    }

    public Dataset[] getClusters() {
        return clusters;
    }

    public void setClusters(Dataset[] clusters) {
        this.clusters = clusters;
    }

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

    public clusterResult clusteringJavaML(
                                                 String clusterAlg,
                                                 int kValue,
                                                 int KMin,
                                                 int KMax,
                                                 int iterations,
                                                 String dmString,
                                                 int repeats,
                                                 String clusterEvaluationString,
                                                 HttpSession session){

        int[] result = new int[rowNum];
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

        ClusterEvaluation ce;
        switch (clusterEvaluationString){
            case "AICScore":{
                ce = new AICScore();
            }
            case "BICScore":{
                ce = new BICScore();
            }
            case "SOSE":{
                ce = new SumOfSquaredErrors();
            }
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + clusterEvaluationString);
        }

        if(clusterAlg.equals("KMeans")){
            System.out.println("this is KMeans");
            clusterer = new KMeans(kValue,iterations,dm);
        }else if(clusterAlg.equals("KMedoids")){
            System.out.println("this is KMedoids");
            clusterer = new KMedoids(kValue,iterations,dm);
        }else if(clusterAlg.equals("MultiKMeans")){
            System.out.println("this is MultiKMeans");
            clusterer = new MultiKMeans(kValue,iterations,repeats,dm,ce);
        }else if(clusterAlg.equals("FarthestFirst")){
            clusterer = new FarthestFirst(kValue, dm);
        }else if(clusterAlg.equals("IterativeFarthestFirst")){
            clusterer = new IterativeFarthestFirst(KMin,KMax,dm,ce);
        }else if(clusterAlg.equals("IterativeKMeans")){
            clusterer = new IterativeKMeans(KMin,KMax,iterations,dm,ce);
        }else if(clusterAlg.equals("IterativeMultiKMeans")){
            clusterer = new IterativeMultiKMeans(KMin,KMax,iterations,repeats,dm,ce);
        }

        if(clusterer!=null){
            session.setAttribute("dataSet", rawData);

            clusters = clusterer.cluster(rawData);
            session.setAttribute("clusters", clusters);

            List<double[]> mapData = new ArrayList<>();
            int clusterNum = clusters.length;
            int[] itemNum = new int[clusterNum];
            Instance tempInstance;
            NormalizeMidrange normal = new NormalizeMidrange();
            normal.build(rawData);

            int numClusters = clusters.length;


            int i = 0;
            centers = new ArrayList<>();
            clusterCenter tempCenter;


            for (Dataset cluster : clusters) {
                tempInstance = new DenseInstance(new double[colNum]);
                itemNum[i] = cluster.size();

                for (Instance instance : cluster) {
                    tempInstance = tempInstance.add(instance);
                }

                tempInstance.divide(itemNum[i]);

//                for(int j = 0;j<colNum;j++){
//                    tempDistance += tempInstance.value(j)/itemNum[i];
//                }

                tempCenter = new clusterCenter(tempInstance.values(),i,itemNum[i]);
                centers.add(tempCenter);

                i++;
            }

            Collections.sort(centers);
            session.setAttribute("centers", centers);

            for(i=0;i<numClusters;i++){
                for (Instance instance : clusters[centers.get(i).getIndex()]) {
                    result[map.get(System.identityHashCode(instance))] = i+1;
                }
            }

            for (i=0;i<clusterNum;i++) {
                Dataset tempCluster = clusters[centers.get(i).getIndex()];
                tempInstance = new DenseInstance(new double[colNum]);
                itemNum[i] = tempCluster.size();
                normal.filter(tempCluster);
                for (Instance instance : tempCluster) {
                    tempInstance = tempInstance.add(instance);
                }


                for(int j = 0;j<colNum;j++){
                    double[] tempDouble = new double[3];
                    tempDouble[0] = i;
                    tempDouble[1] = j;
                    tempDouble[2] = tempInstance.value(j)/itemNum[i];
                    mapData.add(tempDouble);
                }
            }

            return new clusterResult(result, mapData, itemNum, numClusters);
        }

        return null;
    }

    public clusterResult clusteringJavaML(
                                                 String clusterAlg,
                                                 double sig,
                                                 boolean normalize,
                                                 HttpSession session){

        int[] result = new int[rowNum];
        Clusterer clusterer = null;


        if(clusterAlg.equals("AQBC")){
            System.out.println("this is AQBC");
            clusterer = new AQBC(sig,normalize);
        }
        if(clusterer!=null){
            session.setAttribute("dataSet", rawData);

            clusters = clusterer.cluster(rawData);
            session.setAttribute("clusters", clusters);


            List<String> label = new ArrayList<>();
            List<double[]> mapData = new ArrayList<>();
            int clusterNum = clusters.length;
            int[] itemNum = new int[clusterNum];
            Instance tempInstance;
            NormalizeMidrange normal = new NormalizeMidrange();
            normal.build(rawData);

            int numClusters = clusters.length;


            int i = 0;
            centers = new ArrayList<>();
            clusterCenter tempCenter;


            for (Dataset cluster : clusters) {
                tempInstance = new DenseInstance(new double[colNum]);
                itemNum[i] = cluster.size();

                for (Instance instance : cluster) {
                    tempInstance = tempInstance.add(instance);
                }

                tempInstance.divide(itemNum[i]);

//                for(int j = 0;j<colNum;j++){
//                    tempDistance += tempInstance.value(j)/itemNum[i];
//                }

                tempCenter = new clusterCenter(tempInstance.values(),i,itemNum[i]);
                centers.add(tempCenter);

                i++;
            }

            Collections.sort(centers);
            session.setAttribute("centers", centers);

            for(i=0;i<numClusters;i++){
                for (Instance instance : clusters[centers.get(i).getIndex()]) {
                    result[map.get(System.identityHashCode(instance))] = i+1;
                }
            }

            for (i=0;i<clusterNum;i++) {
                Dataset tempCluster = clusters[centers.get(i).getIndex()];
                tempInstance = new DenseInstance(new double[colNum]);
                itemNum[i] = tempCluster.size();
                normal.filter(tempCluster);
                for (Instance instance : tempCluster) {
                    tempInstance = tempInstance.add(instance);
                }


                for(int j = 0;j<colNum;j++){
                    double[] tempDouble = new double[3];
                    tempDouble[0] = i;
                    tempDouble[1] = j;
                    tempDouble[2] = tempInstance.value(j)/itemNum[i];
                    mapData.add(tempDouble);
                }
            }

            return new clusterResult(result, mapData, itemNum, numClusters);
        }

        return null;
    }

    public clusterResult clusteringJavaML(
                                                 String clusterAlg,
                                                 String dmString,
                                                 double epsilon,
                                                 int minpoints,
                                                 HttpSession session){

        int[] result = new int[rowNum];
        Clusterer clusterer = null;

        DistanceMeasure dm;

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

        if(clusterAlg.equals("DBSC")){
            System.out.println("this is DBSC");
            clusterer = new DensityBasedSpatialClustering(epsilon,minpoints,dm);
        }
        if(clusterer!=null){
            session.setAttribute("dataSet", rawData);

            clusters = clusterer.cluster(rawData);
            session.setAttribute("clusters", clusters);


            List<String> label = new ArrayList<>();
            List<double[]> mapData = new ArrayList<>();
            int clusterNum = clusters.length;
            int[] itemNum = new int[clusterNum];
            Instance tempInstance;
            NormalizeMidrange normal = new NormalizeMidrange();
            normal.build(rawData);

            int numClusters = clusters.length;


            int i = 0;
            centers = new ArrayList<>();
            clusterCenter tempCenter;


            for (Dataset cluster : clusters) {
                tempInstance = new DenseInstance(new double[colNum]);
                itemNum[i] = cluster.size();

                for (Instance instance : cluster) {
                    tempInstance = tempInstance.add(instance);
                }

                tempInstance.divide(itemNum[i]);

//                for(int j = 0;j<colNum;j++){
//                    tempDistance += tempInstance.value(j)/itemNum[i];
//                }

                tempCenter = new clusterCenter(tempInstance.values(),i,itemNum[i]);
                centers.add(tempCenter);

                i++;
            }

            Collections.sort(centers);
            session.setAttribute("centers", centers);

            for(i=0;i<numClusters;i++){
                for (Instance instance : clusters[centers.get(i).getIndex()]) {
                    result[map.get(System.identityHashCode(instance))] = i+1;
                }
            }

            for (i=0;i<clusterNum;i++) {
                Dataset tempCluster = clusters[centers.get(i).getIndex()];
                tempInstance = new DenseInstance(new double[colNum]);
                itemNum[i] = tempCluster.size();
                normal.filter(tempCluster);
                for (Instance instance : tempCluster) {
                    tempInstance = tempInstance.add(instance);
                }


                for(int j = 0;j<colNum;j++){
                    double[] tempDouble = new double[3];
                    tempDouble[0] = i;
                    tempDouble[1] = j;
                    tempDouble[2] = tempInstance.value(j)/itemNum[i];
                    mapData.add(tempDouble);
                }
            }

            return new clusterResult(result, mapData, itemNum, numClusters);
        }

        return null;
    }

    public clusterResult clusteringJavaML(
                                                 String clusterAlg,
                                                 double acuity,
                                                 double cutoff,
                                                 HttpSession session){

        int[] result = new int[rowNum];

        Clusterer clusterer = null;


        if(clusterAlg.equals("CobWeb")){
            System.out.println("this is CobWeb");
            clusterer = new Cobweb(acuity,cutoff);
        }
        if(clusterer!=null){
            session.setAttribute("dataSet", rawData);

            clusters = clusterer.cluster(rawData);
            session.setAttribute("clusters", clusters);


            List<String> label = new ArrayList<>();
            List<double[]> mapData = new ArrayList<>();
            int clusterNum = clusters.length;
            int[] itemNum = new int[clusterNum];
            Instance tempInstance;
            NormalizeMidrange normal = new NormalizeMidrange();
            normal.build(rawData);

            int numClusters = clusters.length;


            int i = 0;
            centers = new ArrayList<>();
            clusterCenter tempCenter;


            for (Dataset cluster : clusters) {
                tempInstance = new DenseInstance(new double[colNum]);
                itemNum[i] = cluster.size();

                for (Instance instance : cluster) {
                    tempInstance = tempInstance.add(instance);
                }

                tempInstance.divide(itemNum[i]);

//                for(int j = 0;j<colNum;j++){
//                    tempDistance += tempInstance.value(j)/itemNum[i];
//                }

                tempCenter = new clusterCenter(tempInstance.values(),i,itemNum[i]);
                centers.add(tempCenter);

                i++;
            }

            Collections.sort(centers);
            session.setAttribute("centers", centers);

            for(i=0;i<numClusters;i++){
                for (Instance instance : clusters[centers.get(i).getIndex()]) {
                    result[map.get(System.identityHashCode(instance))] = i+1;
                }
            }

            for (i=0;i<clusterNum;i++) {
                Dataset tempCluster = clusters[centers.get(i).getIndex()];
                tempInstance = new DenseInstance(new double[colNum]);
                itemNum[i] = tempCluster.size();
                normal.filter(tempCluster);
                for (Instance instance : tempCluster) {
                    tempInstance = tempInstance.add(instance);
                }


                for(int j = 0;j<colNum;j++){
                    double[] tempDouble = new double[3];
                    tempDouble[0] = i;
                    tempDouble[1] = j;
                    tempDouble[2] = tempInstance.value(j)/itemNum[i];
                    mapData.add(tempDouble);
                }
            }

            return new clusterResult(result, mapData, itemNum, numClusters);
        }

        return null;
    }

    public double[] getKScores( String clusterAlg, int[] kList,
                                     int iterations, String dmString, int repeats,
                                     String clusterEvaluationString){

        int[] result = new int[rowNum];

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

        int kNum = kList.length;
        double[] SSEScores = new double[kNum];
        SumOfSquaredErrors sse = new SumOfSquaredErrors();

        switch (clusterAlg) {
            case "FarthestFirst":
                System.out.println("this is FarthestFirst");

                for (int i = 0; i < kNum; i++) {
                    clusterer = new FarthestFirst(kList[i], dm);
                    clusters = clusterer.cluster(rawData);
                    SSEScores[i] = sse.score(clusters);
                }

                break;
            case "KMeans":
                System.out.println("this is KMeans");

                for (int i = 0; i < kNum; i++) {
                    clusterer = new KMeans(kList[i], iterations, dm);
                    clusters = clusterer.cluster(rawData);
                    SSEScores[i] = sse.score(clusters);
                }

                break;
            case "KMedoids":
                System.out.println("this is KMedoids");
                for (int i = 0; i < kNum; i++) {
                    clusterer = new KMedoids(kList[i], iterations, dm);
                    clusters = clusterer.cluster(rawData);
                    SSEScores[i] = sse.score(clusters);
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
                    SSEScores[i] = sse.score(clusters);
                }
                break;
        }

//        System.out.println("AICScore:");
//        for (double aicScore : AICScores) {
//            System.out.println(aicScore);
//        }
//        System.out.println("BICScore:");
//        for (double bicScore : BICScores) {
//            System.out.println(bicScore);
//        }
        return SSEScores;
    }
}
