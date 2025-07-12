package com.SJH.NewSimilarity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class fileName {
    public static void main(String[] args) {
        //此程序用来读取一个文件夹中所有的文件名
        String basePath = "E:\\TREC 数据集\\2017MedicineTrackScientific\\standard-input-nor30-60edSO\\";
        File dir = new File(basePath);

        List<File> allFileList = new ArrayList<>();

        // 判断文件夹是否存在
        if (!dir.exists()) {
            System.out.println("目录不存在");
            return;
        }

        getAllFile(dir, allFileList);

        int n=0;
        for (File file : allFileList) {
            n++;
            System.out.print("\""+file.getName()+"\""+",");
            //System.out.println();
            if(n%10==0)
            {
                System.out.println(" ");
            }
        }
        //System.out.println("该文件夹下共有" + allFileList.size() + "个文件");
    }

    public static void getAllFile(File fileInput, List<File> allFileList) {
        // 获取文件列表
        File[] fileList = fileInput.listFiles();
        assert fileList != null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                // 递归处理文件夹
                // 如果不想统计子文件夹则可以将下一行注释掉
                getAllFile(file, allFileList);
            } else {
                // 如果是文件则将其加入到文件数组中
                allFileList.add(file);
            }
        }
    }
}
