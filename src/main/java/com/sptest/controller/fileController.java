package com.sptest.controller;

import com.alibaba.fastjson.JSON;
import com.sptest.pojo.attrStatistics;
import com.sptest.pojo.clusterSetting;
import com.sptest.pojo.csvData;
import com.sptest.pojo.kScores;
import com.sptest.service.attrStatisticCompute;
import com.sptest.service.csvResolver;
import com.sptest.service.clusterCompute;
import com.sptest.utils.PCA;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@CrossOrigin
@RestController
public class fileController {
    float[][] rawData;
    csvData csvdata = null;
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

    @PostMapping("/cluster")
    @ResponseBody
    public int[] cluster(@RequestParam(name = "clusterAlg") String clusterAlg, @RequestParam(name = "kValue") int kValue,
            @RequestParam(name = "epsilon") double epsilon, @RequestParam(name = "minpoints") int minpoints,
            @RequestParam(name = "iterations") int iterations, @RequestParam(name = "distanceMeasure") String dmString,
            @RequestParam(name = "selectedIndex") int[] selectedIndex,@RequestParam(name = "repeats") int repeats,
            @RequestParam(name = "ClusterEvaluation") String clusterEvaluationString,HttpServletRequest request, HttpServletResponse response){

        List<List<String>> data = csvdata.getData();
//        clusterSetting setting = JSON.parseObject(cSetting,clusterSetting.class);
//        System.out.println(clusterAlg);
//        System.out.println(kValue);
//        System.out.println(epsilon);
//        System.out.println(minpoints);
//        System.out.println(iterations);
//        System.out.println(dmString);
        for (int index : selectedIndex) {
            System.out.print(index+",");
        }
//        for (List<String> datum : data) {
//            System.out.print("[");
//            for (int i : index) {
//                System.out.print(datum.get(i)+",");
//            }
//            System.out.print("]\n");
//        }

        if(clusterAlg.equals("myKMeans")){
            return clusterCompute.compute(data,kValue,selectedIndex);
        }else {
            return clusterCompute.clusteringJavaML(data,clusterAlg,kValue,epsilon,minpoints,iterations,dmString,selectedIndex,repeats,clusterEvaluationString);
        }
    }

    @GetMapping("/statistics/{attrIndex}")
    @ResponseBody
    public attrStatistics statistics(@PathVariable("attrIndex") int attrIndex, HttpServletRequest request, HttpServletResponse response){
        System.out.println("attrIndex = "+attrIndex);
        List<List<String>> data = csvdata.getData();
        return attrStatisticCompute.compute(data, attrIndex);
    }

    @PostMapping("/DimensionReduce")
    @ResponseBody
    public float[][] DimensionReduce(@RequestParam(name = "drAlg") String drAlg,@RequestParam(name = "selectedIndex") int[] selectedIndex,HttpServletRequest request, HttpServletResponse response){
        List<List<String>> data = csvdata.getData();
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
        return clusterCompute.getKScores(data,clusterAlg,kList,iterations,dmString,selectedIndex,repeats,clusterEvaluationString);
    }
}
