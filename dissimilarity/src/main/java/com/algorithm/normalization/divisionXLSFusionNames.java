package com.algorithm.normalization;

import com.algorithm.myUtil.XLS_Util.XLS_Util;

import java.io.IOException;

/**
 * 使用一个总排序好的xls文件，将该文件按照成员系统个数分割成多个xls文件
 */
public class divisionXLSFusionNames {
    public static void main(String[] args) throws IOException {
        int startNum = 2;
        int endNum = 19;
        int startEXP = 1;
        int endEXP = 1;
        String xlsPreName = "reHC_K";
        for (int EXP = startEXP; EXP <= endEXP; EXP++) {
            String rawXLSFilePath = "E:\\TRECDateset\\classificationEXP\\2018PrecisionLiterature\\adhoc\\fusion3_7\\classification_HCK20_Sort\\20\\"+xlsPreName+"20_"+EXP+".xls";
            String rewriteXLSFolderPath = "E:\\TRECDateset\\classificationEXP\\2018PrecisionLiterature\\adhoc\\fusion3_7\\classification_HCK20_Sort\\";

            XLS_Util.rewriteXLSFusionFolderFileByMAP(rawXLSFilePath,rewriteXLSFolderPath,startNum,endNum,EXP,xlsPreName);
        }

    }
}
