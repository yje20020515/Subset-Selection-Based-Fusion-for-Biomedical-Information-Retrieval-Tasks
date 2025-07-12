package com.algorithm.fusionAlgorithm;

import com.algorithm.DataStruct.ArrayDocs;
import com.algorithm.DataStruct.Doc;
import com.algorithm.DataStruct.Docs;
import com.algorithm.myUtil.XLS_Util.XLS_Util;

import java.io.*;
import java.util.*;

public class CombMNZ {
    /**
     * 此方法用于实现CombMNZ算法
     * 同时使用hashMap方法进行开发
     */
    /**
     * 1.创建一个finalList作为最终结果列表，每个topic列表作为其中的一个元素
     * 2.获得每一个run的信息并保持到一个list中，每个list的元素是一个topic列表
     *
     */
    public static ArrayList<ArrayDocs> finalList = new ArrayList<>();
    public static HashMap<Doc,Integer> totalMNZ = new HashMap<>();


    public static Docs getDocs(String runPath, int getTopic) throws IOException {
        Docs docs = new Docs(getTopic);
        BufferedReader br = new BufferedReader(new FileReader(runPath));
        String line = "";
        while ((line = br.readLine())!= null){
            String[] str = line.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            if (topic == getTopic){
                String docID = str[2];
                int rank = Integer.parseInt(str[3]);
                double score = Double.parseDouble(str[4]);
                String systemName = str[5];
                Doc d = new Doc(docID,topic);
                d.setRank(rank);
                d.setScore(score);
                d.setSystemName(systemName);
                docs.getList().add(d);
                if (totalMNZ.containsKey(d)){
                    Integer integer = totalMNZ.get(d);
                    totalMNZ.put(d,integer+1);
                }else {
                    totalMNZ.put(d,1);
                }
            }
            //br.close();
        }
        return docs;
    }

    public static ArrayList<Docs> getOneRun(String runPath,int[] topics) throws IOException {
        ArrayList<Docs> oneRun = new ArrayList<>();
        for (int topic : topics) {
            Docs docs = getDocs(runPath, topic);
            oneRun.add(docs);
        }
        return oneRun;
    }

    public static void calculateCombMNZ(String[] runPaths, int[] topics) throws IOException {
        initFinalList(topics);
        for (String runPath : runPaths) {
            System.out.println(runPath);
            ArrayList<Docs> oneRun = getOneRun(runPath,topics);
            /*
            向finalList中添加数据先进行CombSUM计算，而后计算CombMNZ
             */
            joinFinalList(oneRun);
        }
        /*
        MNZ
         */
        for (ArrayDocs finalDocs : finalList) {
            for (Doc finalDoc : finalDocs.getList()) {
                Integer times = totalMNZ.get(finalDoc);
                finalDoc.setScore(finalDoc.getScore()*times);
            }
        }

        sortFinalList();


    }



    private static void sortFinalList() {
        /**
         * 排序Docs
         */
        for (int i = 0; i < finalList.size(); i++) {
            for (int j = 0; j < finalList.size(); j++) {
                if (finalList.get(i).getTopic() < finalList.get(j).getTopic()){
                    Collections.swap(finalList,i,j);
                }
            }
        }

        for (ArrayDocs finalDocs : finalList) {
            for (int i = 0; i < finalDocs.getList().size(); i++) {
                for (int j = 0; j < finalDocs.getList().size(); j++) {
                    if (finalDocs.getList().get(i).getScore() > finalDocs.getList().get(j).getScore()){
                        Collections.swap(finalDocs.getList(),i,j);
                    }
                }
            }
            int rank = 1;
            for (Doc finalDoc : finalDocs.getList()) {
                finalDoc.setRank(rank++);
            }
        }
    }

    private static void joinFinalList(ArrayList<Docs> oneRun) {
        for (Docs oneDocs : oneRun) {
            for (ArrayDocs finalDocs : finalList) {
                if (oneDocs.getTopic() == finalDocs.getTopic()){
                    for (Doc oneDoc : oneDocs.getList()) {
                        if (finalDocs.getList().contains(oneDoc)){
                            //存在当前doc就更新finalDocs中该doc的分数
                            Doc finalDoc = findDoc(finalDocs, oneDoc);
                            finalDoc.setScore(finalDoc.getScore()+oneDoc.getScore());
                        }else {
                            //不存在就进行添加
                            finalDocs.getList().add(oneDoc);
                        }
                    }
                }
            }
        }
    }

