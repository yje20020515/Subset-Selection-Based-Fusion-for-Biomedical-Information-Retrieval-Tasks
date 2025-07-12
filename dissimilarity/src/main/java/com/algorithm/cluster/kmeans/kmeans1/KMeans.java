package com.algorithm.cluster.kmeans.kmeans1;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import com.algorithm.DataStruct.Result;
public class KMeans {


    public static ArrayList<Point> Points = new ArrayList<>();//点列表
    public static ArrayList<Cluster> new_Clusters = new ArrayList<>();//组列表
    public static ArrayList<Cluster> old_Clusters = new ArrayList<>();//组列表
    //2.把点读取到Points列表中
    public static void readAllPoint(String inputpath) throws IOException {
        File input = new File(inputpath);
        File[] runs = input.listFiles();
        for(File run:runs) {
            Point point = new Point();
            BufferedReader readerPoint = new BufferedReader(new FileReader(run));
            String runPointname = run.toString().split("\\\\")[run.toString().split("\\\\").length-1];//获得到点名
            point.setPointname(runPointname);
            String bufferline = new String(readerPoint.readLine());
            System.out.println(runPointname);
            StringTokenizer tokenizer=new StringTokenizer(bufferline);
            //读取topic
            int topic=Integer.parseInt(tokenizer.nextToken());
            //读取docID
            String docid=tokenizer.nextToken();
            //读取分数
            double score=Double.parseDouble(tokenizer.nextToken());
            point.coordinatelist.add(new Result(topic, docid, score));
            while((bufferline=readerPoint.readLine())!=null) {
                tokenizer=new StringTokenizer(bufferline);
                //读取topic
                topic=Integer.parseInt(tokenizer.nextToken());
                //读取docID
                docid=tokenizer.nextToken();
                //读取分数
                score=Double.parseDouble(tokenizer.nextToken());
                point.coordinatelist.add(new Result(topic, docid, score));
            }
            Points.add(point);
            readerPoint.close();
        }
        read_MAP();
        System.out.println("-------------读取所有点   end-------------");
    }
    //3.随机将这些点分为N组
    public static void randomsort_N() throws IOException {
        Collections.shuffle(Points);//Points列表打乱
        int start = 0;////points列表开始读取位置
        int Points_size = Points.size();//point总数
        int one_clusters_size = Points_size/NUM_CLUSTERS;//单个分组最大point数
        for(int i=1;i<NUM_CLUSTERS;i++) {//将前num_clusters分组的point分配好
            Cluster cluster = new Cluster(i);
            for(int j = 0;j<one_clusters_size;j++,start++) {//若单个分组分配的point数量大于每个分组的可分point数量，且以分配point数量大于point总数
                Point point = new Point();
                point = findPoint(Points.get(start).getPointname());
                cluster.Pointlist.add(point);
            }
            showcluster(cluster);
            new_Clusters.add(cluster);
            old_Clusters.add(new Cluster(i));
        }
        Cluster cluster = new Cluster(NUM_CLUSTERS);//分配最后一组的point
        for(;start<Points_size;start++) {
            Point point = new Point();
            point = findPoint(Points.get(start).getPointname());
            cluster.Pointlist.add(point);
        }
        showcluster(cluster);
        new_Clusters.add(cluster);
        old_Clusters.add(new Cluster(NUM_CLUSTERS));
        System.out.println("-------------所有点分类完毕   end-------------");
    }
    public static void new_randomsort_N() throws IOException {
        Collections.shuffle(Points);//Points列表打乱
        int start = 0;////points列表开始读取位置
        int Points_size = Points.size();//point总数
        for(int j=1;j<=NUM_CLUSTERS;j++) {//创建中心点列表
            Cluster cluster = new Cluster(j);
            new_Clusters.add(cluster);
            old_Clusters.add(new Cluster(j));
        }
        for(start=1;start<=Points_size;start++) {
            Point point = new Point();
            point = findPoint(Points.get(start-1).getPointname());
            int j = (int)(start % NUM_CLUSTERS);
            if(j==0) {
                j=NUM_CLUSTERS;
            }
            System.out.print(start+" ");
            System.out.println(j-1);
            new_Clusters.get(j-1).Pointlist.add(point);
        }
        showclusters(new_Clusters);
        System.out.println("-------------所有点分类完毕   end-------------");
    }

