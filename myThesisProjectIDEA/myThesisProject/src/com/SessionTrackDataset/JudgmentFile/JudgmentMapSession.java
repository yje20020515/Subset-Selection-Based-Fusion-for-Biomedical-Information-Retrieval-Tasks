package com.SessionTrackDataset.JudgmentFile;

import com.DataStruct.Qrel;
import com.DataStruct.Qrels;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/*
将JudgmentFile的Topic映射到session上
 */
public class JudgmentMapSession {
    /*
    1.读取原始的Judgment文件，按照topic存储到topic arraylist<Qrels>对象中
    2.读取sessionMapTopic文件，获取session和topic的映射关系
    3.创建session的arraylist<Qrels>对象，并初始化好100个session Qrel对象，按照session序号排序
    4.按照session和topic的映射关系，将topic arraylist<Qrels>对象中的数据映射到session arraylist<Qrels>对象中
    5.输出session arral<list>对象
     */

    public static int[] topics = {//2013Session Topic
            1,  2, 6, 8,11,12,13,14,15,16,
            17,18,21,22,23,24,25,26,28,29,
            30,31,32,33,35,37,38,39,40,41,
            42,43,45,47,48,49,50,51,52,54,
            55,58,60,62,63,64,65,67,69
    };
    public static int[] sessions ={ //2013Session Session
            1,  2, 3, 4, 5, 6, 7, 8, 9,10,
            11,12,13,14,15,16,17,18,19,20,
            21,22,23,24,25,26,27,28,29,30,
            31,32,33,34,35,36,37,38,39,40,
            41,42,43,44,45,46,47,48,49,50,
            51,52,53,54,55,56,57,58,59,60,
            61,62,63,64,65,66,67,68,69,70,
            71,72,73,74,75,76,77,78,79,80,
            81,82,83,84,85,86,87
    };
/*
    public static int[] topics = {//2014Session Topic
            2, 3, 4, 5, 6, 7, 8, 9, 10,13,
            14,15,16,17,18,19,20,21,22,23,
            24,25,26,27,28,29,30,31,32,33,
            34,35,36,37,39,40,42,44,46,47,
            48,51,52,53,54,55,56,57,58,59,
            60
    };
    public static int[] sessions ={ //2014Session Session
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
    public static LinkedHashMap<Integer,Integer> SessionMapTopicList = new LinkedHashMap<>();
    public static ArrayList<Qrels> topicsList = new ArrayList<>();
    public static ArrayList<Qrels> SessionsList = new ArrayList<>();

    public static void getJudgmentFile(String JudgmentPath) throws IOException {
        for (int topic : topics) {
            topicsList.add(getQrel(JudgmentPath,topic));
        }

    }

    private static Qrels getQrel(String judgmentPath, int topic) throws IOException {
        Qrels qrels = new Qrels(topic);
        BufferedReader br = new BufferedReader(new FileReader(judgmentPath));
        String oneLine = "";
        while((oneLine = br.readLine()) != null){
            String[] str = oneLine.split("\\s+");
            int TempTopic =Integer.parseInt(str[0]);
            if (topic == TempTopic){
                String docID = str[2];
                double score = Double.parseDouble(str[3]);
                qrels.getList().add(new Qrel(topic,docID,score));
            }
        }
        br.close();
        return qrels;
    }

    /**
     * 文件的左列是session 右列是topic
     * @param SessionMapTopicPath
     */
    public static void getSessionMapTopicFile(String SessionMapTopicPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(SessionMapTopicPath));
        String oneLine = "";
        while ((oneLine = br.readLine()) != null){
            String[] str = oneLine.split("\\s+");
            int sessionNum = Integer.parseInt(str[0]);
            int topicNum = Integer.parseInt(str[1]);
            SessionMapTopicList.put(sessionNum,topicNum);

        }
        System.out.println(SessionMapTopicList.size());
        br.close();
    }

    public static void main(String[] args) throws IOException {
        String SessionMapTopicPath = "D:\\TREC数据集文件\\methode_qrelNegative\\2013Session\\Judgmentfile\\SessionMapTopic";
        String JudgmentPath = "D:\\TREC数据集文件\\methode_qrelNegative\\2013Session\\Judgmentfile\\judgmentfile";
        String SessionOutputPath = "D:\\TREC数据集文件\\methode_qrelNegative\\2013Session\\Judgmentfile\\SessionJudgmentFile";

        getSessionMapTopicFile(SessionMapTopicPath);
        getJudgmentFile(JudgmentPath);
        initSessionList();
        setSessionList();
        writeSessionJudgmentFile(SessionOutputPath);
        for (Qrels Sessions : SessionsList) {
            System.out.println(Sessions.getTopic()+"\t"+Sessions.getList().size());
        }
    }

    private static void writeSessionJudgmentFile(String SessionOutputPath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(SessionOutputPath));
        String oneLine = "";
        for (Qrels sessions : SessionsList) {
            for (Qrel session : sessions.getList()) {
                oneLine = session.getTopic()+"\t"+"0\t"+session.getDocID()+"\t"+session.getRel()+"\n";
                bw.write(oneLine);
            }
        }
        bw.close();
    }

    private static void setSessionList() {
        for (Qrels Sessions : SessionsList) {
            int topic = SessionMapTopicList.get(Sessions.getTopic());//获取到该Sessions对应的topic
            for (Qrels qrels : topicsList) {
                if (qrels.getTopic()==topic){
                    for (Qrel qrel : qrels.getList()) {
                        Sessions.getList().add(new Qrel(Sessions.getTopic(),qrel.getDocID(),qrel.getRel()));
                    }
                }
            }
        }
    }

    private static void initSessionList() {
        for (int session : sessions) {
            SessionsList.add(new Qrels(session));
        }
    }

}
