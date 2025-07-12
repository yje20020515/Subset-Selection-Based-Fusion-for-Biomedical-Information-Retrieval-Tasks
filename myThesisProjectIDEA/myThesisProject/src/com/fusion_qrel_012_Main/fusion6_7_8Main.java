package com.fusion_qrel_012_Main;

import com.LCfusion.Main.StardardFusionMain.FusionMain;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.ArrayList;

public class fusion6_7_8Main {
    /**
     * 通过xls文件获得到要融合的系统路径
     * 通过P10_jiou_eval_input.xls文件得到要融合的系统路径的奇偶权重
     * 通过得到的奇偶权重进行LC融合
     */
    private static class RunBean{
        public String FusionName;
        public double P10_ji,P10_ou,MAP_ji,MAP_ou;
        public double Diss_ji,Diss_ou;
        public RunBean() {}

        public RunBean(String fusionName, double p10_ji, double p10_ou, double MAP_ji, double MAP_ou) {
            this.FusionName = fusionName;
            this.P10_ji = p10_ji;
            this.P10_ou = p10_ou;
            this.MAP_ji = MAP_ji;
            this.MAP_ou = MAP_ou;
        }
    }
    private static class A_B_diss{
        public String RunA,RunB;
        public double diss;

        public A_B_diss() {
        }