    /*
     * 显示每个cluster分配的系统
     */
    public static void showcluster(Cluster cluster) {
        System.out.println("cluster ID :"+cluster.getClusterID()+"  cluster size: "+cluster.Pointlist.size());
        for(int i=0;i<cluster.Pointlist.size();i++) {
            System.out.print(cluster.Pointlist.get(i).getPointname()+" , ");
        }
        System.out.println();
    }
    /*
     * 显示每个cluster分配的系统
     */
    public static int showclusters( ArrayList<Cluster> clusters) {
        System.out.println("共有： "+clusters.size()+" 组");
        for(int j=0;j<clusters.size();j++) {
            System.out.print("cluster ID :"+clusters.get(j).getClusterID()+"  cluster size: "+clusters.get(j).Pointlist.size());
            System.out.println();
            if(clusters.get(j).Pointlist.size()==0) {
                System.err.println("组内无点："+clusters.get(j).Pointlist.size());
                return 0;
            }
            for(int i=0;i<clusters.get(j).Pointlist.size();i++) {
                System.out.print(clusters.get(j).Pointlist.get(i).getPointname()+" , MAP: "+clusters.get(j).Pointlist.get(i).getMAP()+" , Distance: "+clusters.get(j).Pointlist.get(i).P_C_distance);
            }
            System.out.println();
        }
        return 1;
    }

    //4.计算他们的中心点，并对中心点坐标赋值
    public static void calculate_Center() throws IOException {

        for(int i=0;i<new_Clusters.size();i++) {//有分为i组
            if(new_Clusters.get(i).coordinateList==null) {
                new_Clusters.get(i).createCoordinateList();
            }else {
                new_Clusters.get(i).ZeroCoordinateList();
            }
            int cluster_size = new_Clusters.get(i).Pointlist.size();//获取每个cluster有几个点
            //System.out.println("这是第: "+new_Clusters.get(i).getClusterID()+" 组");
            //System.out.println("改组有: "+cluster_size+" 个点");
            for(int coordinate=0;coordinate<coordinate_size;coordinate++) {//维度坐标计算
                //System.out.println("计算第: "+(coordinate+1)+" 中心点维度坐标值");
                double cluster_coordinaate_score = 0.0;
                for(int clustercount=0;clustercount<cluster_size;clustercount++) {//计算中心点每个维度的值
                    cluster_coordinaate_score += new_Clusters.get(i).Pointlist.get(clustercount).coordinatelist.get(coordinate).getScore();
                }
                new_Clusters.get(i).coordinateList.get(coordinate).setScore(cluster_coordinaate_score/cluster_size);
				/*System.out.println("topic\t"+new_Clusters.get(i).coordinateList.get(coordinate).getTopic()
						+"\tdocid\t"+new_Clusters.get(i).coordinateList.get(coordinate).getDocid()
						+"\tscore\t"+new_Clusters.get(i).coordinateList.get(coordinate).getScore());*/
            }
            //System.out.println();
        }
        //中心点坐标计算
        System.out.println("----------------中心点坐标计算   end-------------");
    }
    /*
     * 输出中心点到文件中
     */
    public static void write_Cluster_File() throws IOException {
        for(int i=0;i<NUM_CLUSTERS;i++) {
            String centerName = "center_"+String.valueOf(i);
            BufferedWriter writer=new BufferedWriter(new FileWriter("health2020\\test_center\\"+centerName));
            for(int j=0;j<coordinate_size;j++) {
                writer.write(new_Clusters.get(i).coordinateList.get(j).getTopic()+
                        "\t"+new_Clusters.get(i).coordinateList.get(j).getDocID()+
                        "\t"+new_Clusters.get(i).coordinateList.get(j).getScore()+
                        "\n");
            }
            writer.close();
        }

    }
    //5.计算点到中心点的距离
    public static double distance_P_C(Point point,Cluster cluster) {
        double distance=0.0;
        for(int i = 0;i<coordinate_size;i++) {
            distance += Math.pow(point.coordinatelist.get(i).getScore()-cluster.coordinateList.get(i).getScore(), 2) ;
        }
        return Math.sqrt(distance);
    }
    public static double distance_C_C(Cluster NEW_cluster,Cluster OLD_cluster) {
        double distance=0.0;
        for(int i = 0;i<coordinate_size;i++) {
            distance += Math.pow(NEW_cluster.coordinateList.get(i).getScore()-OLD_cluster.coordinateList.get(i).getScore(), 2) ;
        }
        return Math.sqrt(distance);
    }
    //将点分给具有最小距离的中心点

