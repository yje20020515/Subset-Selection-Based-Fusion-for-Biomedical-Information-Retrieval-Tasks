package com.Semi_SupervisedMethod.Generate;

import com.DataStruct.Result;
import com.DataStruct.Results;

import java.io.*;
import java.util.ArrayList;

public class GetSemi_SupervisedJudgmentFile {
    /**
     * 1.?????????????????????topic????
     * 2.??????topic??????2.4.6.8.10...??????????????αjudgment???
     * 3.????????????????autoJudgmentFile_N
     */
    public static ArrayList<Results> finalResult = new ArrayList<>();
    public static final int DEEP = 1000;//????topic?????????????????
    /**
     *
     * @param runPath
     * @param getTopic
     * @return ????runPath????getTopic?????result?????б?
     * @throws IOException
     */
    public static Results getResults(String runPath,int getTopic,int semiNUM) throws IOException {
        Results Rs = new Results(getTopic);
        BufferedReader br = new BufferedReader(new FileReader(runPath));
        String line = "";
        int rank = 1;

        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            if(topic==getTopic){
                String docID = str[2];
                double score = Double.parseDouble(str[4]);
                // System.out.println(score);
                String systemName = str[5];
                Result r = new Result(topic,docID,rank,score,systemName);
                Rs.getList().add(r);
                if (rank == semiNUM){
                    break;
                }
                rank++;
            }
        }
        br.close();
        return Rs;
    }

    /**
     * ???????run?е?docID??topic?洢??results??????;
     * @param runPath
     * @param topicsInt
     * @throws IOException
     */
    public static ArrayList<Results> getRun(String runPath,int[] topicsInt,int SemiNUM) throws IOException {
        ArrayList<Results> run = new ArrayList<>();
        ArrayList<Integer> topics = changeInt(topicsInt);
        for (Integer topic : topics) {
            run.add(getResults(runPath,topic,SemiNUM));
        }
        return run;
    }

    /**
     * ??int[]???????????????
     * @param topicsInt
     * @return
     */
    public static ArrayList<Integer> changeInt(int[] topicsInt){
        ArrayList<Integer> topics = new ArrayList<>();
        for (int topic : topicsInt) {
            topics.add(topic);
        }
        return topics;
    }

    public static void initFinalResult(int[] topicsInt){
        for (int topic : topicsInt) {
            finalResult.add(new Results(topic));
        }
    }
    public static void Run_Semi_Supervised(String runsPath,int[] topicsInt,String outputPath,String FusionName,int SemiNUM) throws IOException {
        File path = new File(runsPath);
        File[] runs = path.listFiles();
        initFinalResult(topicsInt);
        for (File runPath : runs) {
            System.out.println(runPath);
            ArrayList<Results> run = getRun(runPath.toString(),topicsInt,SemiNUM);
            getSemiSupervisedJudgmentFile(run,topicsInt,SemiNUM);
        }
        WriteSemi_JudgmentFile(outputPath);
    }
    private static void WriteSemi_JudgmentFile(String outputPath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));
        String oneLine = "";
        for (Results results : finalResult) {
            for (Result result : results.getList()) {
                bw.write(result.getTopic()+"\t"+
                        "Q0"+"\t"+
                        result.getDocID()+"\t"+
                        "1"+"\n");
            }
        }
        bw.close();
    }
    private static void getSemiSupervisedJudgmentFile(ArrayList<Results> run, int[] topicsInt, int semiNUM) {
        for (Results Topics : run) {
            for (Results finalTopics : finalResult) {
                if (finalTopics.getTopic()==Topics.getTopic()){
                    for (Result result : Topics.getList()) {
                        if (!finalTopics.getList().contains(result)){
                            finalTopics.getList().add(result);
                        }
                    }
                }
            }

        }
    }

    public static void RunProgram(String runsPath,int[] topicsInt,String outputPath,String FusionName,int SemiNUM) throws IOException {
        Run_Semi_Supervised(runsPath,topicsInt,outputPath,FusionName,SemiNUM);
    }
    public static void main(String[] args) throws IOException {
        //String runsPath = "F:\\TREC 数据集\\2019MedicineTrackScientific\\standard-input-nor1-60\\";//原版
        String runsPath = "E:\\TREC 数据集\\2018MedicineTrackScientific\\standard-input-nor-19\\";//从前10个MAP值较好的runs里面抽取；
        int SemiNUM =2;
        String FusionFileName = "JudgmentFileSemiNUM_"+SemiNUM;
        String outputPath ="E:\\TREC 数据集\\2018MedicineTrackScientific\\judgement\\"+FusionFileName;
        int[] topicsInt = {
                1, 2, 3, 4, 5,
                6, 7, 8, 9, 10,
                11, 12, 13, 14,
                15, 16, 17, 18,
                19, 20, 21, 22,
                23, 24, 25, 26,
                27, 28, 29, 30,
                31, 32, 33, 34,
                35, 36, 37, 38,
                39, 40, 41, 42,
                43, 44, 45, 46,
                47, 48, 49, 50
        };
        /* 2021deeplearning topic
                2082,23287,30611,112700,168329,
                190623,226975,237669,253263,300025,
                300986,337656,364210,395948,421946,
                493490,505390,508292,540006,596569,
                615176,629937,632075,646091,647362,
                661905,681645,688007,707882,764738,
                806694,818583,832573,835760,845121,
                935353,935964,952262,952284,975079,
                1006728,1040198,1103547,1104300,1104447,
                1107704,1107821,1109840,1110996,1111577,
                1113361,1117243,1117298,1118716,1121909,
                1128632,1129560};*/

        RunProgram(runsPath,topicsInt,outputPath,FusionFileName,SemiNUM);
    }
}
