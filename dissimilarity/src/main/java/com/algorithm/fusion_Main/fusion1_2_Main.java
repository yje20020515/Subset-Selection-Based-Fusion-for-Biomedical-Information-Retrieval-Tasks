package com.algorithm.fusion_Main;

import com.algorithm.fusionAlgorithm.FastCombMNZ;
import com.algorithm.fusionAlgorithm.FastCombSUM;
import com.algorithm.myUtil.XLS_Util.XLS_Util;

import java.io.File;
import java.io.IOException;

public class fusion1_2_Main {
    /**
     * 1.读取xls文件，得到要融合的run的路径##共有两种nor文件类型
     * 2.CombSUM融合
     */
    private static void createDirectory(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    private static void RunProgram(String runsPath,String classificationPath,String fusionPath,Integer[] topicsInt,int StartNum,int EndNum,int startExperimentNum,int EndExperimentNum
    ,String fuseName) throws IOException {
        for (int Num = StartNum; Num <= EndNum ; Num++) {
            String outputPath =fusionPath + Num+"\\";
            createDirectory(outputPath);
            for (int ExperimentNum = startExperimentNum; ExperimentNum <= EndExperimentNum ; ExperimentNum++) {
                System.err.println(Num+"\t"+ExperimentNum);
                String xls_path = classificationPath + Num+"\\all_K"+Num+"_"+ExperimentNum+".xls";//组合为xls路径
                //String[] fusionNames = readXls(xls_path,Num);
                String[] fusionNames = XLS_Util.getXLSFusionNames(xls_path,Num);
                for (int NameNum = 0; NameNum < fusionNames.length; NameNum++) {
                    System.out.println(fusionNames[NameNum]);
                    fusionNames[NameNum] = runsPath + fusionNames[NameNum];
                }

                String FusionFileName = fuseName+"_"+Num+"_"+ExperimentNum;

                String outputFusionPath = outputPath + FusionFileName;
                //CombSUM.RunProgram(fusionNames,FusionFileName,outputFusionPath,topicsInt);
                if (fuseName.equals("CombSUM")){
                    FastCombSUM.RunProgram(runsPath,topicsInt,outputFusionPath,FusionFileName,Num,xls_path);
                }else if (fuseName.equals("CombMNZ")) {
                    FastCombMNZ.RunProgram(runsPath,topicsInt,outputFusionPath,FusionFileName,Num,xls_path);
                }else{
                    System.err.println("fuseName error");
                }
            }
        }

    }
    public static void main(String[] args) throws IOException {
        String runsPath = "E:\\TRECDateset\\paperExperiment\\ideaThree\\2020Health\\Adhoc\\standard_input_nor0-1_doc100\\";
        Integer[] topicsInt ={
                1,2,3,4,5,6,7,8,9,10,
                11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30,
                31,32,33,34,35,36,37,38,39,40,
                41,42,43,44,45,46,47,48,49,50,
                51,52,53,54,55,56,57,58,59,60,
                61,62,63,64,65,66,67,68,69,70,
                71,72,73,74,75,76,77,78,79,80,
                81,82,83,84,85,86,87,88,89,90,
                91,92,93,94,95,96,97,98,
        };
        int StartNum = 50;
        int EndNum = 50;
        int startExperimentNum = 1;
        int EndExperimentNum =1;
        String fuseName = "CombSUM";
        String classificationPath = "E:\\TRECDateset\\paperExperiment\\ideaThree\\2020Health\\judgment\\autoJudgment\\doc100_fusion1\\classification\\";
        String fusionPath = "E:\\TRECDateset\\paperExperiment\\ideaThree\\2020Health\\judgment\\autoJudgment\\doc100_fusion1\\fusion\\";

        RunProgram(runsPath,classificationPath,fusionPath,topicsInt,StartNum,EndNum,startExperimentNum,EndExperimentNum,fuseName);
    }


}
