package com.algorithm.myUtil.XLS_Util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;

public class XLS_Util {
    /**
     *
     * @param xlsPath xls file path
     * @param NUM fusionName size
     * @return fusionNames
     * @throws IOException IO
     */
    public static String[] getXLSFusionNames(String xlsPath,int NUM) throws IOException {
        String[] fusionNames = new String[NUM];
        FileInputStream fip = new FileInputStream(xlsPath);
        HSSFWorkbook rb = new HSSFWorkbook(fip);
        Sheet sheet = rb.getSheetAt(0);
        Row row;
        if (NUM<=sheet.getPhysicalNumberOfRows()){
            for (int rowNum = 0; rowNum < NUM; rowNum++) {
                row = sheet.getRow(rowNum);
                String fusionName = row.getCell(0).getStringCellValue().replaceAll("###","");
                fusionNames[rowNum] = fusionName;
            }
        }else {
            System.err.println("error NUM for xls file col size");
        }
        fip.close();
        return fusionNames;
    }
    private static void createDirectory(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    private static void writeXLSFile(String[] xlsFusionNames, String xlsFilePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(xlsFilePath);

        HSSFWorkbook hw = new HSSFWorkbook();
        Sheet sheet = hw.createSheet();
        Row row;
        for (int i = 0; i < xlsFusionNames.length; i++) {
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(xlsFusionNames[i]);
        }
        hw.write(fos);
        fos.close();
    }
    public static void rewriteXLSFusionFolderFileByMAP(String rawXLSFilePath,String rewriteXLSFolderPath,int startNum,int endNum,int EXP,String xlsPreName) throws IOException {
        for (int NUM = startNum ; NUM <= endNum; NUM++) {
            String[] xlsFusionNames = getXLSFusionNames(rawXLSFilePath, NUM);
            String tempRewriteXLSFolderPath = rewriteXLSFolderPath + "\\"+NUM+"\\";
            createDirectory(tempRewriteXLSFolderPath);
            String XLSFilePath = tempRewriteXLSFolderPath + xlsPreName +NUM+"_"+EXP+".xls";
            writeXLSFile(xlsFusionNames,XLSFilePath);
        }



    }

}
