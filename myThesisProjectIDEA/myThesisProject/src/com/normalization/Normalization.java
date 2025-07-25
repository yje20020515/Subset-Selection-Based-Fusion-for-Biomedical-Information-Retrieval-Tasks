package com.normalization;

import com.DataStruct.Result;
import com.DataStruct.Results;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Normalization {
    /**
     * 对run进行分数归一化
     * type1:30/60+rank;
     * type2:1/N,1/2*N,1/3*N...1/10*N;
     */
    public static int DEATH = 1000;

    /**
     *
     * @param runPath
     * @param getTopic
     * @return 返回runPath中以getTopic开头的result对象列表
     * @throws IOException
     */
    public static Results getResults(String runPath, int getTopic) throws IOException {
        Results Rs = new Results(getTopic);
        BufferedReader br = new BufferedReader(new FileReader(runPath));
        String line = "";
        int rank = 1;
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
           // System.out.println(line);
            int topic = Integer.parseInt(str[0].replaceAll("MB",""));
            if(topic==getTopic){
                String docID = str[2];
                double score = Double.parseDouble(str[4].replaceAll("-Infinity","-100").replaceAll("-nan","-100"));
                String systemName = str[5];
                Result r = new Result(topic,docID,rank,score,systemName);
                Rs.getList().add(r);
                rank++;
            }
        }
        br.close();

        Collections.sort(Rs.getList(), new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                return o1.getRank()- o2.getRank();
            }
        });

        return Rs;
    }

    /**
     * 将输入的run中的docID和topic存储进results对象中;
     * @param runPath
     * @param topicsInt
     * @throws IOException
     */
    public static ArrayList<Results> getRun(String runPath, int[] topicsInt) throws IOException {
        ArrayList<Results> run = new ArrayList<>();
        ArrayList<Integer> topics = changeInt(topicsInt);
        for (Integer topic : topics) {
            Results results = getResults(runPath, topic);
            results.setSystemName(runPath.split("\\\\")[runPath.split("\\\\").length-1].replaceAll("input.",""));
            run.add(results);
        }
        Collections.sort(run, new Comparator<Results>() {
            @Override
            public int compare(Results o1, Results o2) {
                return o1.getTopic()-o2.getTopic();
            }
        });


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

    /**
     *
     * @param type
     * type==0 score == 30/60+rank
     * type==1 score == idea3
     * @param run 要进行赋值的run
     * @param runNum run的总个数
     */
    public static void setScore(int type,ArrayList<Results> run,int runNum){

        for (Results results : run) {

            double max = -1000000;
            double min = 1000000;
            for (Result result : results.getList()) {//获取当前results的最大最小值
                if (result.getScore()>=max){
                    max = result.getScore();
                }
                if (result.getScore()<=min){
                    min = result.getScore();
                }
            }
            for (Result result : results.getList()) {
                if (type == 0){
                    double score = 30.0/(60+result.getRank());
                    result.setScore(score);
                }
                if (type == 1){
                    double score = 0.0;
                    switch ((int) ((result.getRank()-1)/(runNum))){
                        case 0:
                            score = 1.0/runNum;break;
                        case 1:
                            score = 1.0/(runNum*2);break;
                        case 2:
                            score = 1.0/(runNum*3);break;
                        case 3:
                            score = 1.0/(runNum*4);break;
                        case 4:
                            score = 1.0/(runNum*5);break;
                        case 5:
                            score = 1.0/(runNum*6);break;
                        case 6:
                            score = 1.0/(runNum*7);break;
                        case 7:
                            score = 1.0/(runNum*8);break;
                        case 8:
                            score = 1.0/(runNum*9);break;
                        case 9:
                            score = 1.0/(runNum*10);break;
                    }
                    result.setScore(score);
                }
                if (type == 2){
                    double score = 10*30.0/(60+result.getRank());
                    result.setScore(score);
                }
                if (type == 3){
                    double score = result.getScore();
                    result.setScore(score);
                }
                if (type == 4){
                    double score = 1.0/(60+result.getRank());
                    result.setScore(score);
                }
                if (type == 5){
                    double score = 0.0;

                   // System.out.println(max+"\t"+min);
                    if (max-min!=0){
                        score = (result.getScore()-min)/(max-min);
                    }else{
                        score = (results.getList().size()-result.getRank()*1.0+1)/results.getList().size();
                    }

                    result.setScore(score);
                }
            }
        }
    }

    /**
     * 将run中的信息写入到文件中
     * @param writePath
     * @param run
     * @throws IOException
     */
    public static void writeRun(String writePath,ArrayList<Results> run) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(writePath));

        for (Results results : run) {
            System.out.println("write: "+results.getTopic()+"\t"+results.getList().size());
            for (Result result : results.getList()) {
                if (result.getRank() > DEATH){
                    break;
                }
                String line = "";
                line += result.getTopic()+"\t";
                line += "Q0"+"\t";
                line += result.getDocID()+"\t";
                line += result.getRank()+"\t";
                line += result.getScore()+"\t";
                line += results.getSystemName()+"\n";
                bw.write(line);
            }
        }
        bw.close();
    }

    /**
     *
     * @param runsPath 归一化文件夹
     * @param topicsInt 归一化挑选的topic
     * @param writePath 归一化输出文件夹
     * @param type 归一化分数类型
     * @throws IOException
     */
    public static void executeProgram(String runsPath,int[] topicsInt,String writePath,int type) throws IOException {
        File[] runsFile = new File(runsPath).listFiles();
        int runNum = runsFile.length; // 修改N参数
        for (File runPath : runsFile) {
            String runName = runPath.getName().replaceAll("input.","");
            System.out.println(runName);
            ArrayList<Results> run = getRun(runPath.toString(), topicsInt);
            if (type == 3){
                /*
                按照分数进行重排序
                */
                for (Results FResults : run) {

                    for (int i = 0; i < FResults.getList().size(); i++) {
                        for (int j = 0; j < FResults.getList().size()-1; j++) {
                            if (FResults.getList().get(j).getScore()<FResults.getList().get(j+1).getScore()){
                                Collections.swap(FResults.getList(),j,j+1);
                            }
                        }
                    }


                    int rank = 1;
                    for (Result result : FResults.getList()) {
                        result.setRank(rank++);
                    }
                }
            }
            setScore(type,run,runNum);
            writeRun(writePath+runName,run);
        }
    }
    public static void main(String[] args) throws IOException {
        //String runsPath = "D:\\TREC数据集文件\\new_idea3\\health2020\\fusion3\\idea3_raw_input";
        //String writePath = "D:\\TREC数据集文件\\new_idea3\\health2020\\fusion3\\idea3_qrel_nor_input"+"\\";
        //for (int date=20150720;date<=20150729;date++){
            String runsPath = "F:\\TREC 数据集\\2020deeplearning document\\input-folder";
            String writePath = "F:\\TREC 数据集\\2020deeplearning document\\standard-input-nor1-60\\";
            /*int[] topicsInt = {//2020 DeepLearningPassage
                    23849,42255,47210,67316,118440,
                    121171,135802,141630,156498,169208,
                    174463,258062,324585,330975,332593,
                    336901,390360,405163,555530,583468,
                    640502,673670,701453,730539,768208,
                    877809,911232,914916,938400,940547,
                    997622,1030303,1037496,1043135,1051399,
                    1064670,1071750,1105792,1106979,1108651,
                    1109707,1110678,1113256,1115210,1116380,
                    1121353,1122767,1127540,1131069,1132532,
                    1133579,1136043,1136047,1136962
            };*/
        /*int[] topicsInt = {//2020 DeepLearningPassage_ji
                    23849,47210,118440,135802,156498,
                    174463,324585,332593,390360,555530,
                    640502,701453,768208,911232,938400,
                    997622,1037496,1051399,1071750,1106979,
                    1109707,1113256,1116380,1122767,1131069,
                    1133579,1136047
            };*/
        /*int[] topicsInt = {//2020 DeepLearningPassage_ou
                    ,42255,67316,121171,141630,169208,
                    ,258062,330975,336901,405163,583468,
                    ,673670,730539,877809,914916,940547,
                    ,1030303,1043135,1064670,1105792,1108651,
                    ,1110678,1115210,1121353,1127540,1132532,
                    ,1136043,1136962
            };*/
      /*  int[] topicsInt = {//2021 DeepLearningDocument
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
                1128632,1129560,

        };*/
       int[] topicsInt = {//2020 DeepLearningDocument
                42255, 47210, 67316, 135802, 156498, 169208,
                174463, 258062, 324585, 330975, 332593, 336901,
                673670, 701453, 730539, 768208, 877809, 911232,
                938400, 940547, 997622, 1030303, 1037496, 1043135,
                1049519, 1051399, 1056416, 1064670, 1071750, 1103153,
                1105792, 1108729, 1109707, 1113256, 1115210, 1116380,
                1119543, 1122767, 1127540, 1131069, 1132532, 1136043,
                1136047, 1136769, 1136962,

        };
       /* int[] topicsInt = {//2020 deeplearning document-ji
                42255,67316,156498,174463,324585,332593,
                673670,324585,332593,673670, 730539,877809,
                938400, 997622,1037496,1049519, 1056416, 1071750,
                1105792,1109707,1115210, 1119543,1127540, 1132532,
                1136047, 1136962
        };
        int[] topicsInt = {//2020 deeplearning document-ou
                47210,135802,169208,258062,330975, 336901,
                701453, 768208, 911232,940547,1030303, 1043135,
                1051399, 1064670,1103153, 1108729, 1113256,1116380,
                1122767,1131069,1136043,1136769
        };
       /* int[] topicsInt = {//2021 DeepLearningDocument ji
                2082,30611,168329,226975,253263,
                300986,364210,421946,505390,540006,
                615176,632075,647362,681645,707882,
                806694,832573,845121,935964,952284,
                1006728,1103547,1104447,1107821,1110996,
                1113361,1117298,1121909,1129560,

        };*/
       /* int[] topicsInt = {//2021 DeepLearningDocument ou
                23287,112700,190623,237669,300025,
                337656,395948,493490,508292,596569,
                629937,646091,661905,688007,764738,
                818583,835760,935353,952262,975079,
                1040198,1104300,1107704,1109840,1111577,
                1117243,1118716,1128632,

        };*/
            /*int[] topicsInt = {//2014topic
                    2, 3, 4, 5, 6, 7, 8, 9, 10,13,
                    14,15,16,17,18,19,20,21,22,23,
                    24,25,26,27,28,29,30,31,32,33,
                    34,35,36,37,39,40,42,44,46,47,
                    48,51,52,53,54,55,56,57,58,59,
                    60
            };*/
            /*int[] topicsInt = {//2014topic_type1
                    2, 4, 6, 8, 10,
                    14,16,18,20,22,
                    24,26,28,30,32,
                    34,36,39,42,46,
                    48,52,54,56,58,
                    60
            };*/
           /*int[] topicsInt = {//2014topic_type2
                    3, 5, 7, 9, 13,
                    15,17,19,21,23,
                    25,27,29,31,33,
                    35,37,40,44,47,
                    51,53,55,57,59,

            };*/
            /*int[] topicsInt = {//2014session
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
                    11,12,13,14,15,16,17,18,19,20,
                    21,22,23,24,25,26,27,28,29,30,
                    31,32,33,34,35,36,37,38,39,40,
                    41,42,43,44,45,46,47,48,49,50,
                    51,52,53,54,55,56,57,58,59,60,
                    61,62,63,64,65,66,67,68,69,70,
                    71,72,73,74,75,76,77,78,79,80,
                    81,82,83,84,85,86,87,88,89,90,
                    91,92,93,94,95,96,97,98,99,100
            };*/
            /*int[] topicsInt = {//2014session ji
                    1,  3, 5, 7, 9,
                    11,13,15,17,19,
                    21,23,25,27,29,
                    31,33,35,37,39,
                    41,43,45,47,49,
                    51,53,55,57,59,
                    61,63,65,67,69,
                    71,73,75,77,79,
                    81,83,85,87,89,
                    91,93,95,97,99,
            };*/
           /* int[] topicsInt = {//2014session ou
                    2, 4, 6, 8, 10,
                    12,14,16,18,20,
                    22,24,26,28,30,
                    32,34,36,38,40,
                    42,44,46,48,50,
                    52,54,56,58,60,
                    62,64,66,68,70,
                    72,74,76,78,80,
                    82,84,86,88,90,
                    92,94,96,98,100
            };*/
            /*
            int[] topicsInt = {1,2,3,4,5,6,7,8,9,10,
                    11,12,13,14,15,16,17,18,19,20,
                    21,22,23,24,25,26,27,28,29,30,
                    31,32,34,36,37,38,39,40,
                    41,42,43,44,45,47,49,50};
            */
            /*int[] topicsInt = {
                        226,227,228,236,242,243,246,248,249,253,
                        254,255,260,262,265,267,278,284,287,298,
                        305,324,326,331,339,344,348,353,354,357,
                        359,362,366,371,377,379,383,384,389,391,
                        392,401,400,405,409,416,419,432,434,439,448};*/
            /*int[] topicsInt = {//2013mb
                    111,112,113,114,115,116,117,118,119,120,
                    121,122,123,124,125,126,127,128,129,130,
                    131,132,133,134,135,136,137,138,139,140,
                    141,142,143,144,145,146,147,148,149,150,
                    151,152,153,154,155,156,157,158,159,160,
                    161,162,163,164,165,166,167,168,169,170
            };*/
            /*int[] topicsInt = {//2013MB_ji
                    111,113,115,117,119,
                    121,123,125,127,129,
                    131,133,135,137,139,
                    141,143,145,147,149,
                    151,153,155,157,159,
                    161,163,165,167,169
            };*/
           /*int[] topicsInt = {//2013MB_ou
                    112,114,116,118,120,
                    122,124,126,128,130,
                    132,134,136,138,140,
                    142,144,146,148,150,
                    152,154,156,158,160,
                    162,164,166,168,170
            };*/
        /*int[] topicsInt = {//2014MB
                171,172,173,174,175,176,177,178,179,180
                ,181,182,183,184,185,186,187,188,189,190
                ,191,192,193,194,195,196,197,198,199,200
                ,201,202,203,204,205,206,207,208,209,210
                ,211,212,213,214,215,216,217,218,219,220
                ,221,222,223,224,225
        };*/
        /*int[] topicsInt = {//2014MBji
                171,173,175,177,179
                ,181,183,185,187,189
                ,191,193,195,197,199
                ,201,203,205,207,209
                ,211,213,215,217,219
                ,221,223,225
        };*/
        /*int[] topicsInt = {//2014MBou
                172,174,176,178,180
                ,182,184,186,188,190
                ,192,194,196,198,200
                ,202,204,206,208,210
                ,212,214,216,218,220
                ,222,224
        };*/
        /*int[] topicsInt = {//2012MB
                51,52,53,54,55,56,57,58,59,60,
                61,62,63,64,65,66,67,68,69,70,
                71,72,73,74,75,76,77,78,79,80,
                81,82,83,84,85,86,87,88,89,90,
                91,92,93,94,95,96,97,98,99,100,
                101,102,103,104,105,106,107,108,109,110
        };*/
       /* int[] topicsInt = {//2012MB ji
                51,53,55,57,59,
                61,63,65,67,69,
                71,73,75,77,79,
                81,83,85,87,89,
                91,93,95,97,99,
                101,103,105,107,109
        };*/
       /* int[] topicsInt = {//2012MB ou
                52,54,56,58,60,
                62,64,66,68,70,
                72,74,76,78,80,
                82,84,86,88,90,
                92,94,96,98,100,
                102,104,106,108,110
        };*/
        /*int[] topicsInt = {//2011MB
                1,2,3,4,5,6,7,8,9,10,
                11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30,
                31,32,33,34,35,36,37,38,39,40,
                41,42,43,44,45,46,47,48,49,50
        };*/
        /*int[] topicsInt = {//2011MB ji
                1, 3, 5, 7, 9,
                11,13,15,17,19,
                21,23,25,27,29,
                31,33,35,37,39,
                41,43,45,47,49
        };*/
        /*int[] topicsInt = {//2011MB ou
                2, 4, 6, 8, 10,
                12,14,16,18,20,
                22,24,26,28,30,
                32,34,36,38,40,
                42,44,46,48,50
        };*/
            /*
            type == 0  30.0/rank+60
            type == 1  按照前N个得1/N分 前2*N个得 1/2*N分 一直到 前10*N 得1/10*N分为止
            type == 2  10*30.0/rank+60
            type == 3  为原文档分数，此方法为了将原文档的规格进行再次排列
            type == 4  1.0/rank+60
            type == 5  0-1
             */
            int type =4;
            executeProgram(runsPath,topicsInt,writePath,type);
        //}


    }
}
