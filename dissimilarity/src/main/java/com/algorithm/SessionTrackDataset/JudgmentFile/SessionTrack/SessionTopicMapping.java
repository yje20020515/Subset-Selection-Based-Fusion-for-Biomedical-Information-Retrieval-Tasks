package com.algorithm.SessionTrackDataset.JudgmentFile.SessionTrack;

import com.algorithm.DataStruct.Qrel;
import com.algorithm.DataStruct.Qrels;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * 将2012Session的JudgmentFile数据进行映射
 */
public class SessionTopicMapping {
    private static TreeMap<Integer,Integer> topicMapping = new TreeMap<>();
    private static ArrayList<Qrels> qrelsTopicList = new ArrayList<>();
    private static void readSessionTopicMappingFile(String sessionTopicMappingPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(sessionTopicMappingPath));
        String line = "";
        while ((line = br.readLine())!=null){
            String[]  str = line.split("\\s+");
            Integer session = Integer.parseInt(str[0]);
            Integer topic = Integer.parseInt(str[1]);
            topicMapping.put(session,topic);
        }
        br.close();
    }
    private static void initQrelsTopicList(Integer[] topics){
        for (Integer topic : topics) {
            Qrels qs = new Qrels(topic);
            qrelsTopicList.add(qs);
        }
    }
    private static void getOneTopicQrels(String judgmentFilePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(judgmentFilePath));
        String line = "";
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            Integer tempTopic = Integer.parseInt(str[0]);
            String docID = str[2];
            Double rel = Double.parseDouble(str[3]);
            Qrel q = new Qrel(tempTopic,docID,rel);
            for (Qrels qrels : qrelsTopicList) {
                if (qrels.getTopic() == tempTopic){
                    qrels.getList().add(q);
                    break;
                }
            }

        }
    }
    public static void runProgram(String sessionTopicMappingPath,String judgmentFilePath,String outputPath,Integer[] topics ) throws IOException {
        initQrelsTopicList(topics);
        readSessionTopicMappingFile(sessionTopicMappingPath);
        getOneTopicQrels(judgmentFilePath);
        writeMappingJudgmentFile(outputPath);
    }

    private static void writeMappingJudgmentFile(String outputPath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
        for (Map.Entry<Integer, Integer> STEntry : topicMapping.entrySet()) {
            Integer session = STEntry.getKey();
            Integer topic = STEntry.getValue();
            for (Qrels qrels : qrelsTopicList) {
                if (topic == qrels.getTopic()){
                    for (Qrel qrel : qrels.getList()) {
                        String line = "";
                        line = session+"\t"+"0"+"\t"+qrel.getDocID()+"\t"+qrel.getRel()+"\n";
                        bw.write(line);
                    }
                }
            }
        }
        bw.close();
    }

    public static void main(String[] args) throws IOException {
        String sessionTopicMappingPath = "E:\\TRECDataset\\methode_qrelNegative\\2012Session\\Judgmentfile\\SessionTopicMappingFile";
        String judgmentFilePath = "E:\\TRECDataset\\methode_qrelNegative\\2012Session\\Judgmentfile\\judgmentfile.txt";
        String outputPath = "E:\\TRECDataset\\methode_qrelNegative\\2012Session\\Judgmentfile\\MappingJudgmentFileRaw";
        Integer[] topics = {
                1,2,3,4,5,6,7,8,9,10,
                11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30,
                31,32,33,34,35,36,37,38,39,40,
                41,42,43,44,45,46,47,48,
        };
        runProgram(sessionTopicMappingPath,judgmentFilePath,outputPath,topics);
    }
}
