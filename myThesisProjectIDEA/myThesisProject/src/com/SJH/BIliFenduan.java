package com.SJH;

public class BIliFenduan {
    public static void main(String[] args) {
        int sum=1122;//要抽取文档的总数
        int n=9;//按文档出现的次数分成了9类
        double m=sum*1.0/40/n;
        double k=m/4;
        double[] a=new double[9];
        a[0]=m/4;
        double SUM=a[0];
        double f=(m-a[0])/4;
        for (int i = 1; i <9 ; i++) {
            a[i]=a[i-1]+f;
            SUM+=a[i];
        }

        for (int i = a.length-1; i >=0 ; i--) {
            System.out.print(Math.round(a[i])+",");
        }
        System.out.println(" ");
        System.out.println(Math.round(SUM));
    }
}
