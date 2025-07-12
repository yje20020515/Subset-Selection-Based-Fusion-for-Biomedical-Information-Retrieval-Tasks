package com.SJH;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class countNum {
    public static void main(String[] args) throws IOException {
        for (int i = 1; i <=30 ; i++) {//topic
            BufferedReader br=new BufferedReader(new FileReader("E:\\TREC Êý¾Ý¼¯\\2017MedicineTrackScientific\\judgement\\A"));
            String line="";
            int n=0;
            while ((line=br.readLine())!=null)
            {
                String[] str=line.split("\\s+");
                if(Integer.parseInt(str[0])==i)
                {
                    n++;
                }
            }
            br.close();
            System.out.println(n);
        }
    }
}