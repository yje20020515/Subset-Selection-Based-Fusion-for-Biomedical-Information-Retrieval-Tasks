package com.algorithm.evaluation;

import com.algorithm.DataStruct.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 用于进行J指标的评估
 */
public class evalIndicatorJ {
    public static ArrayList<ArrayDocs> getRun(String runPath,Integer[] topics) throws IOException {
        ArrayList<ArrayDocs> run = initRunTopic(topics);
        BufferedReader br = new BufferedReader(new FileReader(runPath));
        String line = "";
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            for (ArrayDocs arrayDocs : run) {
                if (arrayDocs.getTopic() == topic){
                    String docID = str[2];
                    int rank = Integer.parseInt(str[3]);
                    double score = Double.parseDouble(str[4]);
                    String systemName = str[5];
                    Doc doc = new Doc(topic,docID,rank,score,systemName);
                    arrayDocs.getList().add(doc);
                    break;
                }
            }
        }
        br.close();
        return run;
    }

    private static ArrayList<ArrayDocs> initRunTopic(Integer[] topics) {
        ArrayList<ArrayDocs> docsList = new ArrayList<>();
        for (Integer topic : topics) {
            ArrayDocs docs = new ArrayDocs(topic);
            docsList.add(docs);
        }
        return docsList;
    }

    public static ArrayList<ArrayQrels> getQrel(String qrelPath,Integer[] topics) throws IOException {
        ArrayList<ArrayQrels> judgmentFile = initQrelsTopic(topics);
        BufferedReader br = new BufferedReader(new FileReader(qrelPath));
        String line = "";
        while((line = br.readLine() )!=null){
            String[] str = line.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            for (ArrayQrels arrayQrels : judgmentFile) {
                if (arrayQrels.getTopic() == topic){
                    String docID = str[2];
                    double rel = Double.parseDouble(str[3]);
                    arrayQrels.getList().add(new Qrel(topic,docID,rel));
                }
            }
        }
        br.close();
        return judgmentFile;
    }

    private static ArrayList<ArrayQrels> initQrelsTopic(Integer[] topics) {
        ArrayList<ArrayQrels> judgmentFile = new ArrayList<>();
        for (Integer topic : topics) {
            ArrayQrels arrayQrels = new ArrayQrels(topic);
            judgmentFile.add(arrayQrels);
        }
        return judgmentFile;
    }



    private static double calculationIndicator(ArrayList<ArrayDocs> run, ArrayList<ArrayQrels> judgment) {
        for (ArrayDocs arrayDocs : run) {
            for (ArrayQrels arrayQrels : judgment) {
                if (arrayQrels.getTopic() == arrayDocs.getTopic()){
                    for (Doc doc : arrayDocs.getList()) {
                        for (Qrel qrel : arrayQrels.getList()) {
                            if (doc.getTopic() == qrel.getTopic() && doc.getDocID().equals(qrel.getDocID()) ){
                                doc.setRel(qrel.getRel());
                                break;
                            }
                        }

                    }
                    break;
                }
            }
        }
        double indicatorJ = 0.0;
        for (ArrayDocs arrayDocs : run) {
            double lnL = Math.log(arrayDocs.getList().size());
            double docsIndicatorJ = 0.0;
            for (int i = 0; i < arrayDocs.getList().size(); i++) {
                double lni = Math.log(i+1);
                double temp = (1 - lni/lnL)* (arrayDocs.getList().get(i).getRel()>0?1:0);
                docsIndicatorJ += temp;
            }
            indicatorJ += docsIndicatorJ;
        }
        indicatorJ = indicatorJ/run.size();
        return indicatorJ;
    }
    public static void runProgram(String runPath,String qrelPath,Integer[] topics) throws IOException {
        ArrayList<ArrayQrels> judgment = getQrel(qrelPath, topics);

        File files = new File(runPath);
        for (File file : files.listFiles()) {
            ArrayList<ArrayDocs> run = getRun(file.getPath(), topics);

            double indicatorJ = calculationIndicator(run, judgment);
            System.out.println(file.getName() + "\t"+indicatorJ);
        }

    }

    public static void main(String[] args) throws IOException {
        String runPath = "E:\\TRECDateset\\paperExperiment\\ideaOne\\2021Health\\adhoc\\standard_input_nor30-60\\";
        String qrelPath = "E:\\TRECDateset\\paperExperiment\\ideaOne\\2021Health\\judgment\\misinfo-resources-2021\\qrels\\2021-derived-qrels\\misinfo-qrels-binary.useful";
        Integer[] topics ={
                101,102,103,104,105,106,107,108,109,110,
                111,112,114,115,117,118,120,121,122,127,
                128,129,131,132,133,134,136,137,139,140,
                143,144,145,146,149,
        };
        runProgram(runPath,qrelPath,topics);
    }
}
