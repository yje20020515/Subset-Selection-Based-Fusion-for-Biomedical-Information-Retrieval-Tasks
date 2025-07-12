package com.algorithm.fusion_Main;

import com.algorithm.DataStruct.*;
import com.algorithm.Distance.StandardDistance;
import com.algorithm.fusionAlgorithm.FastCombSUM;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * 1.实现融合分类后的每个簇的CombSUM融合和CombMNZ融合
 * 2.实现使用上述融合结果求每个簇成员和该簇融合结果的距离比较
 * 3.使用距离比较得到每个簇距离最小的run，然后输出到一个XLS文件中
 * 4.使用CombSUM、CombMNZ方法进行最后的融合
 */
public class fusion10_Main {
    private static HashMap<HashMapDocKey, HashMapDocValue> changeArrayListToHashMapList(ArrayList<ArrayDocs> arrayList){
        HashMap<HashMapDocKey, HashMapDocValue> hashMapList = new HashMap<>();
        for (ArrayDocs arrayDocs : arrayList) {
            for (Doc doc : arrayDocs.getList()) {
                int topic = doc.getTopic();
                String docID = doc.getDocID();
                Integer rank = doc.getRank();
                Double score = doc.getScore();
                String systemName = doc.getSystemName();
                HashMapDocKey hashMapDocKey = new HashMapDocKey(topic,docID);
                HashMapDocValue hashMapDocValue = new HashMapDocValue(rank,score,systemName);
                hashMapList.put(hashMapDocKey,hashMapDocValue);
            }
        }
        return hashMapList;
    }
    private static void runProgram(String classificationXLSPath,String runPaths,Integer[] topics,String outputXLSPath,String outputFusionPath,String SystemName,int NUM) throws IOException {

        FileInputStream fis = new FileInputStream(classificationXLSPath);
        HSSFWorkbook hw = new HSSFWorkbook(fis);
        Sheet sheet = hw.getSheetAt(0);
        Row row;

        outputXLSPath = outputXLSPath + "reSort_K"+NUM+"_1.xls";
        FileOutputStream fos = new FileOutputStream(outputXLSPath);
        HSSFWorkbook hwFOS = new HSSFWorkbook();
        Sheet sheetFOS = hwFOS.createSheet();
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            String[] fusionNames = new String[row.getPhysicalNumberOfCells()];
            ArrayList<RunPerformanceValues> runNameDistanceList = new ArrayList<>();
            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                fusionNames[j] = row.getCell(j).getStringCellValue();
                System.out.print(fusionNames[j]+"\t");
            }
            System.out.println();
            //融合fusionNames
            ArrayList<ArrayDocs> finalList = FastCombSUM.RunProgram(runPaths, topics, fusionNames);
            //转换格式
            HashMap<HashMapDocKey, HashMapDocValue> finalListHashMapList = changeArrayListToHashMapList(finalList);
            //计算距离
            for (String fusionName : fusionNames) {
                double tempDistance = StandardDistance.getRunARunBDistance(finalListHashMapList,fusionName);
                RunPerformanceValues runBean = new RunPerformanceValues(fusionName,tempDistance);
                runNameDistanceList.add(runBean);
            }
            //重新排序，安装距离从小到大
            sortForPerformanceValuesSmallToLarge(runNameDistanceList);//Sort from small to large 从小到大
            //sortForPerformanceValuesLargeToSmall(runNameDistanceList);//Sort from large to small 从大到小
            for (RunPerformanceValues runPerformanceValues : runNameDistanceList) {
                System.out.println(runPerformanceValues.getName()+"\t"+runPerformanceValues.getPerformanceValues());
            }
            //输出到新的xls文件中
            writeNewClassificationFile(fos,sheetFOS,i,runNameDistanceList);
        }
        hwFOS.write(fos);
        fis.close();
        fos.close();
        hw.close();
        //进行融合实验
        fusionExperiment(outputXLSPath,runPaths,topics,outputFusionPath,SystemName,NUM);
    }

    private static void fusionExperiment(String outputXLSPath, String runPaths, Integer[] topic, String outputFusionPath, String systemName, int NUM) throws IOException {

        outputFusionPath = outputFusionPath+"\\"+"fusion10_"+NUM+"_1";
        FastCombSUM.RunProgram(runPaths,topic,outputFusionPath,systemName,NUM,outputXLSPath);
    }

    private static void writeNewClassificationFile(FileOutputStream fos, Sheet sheetFOS, int i, ArrayList<RunPerformanceValues> runNameDistanceList) throws IOException {
        Row row = sheetFOS.createRow(i);
        for (int j = 0; j < runNameDistanceList.size(); j++) {
            row.createCell(j).setCellValue(runNameDistanceList.get(j).getName().split("\\\\")[runNameDistanceList.get(j).getName().split("\\\\").length-1]);

        }
        fos.flush();
    }

    private static void sortForPerformanceValuesSmallToLarge(ArrayList<RunPerformanceValues> runNameDistanceList) {
        Collections.sort(runNameDistanceList, new Comparator<RunPerformanceValues>() {
            @Override
            public int compare(RunPerformanceValues o1, RunPerformanceValues o2) {
                if (o1.getPerformanceValues() > o2.getPerformanceValues()){
                    return 1;
                }else {
                    return -1;
                }
            }
        });
    }
    private static void sortForPerformanceValuesLargeToSmall(ArrayList<RunPerformanceValues> runNameDistanceList) {
        Collections.sort(runNameDistanceList, new Comparator<RunPerformanceValues>() {
            @Override
            public int compare(RunPerformanceValues o1, RunPerformanceValues o2) {
                if (o1.getPerformanceValues() > o2.getPerformanceValues()){
                    return 1;
                }else {
                    return -1;
                }
            }
        });
    }
    public static void main(String[] args) throws IOException {
        String classificationXLSPath ="E:\\TRECDateset\\paperExperiment\\test\\2012Session\\Adhoc\\classification\\10\\AGG_K10_1.xls";
        String runPaths ="E:\\TRECDateset\\paperExperiment\\test\\2012Session\\Adhoc\\standard_input_nor30-60\\";
        Integer[] topic={
                1,2,3,4,5,6,7,8,9,10,
                11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30,
                31,32,33,34,35,36,37,38,39,40,
                41,42,43,44,45,46,47,48,49,50,
                51,52,53,54,55,56,57,58,59,60,
                61,62,63,64,65,66,67,68,69,70,
                71,72,73,74,75,76,77,78,79,80,
                81,82,83,84,85,86,87,88,89,90,
                91,92,93,94,95,96,97,98
        };
        String outputXLSPath = "E:\\TRECDateset\\paperExperiment\\test\\2012Session\\Adhoc\\fusion10\\reSortClassification\\";
        String outputPath = "E:\\TRECDateset\\paperExperiment\\test\\2012Session\\Adhoc\\fusion10\\fusion\\";
        String SystemName ="fusion10";
        int NUM = 10;
        runProgram(classificationXLSPath,runPaths,topic,outputXLSPath,outputPath,SystemName,NUM);
    }
}