        public A_B_diss(String runA, String runB, double diss) {
            RunA = runA;
            RunB = runB;
            this.diss = diss;
        }
    }
    private static String[] readFusionXls(String xls_path,int Num) throws IOException {
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
    private static ArrayList<RunBean> readEvalXls(String xls_path) throws IOException {
        ArrayList<RunBean> RunList = new ArrayList<>();

        FileInputStream fip = new FileInputStream(xls_path);
        HSSFWorkbook rb = new HSSFWorkbook(fip);
        Sheet sheet = rb.getSheetAt(0);
        Row row;

        for (int rowNum = 1; rowNum < sheet.getPhysicalNumberOfRows(); rowNum++) {
            row = sheet.getRow(rowNum);
            String fusionName = row.getCell(0).getStringCellValue().replaceAll("###","");
            double P10_ji = row.getCell(1).getNumericCellValue();
            double P10_ou = row.getCell(2).getNumericCellValue();
            double MAP_ji = row.getCell(3).getNumericCellValue();
            double MAP_ou = row.getCell(4).getNumericCellValue();
            RunList.add(new RunBean(fusionName,P10_ji,P10_ou,MAP_ji,MAP_ou));
            //System.out.println(fusionName+P10_ji+P10_ou+MAP_ji+MAP_ou);
        }
        fip.close();
        rb.close();
        return RunList;
    }

    /**
     *
     * @param runList   所有的RUN列表，包含了eval信息
     * @param fusionNames   将要融合的RUN集合
     * @param weightFilePath    将要融合的RUN集合的weight文件输出路径
     */
    private static void generateP10WeightFile(ArrayList<RunBean> runList, String[] fusionNames,String weightFilePath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(weightFilePath));
        for (int Num = 0; Num < fusionNames.length; Num++) {
            double P10_ou = getFusionNameP10_ou(runList,fusionNames[Num]);
            bw.write(P10_ou+",");
        }
        bw.write("\n");
        for (int Num = 0; Num < fusionNames.length; Num++) {
            double P10_ji = getFusionNameP10_ji(runList,fusionNames[Num]);
            bw.write(P10_ji+",");
        }
        bw.write("\n");
        bw.close();
    }
    private static void generateMAPWeightFile(ArrayList<RunBean> runList, String[] fusionNames,String weightFilePath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(weightFilePath));
        for (int Num = 0; Num < fusionNames.length; Num++) {
            double P10_ou = getFusionNameMAP_ou(runList,fusionNames[Num]);
            bw.write(P10_ou+",");
        }
        bw.write("\n");
        for (int Num = 0; Num < fusionNames.length; Num++) {
            double P10_ji = getFusionNameMAP_ji(runList,fusionNames[Num]);
            bw.write(P10_ji+",");
        }
        bw.write("\n");
        bw.close();
    }
    private static boolean existFusionName(String RunB,String[] fusionNames){
        for (String fusionName : fusionNames) {
            if (fusionName.split("\\\\")[fusionName.split("\\\\").length-1].equals(RunB)){
                return true;
            }
        }
        return false;
    }
    private static void generate_P10_diss_WeightFile(ArrayList<RunBean> runList, String[] fusionNames,String weightFilePath,ArrayList<A_B_diss> diss_list_ji,ArrayList<A_B_diss> diss_list_ou) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(weightFilePath));
        for (int Num = 0; Num < fusionNames.length; Num++) {
            double diss_ou = 0.0;
            System.out.println("计算ou权重");
            for (A_B_diss a_b_diss : diss_list_ou) {
                if (a_b_diss.RunA.equals(fusionNames[Num].split("\\\\")[fusionNames[Num].split("\\\\").length-1])&&existFusionName(a_b_diss.RunB,fusionNames)&&!a_b_diss.RunA.equals(a_b_diss.RunB)){
                    System.out.println(a_b_diss.RunA+"\t"+a_b_diss.RunB+"\t"+a_b_diss.diss);
                    diss_ou += a_b_diss.diss;
                }
            }

            diss_ou = diss_ou / (fusionNames.length-1);
            double P10_ou = getFusionNameMAP_ou(runList,fusionNames[Num]);
            double weight_ji = diss_ou * P10_ou;
            bw.write(weight_ji+",");
        }
        bw.write("\n");
        for (int Num = 0; Num < fusionNames.length; Num++) {
            double diss_ji = 0.0;
            System.out.println("计算ji权重");
            for (A_B_diss a_b_diss : diss_list_ji) {
                if (a_b_diss.RunA.equals(fusionNames[Num].split("\\\\")[fusionNames[Num].split("\\\\").length-1])&&existFusionName(a_b_diss.RunB,fusionNames)&&!a_b_diss.RunA.equals(a_b_diss.RunB)){
                    System.out.println(a_b_diss.RunA+"\t"+a_b_diss.RunB+"\t"+a_b_diss.diss);
                    diss_ji += a_b_diss.diss;
                }
            }

            diss_ji = diss_ji / (fusionNames.length-1);
            double P10_ji = getFusionNameMAP_ji(runList,fusionNames[Num]);
            double weight_ji = diss_ji * P10_ji;

            bw.write(weight_ji+",");
        }
        bw.write("\n");
        bw.close();
    }
    private static double getFusionNameP10_ji(ArrayList<RunBean> runList,String fusionName) {
        for (RunBean runBean : runList) {
            if (runBean.FusionName.equals(fusionName.split("\\\\")[fusionName.split("\\\\").length-1])){
                //System.out.println(runBean.P10_ji);
                return runBean.P10_ji;
            }
        }
        return 0;
    }
    private static double getFusionNameP10_ou(ArrayList<RunBean> runList,String fusionName) {
        for (RunBean runBean : runList) {
            if (runBean.FusionName.equals(fusionName.split("\\\\")[fusionName.split("\\\\").length-1])){
                return runBean.P10_ou;
            }
        }
        return 0;
    }
    private static double getFusionNameMAP_ji(ArrayList<RunBean> runList,String fusionName) {
        for (RunBean runBean : runList) {
            if (runBean.FusionName.equals(fusionName.split("\\\\")[fusionName.split("\\\\").length-1])){

                return runBean.MAP_ji;
            }
        }
        return 0;
    }
    private static double getFusionNameMAP_ou(ArrayList<RunBean> runList,String fusionName) {
        for (RunBean runBean : runList) {
            if (runBean.FusionName.equals(fusionName.split("\\\\")[fusionName.split("\\\\").length-1])){
                return runBean.MAP_ou;
            }
        }
        return 0;
    }
    private static void clearWeightFile(String weights_output) {
        File[] weights = new File(weights_output).listFiles();
        for (File weight : weights) {
            weight.delete();
        }
    }
    private static ArrayList<A_B_diss> readDissXls(String xls_Path,String SheetName) throws IOException {
        ArrayList<A_B_diss> diss_list = new ArrayList<>();
        //ArrayList<A_B_diss> diss_list_ou = new ArrayList<>();


        FileInputStream fis = new FileInputStream(xls_Path);
        HSSFWorkbook hw = new HSSFWorkbook(fis);
        Sheet sheet = hw.getSheet(SheetName);
        //Sheet sheet_ou = hw.getSheet("diss_ou");
        Row row;

        //读取第一行
        row = sheet.getRow(0);
        String[] RunAs = new String[row.getPhysicalNumberOfCells()];
        //System.out.println(row.getPhysicalNumberOfCells());
        for (int col_Num = 1; col_Num < row.getPhysicalNumberOfCells(); col_Num++) {
            String RunA = row.getCell(col_Num).getStringCellValue().replaceAll("###","");
            RunAs[col_Num] = RunA;
            //System.out.println(col_Num);
        }

        for (int row_NUM = 1; row_NUM < sheet.getPhysicalNumberOfRows(); row_NUM++) {
            row = sheet.getRow(row_NUM);
            String RunB = row.getCell(0).getStringCellValue().replaceAll("###","");
            //System.out.println(RunB);
            for (int col_Num = 1; col_Num < row.getPhysicalNumberOfCells(); col_Num++) {
                double diss = row.getCell(col_Num).getNumericCellValue();
                diss_list.add(new A_B_diss(RunAs[col_Num],RunB,diss));
                //System.out.println(col_Num);
            }

        }
        //System.out.println(diss_list.size());
        hw.close();
        fis.close();
        return diss_list;
    }

    /**
     *
     * @param runsPath 将要融合的run的文件夹路径，一般为30/60的归一化路径
     * @param classificationPath 将要融合的run的的classification路径，并且会按照要融合的系统个数再次创建文件夹
     * @param fusionPath    要融合的结果输出路径
     * @param topicsInt 要融合的全部的topic集合
     * @param all_topic_ji  要分奇偶时的奇的topic集合
     * @param all_topic_ou  要分奇偶时的偶的topic集合
     * @param StartNum  要融合的系统个数的开始数量
     * @param EndNum    要融合的系统个数的结束数量
     * @param startExperimentNum    要重复实验的开始实验次数
     * @param EndExperimentNum  要重复实验的结束实验次数
     * @param Generatefile_output   当要进行最小二乘拟合的权重计算时需要的generatefile路径
     * @param weights_output    权重文件输出路径，并且若是最小二乘拟合时改文件应该为空，若以P10，MAP，P10*DISS为权重时应该在当前页面代码中计算其权重输出权重文档
     * @param qrelsPath 进行最小二乘拟合时所需要的qrel文件路径
     * @param EvalXls   以P10,MAP,P10*DISS为权重时需要改文件作为这些评价指标的输入，文件应该有五列 runName，P10_ji,P10_ou,MAP_ji,MAP_ou
     * @param dissXls   以P10*DISS为权重时需要该文件作为diss的输入,文件应该有两个SHEET，第一个名为diss_ji,第二个名为diss_ou,并且两个SHEET的最左上角应该为固定字符串”diss“
     * @throws Exception
     */
    private static void RunProgram(String runsPath,String classificationPath,String fusionPath,int[] topicsInt,int[] all_topic_ji,int[] all_topic_ou,int StartNum,int EndNum,int startExperimentNum,int EndExperimentNum
            ,String Generatefile_output,String weights_output,String qrelsPath,String EvalXls ,String dissXls,String fusionName) throws Exception {
        FusionMain.setAll_topic(topicsInt);//把topic集合设置
        FusionMain.setAll_topic_ji(all_topic_ji);//设置奇topic集合
        FusionMain.setAll_topic_ou(all_topic_ou);//设置偶topic集合
        String SheetName = "diss_ji";//diss奇的Sheet名字
        ArrayList<RunBean> RunList  = readEvalXls(EvalXls);//读取evalxls文件，将结果保存到runlist对象中;
        ArrayList<A_B_diss> diss_list_ji =null;
        ArrayList<A_B_diss> diss_list_ou =null;
        if (fusionName.equals("fusion8")){


            diss_list_ji = readDissXls(dissXls,SheetName);//获得diss_ji的距离信息，并保存到diss_list_ji对象中
            SheetName = "diss_ou";//diss偶的Sheet名字
            diss_list_ou = readDissXls(dissXls,SheetName);//获得diss_ou的距离信息，并保存到diss_list_ou对象中
        }

        clearWeightFile(weights_output);//清空weight文件夹
        for (int Num = StartNum; Num <= EndNum ; Num++) {
            String outputPath =fusionPath + Num+"\\";
            createDirectory(outputPath);
            for (int ExperimentNum = startExperimentNum; ExperimentNum <= EndExperimentNum ; ExperimentNum++) {
                System.err.println(Num+"\t"+ExperimentNum);
                String xls_path = classificationPath + Num+"\\Semi_K"+Num+"_"+ExperimentNum+".xls";//组合为xls路径
                String[] fusionNames = readFusionXls(xls_path,Num);//获得此次实验应该要融合的fusionName名字
                for (int NameNum = 0; NameNum < fusionNames.length; NameNum++) {
                    System.out.println(fusionNames[NameNum]);
                    fusionNames[NameNum] = runsPath + fusionNames[NameNum];//将fusionName和归一化文件路径进行组合，得到要融合的run的路径
                }
                String weightsFilePath = weights_output +"\\"+"weights_"+Num+"_"+ExperimentNum;//此次权重文件路径
                if (fusionName.equals("fusion6")){
                    generateMAPWeightFile(RunList,fusionNames,weightsFilePath);//设置MAP权重文件 fusion6
                } else if (fusionName.equals("fusion7")) {
                    generateP10WeightFile(RunList,fusionNames,weightsFilePath);//设置P10权重文件    fusion7
                } else if (fusionName.equals("fusion8")) {
                    generate_P10_diss_WeightFile(RunList,fusionNames,weightsFilePath,diss_list_ji,diss_list_ou);//设置diss*P10权重文件  fusion8
                }
                //generateMAPWeightFile(RunList,fusionNames,weightsFilePath);//设置MAP权重文件 fusion6
                //generateP10WeightFile(RunList,fusionNames,weightsFilePath);//设置P10权重文件    fusion7
                //generate_P10_diss_WeightFile(RunList,fusionNames,weightsFilePath,diss_list_ji,diss_list_ou);//设置diss*P10权重文件  fusion8
                String FusionFileName = "LCfusion_"+Num+"_"+ExperimentNum;//此次运行结果路径

                String outputFusionPath = outputPath + FusionFileName;
                FusionMain.RunProgram(fusionNames.length,1,fusionNames,runsPath,qrelsPath,ExperimentNum,Generatefile_output,
                        weights_output,outputFusionPath);
            }
        }

    }




    public static void main(String[] args) throws Exception {
        String runsPath = "F:\\TREC 数据集\\2020deeplearning document\\standard-input-nor30-6\\";
        String classificationPath = "F:\\TREC 数据集\\2020deeplearning document\\classification\\";
        String fusionPath = "F:\\TREC 数据集\\2020deeplearning document\\Deep Learning document\\fusion6_7_8\\fusion\\";
        String Generatefile_output="";
        String weights_output="F:\\TREC 数据集\\2020deeplearning document\\Deep Learning document\\fusion6_7_8\\LCfusion\\weightsfile\\";
        String qrelsPath= "";
        String EvalXls = "F:\\TREC 数据集\\2020deeplearning document\\Deep Learning document\\fusion6_7_8\\all_eval_jiou.xls";
        String dissXls = "";
        String fusionName = "fusion6";
        int[] topicsInt ={
                1, /*2, 3, 4, 5,
                6, 7, 8, 9, 10,
                11, 12, 13, 14,
                15, 16, 17, 18,
                19, 20, 21, 22,
                23, 24, 25, 26,
                27, 28, 29, 30,
                31, 32, 33, 34,
                35, 36, 37, 38,
                39, 40*/};
        int[] all_topic_ji = {
                42255,67316,156498,174463,324585,332593,
                673670,324585,332593,673670, 730539,877809,
                938400, 997622,1037496,1049519, 1056416, 1071750,
                1105792,1109707,1115210, 1119543,1127540, 1132532,
                1136047, 1136962
        };
        int[] all_topic_ou = {
                47210,135802,169208,258062,330975, 336901,
                701453, 768208, 911232,940547,1030303, 1043135,
                1051399, 1064670,1103153, 1108729, 1113256,1116380,
                1122767,1131069,1136043,1136769
        };
        int StartNum = 10;
        int EndNum = 10;
        int startExperimentNum = 1;
        int EndExperimentNum =1;
        RunProgram(runsPath,classificationPath,fusionPath,topicsInt,all_topic_ji,all_topic_ou,StartNum,EndNum,startExperimentNum,EndExperimentNum,
                Generatefile_output,weights_output,qrelsPath,EvalXls,dissXls,fusionName);
    }



}
