package com.algorithm.normalization;

import java.io.File;

public class FolderNameToFileName {
    public static void main(String[] args) {
        String outputFileName = "E:\\TRECDateset\\paperExperiment\\ideaOne\\2021Health\\adhoc\\input_raw"+"\\";
        File folders = new File("E:\\TRECDateset\\paperExperiment\\ideaOne\\2021Health\\adhoc\\input_folder");
        File[] folderNames = folders.listFiles();
        for (File folderName : folderNames) {
            String fileName = outputFileName+folderName.getName();
            File fileCopy = folderName.listFiles()[0];
            fileCopy.renameTo(new File(fileName));
        }
    }
}
