package com.SJH.NewSimilarity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class fileName {
    public static void main(String[] args) {
        //�˳���������ȡһ���ļ��������е��ļ���
        String basePath = "E:\\TREC ���ݼ�\\2017MedicineTrackScientific\\standard-input-nor30-60edSO\\";
        File dir = new File(basePath);

        List<File> allFileList = new ArrayList<>();

        // �ж��ļ����Ƿ����
        if (!dir.exists()) {
            System.out.println("Ŀ¼������");
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
        //System.out.println("���ļ����¹���" + allFileList.size() + "���ļ�");
    }

    public static void getAllFile(File fileInput, List<File> allFileList) {
        // ��ȡ�ļ��б�
        File[] fileList = fileInput.listFiles();
        assert fileList != null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                // �ݹ鴦���ļ���
                // �������ͳ�����ļ�������Խ���һ��ע�͵�
                getAllFile(file, allFileList);
            } else {
                // ������ļ�������뵽�ļ�������
                allFileList.add(file);
            }
        }
    }
}
