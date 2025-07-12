package com.algorithm.fusionAlgorithm;

import com.algorithm.DataStruct.*;
import com.algorithm.myUtil.XLS_Util.XLS_Util;

import java.io.*;
import java.util.*;

public class FastCombSUM {
    private static ArrayList<ArrayDocs> finalList = new ArrayList<>();

    private static ArrayList<ArrayTreeMapOneTopicDocList> arrayTreeMapList = new ArrayList<>();

    private static Integer DEEP = 1000;
    private static void getTotalSUM(String runPath,Integer[] topics) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(runPath));
        List<Integer> topicsList = Arrays.asList(topics);
        String line = "";
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            if (topicsList.contains(topic)){
                String docID = str[2];
                int rank = Integer.parseInt(str[3]);
                double score = Double.parseDouble(str[4]);
                String systemName = str[5];
                HashMapDocKey DKey = new HashMapDocKey(topic,docID);
                HashMapDocValue DValue = new HashMapDocValue();
                DValue.setRank(rank);
                DValue.setScore(score);
                DValue.setSystemName(systemName);
                for (ArrayTreeMapOneTopicDocList arrayTreeMapOneTopicDocList : arrayTreeMapList) {
                    if (arrayTreeMapOneTopicDocList.getTopic() == topic){
                        if (arrayTreeMapOneTopicDocList.getOneTopicDocList().containsKey(DKey)){
                            //若存在,修改值
                            HashMapDocValue treeMapDocValue = arrayTreeMapOneTopicDocList.getOneTopicDocList().get(DKey);
                            treeMapDocValue.setScore(treeMapDocValue.getScore()+score);
                            treeMapDocValue.setMNZ_Num(treeMapDocValue.getMNZ_Num()+1);
                        }else {
                            //若不存在直接添加
                            arrayTreeMapOneTopicDocList.getOneTopicDocList().put(DKey,DValue);
                        }
                        break;
                    }
                }
            }
        }
        br.close();
    }



    public static void calculateCombSUM(String[] runPaths, Integer[] topics) throws IOException {
        initFinalList(topics);
        initFusionMNZList(topics);
        for (String runPath : runPaths) {
            System.out.println(runPath);
            getTotalSUM(runPath,topics);
        }
        getFinalList();
        calculate();
        sortFinalList();
    }
    private static void sortFinalList() {
        // 排序Docs
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

    private static void calculate() {
        for (ArrayDocs arrayDocs : finalList) {
            for (Doc doc : arrayDocs.getList()) {
                doc.setScore(doc.getScore()* 1);
            }
        }
    }

    private static void getFinalList() {
        for (ArrayDocs arrayDocs : finalList) {
            for (ArrayTreeMapOneTopicDocList arrayTreeMapOneTopicDocList : arrayTreeMapList) {
                if (arrayDocs.getTopic() == arrayTreeMapOneTopicDocList.getTopic()){
                    for (Map.Entry<HashMapDocKey, HashMapDocValue> treeMapDocKeyTreeMapDocValueEntry : arrayTreeMapOneTopicDocList.getOneTopicDocList().entrySet()) {
                        HashMapDocKey key = treeMapDocKeyTreeMapDocValueEntry.getKey();
                        HashMapDocValue value = treeMapDocKeyTreeMapDocValueEntry.getValue();
                        Integer topic = key.getTopic();
                        String docID = key.getDocID();
                        Integer rank = value.getRank();
                        Double score = value.getScore();
                        String systemName = value.getSystemName();
                        Integer mnz_num = value.getMNZ_Num();
                        arrayDocs.getList().add(new Doc(topic,docID,rank,score,systemName,mnz_num));
                    }
                    break;
                }
            }
        }
    }

    private static void initFusionMNZList(Integer[] topics) {
        for (Integer topic : topics) {
            ArrayTreeMapOneTopicDocList arrayTreeMapOneTopicDocList = new ArrayTreeMapOneTopicDocList(topic);
            arrayTreeMapList.add(arrayTreeMapOneTopicDocList);
        }
    }




    private static void initFinalList(Integer[] topics) {
        for (Integer topic : topics) {
            ArrayDocs docs = new ArrayDocs(topic);
            finalList.add(docs);
        }
    }
    private static void writeFinalList(String outputPath, String systemName) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));

        for (ArrayDocs finalDocs : finalList) {
            for (Doc finalDoc : finalDocs.getList()) {
                if (finalDoc.getRank() <= DEEP){
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
        }
        bw.close();
    }
    public static ArrayList<ArrayDocs> RunProgram(String runPaths,Integer[] topic,String[] fusionNames) throws IOException {
        clearGlobalVariable();
        System.out.println(finalList.size());
        for (int i = 0; i < fusionNames.length; i++) {
            fusionNames[i] =runPaths+fusionNames[i];
        }
        calculateCombSUM(fusionNames,topic);
        return finalList;
    }

    private static void clearGlobalVariable() {
        finalList.clear();
        arrayTreeMapList.clear();
    }

    public static void setDEEP(Integer deep){
        DEEP = deep;
    }
    public static Integer getDEEP(){
        return DEEP;
    }
    public static void RunProgram(String runPaths,Integer[] topic,String outputPath,String SystemName,int NUM,String XLS_Path) throws IOException {
        setDEEP(1000);
        String[] fusionNames = XLS_Util.getXLSFusionNames(XLS_Path,NUM);
        for (int i = 0; i < fusionNames.length; i++) {
            fusionNames[i] =runPaths+fusionNames[i];
        }
        calculateCombSUM(fusionNames,topic);
        writeFinalList(outputPath,SystemName);
        clearGlobalVariable();
    }


    public static void main(String[] args) throws IOException {
        //long startTime=System.currentTimeMillis(); //获取开始时间
      //for (int p = 1; p <=125; p++) {//最外循环代表多种检索系统组合
            for (int NUM =2;NUM <=10; NUM++) {
                setDEEP(1000);
                //String runPaths ="E:\\TRECDataset\\test\\test_nor\\";
                //String runPaths ="E:\\TREC 数据集\\2022DeepLearningPassageRanking\\standard_borda\\";
                //String runPaths ="E:\\TREC 数据集\\2018MedicineTrackScientific\\standard-input-nor1-60\\cbnu\\";
                String runPaths ="E:\\TREC 数据集\\2022DeepLearningPassageRanking\\standard-input-nor10score\\";
                Integer[] topic = {
                       /* 1, 2, 3, 4, 5,
                        6, 7, 8, 9, 10,
                        11, 12, 13, 14,
                        15, 16, 17, 18,
                        19, 20, 21, 22,
                        23, 24, 25, 26,
                        27, 28, 29, 30,
                        31, 32, 33, 34,
                        35, 36, 37, 38,
                        39, 40 *//*,41, 42,
                    43, 44, 45, 46,
                    47, 48, 49, 50*/
                        //2021DLpassage
                       /*  2082,23287,30611,112700,168329,190623,
                         226975,237669,253263,300025,300986,337656,
                         364210,395948,421946,493490,505390,508292,
                         540006,596569,629937,646091,647362,661905,
                         681645,688007,707882,764738,806694,818583,
                         832573,835760,845121,935353,935964,952262,
                         952284,975079,1006728,1040198,1104300,1104447,
                         1107704,1107821,1109840,1110996,1111577,1113361,
                         1117243,1118716,1121909,1128632,1129560,*/
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
                        //2022DLpassage
                };
                //int NUM =2;
                String SystemName =NUM+"";//CombSUMtest Borda
                //String SystemName =p+"";
                //String outputPath = "E:\\TRECDataset\\test\\fusion\\"+SystemName;
                //String outputPath = "E:\\TREC 数据集\\2022DeepLearningPassageRanking\\Borda\\"+SystemName;
                String outputPath = "E:\\TREC 数据集\\2022DeepLearningPassageRanking\\combsum\\"+SystemName;
                //String XLS_Path = "E:\\TREC 数据集\\2020MedicineTrackScientific\\class\\"+NUM+"\\Semi_K"+NUM+"_"+p+".xls";
                String XLS_Path = "E:\\TREC 数据集\\2022DeepLearningPassageRanking\\classification\\"+NUM+"\\Semi_K"+NUM+"_1.xls";
                RunProgram(runPaths,topic,outputPath,SystemName,NUM,XLS_Path);
            }
        }
        /*long endTime=System.currentTimeMillis(); //获取结束时间

        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");*/

//}

}
