package sjh;

import java.io.File;
import java.io.IOException;

public class CreatFileFunction {
    public static void main(String[] args) throws IOException {
        //创建多个多级文件夹
        for (int i =41; i <=50 ; i++) {//该代码对应的是测试集需要进行相似性匹配的个数
            File f1 = new File("E:\\TREC 数据集\\2018MedicineTrackScientific\\ScoreMatrix\\cu21\\"+i);
            f1.mkdirs();
            /*for (int j = 1; j <=19 ; j++) {//根据训练集的runs的个数、topics的个数决定；
                String f2name=Integer.toString(j);
                File f2=new File(f1,f2name);
                f2.mkdirs();
            }*/
        }
        //按照生成的序号创建文件
       /* for (int n1 = 1; n1 <=648 ; n1++) {
            int k=0;
            int l=0;
            if((n1%40!=0))
            {
                k=(n1/40)+1;
                l=n1%40;
            }else if(n1%40==0)
            {
                k=n1/40;
                l=40;
            }
            System.out.println(l);
        }*/

    }
}

