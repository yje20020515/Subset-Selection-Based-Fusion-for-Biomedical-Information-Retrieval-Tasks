//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.normalization;

import java.io.File;

public class FolderNameToFileName {
    public FolderNameToFileName() {
    }

    public static void main(String[] args) {
        String outputFileName = "E:\\TREC 数据集\\2020MedicineTrackScientific\\input-raw\\";
        File folders = new File("E:\\TREC 数据集\\2020MedicineTrackScientific\\input-folder\\");
        File[] folderNames = folders.listFiles();
        File[] var4 = folderNames;
        int var5 = folderNames.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            File folderName = var4[var6];
            String fileName = outputFileName + folderName.getName();
            File fileCopy = folderName.listFiles()[0];
            fileCopy.renameTo(new File(fileName));
        }

    }
}
