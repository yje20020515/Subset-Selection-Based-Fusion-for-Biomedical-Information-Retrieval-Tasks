package com.SJH;

import java.io.*;
import java.util.LinkedHashSet;

public class score_extract {
    public static void main(String[] args) throws IOException {
        LinkedHashSet<String> lhs = new LinkedHashSet<>();
        newExtract(lhs);
    }

    private static void newExtract(LinkedHashSet<String> lhs) throws IOException {
        for (int i = 1; i <=40 ; i++) {
            int n=0;
            BufferedReader br=new BufferedReader(new FileReader("E:\\TREC 数据集\\2019MedicineTrackScientific\\newtest\\A_Score\\NEW9\\score"+i));
            String line="";
            while ((line=br.readLine())!=null)
            {
                n++;
                String[] str=line.split("\\s+");
                lhs.add(str[0]+"\t"+str[1]+"\t"+str[2]+"\t"+str[3]);
                if(n==26)
                {
                    break;
                }

            }
            br.close();
        }
        BufferedWriter bw=new BufferedWriter(new FileWriter("E:\\TREC 数据集\\2019MedicineTrackScientific\\judgement\\A",true));
        for (String s : lhs) {
            System.out.println(s);
            bw.write(s);
            bw.write("\n");
        }
        bw.close();
    }

}
