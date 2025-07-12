package com.GetRandomSelectSystem;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class getRandomSelectSystemMain {
    /**
     * 1.ȷ������ĸ���N
     * 2.�õ�����ϵͳ������
     * 3.�����ȡ����������ͬϵͳ�������ٴγ�ȡ
     * 4.����ȡ����ϵͳ�����������һ��xls�ļ��У�������ʽΪRandom_K?_?,���е�һ���ʺŴ���Nֵ���ڶ����ʺŴ�������Ĵ���
     */


    public static void main(String[] args) throws IOException {
        int StartNum = 3;
        int EndNum = 20;
        int startExperimentNum = 1;
        int EndExperimentNum =200;
        String allRunPath = "D:\\TREC���ݼ��ļ�\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\standard_input\\";
        String[] AllFusionNames = getAllFusionNames(allRunPath);
        String[] fusionNames = null;

        for (int Num = StartNum; Num <= EndNum ; Num++) {
            String outputPath ="D:\\TREC���ݼ��ļ�\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\classification\\"+Num;
            createDirectory(outputPath);
            for (int experimentNum = startExperimentNum; experimentNum <= EndExperimentNum ; experimentNum++) {
                if (Num<=AllFusionNames.length){
                    fusionNames =getRandomFusionNames(AllFusionNames,Num);
                }else {
                    System.err.println("ѡȡ������������������");
                }
                String outputXlsPath = outputPath+"\\Random_K"+Num+"_"+experimentNum+".xls";
                if (fusionNames != null){
                    writeXls(outputXlsPath,fusionNames);
                }else {
                    System.err.println("fusionNamesΪ��");
                }
            }
        }





    }

    private static void createDirectory(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    private static void writeXls(String outputPath, String[] fusionNames) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputPath);
        HSSFWorkbook hw = new HSSFWorkbook();
        Sheet sheet = hw.createSheet();
        Row row;
        int count = 0;
        for (String fusionName : fusionNames) {
            row = sheet.createRow(count++);
            row.createCell(0).setCellValue(fusionName);
        }

        hw.write(fos);
        fos.close();
    }

    public static boolean repeat(ArrayList<Integer> randomList,int OneRandom){
        for (int oneInt : randomList) {
            if (OneRandom == oneInt){
                return true;
            }
        }
        return false;
    }

    private static String[] getRandomFusionNames(String[] allFusionNames, int num) {
        Random random = new Random();
        String[] fusionNames = new String[num];
        ArrayList<Integer> randomList = new ArrayList<>();
        while (randomList.size()<num) {
            int OneRandom = random.nextInt(allFusionNames.length);
            if (!repeat(randomList,OneRandom)){
                randomList.add(OneRandom);
            }
        }
        int count = 0;
        for (Integer position: randomList) {
            fusionNames[count++] = allFusionNames[position];
        }
        System.out.println("-----------------------------------------------");
        for (String fusionName : fusionNames) {
            System.out.println(fusionName);
        }
        return fusionNames;
    }

    private static String[] getAllFusionNames(String allRunPath) {
        File[] files = new File(allRunPath).listFiles();
        String[] AllFusionNames = new String[files.length];
        for (int i = 0; i < AllFusionNames.length; i++) {
            AllFusionNames[i] = files[i].getName();
            System.out.println(AllFusionNames[i]);
        }
        return AllFusionNames;
    }

}
