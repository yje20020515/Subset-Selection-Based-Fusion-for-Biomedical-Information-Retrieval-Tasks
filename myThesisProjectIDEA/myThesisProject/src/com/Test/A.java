package com.Test;


import java.util.Arrays;

public class A {
    public static void main(String[] args) {
        String s = "axidpmlximppskdlsma";
        String w = "map";
        char[] SChars = s.toCharArray();
        char[] WChars = w.toCharArray();
        int[] CChars = new int[WChars.length];
        for (char sChar : SChars) {
            for (int i = 0; i < WChars.length; i++) {
                if (sChar == WChars[i]){
                    CChars[i]++;
                }
            }
        }

        for (int cChar : CChars) {
            System.out.println(cChar);
        }

    }
}