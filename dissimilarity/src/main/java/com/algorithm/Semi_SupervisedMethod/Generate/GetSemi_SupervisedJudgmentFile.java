package com.algorithm.Semi_SupervisedMethod.Generate;

import com.algorithm.DataStruct.Result;
import com.algorithm.DataStruct.Results;

import java.io.*;
import java.util.ArrayList;

public class GetSemi_SupervisedJudgmentFile {
    /**
     * 1.获得全部的系统的文档，按照topic分类
     * 2.获得每个topic分类的前2.4.6.8.10...个文档，组合成一个伪judgment文件
     * 3.输出该文件命名方式为autoJudgmentFile_N
     */
    public static ArrayList<Results> finalResult = new ArrayList<>();
    public static final int DEEP = 1000;//单个topic输出结果最大排名个数
    /**
     *
     * @param runPath
     * @param getTopic
     * @return 返回runPath中以getTopic开头的result对象列表
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
     * 将输入的run中的docID和topic存储进results对象中;
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
     * 将int[]数组转换为集合对象
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
        String runsPath = "E:\\TREC 数据集\\2019MedicineTrackScientific\\standard-input-nor1-60\\";
        int SemiNUM =10;
        String FusionFileName = "JudgmentFileSemiNUM_"+SemiNUM;
        String outputPath ="E:\\TREC 数据集\\2019MedicineTrackScientific\\judgement\\"+FusionFileName;
        int[] topicsInt ={
                1, 2, 3, 4, 5,
                6, 7, 8, 9, 10,
                11, 12, 13, 14,
                15, 16, 17, 18,
                19, 20, 21, 22,
                23, 24, 25, 26,
                27, 28, 29, 30,
                31, 32, 33, 34,
                35, 36, 37, 38,
                39, 40,/* 41, 42,
                43, 44, 45, 46,
                47, 48, 49, 50*/
        };


        RunProgram(runsPath,topicsInt,outputPath,FusionFileName,SemiNUM);
    }
}
