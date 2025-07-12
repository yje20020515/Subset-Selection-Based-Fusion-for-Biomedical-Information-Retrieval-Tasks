package com.algorithm.normalization;

import com.algorithm.DataStruct.Qrel;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class splitQrelBy012 {


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
            //System.out.println(topic);
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
                }else if (rel == 4){
                    rel = 2;
                    count_rel_2++;
                }else if (rel == 2){
                    rel = 3;
                    count_rel_2++;
                }else if (rel == 3){
                    rel = 4;
                    count_rel_2++;
                }*/

                if (rel <= 0){
                    rel = 0;
                }else {
                    rel = 1;
                }
                //System.out.println(rel);
                list.add(new Qrel(topic,docID,rel));
                // System.out.println(topic);
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
    public static int[] topics = {
            101,102,103,104,105,106,107,108,109,110,
            111,112,114,115,117,118,120,121,122,127,
            128,129,131,132,133,134,136,137,139,140,
            143,144,145,146,149,
    };

    public static void main(String[] args) throws IOException {
        String QrelPath = "E:\\TRECDateset\\paperExperiment\\ideaOne\\2021Health\\judgment\\misinfo-resources-2021\\qrels\\qrels-35topics.txt";
        String outputQrelPath = "E:\\TRECDateset\\paperExperiment\\ideaOne\\2021Health\\judgment\\misinfo-resources-2021\\qrels\\2021-derived-qrels\\misinfo-qrels-binary.useful";
        splitQrel(QrelPath,outputQrelPath);


    }
}
