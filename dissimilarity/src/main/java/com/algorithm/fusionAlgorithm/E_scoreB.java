package com.algorithm.fusionAlgorithm;

import java.io.*;
import java.util.*;

public class E_scoreB {
    public static void main(String[] args) throws IOException {
        for (int n = 1; n <=40 ; n++) {
            LinkedHashMap<String,Double> lhm=new LinkedHashMap<>();
            BufferedReader br=new BufferedReader(new FileReader("E:\\TREC 数据集\\2020MedicineTrackScientific\\A_Score\\NEW1\\"+n));
            String line="";
            while ((line=br.readLine())!=null)
            {
                String[] str=line.split("\\s+");
                lhm.put(str[2],0.0);
            }
            br.close();
            searchMaxScore(lhm,n);
        }


    }

    private static void searchMaxScore(LinkedHashMap<String, Double> lhm,int n) throws IOException {
        for (Map.Entry<String, Double> entry : lhm.entrySet()) {
            for (int i = 1; i <=13 ; i++) {
                BufferedReader br=new BufferedReader(new FileReader("E:\\TREC 数据集\\2020MedicineTrackScientific\\13runs\\"+i));
                String line="";
                while ((line=br.readLine())!=null)
                {
                    String[] str=line.split("\\s+");
                    if(Integer.parseInt(str[0])==n)
                    {
                        if(str[2].equals(entry.getKey()))
                        {
                            if(Double.parseDouble(str[4])>entry.getValue())
                            {
                                lhm.put(entry.getKey(),Double.parseDouble(str[4]));
                            }
                        }
                    }
                }
                br.close();

            }
        }
        List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(lhm.entrySet()); //转换为list
        //集合排序方法二
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        BufferedWriter bw=new BufferedWriter(new FileWriter("E:\\TREC 数据集\\2020MedicineTrackScientific\\A_Score\\NEW1\\Ascore"+n,true));
        System.out.println(n+" ");
        //bw.write(n+" "+"\n");
        //for-each循环
        for (Map.Entry<String, Double> mapping : list) {
            System.out.println(mapping.getKey() + "\t" + mapping.getValue());
            bw.write(n+"\t"+"Q0"+"\t"+mapping.getKey()+"\t"+"1"+"\t"+mapping.getValue());
            bw.write("\n");
        }
       /* for (Map.Entry<String, Double> entry : lhm.entrySet()) {
            System.out.println(entry.getKey()+"\t"+entry.getValue());
            bw.write(n+"\t"+"Q0"+"\t"+entry.getKey()+"\t"+"1"+"\t"+entry.getValue());
            bw.write("\n");
            //注意还差一步，将文档按照分数降序排序
        }*/
        bw.close();

    }
}
