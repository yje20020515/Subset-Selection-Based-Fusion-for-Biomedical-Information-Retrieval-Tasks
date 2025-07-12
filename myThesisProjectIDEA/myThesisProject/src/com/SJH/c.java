package com.SJH;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class c {
    public static void main(String[] args) throws IOException {
        for (int i = 1; i <=40 ; i++) {
            chaxun(i);
        }
    }

    private static void chaxun(int i) throws IOException {
        int sum=0;
        int n=0;
        BufferedReader br=new BufferedReader(new FileReader("E:\\TREC Êý¾Ý¼¯\\2020MedicineTrackScientific\\judgement\\judgmentfile_01"));
        String line="";
        while ((line=br.readLine())!=null)
        {
            String[] str=line.split("\\s+");
            if(Integer.parseInt(str[0])==i)
            {
                sum+=Double.parseDouble(str[3]);
                n++;
            }
            if(n==271)
            {
                break;
            }
        }
        System.out.println(sum);
    }
}