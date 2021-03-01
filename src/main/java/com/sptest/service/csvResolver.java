package com.sptest.service;

import com.sptest.pojo.csvData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class csvResolver {

    //读取CSV文件
    public static csvData readCSV(MultipartFile mFile) {
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = null;

        try {
            inputStream = mFile.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            CSVParser parser = CSVFormat.DEFAULT.parse(bufferedReader);
            List<List<String>> values = new ArrayList<>();
            int[] head = null;
//            List<String> head = null;
            int rowIndex = 1;
            int colNum = 0;
            for (CSVRecord record : parser.getRecords()) {
                colNum = record.size();
//                if (rowIndex == 0 && "".equals(record.get(0))) {
//                    head = new ArrayList<>();
//                    for (int i = 0; i < colNum; i++) {
//                        head.add(record.get(i));
//                    }
//                    rowIndex++;
//                    continue;
//                }

                if(head==null){
//                    head = new ArrayList<>();
//                    for(int i=0;i<colNum;i++){
//                        head.add((char)('A'+i)+"");
//                    }
                    head = new int[colNum];
                    for(int i=0;i<colNum;i++){
                        head[i] = i;
                    }
                }

                List<String> value = new ArrayList<>(colNum+1);
                for (int i = 0; i < colNum; i++) {
                    value.add(record.get(i));
                }
                values.add(value);
                rowIndex++;
            }

            return new csvData(values, head);
        } catch (IOException e) {
            System.out.println("解析CSV内容失败" + e.getMessage());
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
