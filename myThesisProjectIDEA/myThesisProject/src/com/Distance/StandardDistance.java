package com.Distance;

import com.DataStruct.Result;
import com.DataStruct.Results;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class StandardDistance {
    /**
     * 1.计算两个run之间的距离
     *  1.1 先将一个run加载到一个arraylist对象中，其中每个对象都是一个以topic进行分割的arraylist对象
     */


    public static boolean existTopic(int topic){
        for (int t : topics) {
            if(t==topic){
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Results> getRun(String runPath) throws IOException {
        ArrayList<Results> Run = new ArrayList<>();
        for (int topic : topics) {
            Run.add(getTopic(runPath,topic));
        }
        return Run;
    }

    private static Results getTopic(String runPath, int this_topic) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(runPath));
        Results topic_Results = new Results(this_topic);
        String line = "";
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            //String date = str[0];
            int topic = Integer.parseInt(str[0].replaceAll("MB",""));

            if (topic == this_topic){
                String docID = str[2];
                int rank = Integer.parseInt(str[3]);
                double score = Double.parseDouble(str[4]);
                score = 30.0/(rank+60.0);//使用30/60+rank对run进行分数归一化
                String systemName = str[5];
                /*
                    当有date属性时将date数据连同docID属性链接到一起
                 */
                topic_Results.getList().add(new Result(topic,docID,score,rank,systemName));

            }
        }
        return topic_Results;
    }

    public static void runA_runB_Distance(String RunsAPath,String RunsBPath) throws IOException {
        File[] filesA = new File(RunsAPath).listFiles();
        File[] filesB = new File(RunsBPath).listFiles();
        double[][] distanceMatrix = new double[filesA.length][filesB.length];
        System.out.println(filesA.length);
        System.out.println(filesB.length);
        int lengthA = 0;
        int lengthB = 0;
        for (File RunA : filesA) {
            ArrayList<Results> runA = getRun(RunA.toString());
            System.out.println(RunA.getName()+"\t");
            lengthB=0;
            for (File RunB : filesB) {
                System.out.print(RunB.getName()+"\t");
                ArrayList<Results> runB = getRun(RunB.toString());
                double distance = 0.0;
                distance = distanceAB(runA,runB);
                distanceMatrix[lengthA][lengthB]=distance;
                System.out.println(distance);
                lengthB++;
            }
            lengthA++;
        }

        showMatrix(filesA,filesB,distanceMatrix);
    }

    private static double distanceAB(ArrayList<Results> runA, ArrayList<Results> runB) {
        double distance = 0.0;
        for (Results resultsA : runA) {

            for (Results resultsB : runB) {
                if (resultsA.getTopic()==resultsB.getTopic()){

                    for (Result resultA : resultsA.getList()) {
                        for (Result resultB : resultsB.getList()) {
                            if (resultA.equals(resultB)){
                                distance += Math.pow(resultA.getScore()-resultB.getScore(),2) ;
                            }
                        }
                    }
                    for (Result resultA : resultsA.getList()) {
                        if (!resultsB.getList().contains(resultA)){
                            distance += resultA.getScore();
                        }
                    }
                    for (Result resultB : resultsB.getList()) {
                        if (!resultsA.getList().contains(resultB)){
                            distance += resultB.getScore();
                        }
                    }
                }
            }
        }
        return distance;
    }

    public static void showMatrix(File[] filesA,File[] filesB,double[][] distanceMatrix){
        System.out.print("\t");
        for (File runB : filesB) {
            System.out.print(runB.getName()+"\t");
        }
        System.out.println();
        for (int i = 0; i < distanceMatrix.length; i++) {
            System.out.print(filesA[i].getName()+"\t");
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                System.out.print(distanceMatrix[i][j]+"\t");
            }
            System.out.println();
        }

    }


    public static void setTopics(int[] topics) {
        StandardDistance.topics = topics;
    }
    public static void initTopics(int[] topics){
        setTopics(topics);
    }
    public static double get_RunA_RunB_Diss(String runA,String runB) throws IOException {

        double diss = 0;
        ArrayList<Results> OBJrunA = getRun(runA);
        ArrayList<Results> OBJrunB = getRun(runB);
        diss = distanceAB(OBJrunA,OBJrunB);
        return diss;
    }
    public static void main(String[] args) throws IOException {
        String RunsAPath = "D:\\TREC数据集文件\\methode_qrelNegative\\2014Session\\Adhoc\\standard_input_ou";
        String RunsBPath = "D:\\TREC数据集文件\\methode_qrelNegative\\2014Session\\Adhoc\\standard_input_ou";
        Calendar c1 = Calendar.getInstance();
        Date time1 = c1.getTime();

        runA_runB_Distance(RunsAPath,RunsBPath);
        Date time2 = c1.getTime();
        System.out.println(time2.getTime()-time1.getTime());

    }
    public static int[] topics ={
            2, 4, 6, 8, 10,
            12,14,16,18,20,
            22,24,26,28,30,
            32,34,36,38,40,
            42,44,46,48,50,
            52,54,56,58,60,
            62,64,66,68,70,
            72,74,76,78,80,
            82,84,86,88,90,
            92,94,96,98,100};//需留下的topic
}
