package com.SJH.NewSimilarity;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class CreatClassficationExcel {
    public static void main(String[] args) {
        long startTime=System.currentTimeMillis(); //��ȡ��ʼʱ��
        //�ó������ڴ����������ϵͳ����������EXCEL
        String[] systemNames={
                "baseline", "bm25", "bm25_p10", "bm25_synonyms", "CincyMedIR_20", "CincyMedIR_28", "CincyMedIR_28_t",
                "CincyMedIR_dgt", "CincyMedIR28dgt",   "CSIROmed_rlxRR", "CSIROmed_rRRa",
                "CSIROmed_sRRa", "CSIROmed_strDFR",  "DA_DCU_IBM_1", "DA_DCU_IBM_2", "DA_DCU_IBM_3",
                 "damoespb1", "damoespb2",  "damoespcbh2", "damoespcbh3", "duoT5", "duoT5rct",
                  "f_CTD_run1",  "f_run1", "monoT5", "monoT5e1", "monoT5rct", "nnrun1",
                "nnrun2", "nnrun3", "PA1run",  "pozbaseline", "pozreranked", "r1st", "rrf", "rrf_p10",
                "rrf_prf_infndcg", "rrf_prf_p10", "rrf_prf_rprec", "run_bm25", "sibtm_run1",  "sibtm_run3",
                 "sibtm_run5","uog_ufmg_DFRee", "uog_ufmg_s_dfr0", "uog_ufmg_s_dfr5", "uog_ufmg_sb_df5",
                "uog_ufmg_secL2R", "uwbm25",  "uwpr", "uwr", "uwrn",

        };


        for (int i = 0; i < systemNames.length; i++) {
            String[] systemName={
                    "CSIROmed_strRR",
                    "sibtm_run2",
                    "pozadditional",
                    "uwman",
                    "CornellTech1",
                    "sibtm_run4",
                    "f_run0",
                    "DA_DCU_IBM_4",
                    "tier1st",
                    "CornellTech2",
                    "ens",
                    "ebm",
                    "damoespcbh1",
                    "f_CTD_run2",
                    systemNames[i]
            };
            /*for (int j = 0; j < systemName.length; j++) {
                System.out.print("\""+systemName[j]+"\""+",");
            }
            System.out.println();*/
            //����ѭ������������ϵͳ�����
            //������Ҫ�����Ƕ�д������Ӧ��excel��

            // ����������
            Workbook workbook =new HSSFWorkbook();

            // ����������
            Sheet sheet = workbook.createSheet("Sheet1");

            // ��������
            Object[][] data = {
                    {"CSIROmed_strRR",},
                    {"sibtm_run2",},
                    {"pozadditional",},
                    {"uwman",},
                    {"CornellTech1",},
                    {"sibtm_run4",},
                    {"f_run0", },
                    {"DA_DCU_IBM_4",},
                    { "tier1st", },
                    {"CornellTech2", },
                    {"ens", },
                    {"ebm", },
                    {"damoespcbh1", },
                    {"f_CTD_run2", },
                    {systemNames[i]},


            };

            // д������
            int rowNum = 0;
            System.out.println("Writing data into Excel...");

            for (Object[] rowData : data) {
                Row row = sheet.createRow(rowNum++);

                int colNum = 0;
                for (Object field : rowData) {
                    Cell cell = row.createCell(colNum++);
                    if (field instanceof String) {
                        cell.setCellValue((String) field);
                    } else if (field instanceof Integer) {
                        cell.setCellValue((Integer) field);
                    }
                }
            }

            // ���湤�������ļ�
            try (FileOutputStream outputStream = new FileOutputStream("E:\\TREC ���ݼ�\\2020MedicineTrackScientific\\class\\15\\Semi_K15" + "_"+(i+1)+".xls")) {
                workbook.write(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Data has been written successfully.");


        }

        long endTime=System.currentTimeMillis(); //��ȡ����ʱ��

        System.out.println("��������ʱ�䣺 "+(endTime-startTime)+"ms");

    }

}
