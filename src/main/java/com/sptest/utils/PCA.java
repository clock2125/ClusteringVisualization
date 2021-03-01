package com.sptest.utils;

import org.jblas.ComplexFloatMatrix;
import org.jblas.Eigen;
import org.jblas.FloatMatrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PCA {
    /**
     * Reduce matrix dimension     减少矩阵维度
     * @param source         源矩阵
     * @param dimension       目标维度
     * @return Target matrix     返回目标矩阵
     */

    public static FloatMatrix dimensionReduction(FloatMatrix source, int dimension) {
        //C=X*X^t/m     矩阵*矩阵^异或/列数
        FloatMatrix covMatrix = source.mmul(source.transpose()).div(source.columns);
        ComplexFloatMatrix eigVal = Eigen.eigenvalues(covMatrix);
        ComplexFloatMatrix[] eigVectorsVal = Eigen.eigenvectors(covMatrix);
        ComplexFloatMatrix eigVectors = eigVectorsVal[0];
        //通过特征值将符号向量从大到小排序
        List<PCABean> beans = new ArrayList<PCABean>();
        for (int i = 0; i < eigVectors.columns; i++) {
            beans.add(new PCABean(eigVal.get(i).real(), eigVectors.getColumn(i)));
        }
        Collections.sort(beans);
        FloatMatrix newVec = new FloatMatrix(dimension, beans.get(0).vector.rows);
        for (int i = 0; i < dimension; i++) {
            ComplexFloatMatrix dm = beans.get(i).vector;
            FloatMatrix real = dm.getReal();
            newVec.putRow(i, real);
        }
        return newVec.mmul(source);
    }
    static class PCABean implements Comparable<PCABean> {
        float eigenValue;
        ComplexFloatMatrix vector;
        public PCABean(Float eigenValue, ComplexFloatMatrix vector) {
            super();
            this.eigenValue = eigenValue;
            this.vector = vector;
        }
        @Override
        public int compareTo(PCABean o) {
            return Float.compare(o.eigenValue, eigenValue);
        }
        @Override
        public String toString() {
            return "PCABean [eigenValue=" + eigenValue + ", vector=" + vector + "]";
        }
    }

    public static float[][] runReduction(List<List<String>> data, int[] selectedIndex){
        int rowNum = data.size();
        int colNum = selectedIndex.length;
        float[][] dataArray = new float[colNum][rowNum];

        for(int i =0; i<colNum;i++){
            for (int j=0;j<rowNum;j++){
                dataArray[i][j] = Float.parseFloat(data.get(j).get(selectedIndex[i]));
            }
        }

        float[][] dataArrayT = new float[rowNum][colNum];
        for(int i =0; i<rowNum;i++){
            for (int j=0;j<colNum;j++){
                dataArrayT[i][j] = Float.parseFloat(data.get(i).get(selectedIndex[j]));
            }
        }

        if(colNum == 2){
            return dataArrayT;
        }else {
            FloatMatrix source = new FloatMatrix(dataArray);
            float[][] sourceArray = source.toArray2();
            System.out.println("原始数据：");
            for (float[] floats : sourceArray) {
                for (float aFloat : floats) {
                    System.out.print(aFloat+",");
                }
                System.out.print("\n");
            }
            FloatMatrix result = PCA.dimensionReduction(source, 2);
            float[][] resultArrayT = result.toArray2();
            float[][] resultArray = new float[rowNum][2];
            for(int i=0;i<rowNum;i++){
                resultArray[i][0] = resultArrayT[0][i];
                resultArray[i][1] = resultArrayT[1][i];
            }

            System.out.println("降维结果：");
            for (float[] floats : resultArray) {
                for (float aFloat : floats) {
                    System.out.print(aFloat+",");
                }
                System.out.print("\n");
            }
            return resultArray;
        }
    }
}
