package com.algorithm.cluster.kmeans.kmeans2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
/*
 * Author:HYD黄一栋
 * Last update time:2021/05/15
 * (1)This program has two inputs, the first is the distance matrix .xls file, and the second is
 * the MAP value sorting .xls file of the points in the distance matrix.
 * (2)One output is the k groups grouped by the "k-center point" algorithm, and the
 * second output is the point with the best MAP value for each group.
 */
public class run_k_centre {

    public static double[][] A_B_matrix = new double[52][52];
    /*
     * 读取距离表，并将距离存储到A_B_matrix矩阵中
     */
    public static void init_matrix_A_B() throws IOException {
        FileInputStream fip = new FileInputStream("distance-all-all.xls");//注意必须是.xls文件
        HSSFWorkbook wb = new HSSFWorkbook(fip);
        Sheet sheet = wb.getSheetAt(0);
        Row row=sheet.getRow(0);
        for(int i=1;i<=51;i++) {
            row=sheet.getRow(i);
            for(int j=1;j<=51;j++) {
                A_B_matrix[i][j]=row.getCell(j).getNumericCellValue();
            }
        }
        fip.close();
    }
    /*
     * 函数功能:对所有的run点随机挑选k个点设置为中心点
     * 传参1:所有的run点	ArrayList<pointbean> points
     * 传参2:需要设置几个中心点	int k
     * 返回值1:设置的k个中心点	ArrayList<pointbean> centres
     */
    public static ArrayList<pointbean> findcentre_all(ArrayList<pointbean> points,int k) {

        ArrayList<pointbean> centres= new ArrayList<>();
        Random r= new Random();
        int i,j=0;
        for(i=0;i<k;i++) {
            int randomsystemID = r.nextInt(51);
            pointbean pointtemp = new pointbean();

            for(j=0;j<centres.size();j++) {
                //System.out.println(j+" "+(centres.get(j).id-1));
                if(centres.get(j).id-1==randomsystemID) {
                    System.out.println("重复");
                    i--;
                    break;
                }
            }

            if(j==centres.size()||centres.size()==0) {
                points.get(randomsystemID).setIscentre(true);//更新该点中心点状态
                pointtemp.setName(points.get(randomsystemID).getName());
                pointtemp.setCost(points.get(randomsystemID).getCost());
                pointtemp.setId(points.get(randomsystemID).getId());
                pointtemp.setIscentre(true);
                pointtemp.setIscentre(points.get(randomsystemID).isIscentre());
                //pointtemp=points.get(randomsystemID);
                System.out.println("添加:"+pointtemp.getName()+"到中心点列表"+pointtemp.isIscentre());
                centres.add(pointtemp);//将中心点放入中心点列表中
            }
        }

        for(i=0;i<k;i++) {
            System.out.println(centres.get(i).getName());
        }
        return centres;

    }
    /*
     * 函数功能:在所有的run点中随机一个新的中心点，但是不能和原来的中心点相同
     * 传参1:所有的run点	ArrayList<pointbean> points
     * 传参2:已有的中心点	ArrayList<pointbean> centres
     * 返回值1:新的中心点	pointbean newcentre
     * 注:这个函数在最后并没有用到
     */
    public static pointbean new_centre(ArrayList<pointbean> points,ArrayList<pointbean> centres) {
        pointbean newcentre = new pointbean();
        Random r = new Random(1);
        int randomsystemID = r.nextInt(51);
        int i=0;
        while(i<centres.size()) {
            if(centres.get(i).getId()-1==randomsystemID) {
                randomsystemID = r.nextInt(51);
                i=0;
            }
            i++;
        }
        newcentre=points.get(randomsystemID);
        System.out.println(randomsystemID);
        return newcentre;
    }
    /*
     * 函数功能1:把51个点按照中心点分成k类，也就是把run点的becentre属性改变为所属中心点的名字
     * 传参1:所有的run点	ArrayList<pointbean> points
     * 传参2:已有的中心点	ArrayList<pointbean> centres
     * 注1:此函数无返回值，因为传参是对象，所以将直接改变传参内的值
     * 注2:中心点的becentre属性值是自己
     * 注3:最下面有个调用copy_value函数
     */
    public static void classification_k(ArrayList<pointbean> points,ArrayList<pointbean> centres) {
        double[] minidistance = new double[centres.size()];
        int minitemp = 0;
        for(int i=1;i<=points.size();i++) {
            int count = 0;
            int j;
            for(j=1;j<=centres.size();j++) {
                //System.out.print(!points.get(j-1).isIscentre()+" "+centres.get(j-1).getName()+" "+points.get(i-1).getName());
                minidistance[count++]=distance_p_p(centres.get(j-1),points.get(i-1));
                //System.out.println("points.NAME  "+points.get(i-1).getName()+"  centres.get(j-1)  "+centres.get(j-1).getName()+" "+distance_p_p(centres.get(j-1),points.get(i-1)));

            }

            for(int temp=0;temp<minidistance.length;temp++) {
                if(minidistance[minitemp]>minidistance[temp]) {
                    minitemp=temp;
                }
            }
            //System.out.println("pointname:"+points.get(i-1).getName()+"   centresName:"+centres.get(minitemp).getName()+" 最小值:"+minidistance[minitemp]);
            //centres.get(minitemp).setCost(centres.get(minitemp).getCost()+minidistance[minitemp]);
            points.get(i-1).setBecentre(centres.get(minitemp).getName());
            copy_value(points, centres);
            minitemp = 0;
        }
    }
    /*
     * 函数功能1:将中心点的cost属性值信息复制到对应的run点cost属性值中
     * 传参1:所有的run点	ArrayList<pointbean> points
     * 传参2:已有的中心点	ArrayList<pointbean> centres
     * 注1:应该是没啥用的函数，而且run的cost属性最后被我视为该点的MAP值了
     */
    public static void copy_value(ArrayList<pointbean> points,ArrayList<pointbean> centres) {
        for(int i=0;i<points.size();i++) {
            for(int j=0;j<centres.size();j++) {
                if(centres.get(j).getId()==points.get(i).getId()) {
                    points.get(i).setCost(centres.get(j).getCost());
                }
            }
        }
    }
    /*
     * 函数功能1:计算若将新run点设置为中心点的代价
     * 传参1:所有的run点	ArrayList<pointbean> points
     * 传参2:已有的中心点	ArrayList<pointbean> centres
     * 传参3:被替换的中心点	pointbean oldcentre
     * 传参4:新的中心点		pointbean newcentre
     * 返回值1:将新的点设置为中心点所需要的代价值
     * 注1:代价值为新中心点到某点的距离减去旧中心点到该点的距离，计算所有并累加后为总代价
     * 注2:新中心点进入后会对其他的组造成影响，这些代价值也被累加
     */
    public static double new_Cost(ArrayList<pointbean> points,ArrayList<pointbean> centres,pointbean oldcentre,pointbean newcentre) {
        double new_cost = 0;

        ArrayList<pointbean> points_new = new ArrayList<>();
        ArrayList<pointbean> centres_new = new ArrayList<>();
        for(int i = 0;i<points.size();i++) {
            pointbean temppoint = new pointbean();
            temppoint.setBecentre(points.get(i).getBecentre());
            temppoint.setCost(points.get(i).getCost());
            temppoint.setId(points.get(i).getId());
            temppoint.setIscentre(points.get(i).isIscentre());
            temppoint.setName(points.get(i).getName());
            if(points.get(i).getId()==oldcentre.getId()) {
                temppoint.setIscentre(false);
            }
            if(points.get(i).getId()==newcentre.getId()) {
                temppoint.setIscentre(true);
            }
            points_new.add(temppoint);
        }
        for(int i=0 ; i<centres.size();i++) {
            pointbean temppoint1 = new pointbean();
            temppoint1.setBecentre(centres.get(i).getBecentre());
            temppoint1.setCost(centres.get(i).getCost());
            temppoint1.setId(centres.get(i).getId());
            temppoint1.setIscentre(centres.get(i).isIscentre());
            temppoint1.setName(centres.get(i).getName());
            if(centres.get(i).getId()==oldcentre.getId()) {
                pointbean temppoint = new pointbean();
                temppoint.setBecentre(newcentre.getBecentre());
                temppoint.setCost(newcentre.getCost());
                temppoint.setId(newcentre.getId());
                temppoint.setIscentre(newcentre.isIscentre());
                temppoint.setName(newcentre.getName());
                centres_new.add(temppoint);
                continue;
            }
            centres_new.add(temppoint1);
        }

        classification_k(points_new, centres_new);

        for(int i=0;i<points.size();i++) {
            //System.out.print("pointsname:"+points.get(i).getName()+"pointsbecentre:"+points.get(i).getBecentre());
            //System.out.println("new pointsname:"+points_new.get(i).getName()+"new pointsbecentre:"+points_new.get(i).getBecentre());
            if(points.get(i).getBecentre().equals(points_new.get(i).getBecentre())) {
                //System.err.println("points.get(i).getBecentre(): "+points.get(i).getBecentre()+"points_new.get(i).getBecentre():"+points_new.get(i).getBecentre());
                //new_cost=new_cost;代价不变
            }else {//若不同则代价累加方式为，当前点的新中心点到该点的距离减去旧中心点到该点的距离
                //System.err.print(i+"   ");
                //System.out.print("pointsname: "+points.get(i).getName()+" pointsbecentre: "+points.get(i).getBecentre());
                //System.out.print(" new pointsname: "+points_new.get(i).getName()+" new pointsbecentre: "+points_new.get(i).getBecentre());
                new_cost +=cost_centreA_centreB_pointC(search_point(points_new, points_new.get(i).getBecentre()), search_point(points, points.get(i).getBecentre()), points.get(i));
                //System.err.println(new_cost);
            }
        }
        return new_cost;
    }
    /*
     * 函数功能1:由run名检索到该点的信息，并返回该对象
     * 传参1:所有的point点	ArrayList<pointbean> points
     * 传参2:需要查询的run点
     * 返回值1:返回由run名查询到的该run点对象
     */
    public static pointbean search_point(ArrayList<pointbean> points,String name) {
        pointbean point = new pointbean();
        int i;
        for(i=0;i<points.size();i++) {
            if(points.get(i).getName().equals(name)) {
                break;
            }
        }
        point = points.get(i);
        return point;
    }
    /*
     * 函数功能1:选择代价最小的替换，并将需要交换的Point点的ID和中心点ID返回
     * 传参1:所有的run点对象列表	ArrayList<pointbean> points
     * 传参2:所有的中心点对象列表	ArrayList<pointbean> centres
     * 返回值1:需要交换的Point点ID	ID[0]
     * 返回值2:需要交换的中心点ID		ID[1]
     * 注1:当两次调用该函数返回同样的Point点和中心点ID时就视为该算法收敛
     */
    public static int[] small_cost_centre(ArrayList<pointbean> points,ArrayList<pointbean> centres) {
        double costmini=0;
        double tempcost=0;
        int point_ID=0,centre_ID=0;
        int ID[]=new int[2];
        boolean flag= false;
        for(int j=0;j<centres.size();j++) {
            flag= false;
            for(int i=0;i<points.size();i++) {
                for(int flagi=0;flagi<centres.size();flagi++) {//判断新的中心点是否和将要替换的点的其他的中心点相同
                    if(points.get(i).getId()==centres.get(j).getId())
                        break ;
                    if(points.get(i).getId()==centres.get(flagi).getId())
                        flag=true;
                }
                if(flag==true) {
                    flag= false;
                    continue ;
                }

                tempcost=new_Cost(points, centres, centres.get(j), points.get(i));
                //System.out.println(centres.get(j).getName()+" "+points.get(i).getName()+" cost: "+tempcost+"  pointsId: "+points.get(i).getId()+"  centre_ID:  "+centres.get(j).getId());
                if(costmini>=tempcost) {
                    costmini=tempcost;
                    point_ID=points.get(i).getId();
                    centre_ID=centres.get(j).getId();
                    //System.out.println("costmini:  "+costmini+" point_ID "+point_ID+" centre_ID: "+centre_ID);
                }
            }
        }
        ID[0]=point_ID;
        ID[1]=centre_ID;
        return ID;
    }
    /*
     * 函数功能1:计算中心点A，中心点B到Point点C的代价：实质为A到C的距离减去B到C的距离
     * 传参1:中心点A的ID	int centre_A_ID
     * 传参2:中心点B的ID	int centre_B_ID
     * 传参3:Point点C的ID	int point_C_ID
     * 返回值1:中心点A，中心点B到Point点C的代价	double cost
     */
    public static double cost_centreA_centreB_pointC(int centre_A_ID,int centre_B_ID,int point_C_ID) {//计算点C的中心点由中心点A变为中心点B的代价
        double cost = 0;
        cost=A_B_matrix[centre_A_ID][point_C_ID]-A_B_matrix[centre_B_ID][point_C_ID];
        return cost;
    }
    /*
     * 函数功能1:计算中心点A，中心点B到Point点C的代价：实质为A到C的距离减去B到C的距离
     * 传参1:中心点A的对象	pointbean centre_A
     * 传参2:中心点B的对象	pointbean centre_B
     * 传参3:Point点C的对象	pointbean point_C
     * 返回值1:中心点A，中心点B到Point点C的代价	double cost
     */
    public static double cost_centreA_centreB_pointC(pointbean centre_A,pointbean centre_B,pointbean point_C) {//计算点C的中心点由中心点A变为中心点B的代价
        double cost = 0;
        //System.out.println("A__C: "+A_B_matrix[centre_A.getId()][point_C.getId()]+"   B___C:  "+A_B_matrix[centre_B.getId()][point_C.getId()]);
        cost=A_B_matrix[centre_A.getId()][point_C.getId()]-A_B_matrix[centre_B.getId()][point_C.getId()];
        return cost;
    }
    /*
     * 函数功能1:计算两个点之间的距离，使用对应的距离矩阵
     * 传参1:A点对象	pointbean pointA
     * 传参2:B点对象	pointbean pointB
     * 返回值1:两个点之间的距离	double distance
     */
    public static double distance_p_p(pointbean pointA,pointbean pointB) {
        double distance=0.0;
        distance=A_B_matrix[pointA.getId()][pointB.getId()];
        return distance;
    }
    /*
     * 函数功能1:计算两个点之间的距离，使用对应的距离矩阵
     * 传参1:A点的ID	int pointA_ID
     * 传参2:B点的ID	int pointB_ID
     * 返回值1:两个点之间的距离	double distance
     */
    public static double distance_p_p(int pointA_ID,int pointB_ID) {
        double distance=0.0;
        distance=A_B_matrix[pointA_ID][pointB_ID];
        return distance;
    }
    /*
     * 函数功能1:将传入的对应的Point点和中心点进行互换，并把互换的状态体现在点列表和中心点列表中
     * 传参1:所有的点列表	ArrayList<pointbean> points
     * 传参2:所有的中心点列表	ArrayList<pointbean> centres
     * 传参3:需要替换的点的ID	int pointID
     * 传参4:需要替换的中心点ID	int centreID
     */
    public static void replace(ArrayList<pointbean> points,ArrayList<pointbean> centres,int pointID,int centreID) {
        pointbean point = new pointbean();
        for(int i=0;i<points.size();i++) {
            if(points.get(i).getId()==pointID) {
                points.get(i).setIscentre(true);
                point.setBecentre(points.get(i).getBecentre());
                point.setCost(points.get(i).getCost());
                point.setId(points.get(i).getId());
                point.setIscentre(points.get(i).isIscentre());
                point.setName(points.get(i).getName());

            }
        }
        for(int i=0;i<points.size();i++) {
            if(points.get(i).getId()==centreID) {
                points.get(i).setIscentre(false);
            }
        }

        for(int i=0;i<centres.size();i++) {
            if(centres.get(i).getId()==centreID) {
                centres.get(i).setId(point.getId());
                centres.get(i).setCost(point.getCost());
                centres.get(i).setBecentre(point.getBecentre());
                centres.get(i).setIscentre(point.isIscentre());
                centres.get(i).setName(point.getName());
            }
        }

    }
    /*
     * 函数功能1:显示分类效果
     * 传参1:所有点列表	ArrayList<pointbean> points
     * 传参2:所有中心点列表	ArrayList<pointbean> centres
     */
    public static void show_class(ArrayList<pointbean> points,ArrayList<pointbean> centres) {
        for(int i=0;i<centres.size();i++) {
            System.out.print("第 "+i+" 组{");
            for(int j=0;j<points.size();j++) {
                if(centres.get(i).getName()==points.get(j).getBecentre()) {
                    System.out.print(""+points.get(j).getName()+",");
                }
            }
            System.out.println(" }");
        }
        System.out.print("中心点为：{");
        for(int i=0;i<centres.size();i++) {
            System.out.print(" "+centres.get(i).getName()+",");
        }
        System.out.println("}");
    }
    /*
     * 函数功能1:根据所有run点的MAP值对每一分组进行排序，并把每组MAP最高值对应的point点的名输出至一个xls文件中
     * 传参1:所有run点列表	ArrayList<pointbean> points
     * 传参2:所有的中心点对象列表	ArrayList<pointbean> centres
     * 传参3:实验次数并且体现在输出的xls文档名上	String copycount
     */
    public static void Best_MAP(ArrayList<pointbean> points,ArrayList<pointbean> centres,String copycount) throws IOException {
        /*
         * 结果存入.xls文件中
         */
        int count_row= 0;
        FileOutputStream fop_excel = new FileOutputStream("2020health_centre_bestMAP_"+copycount+".xls");
        HSSFWorkbook wbwrite = new HSSFWorkbook();
        Sheet sheetwrite = wbwrite.createSheet();
        Row rowwrite = sheetwrite.createRow(count_row);

        ArrayList<pointbean> points_descend = new ArrayList<>();
        boolean flag = true;
        FileInputStream fip = new FileInputStream("2020healthMAP_Descend.xls");
        HSSFWorkbook wb = new HSSFWorkbook(fip);
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(1);
        for(int i=1;i<sheet.getPhysicalNumberOfRows();i++) {
            pointbean pointtemp = new pointbean();
            row=sheet.getRow(i);
            pointtemp.setName(row.getCell(0).getStringCellValue());
            pointtemp.setCost(row.getCell(1).getNumericCellValue());
            pointtemp.setId((int)row.getCell(3).getNumericCellValue());
            points_descend.add(pointtemp);
            //System.out.println(points_descend.get(i-1).getName()+" MAP: "+points_descend.get(i-1).getCost()+" ID: "+points_descend.get(i-1).getId());
        }

        for(int i=0;i<centres.size();i++) {
            System.out.println("centres:"+centres.get(i).getName());
            for(int j = 0 ; j< points.size();j++) {
                for(int z=0;z<points_descend.size();z++) {
                    if(points.get(j).getId()==points_descend.get(z).getId()&&points.get(j).getBecentre()==centres.get(i).getName()) {
                        System.err.println(" NAME: "+points_descend.get(z).getName()+" best MAP:"+points_descend.get(z).getCost()+" centres: "+centres.get(i).getName());
                        rowwrite.createCell(0).setCellValue(centres.get(i).getName());
                        rowwrite.createCell(1).setCellValue(points_descend.get(z).getName().replaceAll("MAP_", ""));
                        count_row++;
                        rowwrite = sheetwrite.createRow(count_row);
                        flag=false;
                        break;
                    }
                }
                if(flag==false) {
                    break;
                }
            }
            flag=true;
        }
        wbwrite.write(fop_excel);
        fop_excel.close();
        fip.close();
    }
    public static void main(String[] args) throws IOException {
        for(int copycount= 0;copycount<10;copycount++) {
            ArrayList<pointbean> points = new ArrayList<>();
            ArrayList<pointbean> centres= new ArrayList<>();
            int flag=1;
            int[] ID = new int[2];
            FileInputStream fip = new FileInputStream("distance-all-all.xls");
            //FileInputStream fip = new FileInputStream("distancetest.xls");
            HSSFWorkbook wb = new HSSFWorkbook(fip);
            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(0);
            for(int i=1;i<sheet.getPhysicalNumberOfRows()-1;i++) {//51个点
                String name = row.getCell(i).getStringCellValue();
                pointbean point = new pointbean();
                point.setName(name);
                point.setId(i);
                //System.out.println(point.getName()+" "+point.getId());
                points.add(point);
            }
            init_matrix_A_B();
            centres=findcentre_all(points, 4);
            classification_k(points, centres);
            ID=small_cost_centre(points, centres);
            while(true) {
                System.err.println("pointID: "+ID[0]+" centreID: "+ID[1]);
                System.err.println("这是第："+(flag++)+"次运行.");
                show_class(points, centres);
                if(ID[0]==ID[1]) {
                    break;
                }

                replace(points, centres,ID[0] , ID[1]);


                //show_class(points, centres);
                classification_k(points, centres);
                ID=small_cost_centre(points, centres);
            }
            //Best_MAP(points, centres,String.valueOf(copycount));
            System.out.println("收敛");
            System.out.println();
            System.out.println();

			/*
			for(int i=0;i<points.size();i++) {
				System.out.println(points.get(i).getName()+"\t"+points.get(i).getId()+"\t"+points.get(i).getBecentre()+
						" iscentre: "+points.get(i).isIscentre()+" cost :"+points.get(i).getCost());
			}
			*/
            fip.close();
        }

    }
}
