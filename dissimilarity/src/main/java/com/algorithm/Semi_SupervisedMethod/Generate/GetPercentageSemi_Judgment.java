package com.algorithm.Semi_SupervisedMethod.Generate;

import com.algorithm.DataStruct.Doc;
import com.algorithm.DataStruct.Docs;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * 获取real Judgment  doc列表 ，仅仅记录rel大于0的
 * 获取fake Judgment doc列表
 * 遍历fake judgment 列表看有多少是在real Judgment中
 *
 */
class TopicsNUM{
    private int topic;
    private int count;

    public TopicsNUM(int topic, int count) {
        this.topic = topic;
        this.count = count;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicsNUM topicsNUM = (TopicsNUM) o;
        return topic == topicsNUM.topic && count == topicsNUM.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, count);
    }
}

public class GetPercentageSemi_Judgment {
    /**
     * 获取real Judgment  doc列表 ，仅仅记录rel大于0的
     * 获取fake Judgment doc列表
     * 遍历fake judgment 列表看有多少是在real Judgment中
     *
     */



    /**
     * 获取judgment列表
     * @param JudgmentPath
     * @return
     * @throws IOException
     */
    private static Docs getDocs(String JudgmentPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(JudgmentPath));
        String oneLine = "";
        Docs Judgment = new Docs(0);
        while ((oneLine = br.readLine())!=null){
            String[] str = oneLine.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            String docID = str[2];
            double rel = Double.parseDouble(str[3]);
            if (rel > 0){
                Judgment.getList().add(new Doc(docID,topic));
            }
        }
        br.close();
        return Judgment;
    }

    private static void calculatePercentage(String realJudgmentPath,String fakeJudgmentPath) throws IOException {
        Docs realJudgmentList = getDocs(realJudgmentPath);
        Docs fakeJudgmentList = getDocs(fakeJudgmentPath);
        int fakeRealNum = 0;
        for (Doc doc : fakeJudgmentList.getList()) {
            if (realJudgmentList.getList().contains(doc)){
                fakeRealNum++;
            }
        }

        System.out.println(realJudgmentList.getList().size());
        double percentage = fakeRealNum * 1.0 / realJudgmentList.getList().size();
        System.out.print(fakeRealNum+"\t");
        System.out.println(percentage);

        CountTopicJudgmentNum(fakeJudgmentList,realJudgmentList);
        CountTopicJudgmentNum(realJudgmentList,realJudgmentList);


       // writeFake_RealJudgmentListFile(realJudgmentList,fakeJudgmentList);
    }

    /*private static void writeFake_RealJudgmentListFile(Docs realJudgmentList, Docs fakeJudgmentList) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("E:\\TRECDataset\\Semi-SupervisedMethodExperiment\\2020DeepLearning_Passage\\judgmentfile\\test\\testJudgmentFile"));
        for (Doc doc : fakeJudgmentList.getList()) {
            if (realJudgmentList.getList().contains(doc)){
                //输出
                bw.write(doc.getTopic()+"\t"+"Q0\t"+ doc.getDocID()+"\t"+"1"+"\n");
            }else {
                bw.write(doc.getTopic()+"\t"+"Q0\t"+ doc.getDocID()+"\t"+"0"+"\n");
            }
        }
        bw.close();
    }*/


    private static void CountTopicJudgmentNum(Docs JudgmentList,Docs realJudgmentList){
        ArrayList<TopicsNUM> topicsNUMS = new ArrayList<>();
        for (int topic : topics) {
            topicsNUMS.add(new TopicsNUM(topic,0));
        }

        for (Doc doc : JudgmentList.getList()) {
            for (TopicsNUM topicsNUM : topicsNUMS) {
                if (doc.getTopic()==topicsNUM.getTopic()&&realJudgmentList.getList().contains(doc)){
                    topicsNUM.setCount(topicsNUM.getCount()+1);
                }
            }
        }

        System.out.println("-----------------------------------------------");
        for (TopicsNUM topicsNUM : topicsNUMS) {
            System.out.println(topicsNUM.getTopic()+"\t"+topicsNUM.getCount());
        }
        System.out.println("-----------------------------------------------");
    }
    private static int[] topics = {
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
    public static void main(String[] args) throws IOException {
        String realJudgmentPath = "E:\\TREC 数据集\\2019MedicineTrackScientific\\judgement\\judgmentfile_01";
        String fakeJudgmentPath = "E:\\TREC 数据集\\2019MedicineTrackScientific\\judgement\\JudgmentFileSemiNUM_"+"10";
        calculatePercentage(realJudgmentPath,fakeJudgmentPath);
    }
}
