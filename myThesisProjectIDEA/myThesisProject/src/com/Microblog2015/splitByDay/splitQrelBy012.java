package com.Microblog2015.splitByDay;

import com.Microblog2015.DatasetStruct.Qrel;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class splitQrelBy012 {
    public static int[] topics = {
            1,2,3,4,5,6,7,8,9,10,
            11,12,13,14,15,16,17,18,19,20,
            21,22,23,24,25,26,27,28,29,30,
            31,32,33,34,35,36,37,38,39,40,
            };

    public static boolean existTopic(int topic){
        for (int t : topics) {
            if(t==topic){
                return true;
            }
        }
        return false;
    }
    public static void splitQrel(String QrelPath,String outputQrelPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(QrelPath));
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputQrelPath));
        ArrayList<Qrel> list = new ArrayList<>();
        String line = "";
        int count_rel_1=0;
        int count_rel_2=0;
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            if (existTopic(topic)){
                String docID = str[2];
                double rel = Double.parseDouble(str[3]);
                /*if(rel == -2){
                    rel = 0;
                }else if (rel == 0){
                    rel = 0;
                    count_rel_1++;
                }else if (rel == 1){
                    rel = 1;
                    count_rel_2++;
                }else if (rel == 2){
                    rel = 2;
                    count_rel_2++;
                }else if (rel == 3){
                    rel = 3;
                    count_rel_2++;
                }else if (rel == 4){
                    rel = 4;
                    count_rel_2++;
                }*/

              /*  if (rel <= 0){
                    rel = 0;
                }else {
                    rel = 1;
                }
              */
                if (rel <= 1.0){
                    rel = 0.0;
                }else {
                    rel = 1.0;
                }
                list.add(new Qrel(topic,docID,rel));
                //System.out.println(topic);
            }
        }
        Collections.sort(list, new Comparator<Qrel>() {
            @Override
            public int compare(Qrel o1, Qrel o2) {
                return o1.getTopic()- o2.getTopic();
            }
        });
        for (Qrel qrel : list) {
            bw.write(qrel.getTopic()+"\t"+"Q0"+"\t"+qrel.getDocID()+"\t"+qrel.getRel()+"\n");
        }
        br.close();
        bw.close();
    }

    public static void main(String[] args) throws IOException {
        String QrelPath = "E:\\TREC 数据集\\2020MedicineTrackScientific\\judgement\\judgementfile1.txt\\";
        String outputQrelPath = "E:\\TREC 数据集\\2020MedicineTrackScientific\\judgement\\judgmentfile1_01";
        splitQrel(QrelPath,outputQrelPath);


    }
}