package com.fusion_qrel_012_Main;

import com.LCfusion.Main.StardardFusionMain.FusionMain;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.ArrayList;

public class fusion9Main {
    /**
     * LC�ں�ʵ�飨����ż��
     * ͨ��xls�ļ��õ�Ҫ�ںϵ�ϵͳ·��
     * fusion3�õ���weight�ļ������ٴδ�������ʽΪ����һ���ļ���Ȩ�ؽ��������ָ���������ƽ��ֵavg_weight��Ȼ��Ȩ�ص÷�Ϊweight-avg_weight
     * ����Ȩ���ļ��������LC�����ں�
     * ���õ��Ľ��������ΪΪһ���ļ�
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
     * @param runsPath ��Ҫ�ںϵ�run���ļ���·����һ��Ϊ30/60�Ĺ�һ��·��
     * @param classificationPath ��Ҫ�ںϵ�run�ĵ�classification·�������һᰴ��Ҫ�ںϵ�ϵͳ�����ٴδ����ļ���
     * @param fusionPath    Ҫ�ںϵĽ�����·��
     * @param topicsInt Ҫ�ںϵ�ȫ����topic����
     * @param all_topic_ji  Ҫ����żʱ�����topic����
     * @param all_topic_ou  Ҫ����żʱ��ż��topic����
     * @param StartNum  Ҫ�ںϵ�ϵͳ�����Ŀ�ʼ����
     * @param EndNum    Ҫ�ںϵ�ϵͳ�����Ľ�������
     * @param startExperimentNum    Ҫ�ظ�ʵ��Ŀ�ʼʵ�����
     * @param EndExperimentNum  Ҫ�ظ�ʵ��Ľ���ʵ�����
     * @param Generatefile_output   ��Ҫ������С������ϵ�Ȩ�ؼ���ʱ��Ҫ��generatefile·��
     * @param weights_output    Ȩ���ļ����·��������������С�������ʱ���ļ�Ӧ��Ϊ�գ�����P10��MAP��P10*DISSΪȨ��ʱӦ���ڵ�ǰҳ������м�����Ȩ�����Ȩ���ĵ�
     * @param qrelsPath ������С�������ʱ����Ҫ��qrel�ļ�·��
     * @throws Exception
     */
    private static void RunProgram(String runsPath,String classificationPath,String fusionPath,int[] topicsInt,int[] all_topic_ji,int[] all_topic_ou,int StartNum,int EndNum,int startExperimentNum,int EndExperimentNum
            ,String Generatefile_output,String weights_output,String qrelsPath,String newWeights_output) throws Exception {
        FusionMain.setAll_topic(topicsInt);//��topic��������
        FusionMain.setAll_topic_ji(all_topic_ji);//������topic����
        FusionMain.setAll_topic_ou(all_topic_ou);//����żtopic����
        for (int Num = StartNum; Num <= EndNum ; Num++) {
            String outputPath =fusionPath + Num+"\\";
            createDirectory(outputPath);
            for (int ExperimentNum = startExperimentNum; ExperimentNum <= EndExperimentNum ; ExperimentNum++) {
                System.err.println(Num+"\t"+ExperimentNum);
                String xls_path = classificationPath + Num+"\\Semi_K"+Num+"_"+ExperimentNum+".xls";//���Ϊxls·��
                String[] fusionNames = readFusionXls(xls_path,Num);//��ô˴�ʵ��Ӧ��Ҫ�ںϵ�fusionName����
                for (int NameNum = 0; NameNum < fusionNames.length; NameNum++) {
                    System.out.println(fusionNames[NameNum]);
                    fusionNames[NameNum] = runsPath + fusionNames[NameNum];//��fusionName�͹�һ���ļ�·��������ϣ��õ�Ҫ�ںϵ�run��·��
                }
                String weightsFilePath = weights_output +"\\"+"weights_"+Num+"_"+ExperimentNum;//�˴�Ȩ���ļ�·�� ����fusion3��Ȩ��·��
                String newWeightsFilePath = newWeights_output +"\\"+"weights_"+Num+"_"+ExperimentNum;
                fusion11_handleWeightsFile(weightsFilePath,newWeightsFilePath,2);  //��Ȩ�ؽ��д���,�������������·����
                String FusionFileName = "LCfusion_"+Num+"_"+ExperimentNum;//�˴����н��·��

                String outputFusionPath = outputPath + FusionFileName;
                FusionMain.RunProgram(fusionNames.length,2,fusionNames,runsPath,qrelsPath,ExperimentNum,Generatefile_output,newWeights_output,outputFusionPath);
            }
        }

    }



    public static void main(String[] args) throws Exception {
       /* String runsPath = "D:\\TREC���ݼ��ļ�\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\standard_input_nor30-60\\";
        String classificationPath = "D:\\TREC���ݼ��ļ�\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\classification\\";
        String fusionPath = "D:\\TREC���ݼ��ļ�\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\fusion17\\fusion\\";
        String Generatefile_output="";
        String weights_output="D:\\TREC���ݼ��ļ�\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\fusion17\\LCfusion\\Weightfile\\";
        String newWeights_output = "D:\\TREC���ݼ��ļ�\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\fusion17\\LCfusion\\newWeightfile\\";
        String qrelsPath= "";(ԭ�����)*/
        String runsPath = "F:\\TREC ���ݼ�\\2019MedicineTrackScientific\\standard-input-nor1-60_top1-10runs\\";
        String classificationPath = "F:\\TREC ���ݼ�\\2019MedicineTrackScientific\\classification\\";
        String fusionPath = "F:\\TREC ���ݼ�\\2019MedicineTrackScientific\\fusionTest\\fusion\\";
        String Generatefile_output="F:\\TREC ���ݼ�\\2019MedicineTrackScientific\\fusionTest\\LCfuison\\GenerateFileFQ\\";
        String weights_output="F:\\TREC ���ݼ�\\2019MedicineTrackScientific\\fusionTest\\LCfuison\\weightsFileFQ\\";
        String newWeights_output = "F:\\TREC ���ݼ�\\2019MedicineTrackScientific\\fusionTest\\LCfuison\\newWeightfile\\";
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
