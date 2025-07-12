package com.algorithm.fusion_Main;

import com.algorithm.DataStruct.ArrayDocs;
import com.algorithm.DataStruct.Doc;
import com.algorithm.fusionAlgorithm.FastCombSUM;
import com.algorithm.myUtil.XLS_Util.XLS_Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class fusion11_Main {

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
    private static void RunProgram(String runsPath,String classificationPath,String fusionPath,Integer[] topics,int startNum,int endNum,int startExperimentNum,int endExperimentNum
    ) throws IOException {
        for (int Num = startNum; Num <= endNum ; Num++) {
            String outputPath =fusionPath + Num+"\\";
            createDirectory(outputPath);
            for (int ExperimentNum = startExperimentNum; ExperimentNum <= endExperimentNum ; ExperimentNum++) {
                System.err.println(Num+"\t"+ExperimentNum);
                String xls_path = classificationPath + Num+"\\AGG_K"+Num+"_"+ExperimentNum+".xls";//组合为xls路径
                //String[] fusionNames = readXls(xls_path,Num);
                String[] fusionNames = XLS_Util.getXLSFusionNames(xls_path,Num);
                for (int NameNum = 0; NameNum < fusionNames.length; NameNum++) {
                    System.out.println(fusionNames[NameNum]);
                    //fusionNames[NameNum] = runsPath + fusionNames[NameNum];
                }

                String FusionFileName = "CombSUM_"+Num+"_"+ExperimentNum;

                String outputFusionPath = outputPath + FusionFileName;

                System.out.println();
                //融合fusionNames
                ArrayList<ArrayDocs> finalList = FastCombSUM.RunProgram(runsPath, topics, fusionNames);

                writeFusionJudgmentFile(finalList,100,outputFusionPath);
            }
        }

    }

    private static void writeFusionJudgmentFile(ArrayList<ArrayDocs> finalList, int DEEP, String outputFusionPath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFusionPath));

        for (ArrayDocs arrayDocs : finalList) {
            for (Doc doc : arrayDocs.getList()) {
                if (doc.getRank() >DEEP){
                    continue;
                }
                String line = "";
                line += doc.getTopic()+"\t";
                line += "Q0\t";
                line += doc.getDocID()+"\t";
                //line += doc.getScore()+"\n";
                line += "1"+"\n";
                bw.write(line);
            }
        }
        bw.close();
    }

    public static void main(String[] args) throws IOException {
        String runsPath ="E:\\TRECDateset\\paperExperiment\\ideaTwo\\2018PrecisionLiterature\\adhoc\\standard_input_nor30_60\\";
        String classificationPath="E:\\TRECDateset\\paperExperiment\\ideaTwo\\2018PrecisionLiterature\\adhoc\\classification\\";
        String fusionPath ="E:\\TRECDateset\\paperExperiment\\ideaTwo\\2018PrecisionLiterature\\adhoc\\fusion11_1\\judgment\\";
        Integer[] topics={
                1,2,3,4,5,6,7,8,9,10,
                11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30,
                31,32,33,34,35,36,37,38,39,40,
                41,42,43,44,45,46,47,48,49,50
        };
        int startNum= 84;
        int endNum =84;
        int startExperimentNum=1;
        int endExperimentNum=1;
        RunProgram(runsPath,classificationPath,fusionPath,topics,startNum,endNum,startExperimentNum,endExperimentNum);
    }
}