    public static void sort() throws IOException {
        save_Center();//将new_cluster的point点保存到old_cluster中
        /*
         * 清理掉中心点中的run点，为之后替换新的坐准备
         */
        for(int i=0;i<new_Clusters.size();i++) {
            new_Clusters.get(i).Pointlist.clear();
        }
        //System.out.println("清空 中心点内的pointlist列表");
        //showclusters(new_Clusters);
        for(int i=0;i<Points.size();i++) {//获取到每个点
            double[] distance_set = new double[new_Clusters.size()];
            for(int j=0;j<new_Clusters.size();j++) {//获取每个中心点
                distance_set[j]=distance_P_C(Points.get(i), new_Clusters.get(j));
                //System.out.println("j= "+j+"\t 距离：\t"+distance_set[j]+"\tPoint: \t"+Points.get(i).getPointname()+"\tcluster: \t"+new_Clusters.get(j).getClusterID());
            }
            int position_min = 0;//中心点在distance_set中的位置坐标
            double distance_min = 1000.0;
            for(int j=0;j<new_Clusters.size();j++) {//找到最小的距离
                if(distance_min>=distance_set[j]) {
                    distance_min=distance_set[j];
                    position_min=j;
                }
            }
            //System.err.println("最小："+distance_min+" 位置 ： "+(position_min+1));
            Point point = new Point();
            point = findPoint(Points.get(i).getPointname());
            //copyPoint(point, Points.get(i));
            new_Clusters.get(position_min).Pointlist.add(point);
        }
        //System.out.println("对每个点重新计算其中心点");
        //showclusters(new_Clusters);
        //System.out.println("重新计算中心点的坐标");
        calculate_Center();
    }

