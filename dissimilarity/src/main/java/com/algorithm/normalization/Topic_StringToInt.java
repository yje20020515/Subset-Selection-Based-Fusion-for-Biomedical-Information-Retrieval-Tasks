package com.algorithm.normalization;

import java.io.*;

public class Topic_StringToInt {
    public static void StringToInt(String runsPath,String outputRunsPath) throws IOException {
        File[] runs = new File(runsPath).listFiles();
        for (File run : runs) {
            String runName = run.getName();
            BufferedReader br = new BufferedReader(new FileReader(run));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputRunsPath+"\\"+runName));
            String line = "";
            while((line = br.readLine())!=null){
                String[] str = line.split("\\s+");
                String date = str[0];
                String topic = str[1].replaceAll("BW","");
                String Q0 = str[2];
                String docID = str[3];
                String rank = str[4];
                String score = str[5];
                String system = str[6];

                line = "";
                line += topic +"\t";
                line += Q0 +"\t";
                line += docID +"\t";
                line += rank +"\t";
                line += score +"\t";
                line += system +"\t";
            }
        }
    }

    public static void main(String[] args) {

    }
}
