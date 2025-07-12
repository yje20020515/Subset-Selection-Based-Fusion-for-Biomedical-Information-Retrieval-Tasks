package com.algorithm.cluster.kmeans.kmeans1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.algorithm.DataStruct.Result;

public class Cluster {

    public int clusterID;
    public ArrayList<Point> Pointlist;//该类的点
    public ArrayList<Result> coordinateList;//坐标
    public Cluster(int clusterID) throws IOException {
        Pointlist = new ArrayList<>();
        this.clusterID=clusterID;
    }
    public void createCoordinateList() throws IOException {
        this.coordinateList = new ArrayList<>();
        File nullpoint = new File("E:\\TRECDateset\\classificationEXP\\2020Health\\Adhoc\\nullPoint");
        //File nullpoint = new File("health2020\\test_center\\testnullcenter");//测试
        BufferedReader reader=new BufferedReader(new FileReader(nullpoint));
        String bufferline = new String(reader.readLine());
        StringTokenizer tokenizer=new StringTokenizer(bufferline);
        //System.out.println(bufferline);
        //topic
        int topic=Integer.parseInt(tokenizer.nextToken());
        //zoey 读取文档id
        String docid=tokenizer.nextToken();

        this.coordinateList.add(new Result(topic, docid, 0));
        while((bufferline=reader.readLine())!=null) {
            tokenizer=new StringTokenizer(bufferline);
            topic=Integer.parseInt(tokenizer.nextToken());
            docid=tokenizer.nextToken();
            this.coordinateList.add(new Result(topic, docid, 0));
        }
        reader.close();
    }
    public void clearPoint() {//将该组的所有点清空
        Pointlist.clear();
    }
    public void ZeroCoordinateList() {//将坐标位置的分数清零
        for(int i=0;i<coordinateList.size();i++) {
            coordinateList.get(i).setScore(0);
        }
    }
    public int getClusterID() {
        return clusterID;
    }
    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }
}
