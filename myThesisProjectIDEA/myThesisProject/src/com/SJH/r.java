package com.SJH;

import java.util.Random;

public class r {
    public static void main(String[] args) {
        Random random = new Random();
        System.out.println(0.00001 + (0.99999 - 0.00001) * random.nextDouble());
    }
}
