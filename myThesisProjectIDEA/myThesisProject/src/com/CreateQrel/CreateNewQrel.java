package com.CreateQrel;

import java.io.*;

public class CreateNewQrel {
    /**
     * 将输入的文件按照qrel格式进行输出，并控制每个topic名下的doc文档不超过StandardNum
     * @param inputSumPath
     * @param outputQrelPath
     * @param StandardNum
     * @throws IOException
     */
    public static void writeQrel(String inputSumPath,String outputQrelPath,int StandardNum) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(inputSumPath));
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputQrelPath));

        String line = "";
        while((line = br.readLine())!=null){

            String[] str = line.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            String docID = str[2];
            int rank = Integer.parseInt(str[3]);
            if(rank > StandardNum){
                continue;
            }
            double score = Double.parseDouble(str[4]);

            line = "";
            line += topic + "\t";
            line += "0" + "\t";
            line += docID + "\t";
            line += score + "\n";
            bw.write(line);
        }
        br.close();
        bw.close();
    }

    public static void main(String[] args) throws IOException {
        String inputSumPath ="D:\\TREC数据集文件\\new_idea3\\health2020\\fusion8\\my_qrel\\new_fusion_nor\\LCfusion_k50_1";
        String outputQrelPath = "D:\\TREC数据集文件\\new_idea3\\health2020\\fusion8\\my_qrel\\idea3_qrel";
        int StandardNum = 1000;
        writeQrel(inputSumPath,outputQrelPath,StandardNum);
    }
}
