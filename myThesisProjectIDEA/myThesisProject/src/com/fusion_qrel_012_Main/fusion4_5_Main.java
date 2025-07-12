package com.fusion_qrel_012_Main;

import com.fusionAlgorithm.CombSUM;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.xml.stream.events.StartElement;
import java.io.*;

public class fusion4_5_Main {
    /**
     *
     * 提前计算好各个系统的Probabilityfile文件并且生成按照Probablilityfile的值交叉得到的分数归一化文件
     * 通过xls文件获得要融合的系统路径，对其进行CombSUM融合
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

    private static void RunProgramJi(String runsPath,String classificationPath,String fusionPath,int[] topicsInt,int StartNum,int EndNum,int startExperimentNum,int EndExperimentNum
    ) throws IOException {
        for (int Num = StartNum; Num <= EndNum ; Num++) {
            String outputPath =fusionPath + Num+"\\";
            createDirectory(outputPath);
            for (int ExperimentNum = startExperimentNum; ExperimentNum <= EndExperimentNum ; ExperimentNum++) {
                System.err.println(Num+"\t"+ExperimentNum);
                String xls_path = classificationPath + Num+"\\Random_K"+Num+"_"+ExperimentNum+".xls";//组合为xls路径
                String[] fusionNames = readXls(xls_path,Num);
                for (int NameNum = 0; NameNum < fusionNames.length; NameNum++) {
                    System.out.println(fusionNames[NameNum]);
                    fusionNames[NameNum] = runsPath + fusionNames[NameNum];
                }

                String FusionFileName = "CombSUM_Ji_"+Num+"_"+ExperimentNum;

                String outputFusionPath = outputPath + FusionFileName;
                CombSUM.RunProgram(fusionNames,FusionFileName,outputFusionPath,topicsInt);
            }
        }

    }
    private static void RunProgramOu(String runsPath,String classificationPath,String fusionPath,int[] topicsInt,int StartNum,int EndNum,int startExperimentNum,int EndExperimentNum
    ) throws IOException {
        for (int Num = StartNum; Num <= EndNum ; Num++) {
            String outputPath =fusionPath + Num+"\\";
            createDirectory(outputPath);
            for (int ExperimentNum = startExperimentNum; ExperimentNum <= EndExperimentNum ; ExperimentNum++) {

                System.err.println(Num+"\t"+ExperimentNum);
                String xls_path = classificationPath + Num+"\\Random_K"+Num+"_"+ExperimentNum+".xls";//组合为xls路径
                String[] fusionNames = readXls(xls_path,Num);
                for (int NameNum = 0; NameNum < fusionNames.length; NameNum++) {
                    System.out.println(fusionNames[NameNum]);
                    fusionNames[NameNum] = runsPath + fusionNames[NameNum];
                }

                String FusionFileName = "CombSUM_Ou_"+Num+"_"+ExperimentNum;

                String outputFusionPath = outputPath + FusionFileName;
                CombSUM.RunProgram(fusionNames,FusionFileName,outputFusionPath,topicsInt);
            }
        }

    }
    public static void mergeJiOu(String fusionPath,int StartNum,int EndNum,int startExperimentNum,int EndExperimentNum) throws IOException {

        for (int Num = StartNum; Num <= EndNum; Num++) {
            for (int ExperimentNum = startExperimentNum; ExperimentNum <= EndExperimentNum; ExperimentNum++) {
                System.err.println(Num+"\t"+ExperimentNum);
                String fusionResultPath_Ji = fusionPath + Num + "\\CombSUM_Ji_"+Num+"_"+ExperimentNum;
                String fusionResultPath_Ou = fusionPath + Num + "\\CombSUM_Ou_"+Num+"_"+ExperimentNum;
                String fusionResultMergePath = fusionPath + Num+"\\CombSUM_"+Num+"_"+ExperimentNum;
                BufferedReader brJi = new BufferedReader(new FileReader(fusionResultPath_Ji));
                BufferedReader brOu = new BufferedReader(new FileReader(fusionResultPath_Ou));
                BufferedWriter bw  = new BufferedWriter(new FileWriter(fusionResultMergePath));
                String oneline = "";
                while ((oneline = brJi.readLine())!=null){
                    bw.write(oneline+"\n");
                    bw.flush();
                }
                while ((oneline = brOu.readLine())!=null){
                    bw.write(oneline +"\n");
                    bw.flush();
                }
                bw.close();
                brJi.close();
                brOu.close();
                File tempJi = new File(fusionResultPath_Ji);
                File tempOu = new File(fusionResultPath_Ou);
                tempJi.delete();
                tempOu.delete();
            }
        }

    }
    public static void main(String[] args) throws IOException {
        String runsPath_Ji = "D:\\TREC数据集文件\\methode_qrel012\\BigExperiment\\2014MB\\Adhoc\\standard_input_norSlide_ji\\";
        String runsPath_Ou = "D:\\TREC数据集文件\\methode_qrel012\\BigExperiment\\2014MB\\Adhoc\\standard_input_norSlide_ou\\";
        int[] topicsInt_Ji ={
                171,173,175,177,179
                ,181,183,185,187,189
                ,191,193,195,197,199
                ,201,203,205,207,209
                ,211,213,215,217,219
                ,221,223,225};
        int[] topicsInt_Ou ={
                172,174,176,178,180
                ,182,184,186,188,190
                ,192,194,196,198,200
                ,202,204,206,208,210
                ,212,214,216,218,220
                ,222,224};
        int StartNum = 74;
        int EndNum = 74;
        int startExperimentNum = 1;
        int EndExperimentNum =1;
        String classificationPath = "D:\\TREC数据集文件\\methode_qrel012\\BigExperiment\\2014MB\\Adhoc\\classification\\";
        String fusionPath = "D:\\TREC数据集文件\\methode_qrel012\\BigExperiment\\2014MB\\Adhoc\\fusion13\\fusion\\";

        RunProgramJi(runsPath_Ji,classificationPath,fusionPath,topicsInt_Ji,StartNum,EndNum,startExperimentNum,EndExperimentNum);
        RunProgramOu(runsPath_Ou,classificationPath,fusionPath,topicsInt_Ou,StartNum,EndNum,startExperimentNum,EndExperimentNum);
        mergeJiOu(fusionPath,StartNum,EndNum,startExperimentNum,EndExperimentNum);
    }
}
