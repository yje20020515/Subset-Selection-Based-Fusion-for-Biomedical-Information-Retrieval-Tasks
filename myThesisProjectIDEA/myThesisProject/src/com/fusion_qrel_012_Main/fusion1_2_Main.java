package com.fusion_qrel_012_Main;

import com.fusionAlgorithm.CombSUM;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class fusion1_2_Main {
    /**
     * 1.读取xls文件，得到要融合的run的路径##共有两种nor文件类型
     * 2.CombSUM融合
     */
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

    private static void RunProgram(String runsPath,String classificationPath,String fusionPath,int[] topicsInt,int StartNum,int EndNum,int startExperimentNum,int EndExperimentNum
                                  ) throws IOException {
        for (int Num = StartNum; Num <= EndNum ; Num++) {
            String outputPath =fusionPath + Num+"\\";
            createDirectory(outputPath);
            for (int ExperimentNum = startExperimentNum; ExperimentNum <= EndExperimentNum ; ExperimentNum++) {
                System.err.println(Num+"\t"+ExperimentNum);
                String xls_path = classificationPath + Num+"\\Semi_K"+Num+"_"+ExperimentNum+".xls";//组合为xls路径
                String[] fusionNames = readXls(xls_path,Num);
                for (int NameNum = 0; NameNum < fusionNames.length; NameNum++) {
                    System.out.println(fusionNames[NameNum]);
                    fusionNames[NameNum] = runsPath + fusionNames[NameNum];
                }

                String FusionFileName = "CombSUM_"+Num+"_"+ExperimentNum;

                String outputFusionPath = outputPath + FusionFileName;
                CombSUM.RunProgram(fusionNames,FusionFileName,outputFusionPath,topicsInt);
            }
        }

    }
    public static void main(String[] args) throws IOException {
        String runsPath = "F:\\TREC 数据集\\2020deeplearning document\\Deep Learning document\\posfuse\\standard_input_norSlide_ou\\";
        /*int[] topicsInt ={//2020deep learning document
                42255, 47210, 67316, 135802, 156498, 169208,
                174463, 258062, 324585, 330975, 332593, 336901,
                673670, 701453, 730539, 768208, 877809, 911232,
                938400, 940547, 997622, 1030303, 1037496, 1043135,
                1049519, 1051399, 1056416, 1064670, 1071750, 1103153,
                1105792, 1108729, 1109707, 1113256, 1115210, 1116380,
                1119543, 1122767, 1127540, 1131069, 1132532, 1136043,
                1136047, 1136769, 1136962
        };*/
        int[]  topicsInt  = {//2020deep learning document
                47210,135802,169208,258062,330975, 336901,
                701453, 768208, 911232,940547,1030303, 1043135,
                1051399, 1064670,1103153, 1108729, 1113256,1116380,
                1122767,1131069,1136043,1136769
        };
        int StartNum = 2;
        int EndNum = 19;
        int startExperimentNum = 1;
        int EndExperimentNum = 1;
        String classificationPath = "F:\\TREC 数据集\\2020deeplearning document\\classification\\";
        String fusionPath = "F:\\TREC 数据集\\2020deeplearning document\\Deep Learning document\\posfuse\\ComSUM fusion ou\\";

        RunProgram(runsPath,classificationPath,fusionPath,topicsInt,StartNum,EndNum,startExperimentNum,EndExperimentNum);
    }


}
