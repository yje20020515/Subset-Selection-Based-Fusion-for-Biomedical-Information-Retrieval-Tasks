package com.SJH;

import com.sun.source.tree.WhileLoopTree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class judgement {
    public static void main(String[] args) throws IOException {
        BufferedReader br=new BufferedReader(new FileReader("E:\\TREC Êı¾İ¼¯\\2020MedicineTrackScientific\\judgement\\judgementfile2.txt"));
        String line="";
        while ((line=br.readLine())!=null)
        {
            String[] str=line.split("\\s+");
            /*if(Integer.parseInt(str[3])>0 && Integer.parseInt(str[4])==0 )
            {
                System.out.println(str[0]+"\t"+"Q0"+"\t"+str[2]+"\t"+"0");
            }
            if(Integer.parseInt(str[3])>0 && Integer.parseInt(str[4])>0 )
            {
                System.out.println(str[0]+"\t"+"Q0"+"\t"+str[2]+"\t"+"1");
            }
            if(Integer.parseInt(str[3])>0 && Integer.parseInt(str[4])<0 )
            {
                System.out.println(str[0]+"\t"+"Q0"+"\t"+str[2]+"\t"+"0");
            }*/
            System.out.println(str[0]+"\t"+"Q0"+"\t"+str[2]+"\t"+str[3]);
        }
        br.close();
    }

}
