package com.sptest.controller;

import com.alibaba.fastjson.JSON;
import com.sptest.pojo.*;
import com.sptest.response.clusterResult;
import com.sptest.response.deleteResponse;
import com.sptest.response.globalCovItem;
import com.sptest.response.globalCovResponse;
import com.sptest.service.attrStatisticCompute;
import com.sptest.service.csvResolver;
import com.sptest.service.clusterCompute;
import com.sptest.utils.PCA;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.filter.normalize.NormalizeMidrange;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@CrossOrigin
@RestController
public class fileController {
    csvData csvdata;
    clusterCompute clustering;

    @PostMapping("/fileupload")
    @ResponseBody
    public csvData fileUpload(@RequestBody MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        request.setCharacterEncoding("UTF-8");

//        csvResolver csvResolver = null;
        System.out.println("上传文件是否为空："+file.isEmpty());

//        csvData userRoleLists = null;
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            System.out.println("file received " + fileName);

            csvdata = csvResolver.readCSV(file);
//            HttpSession session = request.getSession();
//            session.setAttribute("data", csvdata.getData());
//            if(csvdata!=null) {
//                if(csvdata.getHead()!=null) {
//                    for (String s : csvdata.getHead()) {
//                        System.out.print(s + "&");
//                    }
//                    System.out.print("\n");
//                }
//
//                for (List<String> datum : csvdata.getData()) {
//                    for (String s : datum) {
//                        System.out.print(s + "&");
//                    }
//                    System.out.print("\n");
//                }
//            }
        }

        return csvdata;

    }

