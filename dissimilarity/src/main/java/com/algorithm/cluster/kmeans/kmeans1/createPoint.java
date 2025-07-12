package com.algorithm.cluster.kmeans.kmeans1;

import com.algorithm.DataStruct.HashMapDocKey;
import com.algorithm.DataStruct.HashMapDocValue;

import java.io.*;
import java.util.*;

/**
 * 用于将run转换为Point文件，以提供给k-means程序使用
 */
public class createPoint {
    public static void runProgram(String runPath,Integer[] topics,String outputRunPath) throws IOException {
        HashMap<HashMapDocKey, HashMapDocValue> nullPoint = getNullPoint(runPath, topics);
        writeNullPoint(nullPoint);
        writePoint(nullPoint,runPath,outputRunPath,topics);
    }

    private static void writeNullPoint(HashMap<HashMapDocKey, HashMapDocValue> nullPoint) throws IOException {
        String nullPointFilePath = "E:\\TRECDateset\\classificationEXP\\2020Health\\Adhoc\\nullPoint";
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(nullPointFilePath)));
        String bwLine ;
        for (Map.Entry<HashMapDocKey, HashMapDocValue> nullPointEntry : nullPoint.entrySet()) {
            HashMapDocKey nullPointEntryKey = nullPointEntry.getKey();
            bwLine = nullPointEntryKey.getTopic() + "\t" + nullPointEntryKey.getDocID() + "\t" +  "0" + "\n";
            bw.write(bwLine);
        }
        bw.close();
    }

    private static void writePoint(HashMap<HashMapDocKey, HashMapDocValue> nullPoint, String runPath, String outputRunPath,Integer[] topics) throws IOException {
        List<Integer> topicList = new ArrayList<>();
        topicList.addAll((Collection<? extends Integer>) Arrays.asList(topics));
        File[] files = new File(runPath).listFiles();
        for (File file : files) {
            String runName = file.getName();
            String outputRunFilePath = outputRunPath + "\\" + runName;
            BufferedReader br = new BufferedReader(new FileReader(file));
            String brLine = "";
            HashMap<HashMapDocKey, HashMapDocValue> oneRunHM = new HashMap<>();
            while ((brLine = br.readLine()) != null){
                String[] str = brLine.split("\\s+");
                int topic = Integer.parseInt(str[0]);
                if (!topicList.contains(topic)){
                    continue;
                }
                String docID = str[2];
                int rank = Integer.parseInt(str[3]);
                double score = Double.parseDouble(str[4]);
                String systemName = str[5];
                oneRunHM.put(new HashMapDocKey(topic,docID),new HashMapDocValue(rank,score,systemName));
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputRunFilePath)));
            String bwLine ;
            for (Map.Entry<HashMapDocKey, HashMapDocValue> nullPointEntry : nullPoint.entrySet()) {
                HashMapDocKey nullPointEntryKey = nullPointEntry.getKey();
                HashMapDocValue nullPointEntryValue = nullPointEntry.getValue();
                HashMapDocValue oneRunHMValue = oneRunHM.get(nullPointEntryKey);
                if (oneRunHMValue!=null){
                    bwLine = nullPointEntryKey.getTopic() + "\t" + nullPointEntryKey.getDocID() + "\t" +  oneRunHMValue.getScore() + "\n";
                    bw.write(bwLine);
                }else {
                    bwLine = nullPointEntryKey.getTopic() + "\t" + nullPointEntryKey.getDocID() + "\t" + "0"  + "\n";
                    bw.write(bwLine);
                }
            }
            bw.close();
            br.close();

        }
    }

    private static HashMap<HashMapDocKey, HashMapDocValue> getNullPoint(String runPath, Integer[] topics) throws IOException {
        List<Integer> topicList = new ArrayList<>();
        topicList.addAll((Collection<? extends Integer>) Arrays.asList(topics));
        HashMap<HashMapDocKey, HashMapDocValue> nullPoint = new HashMap<>();
        File[] files = new File(runPath).listFiles();
        for (File file : files) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null){
                String[] str = line.split("\\s+");
                int topic = Integer.parseInt(str[0]);
                if (!topicList.contains(topic)){
                    continue;
                }
                String docID = str[2];
                int rank = Integer.parseInt(str[3]);
                double score = Double.parseDouble(str[4]);
                String systemName = str[5];
                nullPoint.put(new HashMapDocKey(topic,docID),new HashMapDocValue(rank,score,systemName));
            }
            br.close();
        }
        return nullPoint;
    }

    public static void main(String[] args) throws IOException {
        String runPath= "E:\\TRECDateset\\classificationEXP\\2020Health\\Adhoc\\standard_input_nor30-60\\";
        Integer[] topics={
                1,2,3,4,5,6,7,8,9,10,
                11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30,
                31,32,34,36,37,38,39,40,41,42,
                43,44,45,47,49,50,
        };
        String outputRunPath="E:\\TRECDateset\\classificationEXP\\2020Health\\Adhoc\\input_Point\\";
        runProgram(runPath,topics,outputRunPath);

    }
}
