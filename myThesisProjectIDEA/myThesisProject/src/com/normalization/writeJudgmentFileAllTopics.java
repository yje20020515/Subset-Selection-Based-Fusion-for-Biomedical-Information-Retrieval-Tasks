package com.normalization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

public class writeJudgmentFileAllTopics {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("E:\\TREC Êý¾Ý¼¯\\2020MedicineTrackScientific\\judgement\\judgementfile1.txt"));
        TreeSet<Integer> topics = new TreeSet<>();
        String oneLine = "";
        while ((oneLine = br.readLine())!=null){
            String[] str = oneLine.split("\\s+");
            topics.add(Integer.parseInt(str[0]));
        }
        Iterator<Integer> iteratorTopics = topics.iterator();
        while (iteratorTopics.hasNext()){
            System.out.println(iteratorTopics.next());
        }
        br.close();
    }
}