//    @PostMapping("/cluster")
//    @ResponseBody
//    public clusterResult cluster(@RequestParam(name = "clusterAlg") String clusterAlg, @RequestParam(name = "kValue") int kValue,
//                                 @RequestParam(name = "epsilon") double epsilon, @RequestParam(name = "minpoints") int minpoints,
//                                 @RequestParam(name = "iterations") int iterations, @RequestParam(name = "distanceMeasure") String dmString,
//                                 @RequestParam(name = "selectedIndex") int[] selectedIndex, @RequestParam(name = "repeats") int repeats,
//                                 @RequestParam(name = "ClusterEvaluation") String clusterEvaluationString, HttpServletRequest request, HttpServletResponse response){
//
//        List<List<String>> data = csvdata.getData();
////        clusterSetting setting = JSON.parseObject(cSetting,clusterSetting.class);
////        System.out.println(clusterAlg);
////        System.out.println(kValue);
////        System.out.println(epsilon);
////        System.out.println(minpoints);
////        System.out.println(iterations);
////        System.out.println(dmString);
//        for (int index : selectedIndex) {
//            System.out.print(index+",");
//        }
////        for (List<String> datum : data) {
////            System.out.print("[");
////            for (int i : index) {
////                System.out.print(datum.get(i)+",");
////            }
////            System.out.print("]\n");
////        }
//
//
//        return clusterCompute.clusteringJavaML(data,clusterAlg,kValue,epsilon,minpoints,iterations,dmString,selectedIndex,repeats,clusterEvaluationString);
//    }

    @PostMapping("/cluster/CobWeb")
    @ResponseBody
    public clusterResult CobWebCluster(@RequestParam(name = "clusterAlg") String clusterAlg,
                                       @RequestParam(name = "selectedIndex") int[] selectedIndex,
                                       @RequestParam(name = "acuity") double acuity,
                                       @RequestParam(name = "cutoff") double cutoff,
                                       HttpServletRequest request,
                                       HttpServletResponse response){
        HttpSession session = request.getSession();
//        List<List<String>> data = (List<List<String>>) session.getAttribute("data");
        List<List<String>> data = csvdata.getData();
        clustering = new clusterCompute(data, selectedIndex);
        return clustering.clusteringJavaML(clusterAlg,acuity,cutoff,session);
    }

    @PostMapping("/cluster/DBSC")
    @ResponseBody
    public clusterResult DBSCCluster(@RequestParam(name = "clusterAlg") String clusterAlg,
                                     @RequestParam(name = "distanceMeasure") String dmString,
                                     @RequestParam(name = "selectedIndex") int[] selectedIndex,
                                     @RequestParam(name = "epsilon") double epsilon,
                                     @RequestParam(name = "minpoints") int minpoints,
                                     HttpServletRequest request, HttpServletResponse response){
        List<List<String>> data = csvdata.getData();
        HttpSession session = request.getSession();
//        List<List<String>> data = (List<List<String>>) session.getAttribute("data");
        clustering = new clusterCompute(data, selectedIndex);
        return clustering.clusteringJavaML(clusterAlg,dmString,epsilon,minpoints,session);
    }

    @PostMapping("/cluster/AQBC")
    @ResponseBody
    public clusterResult AQBCCluster(@RequestParam(name = "clusterAlg") String clusterAlg,
                                     @RequestParam(name = "selectedIndex") int[] selectedIndex,
                                     @RequestParam(name = "sig") double sig,
                                     @RequestParam(name = "normalize") boolean normalize,
                                     HttpServletRequest request, HttpServletResponse response){
        List<List<String>> data = csvdata.getData();
        HttpSession session = request.getSession();
//        List<List<String>> data = (List<List<String>>) session.getAttribute("data");
        clustering = new clusterCompute(data, selectedIndex);
        return clustering.clusteringJavaML(clusterAlg,sig,normalize,session);
    }

    @PostMapping("/cluster/PartitionBasedClustering")
    @ResponseBody
    public clusterResult PartitionBasedCluster(@RequestParam(name = "clusterAlg") String clusterAlg, @RequestParam(name = "kValue") int kValue,
                                               @RequestParam(name = "KMin") int KMin, @RequestParam(name = "KMax") int KMax,
                                               @RequestParam(name = "iterations") int iterations, @RequestParam(name = "distanceMeasure") String dmString,
                                               @RequestParam(name = "selectedIndex") int[] selectedIndex, @RequestParam(name = "repeats") int repeats,
                                               @RequestParam(name = "ClusterEvaluation") String clusterEvaluationString, HttpServletRequest request, HttpServletResponse response){
        List<List<String>> data = csvdata.getData();
        HttpSession session = request.getSession();
//        System.out.println(session.getAttribute("csvData"));
//        List<List<String>> data = (List<List<String>>) session.getAttribute("data");
        clustering = new clusterCompute(data, selectedIndex);
        return clustering.clusteringJavaML(clusterAlg,kValue,KMin,KMax,iterations,dmString,repeats,clusterEvaluationString,session);
    }

    @GetMapping("/statistics/{attrIndex}")
    @ResponseBody
    public attrStatistics statistics(@PathVariable("attrIndex") int attrIndex, HttpServletRequest request, HttpServletResponse response){
        System.out.println("attrIndex = "+attrIndex);
        List<List<String>> data = csvdata.getData();
        HttpSession session = request.getSession();
//        List<List<String>> data = (List<List<String>>) session.getAttribute("data");
        return attrStatisticCompute.compute(data, attrIndex);
    }

    @GetMapping("/GlobalCov")
    @ResponseBody
    public List<globalCovItem> getGlobalCov(HttpServletRequest request, HttpServletResponse response){
        List<globalCovItem> items = new ArrayList<>();
        List<List<String>> data = csvdata.getData();
        HttpSession session = request.getSession();
//        List<List<String>> data = (List<List<String>>) session.getAttribute("data");
        int colNum = data.get(0).size();
        attrStatistics temp;
        globalCovItem tempItem;
        for(int i=0;i<colNum;i++){
            temp = attrStatisticCompute.compute(data,i);
            for(int j=i;j<colNum-1;j++){
                String tempString = "属性"+i+"-"+temp.getCovLabelList().get(j);
                tempItem = new globalCovItem(tempString, temp.getCovDataList()[j]);
                items.add(tempItem);
            }
        }
        Collections.sort(items);
        return items;
    }

    @PostMapping("/DimensionReduce")
    @ResponseBody
    public float[][] DimensionReduce(@RequestParam(name = "drAlg") String drAlg,@RequestParam(name = "selectedIndex") int[] selectedIndex,HttpServletRequest request, HttpServletResponse response){
        List<List<String>> data = csvdata.getData();
        HttpSession session = request.getSession();
//        List<List<String>> data = (List<List<String>>) session.getAttribute("data");
        System.out.println(drAlg);
        if(drAlg.equals("PCA")){
            return PCA.runReduction(data, selectedIndex);
        }
        return null;
    }

    @PostMapping("/kScore")
    @ResponseBody
    public double[] getKScore(@RequestParam(name = "kList") int[] kList,@RequestParam(name = "clusterAlg") String clusterAlg,
                             @RequestParam(name = "iterations") int iterations, @RequestParam(name = "distanceMeasure") String dmString,
                             @RequestParam(name = "selectedIndex") int[] selectedIndex,@RequestParam(name = "repeats") int repeats,
                             @RequestParam(name = "ClusterEvaluation") String clusterEvaluationString,HttpServletRequest request, HttpServletResponse response){
        List<List<String>> data = csvdata.getData();
        HttpSession session = request.getSession();
//        List<List<String>> data = (List<List<String>>) session.getAttribute("data");
        clusterCompute cc = new clusterCompute(data,selectedIndex);
        return cc.getKScores(clusterAlg,kList,iterations,dmString,repeats,clusterEvaluationString);
    }

    @DeleteMapping("/DeleteRow/{rowIndex}/{clusterIndex}")
    @ResponseBody
    public deleteResponse deleteRow(@PathVariable("rowIndex") int rowIndex,@PathVariable("clusterIndex") int clusterIndex , HttpServletRequest request, HttpServletResponse response){


        csvdata.getData().remove(rowIndex);
        Dataset rawData = clustering.getRawData();

        Dataset[] clusters = clustering.getClusters();
        List<clusterCenter> centerList = clustering.getCenters();

        Instance deletedInstance = rawData.instance(rowIndex);
        rawData.remove(rowIndex);

//        for (Dataset cluster : clusters) {
//            try{
//                cluster.remove(deletedInstance);
//            }catch (Exception e){
//                continue;
//            }
//        }

        if(clusterIndex != 0){
            clusterIndex--;
        }else{
            return null;
        }

        clusters[centerList.get(clusterIndex).getIndex()].remove(deletedInstance);

        int clusterNum = clustering.getClusters().length;
//        System.out.println(clusterNum);
        int colNum = clustering.getColNum();
        int[] itemNum = new int[clusterNum];
        List<double[]> mapData = new ArrayList<>();

//        for (clusterCenter clusterCenter : centerList) {
//            System.out.println(clusterCenter.getIndex());
//        }

        Instance tempInstance;
        for (int i=0;i<clusterNum;i++) {
            Dataset tempCluster = clusters[centerList.get(i).getIndex()];
            tempInstance = new DenseInstance(new double[colNum]);
            itemNum[i] = tempCluster.size();
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

        return new deleteResponse(mapData,itemNum);
    }

}
