package com.algorithm.cluster.eval;

import com.algorithm.DataStruct.HashMapDocKey;
import com.algorithm.DataStruct.HashMapDocValue;
import com.algorithm.Distance.StandardDistance;
import com.algorithm.cluster.hierarchical.agglomerative1.ClusterPoint;
import com.algorithm.cluster.hierarchical.agglomerative1.RunPoint;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * 实现聚类结果的内部评价指标，DBI(Davies-Bouldin index),CH(Calinski-Harbasz Score),
 */
public class evaluationInternal {
    public static void main(String[] args) throws IOException {
        int startNum = 20;
        int endNum = 20;
        int startEXP = 1;
        int endEXP = 1;
        String classificationResultXLSPath = "E:\\TRECDateset\\classificationEXP\\2021Health\\adhoc\\classification_CC\\";
        String inputPaths = "E:\\TRECDateset\\classificationEXP\\2021Health\\adhoc\\standard_input_nor30-60\\";
        runProgram(classificationResultXLSPath,inputPaths,startNum,endNum,startEXP,endEXP);
    }
    public static void runProgram(String classificationResultXLSPath,String inputPaths,int startNum,int endNum,int startEXP,int endEXP) throws IOException {
        for (int NUM = startNum; NUM <= endNum; NUM++) {
            for (int EXP = startEXP; EXP <= endEXP; EXP++) {
                String xlsName = "CC"+"_K"+NUM+"_"+EXP+".xls";
                String classificationXLSNamePath = classificationResultXLSPath+"\\"+NUM+"\\"+xlsName;
                ArrayList<ClusterPoint> clusterPoints = readClassificationResultXLSPath(classificationXLSNamePath, inputPaths);
                clusterPoints = calculateClusterDimensionList(clusterPoints);
                Double DBI = calculateDBI(clusterPoints);
                Double CH = calculateCH(clusterPoints);
                String XLSName = classificationXLSNamePath.split("\\\\")[classificationXLSNamePath.split("\\\\").length - 1];
                System.out.println(XLSName+"\t"+DBI+"\t"+CH+"\t");
            }
        }
    }

    private static ArrayList<ClusterPoint> calculateClusterDimensionList(ArrayList<ClusterPoint> clusterPoints) {
        for (ClusterPoint clusterPoint : clusterPoints) {

            for (RunPoint runPoint : clusterPoint.getClusterList()) {
                //System.out.println(runPoint.getSystemName());
                for (Map.Entry<HashMapDocKey, HashMapDocValue> runPointEntry : runPoint.getDimensionList().entrySet()) {
                    HashMapDocKey pointKey = runPointEntry.getKey();
                    HashMapDocValue pointValue = runPointEntry.getValue();
                    HashMapDocValue clusterValue = clusterPoint.getDimensionList().get(pointKey);
                    if (clusterValue != null){
                        clusterValue.setScore(clusterValue.getScore()+pointValue.getScore());
                    }else {
                        Integer topic = pointKey.getTopic();
                        String docID = pointKey.getDocID();
                        Integer rank = pointValue.getRank();
                        Double score = pointValue.getScore();
                        String systemName = pointValue.getSystemName();

                        clusterPoint.getDimensionList().put(new HashMapDocKey(topic,docID),new HashMapDocValue(rank,score,systemName));
                    }

                }
            }

            for (Map.Entry<HashMapDocKey, HashMapDocValue> clusterEntry : clusterPoint.getDimensionList().entrySet()) {
                clusterEntry.getValue().setScore(clusterEntry.getValue().getScore()/clusterPoint.getClusterList().size());
            }
        }
        return clusterPoints;
    }

