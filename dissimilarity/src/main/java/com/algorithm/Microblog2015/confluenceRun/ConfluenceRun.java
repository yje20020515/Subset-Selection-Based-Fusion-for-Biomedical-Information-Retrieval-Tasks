package com.algorithm.Microblog2015.confluenceRun;

import java.io.*;

/**
 * 将10天的数据汇合成一个，以便于使用python程序进行评估
 */
public class ConfluenceRun {

    public static void Confluence(String fusionPath,String outputFusionPath, String fusionName) throws IOException {
        File[] files = new File(fusionPath).listFiles();
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFusionPath+"\\Confluence_"+fusionName));
        for (File run : files) {
            String runName = run.getName();
            String date = runName.substring(0,8);
            System.out.println(date);
            BufferedReader br = new BufferedReader(new FileReader(run));
            String line = "";
            while ((line = br.readLine())!=null){
                String[] str = line.split("\\s+");
                String topic = "MB"+str[0];
                String docID = str[2];
                int rank =Integer.parseInt(str[3]) ;
                if (rank >100){
                    continue;
                }
                String score = str[4];
                String systemName = str[5];


                bw.write(date+"\t"
                            +topic+"\t"
                            +"Q0"+"\t"
                            +docID+"\t"
                            +rank+"\t"
                            +score+"\t"
                            +systemName+"\n");
                bw.flush();
            }

            br.close();
        }
        bw.close();
    }

    public static void main(String[] args) throws IOException {
//        String fusionName = "fusion27";
//        String fusionPath = "D:\\TREC数据集文件\\2015Microblog\\Microblog_TaskB\\"+fusionName+"\\fusion";
//        String outputFusionPath = "D:\\TREC数据集文件\\2015Microblog\\Microblog_TaskB\\"+fusionName+"\\Confluence_Fusion";
//        Confluence(fusionPath,outputFusionPath,fusionName);

        System.out.println(Math.pow(2,3));
        System.out.println(Math.log(2));
    }
}
