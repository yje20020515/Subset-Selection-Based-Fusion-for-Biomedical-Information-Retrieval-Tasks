package com.algorithm.Microblog2015.splitByDay;

import com.algorithm.Microblog2015.DatasetStruct.Result;

import java.io.*;
import java.util.ArrayList;

/**
 * 获取一个原始的run文件，将run按照时间分割，分割后的命名方式为201507XX_runName
 */
public class splitRunByDay {
    public static int[] topics ={226,227,228,236,242,243,246,248,249,253,
                                254,255,260,262,265,267,278,284,287,298,
                                305,324,326,331,339,344,348,353,354,357,
                                359,362,366,371,377,379,383,384,389,391,
                                392,401,400,405,409,416,419,432,434,439,448};

    public static boolean existTopic(int topic){
        for (int t : topics) {
            if(t==topic){
                return true;
            }
        }
        return false;
    }

    public static void splitByDay(String InputRunPath,String runName,String outputRunPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(InputRunPath+"\\"+runName));

        ArrayList<Result>[] lists = new ArrayList[10];
        for (int i=0;i< lists.length;i++){
            lists[i] = new ArrayList<>();
        }
        String line = "";
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            int date = Integer.parseInt(str[0]);
            int topic = Integer.parseInt(str[1].substring(2));
            if (existTopic(topic)){
                String docID = str[3];
                int rank = Integer.parseInt(str[4]);
                double score = Double.parseDouble(str[5]);
                String systemName = str[6];
                switch (date){
                    case 20150720:
                        lists[0].add(new Result(date+"",topic,docID,rank,score,systemName));
                        break;
                    case 20150721:
                        lists[1].add(new Result(date+"",topic,docID,rank,score,systemName));
                        break;
                    case 20150722:
                        lists[2].add(new Result(date+"",topic,docID,rank,score,systemName));
                        break;
                    case 20150723:
                        lists[3].add(new Result(date+"",topic,docID,rank,score,systemName));
                        break;
                    case 20150724:
                        lists[4].add(new Result(date+"",topic,docID,rank,score,systemName));
                        break;
                    case 20150725:
                        lists[5].add(new Result(date+"",topic,docID,rank,score,systemName));
                        break;
                    case 20150726:
                        lists[6].add(new Result(date+"",topic,docID,rank,score,systemName));
                        break;
                    case 20150727:
                        lists[7].add(new Result(date+"",topic,docID,rank,score,systemName));
                        break;
                    case 20150728:
                        lists[8].add(new Result(date+"",topic,docID,rank,score,systemName));
                        break;
                    case 20150729:
                        lists[9].add(new Result(date+"",topic,docID,rank,score,systemName));
                        break;
                }
            }

        }
//        for (ArrayList<Result> list : lists) {
//            Collections.sort(list, new Comparator<Result>() {
//                @Override
//                public int compare(Result o1, Result o2) {
//                    if (o1.getTopic()== o2.getTopic()){
//                        return o1.getRank()- o2.getRank();
//                    }
//                    return o1.getTopic()- o2.getTopic();
//                }
//            });
//        }
        for (ArrayList<Result> list : lists) {
            if (list .size()==0){
                System.out.println(runName);
                continue;
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputRunPath+"\\"+list.get(0).getDate()+"_"+runName));
            for (Result result : list) {
                bw.write(result.getTopic()+"\t"
                        +"Q0"+"\t"
                        +result.getDocID()+"\t"
                        +result.getRank()+"\t"
                        +result.getScore()+"\t"
                        +result.getSystemName()+"\n");
            }
            bw.close();
        }

        /**
         * 进行LC融合前的分数归一化
         */
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputRunPath+"\\"+"LC_"+runName));
        for (ArrayList<Result> list : lists) {
            if (list .size()==0){
                System.out.println(runName);
                continue;
            }
            for (Result result : list) {
                //double score = 30.0/(60.0+ result.getRank());
                double score = result.getScore();
                bw.write(result.getTopic()+"\t"
                        +"Q0"+"\t"
                        +result.getDocID()+"\t"
                        +result.getRank()+"\t"
                        +score+"\t"
                        +result.getSystemName()+"\n");
            }
        }
        bw.close();
        br.close();
    }

    public static void nor_date_run(String InputRunPath,String runName,String outputRunPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(InputRunPath+"\\"+runName));
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputRunPath+"\\"+runName));

        String line = "";
        while((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            String docID = str[2];
            int rank = Integer.parseInt(str[3]);
            double score = Double.parseDouble(str[4]);
            //score = 30.0/(60.0+rank);
            String systemName = str[5];

            bw.write(topic+"\t"
                        +"Q0"+"\t"
                        +docID+"\t"
                        +rank+"\t"
                        +score+"\t"
                        +systemName+"\n");
        }
        br.close();
        bw.close();
    }
    public static void main(String[] args) throws IOException {
//        String InputRunPath = "D:\\TREC数据集文件\\2015Microblog\\Microblog_TaskB\\test";
//        String OutputRunPath = "D:\\TREC数据集文件\\2015Microblog\\Microblog_TaskB\\outputTest";
//        String runName = "BJUTllyQE";
//        splitByDay(InputRunPath,runName,OutputRunPath);
        String date = "20150720";
        String InputRunPath = "D:\\TREC数据集文件\\2015Microblog\\Microblog_TaskB\\distance_myndcg10Top8_input";
        String OutputRunPath = "D:\\TREC数据集文件\\2015Microblog\\Microblog_TaskB\\fusion21\\split_myndcg10top8_raw_input\\20150720";
        File[] files = new File(InputRunPath).listFiles();
        for (File run : files) {
            String runName = run.getName();
            //nor_date_run(InputRunPath,runName,OutputRunPath);
            splitByDay(InputRunPath,runName,OutputRunPath);
        }


    }
}