    private static ArrayList<ClusterPoint>  readClassificationResultXLSPath(String classificationResultXLSPath,String inputPaths) throws IOException {
        ArrayList<ClusterPoint> clusterPoints = new ArrayList<>();
        FileInputStream fis = new FileInputStream(classificationResultXLSPath);
        HSSFWorkbook hw = new HSSFWorkbook(fis);
        Sheet sheet = hw.getSheetAt(0);
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);
            ClusterPoint clusterPoint = new ClusterPoint(i);
            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                String runName = row.getCell(j).getStringCellValue().split(",")[row.getCell(j).getStringCellValue().split(",").length-1];
                RunPoint runPoint = getRunPoint(runName, inputPaths);
                clusterPoint.getClusterList().add(runPoint);
            }
            clusterPoints.add(clusterPoint);
        }
        hw.close();
        fis.close();
        return clusterPoints;
    }

    private static RunPoint getRunPoint(String runName, String inputPaths) throws IOException {
        RunPoint runPoint = new RunPoint(runName);
        String runPointPath = inputPaths +"\\"+runName;
        BufferedReader br = new BufferedReader(new FileReader(runPointPath));
        String line = "";
        while ((line = br.readLine())!=null){
            String[] str = line.split("\\s+");
            Integer topic = Integer.parseInt(str[0]);
            String docID = str[2];
            Double score = Double.parseDouble(str[4]);
            HashMapDocKey key = new HashMapDocKey(topic,docID);
            HashMapDocValue value = new HashMapDocValue();
            value.setScore(score);
            runPoint.getDimensionList().put(key,value);
        }
        br.close();
        return runPoint;
    }

    private static Double calculateCH(ArrayList<ClusterPoint> clusterPoints) {
        Double CH = 0.0;
        //CH=(BGSS/WGSS)*(点总个数-聚类数)/(聚类数-1),WGSS=1/2[(n1-1)diss1+(n2-1)diss2+...+(nk-1)dissk],BGSS=1/2[(K-1)diss+(N-k)Ak],https://blog.csdn.net/qqwowo99/article/details/107799714
        Double AllPointNum = 0.0;//点个数
        for (ClusterPoint clusterPoint : clusterPoints) {
            AllPointNum += clusterPoint.getClusterList().size();
        }
        if (AllPointNum == clusterPoints.size() || clusterPoints.size() == 1){

        }else {
            Double WGSS = calculateWGSS(clusterPoints);
            Double BGSS = calculateBGSS(clusterPoints);
            CH = (BGSS/WGSS)*((AllPointNum - clusterPoints.size())/(clusterPoints.size() -1));
        }
        return CH;
    }

    private static Double calculateBGSS(ArrayList<ClusterPoint> clusterPoints) {
        Double BGSS = 0.0;
        Double meanALLDistance = 0.0;
        Double AllPointNum = 0.0;
        for (ClusterPoint clusterPoint : clusterPoints) {
            AllPointNum += clusterPoint.getClusterList().size();
        }
        for (ClusterPoint clusterPoint : clusterPoints) {
            for (RunPoint runPointA : clusterPoint.getClusterList()) {
                for (RunPoint runPointB : clusterPoint.getClusterList()) {
                    if (!runPointA.getSystemName().equals(runPointB.getSystemName())){
                        meanALLDistance += StandardDistance.getRunARunBDistance(runPointA,runPointB);
                    }
                }
            }
        }
        meanALLDistance = meanALLDistance/2;
        meanALLDistance = meanALLDistance/getMeanCount((int) Math.round(AllPointNum));
        Double Ak = 0.0;

        if (clusterPoints.size() != AllPointNum){
            Double tempSUM = 0.0;
            for (ClusterPoint clusterPoint : clusterPoints) {
                Double meanDistance = 0.0;
                if (clusterPoint.getClusterList().size()!=1){
                    for (RunPoint runPointA : clusterPoint.getClusterList()) {
                        for (RunPoint runPointB : clusterPoint.getClusterList()) {
                            if (!runPointA.getSystemName().equals(runPointB.getSystemName())){
                                meanDistance += StandardDistance.getRunARunBDistance(runPointA,runPointB);
                            }
                        }
                    }
                }
                meanDistance = meanDistance/2;
                meanDistance = meanDistance/getMeanCount(clusterPoint.getClusterList().size());
                tempSUM += (clusterPoint.getClusterList().size()-1)*(meanALLDistance-meanDistance);
            }

            Ak = tempSUM/(AllPointNum-clusterPoints.size());
        }
        BGSS = (clusterPoints.size()-1)*meanALLDistance + (AllPointNum - clusterPoints.size()*Ak);
        BGSS = BGSS/2;
        return BGSS;
    }

    private static Double calculateWGSS(ArrayList<ClusterPoint> clusterPoints) {
        Double WGSS = 0.0;
        for (ClusterPoint clusterPoint : clusterPoints) {
            Double meanDistance = 0.0;
            if (clusterPoint.getClusterList().size()!=1){
                for (RunPoint runPointA : clusterPoint.getClusterList()) {
                    for (RunPoint runPointB : clusterPoint.getClusterList()) {
                        if (!runPointA.getSystemName().equals(runPointB.getSystemName())){
                            meanDistance += StandardDistance.getRunARunBDistance(runPointA,runPointB);
                        }
                    }
                }
            }
            meanDistance = meanDistance/2;
            meanDistance = meanDistance/getMeanCount(clusterPoint.getClusterList().size());
            WGSS += (clusterPoint.getClusterList().size()-1)*meanDistance;
        }
        WGSS = WGSS/2;
        return WGSS;

    }

    private static int getMeanCount(int count) {
        int MeanCount = 1;
        if (count != 1){
            for (int i = 1; i <count; i++) {
                MeanCount += i;
            }
            MeanCount--;
        }
        return MeanCount;
    }

    private static Double calculateDBI(ArrayList<ClusterPoint> clusterPoints) {
        Double DBI = 0.0;
        //计算每个簇中心点到簇内各个点的平均距离
        calculateMeanDistanceForC_P(clusterPoints);
        for (ClusterPoint clusterPointA : clusterPoints) {
            //计算当前簇和其他簇的所有R_AB值，R_AB=(ClusterPointA.meanDistanceForC_P+ClusterPointB.meanDistanceForC_P)/(Distance(ClusterPointA,ClusterPointB))
            ArrayList<Double> R_AB = new ArrayList<>();
            for (ClusterPoint clusterPointB : clusterPoints) {
                if (clusterPointA.getClusterID()!=clusterPointB.getClusterID()){
                    R_AB.add((clusterPointA.getMeanDistanceForC_P()+clusterPointB.getMeanDistanceForC_P())/StandardDistance.getRunARunBDistance(clusterPointA,clusterPointB));
                }
            }
            //选择R_AB中最大的
            Double D_A = Collections.max(R_AB);
            //System.out.println(D_A);
            DBI += D_A;
        }
        DBI = DBI/clusterPoints.size();
        return DBI;
    }

    private static void calculateMeanDistanceForC_P(ArrayList<ClusterPoint> clusterPoints) {
        for (ClusterPoint clusterPoint : clusterPoints) {
            Double meanDistanceForC_P = 0.0;
            for (RunPoint runPoint : clusterPoint.getClusterList()) {
                meanDistanceForC_P += StandardDistance.getRunARunBDistance(clusterPoint,runPoint);
            }
            meanDistanceForC_P = meanDistanceForC_P/clusterPoint.getClusterList().size();
            clusterPoint.setMeanDistanceForC_P(meanDistanceForC_P);
        }
    }


}
