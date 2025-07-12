package com.algorithm.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class readALL {

    public static double meanMAP =0.0;
    public static double meanAP = 0.0;
    public static double meanRP = 0.0;
    public static double meanP10 = 0.0;
    public static double meanNDCG10 = 0.0;

    public static void main(String[] args) throws IOException {
        int StartNum = 3;
        int EndNum = 20;
        for (int NUM = StartNum; NUM <= EndNum ; NUM++) {
            meanMAP =0.0;
            meanAP = 0.0;
            meanRP = 0.0;
            meanP10 = 0.0;
            meanNDCG10 = 0.0;

            String evalPath="D:\\TRECDateset\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\fusion8\\eval\\ALL\\"+NUM+"\\";
            int excNum = new File(evalPath).listFiles().length;
            for (File evalOnePath : new File(evalPath).listFiles()){
                readFile(evalOnePath.getPath());
            }

            /*String evalPath="D:\\TRECDateset\\methode_qrel012\\BigExperiment\\2012MB\\Adhoc\\eval\\all\\";
            int excNum = new File(evalPath).listFiles().length;
            for (File file : new File(evalPath).listFiles()) {
                readFile(file.getPath());
            }*/
            meanMAP = meanMAP/excNum;
            meanAP = meanAP/excNum;
            meanRP = meanRP/excNum;
            meanP10 = meanP10/excNum;
            meanNDCG10 = meanNDCG10/excNum;
            System.out.println(NUM+"\t"+meanMAP+"\t"+meanAP+"\t"+meanRP+"\t"+meanP10+"\t"+meanNDCG10+"\t");
        }

    }

    private static void readFile(String evalPath) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(evalPath));
        String oneLine = "";
        double evalMAP =0.0;
        double evalAP = 0.0;
        double evalRP = 0.0;
        double evalP10 = 0.0;
        double evalNDCG10 = 0.0;
        while((oneLine = br.readLine())!=null){
            String[] strs = oneLine.split(",");
            if (strs[1].equals("MAP")){
                evalMAP = Double.parseDouble(strs[2]);
            }else if (strs[1].equals("AP")){
                evalAP = Double.parseDouble(strs[2]);
            }else if (strs[1].equals("RP")){
                evalRP = Double.parseDouble(strs[2]);
            }else if (strs[1].equals("P10")){
                evalP10 = Double.parseDouble(strs[2]);
            }else if (strs[1].equals("NDCG10")){
                evalNDCG10 = Double.parseDouble(strs[2]);
            }
        }
        //System.out.println(evalPath.split("\\\\")[evalPath.split("\\\\").length-1].replaceAll("ALL_U_2012MB_","")+"\t"+evalMAP+"\t"+evalAP+"\t"+evalRP+"\t"+evalP10+"\t"+evalNDCG10);
        meanMAP += evalMAP;
        meanAP += evalAP;
        meanRP += evalRP;
        meanP10 += evalP10;
        meanNDCG10 += evalNDCG10;
        br.close();
    }
}
