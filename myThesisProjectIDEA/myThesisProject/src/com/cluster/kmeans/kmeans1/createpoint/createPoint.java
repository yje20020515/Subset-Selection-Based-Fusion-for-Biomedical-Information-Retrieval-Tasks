package com.cluster.kmeans.kmeans1.createpoint;

import com.DataStruct.Result;
import com.DataStruct.Results;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 将数据集中的每个系统创建为全文档点（在当前系统中的文档保留分数信息，不在当前文档中出现但是在其他文档中出现了，分数为0）
 */
public class createPoint {
    public static ArrayList<Results> createPoint = new ArrayList<>();
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
     * @param runPath
     * @param getTopic
     * @return 返回runPath中以getTopic开头的result对象列表
     * @throws IOException
     */
    public static Results getResults(String runPath,int getTopic) throws IOException {
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
                rank++;
            }
        }
        br.close();
        return Rs;
    }
    public static void initNullPoint(int[] topicsInt){
        for (int topic : topicsInt) {
            createPoint.add(new Results(topic));

        }
    }
    public static ArrayList<Results> getRun(String runPath, int[] topicsInt) throws IOException {
        ArrayList<Results> run = new ArrayList<>();
        ArrayList<Integer> topics = changeInt(topicsInt);
        for (Integer topic : topics) {
            run.add(getResults(runPath,topic));
        }
        return run;
    }
    public static void addNewDocID(ArrayList<Results> run) {
        for (Results results : run) {
            for (Results fResults : createPoint) {
                if (results.getTopic() == fResults.getTopic()){//topic相等时进行docID的比较
                    for (Result result : results.getList()) {
                        if (fResults.getList().contains(result)){
                        }else{
                            fResults.getList().add(new Result(result.getTopic()
                                    ,result.getDocID()
                                    , result.getRank()
                                    , 0.0
                                    , "NullPoint"));
                        }
                    }
                }
            }
        }
    }

    /**
     * 读取所有的系统，将每个文档ID以及对应的topicID进行记录，输入到createPoint中
     */
    public static void setNullPoint(String systemPath,int[] topicsInt) throws IOException {
        initNullPoint(topicsInt);//创建nullPoint的topic队列
        for (File file : new File(systemPath).listFiles()) {
            ArrayList<Results> run = getRun(file.getPath(),topicsInt);

            addNewDocID(run);
            System.out.println(file.getName());
            for (Results results : createPoint) {
                System.out.println(results.getTopic()+"\t"+results.getList().size());
            }

        }
    }

    public static void main(String[] args) throws IOException {
        //String systemPath = "D:\\TREC数据集文件\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\standard_input_nor30-60\\";

        int[] topicsInt={1,2,3,4,5,6,7,8,9,10,
                11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30,
                31,32,33,34,35,36,37,38,39,40,
                41,42,43,44,45,46,47,48,49,50};
        //setNullPoint(systemPath,topicsInt);
    }
}

