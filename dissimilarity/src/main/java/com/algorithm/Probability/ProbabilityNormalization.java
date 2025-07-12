package com.algorithm.Probability;

import com.algorithm.DataStruct.Result;
import com.algorithm.DataStruct.Results;

import java.io.*;
import java.util.ArrayList;
/**
 * 将生成的probability文件的分数归一化到run中
 */
public class ProbabilityNormalization {


    /**
     *
     * @param runPath
     * @param getTopic
     * @return 返回runPath中以getTopic开头的result对象列表
     * @throws IOException
     */
    public static Results getResults(String runPath, int getTopic) throws IOException {
        Results Rs = new Results(getTopic);
        BufferedReader br = new BufferedReader(new FileReader(runPath));
        String line = "";
        int rank = 1;
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            if(topic==getTopic){
                String docID = str[2];
                double score = Double.parseDouble(str[4]);
                String systemName = str[5];
                Result r = new Result(topic,docID,rank,score,systemName);
                Rs.getList().add(r);
                rank++;
            }
        }
        br.close();
        return Rs;
    }

    /**
     * 将输入的run中的docID和topic存储进results对象中;
     * @param runPath
     * @param topicsInt
     * @throws IOException
     */
    public static ArrayList<Results> getRun(String runPath, int[] topicsInt) throws IOException {
        ArrayList<Results> run = new ArrayList<>();
        ArrayList<Integer> topics = changeInt(topicsInt);
        for (Integer topic : topics) {
            run.add(getResults(runPath,topic));
        }
        return run;
    }

    /**
     * 将int[]数组转换为集合对象
     * @param topicsInt
     * @return
     */
    public static ArrayList<Integer> changeInt(int[] topicsInt){
        ArrayList<Integer> topics = new ArrayList<>();
        for (int topic : topicsInt) {
            topics.add(topic);
        }
        return topics;
    }

    public static double[] getProbabilityFile(String ProPath) throws IOException {
        double[] ProbabilityMatrix = new double[1000];
        BufferedReader br = new BufferedReader(new FileReader(ProPath));
        String line = br.readLine();//跳过第一行
        int count = 0;
        while ((line = br.readLine())!=null){
            double pro = Double.parseDouble(line);
            ProbabilityMatrix[count] = pro;
            count++;
        }
        br.close();
        return ProbabilityMatrix;
    }

    public static void ProbabilityNor(int[] topicsInt,String ProPath,String inputPath,String outputPath) throws IOException {

        File[] Profiles= new File(ProPath).listFiles();
        for (File profile : Profiles) {
            String profileName = profile.getName();
            double[] ProbabilityMatrix =  getProbabilityFile(profile.toString());
            System.out.println(profileName);
            ArrayList<Results> run = getRun(inputPath+"\\"+profileName,topicsInt);
            run = Nor(run,ProbabilityMatrix);
            writeRun(run,outputPath+"\\"+profileName);
        }





    }

    private static void writeRun(ArrayList<Results> run, String outputRun) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputRun));
        for (Results results : run) {
            for (Result result : results.getList()) {
                bw.write(result.getTopic()+"\t"+
                        "Q0"+"\t"+
                        result.getDocID()+"\t"+
                        result.getRank()+"\t"+
                        result.getScore()+"\t"+
                        result.getSystemName()+"\n"
                );
            }
        }
        bw.close();
    }

    private static ArrayList<Results> Nor(ArrayList<Results> run,double[] ProbabilityMatrix) {
        for (Results results : run) {
            for (Result result : results.getList()) {
                result.setScore(ProbabilityMatrix[result.getRank()-1]);
            }
        }
        return run;
    }

    public static void main(String[] args) throws IOException {
        /*int[] topicsInt = {//2011MB ji
                1, 3, 5, 7, 9,
                11,13,15,17,19,
                21,23,25,27,29,
                31,33,35,37,39,
                41,43,45,47,49
        };*/
        /*int[] topicsInt = {//2011MB ou
                2, 4, 6, 8, 10,
                12,14,16,18,20,
                22,24,26,28,30,
                32,34,36,38,40,
                42,44,46,48,50
        };*/
        int[] topicsInt ={
                2, 4, 6, 8, 10,
                12,14,16,18,20,
                22,24,26,28,30,
                32,34,36,38,40,
                42,44,46,48,50};
        /*int[] topicsInt ={171,172,173,174,175,176,177,178,179,180
                        ,181,182,183,184,185,186,187,188,189,190
                        ,191,192,193,194,195,196,197,198,199,200
                        ,201,202,203,204,205,206,207,208,209,210
                        ,211,212,213,214,215,216,217,218,219,220
                        ,221,222,223,224,225};*/
        //int[] topicsInt={1,2,4,5,6,7,8,9,10,12,13,14,15,16,18,19,20,21,22,23,24,25,27,28,29,30,31,34,36,37,39,40,41,43,47,49,50};
        String date = "20150729";
        String ProPath = "D:\\TRECDateset\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\ProbabilityfileSlide_ji\\";
        String inputPath = "D:\\TRECDateset\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\standard_input_ou\\";
        String outputPath = "D:\\TRECDateset\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\standard_input_norSlide_ou\\";
        ProbabilityNor(topicsInt,ProPath,inputPath,outputPath);
    }
}
