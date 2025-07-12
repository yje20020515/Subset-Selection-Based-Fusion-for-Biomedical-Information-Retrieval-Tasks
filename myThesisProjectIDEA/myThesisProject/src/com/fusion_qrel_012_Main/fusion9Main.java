package com.fusion_qrel_012_Main;

import com.LCfusion.Main.StardardFusionMain.FusionMain;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.ArrayList;

public class fusion9Main {
    /**
     * LC融合实验（分奇偶）
     * 通过xls文件得到要融合的系统路径
     * fusion3得到的weight文件进行再次处理，处理方式为将其一个文件的权重进行正负分隔，先求其平均值avg_weight，然后权重得分为weight-avg_weight
     * 按照权重文件交叉进行LC数据融合
     * 将得到的结果再生成为为一个文件
     */
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

    private static void fusion9_handleWeightsFile(String weightsFilePath,String newWeightsFilePath,int weightNUM) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(weightsFilePath));
        BufferedWriter bw = new BufferedWriter(new FileWriter(newWeightsFilePath));

        String oneLine = "";
        while ((oneLine = br.readLine())!=null) {
            ArrayList<Double> oneLineList = new ArrayList();
            for (String WeightStr : oneLine.split(",")) {
                oneLineList.add(Double.parseDouble(WeightStr));
            }
            double max = oneLineList.get(0);
            double min = oneLineList.get(0);
            for (Double weight : oneLineList) {
                if (max <= weight) {
                    max = weight;
                }
                if (min >= weight) {
                    min = weight;
                }
            }
            for (int i = 0; i < oneLineList.size(); i++) {
                //oneLineList.set(i,Math.abs(min) + oneLineList.get(i) );
                if (oneLineList.get(i)>0){
                    oneLineList.set(i,max);
                }
                if (oneLineList.get(i)<=0){
                    oneLineList.set(i,min);
                }
            }
            for (Double weight : oneLineList) {
                bw.write(weight + ",");
            }
            bw.write("\n");
        }
        bw.close();
        br.close();
    }

    private static void fusion10_handleWeightsFile(String weightsFilePath,String newWeightsFilePath,int weightNUM) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(weightsFilePath));
        BufferedWriter bw = new BufferedWriter(new FileWriter(newWeightsFilePath));

        String oneLine = "";
        while ((oneLine = br.readLine())!=null) {
            ArrayList<Double> oneLineList = new ArrayList();
            for (String WeightStr : oneLine.split(",")) {
                oneLineList.add(Double.parseDouble(WeightStr));
            }
            double max = oneLineList.get(0);
            double min = oneLineList.get(0);
            for (Double weight : oneLineList) {
                if (max <= weight) {
                    max = weight;
                }
                if (min >= weight) {
                    min = weight;
                }
            }

            for (int i = 0; i < oneLineList.size(); i++) {
                //oneLineList.set(i,Math.abs(min) + oneLineList.get(i) );
                oneLineList.set(i,Math.pow(oneLineList.get(i),3 ));
            }
            for (Double weight : oneLineList) {
                bw.write(weight + ",");
            }
            bw.write("\n");
        }
        bw.close();
        br.close();
    }
    private static void fusion11_handleWeightsFile(String weightsFilePath,String newWeightsFilePath,int weightNUM) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(weightsFilePath));
        BufferedWriter bw = new BufferedWriter(new FileWriter(newWeightsFilePath));

        String oneLine = "";
        while ((oneLine = br.readLine())!=null) {
            ArrayList<Double> oneLineList = new ArrayList();
            for (String WeightStr : oneLine.split(",")) {
                oneLineList.add(Double.parseDouble(WeightStr));
            }
            double max = oneLineList.get(0);
            double min = oneLineList.get(0);
            for (Double weight : oneLineList) {
                if (max <= weight) {
                    max = weight;
                }
                if (min >= weight) {
                    min = weight;
                }
            }
            double count_1 = 0.0;
            double count_2 = 0.0;
            double count_3 = 0.0;
            double count_4 = 0.0;
            double count_5 = 0.0;
            double count_6 = 0.0;
            double count_7 = 0.0;
            double count_8 = 0.0;
            double count_9 = 0.0;
            double count_10 = 0.0;
            double half_weight = (max - min)/2;
            double weight_10_1 = (max - min)/10;
            for (int i = 0; i < oneLineList.size(); i++) {

                if ((oneLineList.get(i)-min)>weight_10_1*9){
                    //oneLineList.set(i,max);
                    count_1++;
                }else
                if ((oneLineList.get(i)-min)<=weight_10_1 * 9 && (oneLineList.get(i)-min)> weight_10_1 * 8 ){
                    //oneLineList.set(i,max);
                    count_2++;
                }else
                if ((oneLineList.get(i)-min)<=weight_10_1 * 8 && (oneLineList.get(i)-min)> weight_10_1 * 7 ){
                    //oneLineList.set(i,max);
                    count_3++;
                }else
                if ((oneLineList.get(i)-min)<=weight_10_1 * 7 && (oneLineList.get(i)-min)> weight_10_1 * 6 ){
                    //oneLineList.set(i,max);
                    count_4++;
                }else
                if ((oneLineList.get(i)-min)<=weight_10_1 * 6 && (oneLineList.get(i)-min)> weight_10_1 * 5 ){
                    //oneLineList.set(i,max);
                    count_5++;
                }else
                if ((oneLineList.get(i)-min)<=weight_10_1 * 5 && (oneLineList.get(i)-min)> weight_10_1 * 4 ){
                    //oneLineList.set(i,max);
                    count_6++;
                }else
                if ((oneLineList.get(i)-min)<=weight_10_1 * 4 && (oneLineList.get(i)-min)> weight_10_1 * 3 ){
                    //oneLineList.set(i,max);
                    count_7++;
                }else
                if ((oneLineList.get(i)-min)<=weight_10_1 * 3 && (oneLineList.get(i)-min)> weight_10_1 * 2 ){
                    //oneLineList.set(i,min);
                    count_8++;
                }else
                if ((oneLineList.get(i)-min)<=weight_10_1 * 2 && (oneLineList.get(i)-min)> weight_10_1 * 1 ){
                    //oneLineList.set(i,0.0);
                    count_9++;
                }else
                if ((oneLineList.get(i)-min)<=weight_10_1 * 1 && (oneLineList.get(i)-min)>= weight_10_1 * 0 ){
                    //oneLineList.set(i,0.0);
                    count_10++;
                }

            }
            System.out.println("count_1 "+count_1);
            System.out.println("count_2 "+count_2);
            System.out.println("count_3 "+count_3);
            System.out.println("count_4 "+count_4);
            System.out.println("count_5 "+count_5);
            System.out.println("count_6 "+count_6);
            System.out.println("count_7 "+count_7);
            System.out.println("count_8 "+count_8);
            System.out.println("count_9 "+count_9);
            System.out.println("count_10 "+count_10);
            for (Double weight : oneLineList) {
                bw.write(weight + ",");
            }
            bw.write("\n");
        }
        bw.close();
        br.close();
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
     * @throws Exception
     */
    private static void RunProgram(String runsPath,String classificationPath,String fusionPath,int[] topicsInt,int[] all_topic_ji,int[] all_topic_ou,int StartNum,int EndNum,int startExperimentNum,int EndExperimentNum
            ,String Generatefile_output,String weights_output,String qrelsPath,String newWeights_output) throws Exception {
        FusionMain.setAll_topic(topicsInt);//把topic集合设置
        FusionMain.setAll_topic_ji(all_topic_ji);//设置奇topic集合
        FusionMain.setAll_topic_ou(all_topic_ou);//设置偶topic集合
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
                String weightsFilePath = weights_output +"\\"+"weights_"+Num+"_"+ExperimentNum;//此次权重文件路径 这是fusion3的权重路径
                String newWeightsFilePath = newWeights_output +"\\"+"weights_"+Num+"_"+ExperimentNum;
                fusion11_handleWeightsFile(weightsFilePath,newWeightsFilePath,2);  //对权重进行处理,并将其输出到新路径中
                String FusionFileName = "LCfusion_"+Num+"_"+ExperimentNum;//此次运行结果路径

                String outputFusionPath = outputPath + FusionFileName;
                FusionMain.RunProgram(fusionNames.length,2,fusionNames,runsPath,qrelsPath,ExperimentNum,Generatefile_output,newWeights_output,outputFusionPath);
            }
        }

    }



    public static void main(String[] args) throws Exception {
       /* String runsPath = "D:\\TREC数据集文件\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\standard_input_nor30-60\\";
        String classificationPath = "D:\\TREC数据集文件\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\classification\\";
        String fusionPath = "D:\\TREC数据集文件\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\fusion17\\fusion\\";
        String Generatefile_output="";
        String weights_output="D:\\TREC数据集文件\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\fusion17\\LCfusion\\Weightfile\\";
        String newWeights_output = "D:\\TREC数据集文件\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\fusion17\\LCfusion\\newWeightfile\\";
        String qrelsPath= "";(原版代码)*/
        String runsPath = "F:\\TREC 数据集\\2019MedicineTrackScientific\\standard-input-nor1-60_top1-10runs\\";
        String classificationPath = "F:\\TREC 数据集\\2019MedicineTrackScientific\\classification\\";
        String fusionPath = "F:\\TREC 数据集\\2019MedicineTrackScientific\\fusionTest\\fusion\\";
        String Generatefile_output="F:\\TREC 数据集\\2019MedicineTrackScientific\\fusionTest\\LCfuison\\GenerateFileFQ\\";
        String weights_output="F:\\TREC 数据集\\2019MedicineTrackScientific\\fusionTest\\LCfuison\\weightsFileFQ\\";
        String newWeights_output = "F:\\TREC 数据集\\2019MedicineTrackScientific\\fusionTest\\LCfuison\\newWeightfile\\";
        String qrelsPath= "";
        int[] topicsInt ={
                1,2,3,4,5,6,7,8,9,10,
                11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30,
                31,32,33,34,35,36,37,38,39,40,
               /* 41,42,43,44,45,46,47,48,49,50*/};
        int[] all_topic_ji = {
                1, 3, 5, 7, 9,
                11,13,15,17,19,
                21,23,25,27,29,
                31,33,35,37,39,
                /*41,43,45,47,49*/
        };
        int[] all_topic_ou = {
                2, 4, 6, 8, 10,
                12,14,16,18,20,
                22,24,26,28,30,
                32,34,36,38,40,
               /* 42,44,46,48,50*/
        };
        int StartNum = 10;
        int EndNum = 10;
        int startExperimentNum = 1;
        int EndExperimentNum =1;
        RunProgram(runsPath,classificationPath,fusionPath,topicsInt,all_topic_ji,all_topic_ou,StartNum,EndNum,startExperimentNum,EndExperimentNum,
                Generatefile_output,weights_output,qrelsPath,newWeights_output);
    }
}
