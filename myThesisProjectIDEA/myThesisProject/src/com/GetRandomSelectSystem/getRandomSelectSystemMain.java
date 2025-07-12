package com.GetRandomSelectSystem;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class getRandomSelectSystemMain {
    /**
     * 1.确定随机的个数N
     * 2.得到所有系统的名字
     * 3.随机抽取，若含有相同系统则重新再次抽取
     * 4.将抽取到的系统的名字输出到一个xls文件中，命名方式为Random_K?_?,其中第一个问号代表N值，第二个问号代表随机的次数
     */


    public static void main(String[] args) throws IOException {
        int StartNum = 3;
        int EndNum = 20;
        int startExperimentNum = 1;
        int EndExperimentNum =200;
        String allRunPath = "D:\\TREC数据集文件\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\standard_input\\";
        String[] AllFusionNames = getAllFusionNames(allRunPath);
        String[] fusionNames = null;

        for (int Num = StartNum; Num <= EndNum ; Num++) {
            String outputPath ="D:\\TREC数据集文件\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\classification\\"+Num;
            createDirectory(outputPath);
            for (int experimentNum = startExperimentNum; experimentNum <= EndExperimentNum ; experimentNum++) {
                if (Num<=AllFusionNames.length){
                    fusionNames =getRandomFusionNames(AllFusionNames,Num);
                }else {
                    System.err.println("选取的数量大于总体数量");
                }
                String outputXlsPath = outputPath+"\\Random_K"+Num+"_"+experimentNum+".xls";
                if (fusionNames != null){
                    writeXls(outputXlsPath,fusionNames);
                }else {
                    System.err.println("fusionNames为空");
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
