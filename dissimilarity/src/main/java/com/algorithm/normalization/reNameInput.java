package com.algorithm.normalization;

import java.io.File;

public class reNameInput {
    public static void main(String[] args) {
        File file = new File("D:\\TRECDateset\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\input_directory\\");
        File[] files = file.listFiles();
        for (File oneFile : files) {
            String fileName = oneFile.getName();
            System.out.println(fileName.replaceAll("input.",""));
            File inputFile = oneFile.listFiles()[0];
            inputFile.renameTo(new File("D:\\TRECDateset\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\input\\"+fileName));
        }

    }
}
