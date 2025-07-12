package sjh;


import java.io.*;

public class cuPaixu {
    public static void main(String[] args) throws IOException {
        BufferedReader br=new BufferedReader(new FileReader("E:\\TREC 数据集\\2018MedicineTrackScientific\\testeval\\cu"));
        String line="";
        int sum1=0;
        int sum2=0;

        while ((line=br.readLine())!=null)
        {
            String[] str=line.split("\\s+");
            String s=line.replaceAll("[\\[\\]]"," ");
            String s1=s.replaceAll(" ","\t");
            System.out.println(s1);
           /* sum1+=Integer.parseInt(str[2]);
            sum2+=Integer.parseInt(str[3]);*/
        }
        /*System.out.println(sum1);
        System.out.println(sum2);*/
        br.close();
        //BufferedWriter bw=new BufferedWriter(new FileWriter("E:\\TREC 数据集\\2018MedicineTrackScientific\\testeval\\cu1"));
    }
}
