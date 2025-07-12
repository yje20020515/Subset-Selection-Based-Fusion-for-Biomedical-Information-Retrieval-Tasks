package com.algorithm.fusion_Main;

import com.algorithm.LCfusion.Main.StardardFusionMain.FusionMain;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class fusion3_Main {

    private static String[] readXls(String xls_path,int Num) throws IOException {
        String[] fusionNames = new String[Num];

        FileInputStream fip = new FileInputStream(xls_path);
        HSSFWorkbook rb = new HSSFWorkbook(fip);
        Sheet sheet = rb.getSheetAt(0);
        Row row;

        for (int rowNum = 0; rowNum < sheet.getPhysicalNumberOfRows(); rowNum++) {
            row = sheet.getRow(rowNum);
            String fusionName = row.getCell(0).getStringCellValue().replaceAll("###","");
            fusionNames[rowNum] = fusionName;
        }
        fip.close();
        return fusionNames;
    }
    private static void createDirectory(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()){
            file.mkdirs();
        }
    }
    private static void RunProgram(String runsPath,String classificationPath,String fusionPath,int[] topicsInt,int[] all_topic_ji,int[] all_topic_ou,int StartNum,int EndNum,int startExperimentNum,int EndExperimentNum
            ,String Generatefile_output,String weights_output,String qrelsPath) throws Exception {
        FusionMain.setAll_topic(topicsInt);
        FusionMain.setAll_topic_ji(all_topic_ji);
        FusionMain.setAll_topic_ou(all_topic_ou);
        for (int Num = StartNum; Num <= EndNum ; Num++) {
            String outputPath =fusionPath + Num+"\\";
            createDirectory(outputPath);
            for (int ExperimentNum = startExperimentNum; ExperimentNum <= EndExperimentNum ; ExperimentNum++) {
                System.err.println();
                System.err.println(Num+"\t"+ExperimentNum);
                String xls_path = classificationPath + Num+"\\reKMeans_k"+Num+"_"+ExperimentNum+".xls";
                String[] fusionNames = readXls(xls_path,Num);
                for (int NameNum = 0; NameNum < fusionNames.length; NameNum++) {
                    System.out.println(fusionNames[NameNum]);
                    fusionNames[NameNum] = runsPath + fusionNames[NameNum];
                }

                String FusionFileName = "LCfusion_"+Num+"_"+ExperimentNum;
                FusionMain.setSystemName(FusionFileName);
                String outputFusionPath = outputPath + FusionFileName;
                //CombSUM.RunProgram(fusionNames,FusionFileName,outputFusionPath,topicsInt);
                FusionMain.RunProgram(fusionNames.length,2,fusionNames,runsPath,qrelsPath,ExperimentNum,Generatefile_output,
                        weights_output,outputFusionPath);
            }
        }

    }

    public static void main(String[] args) throws Exception {
        String runsPath = "E:\\TRECDateset\\classificationEXP\\2018PrecisionLiterature\\adhoc\\standard_input_nor30_60\\";
        int[] topicsInt ={
                1,2,3,4,5,6,7,8,9,10,
                11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30,
                31,32,33,34,35,36,37,38,39,40,
                41,42,43,44,45,46,47,48,49,50
        };
        int[] all_topic_ji = {
                1,3,5,7,9,
                11,13,15,17,19,
                21,23,25,27,29,
                31,33,35,37,39,
                41,43,45,47,49
        };
        int[] all_topic_ou = {
                2,4,6,8,10,
                12,14,16,18,20,
                22,24,26,28,30,
                32,34,36,38,40,
                42,44,46,48,50
        };
        int StartNum = 2;
        int EndNum = 20;
        int startExperimentNum = 1;
        int EndExperimentNum =50;
        String classificationPath = "E:\\TRECDateset\\classificationEXP\\2018PrecisionLiterature\\adhoc\\fusion3_6\\classification_KmeansK20_Sort\\";
        String fusionPath =         "E:\\TRECDateset\\classificationEXP\\2018PrecisionLiterature\\adhoc\\fusion3_6\\fusion\\";
        String Generatefile_output= "E:\\TRECDateset\\classificationEXP\\2018PrecisionLiterature\\adhoc\\fusion3_6\\LCfusion\\Generatefile\\";
        String weights_output=      "E:\\TRECDateset\\classificationEXP\\2018PrecisionLiterature\\adhoc\\fusion3_6\\LCfusion\\Weightsfile\\";
        String qrelsPath=           "E:\\TRECDateset\\classificationEXP\\2018PrecisionLiterature\\judgmentFile\\judgmentfile_01";
        RunProgram(runsPath,classificationPath,fusionPath,topicsInt,all_topic_ji,all_topic_ou,StartNum,EndNum,startExperimentNum,EndExperimentNum,
                Generatefile_output,weights_output,qrelsPath);
    }


}