    /*
     * 把旧中心点保存下来和旧中心点的坐标位置记录下来，保存至oldClusters对象中
     */
    public static void save_Center() throws IOException {
        //old_Clusters.clear();

        for(int i=0;i<old_Clusters.size();i++) {
            old_Clusters.get(i).clearPoint();
            if(old_Clusters.get(i).coordinateList==null) {
                old_Clusters.get(i).createCoordinateList();
            }else {
                old_Clusters.get(i).ZeroCoordinateList();
            }
        }
        for(int i=0;i<new_Clusters.size();i++) {
            //old_Clusters.get(i).setClusterID(new_Clusters.get(i).getClusterID());
            for(int j=0;j<new_Clusters.get(i).Pointlist.size();j++) {
                old_Clusters.get(i).Pointlist.add(new_Clusters.get(i).Pointlist.get(j));
            }

            for(int j=0;j<coordinate_size;j++) {
                old_Clusters.get(i).coordinateList.get(j).setScore(new_Clusters.get(i).coordinateList.get(j).getScore());
            }
        }

		/*
		for(int i=0;i<new_Clusters.size();i++) {
			Cluster cluster = new Cluster();
			cluster.setClusterID(new_Clusters.get(i).getClusterID());
			for(int j=0;j<new_Clusters.get(i).Pointlist.size();j++) {
				Point point = new Point();
				point = findPoint(new_Clusters.get(i).Pointlist.get(j).getPointname());
				cluster.Pointlist.add(point);
			}
			cluster.createCoordinateList();
			for(int j=0;j<coordinate_size;j++) {
				cluster.coordinateList.get(j).setScore(new_Clusters.get(i).coordinateList.get(j).getScore());
			}
			old_Clusters.add(cluster);
		}
		*/
		/*
		System.out.println("--------------------------这是新点-------------------------");
		showclusters(new_Clusters);
		System.out.println("坐标大小"+new_Clusters.get(0).coordinateList.size());
		System.out.println("--------------------------这是旧点-------------------------");
		showclusters(old_Clusters);
		System.out.println("坐标大小"+old_Clusters.get(0).coordinateList.size());
		*/
    }
    /*
     * 找到对应的point
     */
    public static Point findPoint(String name) {
        for(int i=0;i<Points.size();i++) {
            if(name.equals(Points.get(i).getPointname())) {
                return Points.get(i);
            }
        }
        return null;
    }
    /*
     * 判断new_clusters和old_clusters中的点有无变化
     */
    public static boolean change_if_distance() {
        double distanceCC=0.0;
        for(int i = 0; i<new_Clusters.size();i++) {
            if(new_Clusters.get(i).Pointlist.size()==0) {
                continue;
            }
            distanceCC += distance_C_C(new_Clusters.get(i),old_Clusters.get(i));

        }
        System.out.println("中心点的距离： "+distanceCC);
        if(distanceCC==0) {
            return true;
        }else {
            return false;
        }
    }
    /*
     * 对每个point点赋MAP值
     */
    public static void read_MAP() throws IOException {
        FileInputStream fip = new FileInputStream("E:\\TRECDateset\\classificationEXP\\2020Health\\Adhoc\\2020Health_evalMAP.xls");
        HSSFWorkbook wb = new HSSFWorkbook(fip);
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(0);
        String runName;
        double MAP;
        //i为run的个数
        for(int i= 1;i<sheet.getPhysicalNumberOfRows();i++) {
            row = sheet.getRow(i);
            runName = row.getCell(0).getStringCellValue();
            MAP = row.getCell(1).getNumericCellValue();
            Point point = findPoint(runName);
            point.setMAP(MAP);
        }
        wb.close();
        fip.close();
    }
    /*
     * 比较器将point按照MAP进行排序
     */
    public static class pointCompareByMAP implements Comparator<Point>{
        @Override
        public int compare(Point o1, Point o2) {
            if(o1.getMAP() > o2.getMAP()) {
                return -1;
            }
            if(o1.getMAP() < o2.getMAP()) {
                return 1;
            }
            return (int) (o2.getMAP()-o1.getMAP());
        }
    }
    /*
     * 排序将pointlist按照MAP进行排序
     */
    public static void sortPoint(ArrayList<Cluster> Clusters) {
        for(int i=0;i<Clusters.size();i++) {
            Collections.sort(Clusters.get(i).Pointlist,new pointCompareByMAP());;
        }
    }
    /*
     * 将结果输入到一个表格中
     */
    public static int writeTo_XLS(String xlspath,ArrayList<Cluster> Clusters) throws IOException {
        int flag = 1;//为1则不许需要重新运行;为0则需要重新运行;
        int count_row = 0;
        FileOutputStream fop = new FileOutputStream(xlspath);
        HSSFWorkbook wb= new HSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row row;

        for(int i=0;i<Clusters.size();i++) {
            row = sheet.createRow(count_row);
            if(Clusters.get(i).Pointlist.size()==0) {
                row.createCell(0).setCellValue("null");
                return 0;
            }else {

                for(int j=0;j<Clusters.get(i).Pointlist.size();j++) {
                    row.createCell(j).setCellValue(getID(Clusters.get(i).Pointlist.get(j).getPointname())+","+Clusters.get(i).Pointlist.get(j).getPointname());
                }

            }
            count_row++;
        }
        wb.write(fop);
        wb.close();
        fop.close();
        return 1;
    }
    /*
     * 根据名字填获得系统序号
     */
    public static int getID(String PointName) {
        int ID = 0;
        //原始文件路径
        //File file = new File("D:\\eclipse\\eclipse-workspace\\my_k_means2\\health2020\\input_raw");
        File file = new File("E:\\TRECDateset\\classificationEXP\\2020Health\\Adhoc\\input_Point");
        File[] files = file.listFiles();
        for(File run:files) {
            //String path_runName = run.toString().split("\\\\")[run.toString().split("\\\\").length-1].replaceAll("input.", "");
            String path_runName = run.toString().split("\\\\")[run.toString().split("\\\\").length-1].replaceAll("input.", "");
            if(PointName.equals(path_runName)||PointName.contains(path_runName)||path_runName.contains(PointName)) {
                return ID;
            }else {
                ID++;
            }
        }
        return ID;
    }
    private static void createDirectory(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()){
            file.mkdirs();
        }
    }
    //1.设置分N数
    /*
     * 2020health corrdinate_size = 433394
     * 2019decision coordinate_size = 275972
     * 2019PrecisionLiterature        302662
     * 2018PrecisionLiterature        769971
     */
    public static int coordinate_size = 433394;//每个run的维度大小
    //public static int coordinate_size = 15;//每个点的维度大小   测试
    public static int NUM_CLUSTERS = 16;//分组数

    public static int num_experiments_start = 1;//实验次数
    public static int num_experiments_end = 50;//实验次数
    //6.重复4-5步骤，直到各个组的点没有变化视为收敛，或者迭代次数为MAX时输出各个组
    public static int run_program(int num_experiments) throws IOException {


        //String inputfilepath = "D:\\TREC数据集文件\\new_health2020\\runPoint";
        String inputfilepath = "E:\\TRECDateset\\classificationEXP\\2020Health\\Adhoc\\input_Point";

        //output_XLS_path="文件路径\\分类次数文件路径\\keams_k+分类次数+实验次数+.xls"

        //String output_XLS_path = "D:\\TREC数据集文件\\new_health2020\\kmeans_classification_xls\\"+NUM_CLUSTERS+"\\"+"kmeans_k"+NUM_CLUSTERS+"_"+num_experiments+".xls";

        //String output_XLS_path = "D:\\TREC数据集文件\\new_idea\\2020Health\\classification_fusion\\"+NUM_CLUSTERS+"\\classification\\kmeans_k"+NUM_CLUSTERS+"_"+num_experiments+".xls";
        String output_XLS_path = "E:\\TRECDateset\\classificationEXP\\2020Health\\Adhoc\\classification_Kmeans\\"+NUM_CLUSTERS+"\\";
        createDirectory(output_XLS_path);
        output_XLS_path = output_XLS_path + "\\KMeans_k"+NUM_CLUSTERS+"_"+num_experiments+".xls";



        //String inputfilepath = "health2020\\test_points";
        readAllPoint(inputfilepath);
        //randomsort_N();
        new_randomsort_N();
        calculate_Center();
        System.out.println("这是第： 1  次运行");
        sort();
        int flag;
        flag = showclusters(new_Clusters);

        if(flag==0) {
            System.out.println("---------------------重新执行程序-----------------");
            return 0;
        }

        for(int i=1;i<100;i++) {
            if(change_if_distance()) {
                break;
            }
            System.out.println("这是第： "+(i+1)+" 次运行");
            sort();
            flag = showclusters(new_Clusters);

            if(flag==0) {
                System.out.println("---------------------重新执行程序-----------------");
                return 0;
            }
        }
        //使用MAP对其进行排序
        sortPoint(new_Clusters);

        showclusters(new_Clusters);

        writeTo_XLS(output_XLS_path, new_Clusters);

        //write_Cluster_File();
        System.out.println("------------------end----------------");

        return 1;
    }



    public static void main(String[] args) throws IOException {
        int num_experiments;//实验次数
        for(num_experiments=num_experiments_start;num_experiments<=num_experiments_end;num_experiments++) {
            System.err.println("第： "+num_experiments+" 次实验开始");
            Points.clear();
            new_Clusters.clear();
            old_Clusters.clear();
            while(run_program(num_experiments)==0) {
                Points.clear();
                new_Clusters.clear();
                old_Clusters.clear();
            }
        }
    }


}
