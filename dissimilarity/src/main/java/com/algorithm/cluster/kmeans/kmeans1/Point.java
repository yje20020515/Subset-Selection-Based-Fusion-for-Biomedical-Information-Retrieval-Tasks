package com.algorithm.cluster.kmeans.kmeans1;
import java.util.ArrayList;
import com.algorithm.DataStruct.Result;
public class Point {
    public ArrayList<Result> coordinatelist;//坐标位分数
    public String Pointname;//每个点的名字
    public double MAP;
    public double P_C_distance; //点到中心的的距离
    public Point() {
        coordinatelist=new ArrayList<>();
    }
    public String getPointname() {
        return Pointname;
    }
    public void setPointname(String pointname) {
        Pointname = pointname;
    }
    public double getMAP() {
        return MAP;
    }
    public void setMAP(double mAP) {
        MAP = mAP;
    }
}
