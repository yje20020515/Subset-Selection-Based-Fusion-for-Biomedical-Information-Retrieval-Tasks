package com.algorithm.cluster;

import com.algorithm.cluster.hierarchical.agglomerative1.Main;

import java.io.IOException;

public class CalculateDistanceMatrix {
    //计算距离矩阵
    public static double[][] runProgram(String pointPath) throws IOException {
        Main.initClusterPoints(pointPath);
        double[][] Matrix = Main.calculateDistanceMatrix();
        return Matrix;
    }
    public static void main(String[] args) throws IOException {
        String pointPath = "E:\\TRECDateset\\paperExperiment\\methode_qrelNegative\\2012Session\\Adhoc\\standard_input_nor30-60";
        runProgram(pointPath);
    }
}