    private static Doc findDoc(ArrayDocs finalDocs, Doc oneDoc) {
        for (Doc finalDoc : finalDocs.getList()) {
            if (finalDoc.equals(oneDoc)){
                return finalDoc;
            }
        }
        return null;
    }


    private static void initFinalList(int[] topics) {
        for (int topic : topics) {
            ArrayDocs docs = new ArrayDocs(topic);
            finalList.add(docs);
        }
    }

    private static void writeFinalList(String outputPath, String systemName) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));

        for (ArrayDocs finalDocs : finalList) {
            for (Doc finalDoc : finalDocs.getList()) {
                String line = "";
                line += finalDoc.getTopic()+"\t";
                line += "Q0"+"\t";
                line += finalDoc.getDocID()+"\t";
                line += finalDoc.getRank()+"\t";
                line += finalDoc.getScore()+"\t";
                line += systemName + "\n";
                bw.write(line);
            }
        }
        bw.close();
    }
    public static void RunProgram(String runPaths,int[] topic,String outputPath,String SystemName,int NUM,String XLS_Path) throws IOException {
        String[] fusionNames = XLS_Util.getXLSFusionNames(XLS_Path,NUM);
        for (int i = 0; i < fusionNames.length; i++) {
            fusionNames[i] =runPaths+fusionNames[i];
        }
        calculateCombMNZ(fusionNames,topic);
        writeFinalList(outputPath,SystemName);
        clearGlobalVariable();
    }
    public static void clearGlobalVariable(){
        finalList.clear();
        totalMNZ.clear();
    }
    public static void main(String[] args) throws IOException {

        for (int NUM =2; NUM <=10 ; NUM++) {
            //String runPaths ="E:\\TREC 数据集\\2017MedicineTrackScientific\\standard-input-nor1-60\\";
            String runPaths ="E:\\TREC 数据集\\2022DeepLearningPassageRanking\\standard-input-nor10score\\";
            int[] topic = {
                  /*  1, 2, 3, 4, 5,
                    6, 7, 8, 9, 10,
                    11, 12, 13, 14,
                    15, 16, 17, 18,
                    19, 20, 21, 22,
                    23, 24, 25, 26,
                    27, 28, 29, 30,
                    31, 32, 33, 34,
                    35, 36, 37, 38,
                    39, 40,*/ /*41, 42,
                43, 44, 45, 46,
                47, 48, 49, 50*/
                    //2021DLpassage
                   /* 2082,23287,30611,112700,168329,190623,
                    226975,237669,253263,300025,300986,337656,
                    364210,395948,421946,493490,505390,508292,
                    540006,596569,629937,646091,647362,661905,
                    681645,688007,707882,764738,806694,818583,
                    832573,835760,845121,935353,935964,952262,
                    952284,975079,1006728,1040198,1104300,1104447,
                    1107704,1107821,1109840,1110996,1111577,1113361,
                    1117243,1118716,1121909,1128632,1129560,*/
                    //2022DLpassage
                    2000511,2000719,2001532,2001908,2001975,2002146,
                    2002269,2002533,2002798,2003157,2003322,2003976,
                    2004237,2004253,2005810,2005861,2006211,2006375,
                    2006394,2006627,2007055,2007419,2008871,2009553,
                    2009871,2012431,2012536,2013306,2016333,2017299,
                    2025747,2026150,2026558,2027130,2027497,2028378,
                    2029260,2030323,2030608,2031726,2032090,2032949,
                    2032956,2033232,2033396,2033470,2034205,2034676,
                    2035009,2035447,2035565,2036182,2036968,2037251,
                    2037609,2037924,2038466,2038890,2039908,2040287,
                    2040352,2040613,2043895,2044423,2045272,2046371,
                    2049417,2049687,2053884,2054355,2055211,2055480,
                    2055634,2055795,2056158,2056323
            };
            //int NUM =10;
            String SystemName = NUM+"";
            String outputPath = "E:\\TREC 数据集\\2022DeepLearningPassageRanking\\combmnz\\"+SystemName;
            String XLS_Path = "E:\\TREC 数据集\\2022DeepLearningPassageRanking\\classification\\"+NUM+"\\Semi_K"+NUM+"_1.xls";
            RunProgram(runPaths,topic,outputPath,SystemName,NUM,XLS_Path);
        }

    }

}
