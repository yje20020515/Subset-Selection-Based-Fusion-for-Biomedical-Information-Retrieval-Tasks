package com.SJH.NewSimilarity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;

public class DSC {
    public static void main(String[] args) throws IOException {
        String[] topic={
                "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "10",
                "11","12", "13", "14",
                "15","16", "17", "18",
                "19","20", "21", "22",
                "23","24", "25", "26",
                "27","28", "29", "30",
                "31","32", "33", "34",
                "35","36", "37", "38",
                "39","40", /*"41", "42",
                "43","44", "45", "46",
                "47","48", "49", "50"*/
        };
        String[] A={
                "baseline","bm25","bm25_p10","bm25_synonyms","CincyMedIR28dgt","CincyMedIR_20","CincyMedIR_28","CincyMedIR_28_t","CincyMedIR_dgt","CornellTech1",
                "CornellTech2","CSIROmed_rlxRR","CSIROmed_rRRa","CSIROmed_strDFR","damoespb2","damoespcbh1","damoespcbh2",
                "damoespcbh3","DA_DCU_IBM_1","DA_DCU_IBM_2","DA_DCU_IBM_3","duoT5rct","ens","f_CTD_run1",
               "f_run1","monoT5e1","monoT5rct","nnrun1","nnrun2","nnrun3","PA1run",
                "pozbaseline","pozreranked","rrf","rrf_p10","rrf_prf_infndcg","rrf_prf_p10","rrf_prf_rprec","run_bm25",
                "sibtm_run2","sibtm_run3","sibtm_run4","sibtm_run5","uog_ufmg_DFRee","uog_ufmg_sb_df5","uog_ufmg_secL2R","uog_ufmg_s_dfr0",
                "uog_ufmg_s_dfr5","uwbm25","uwpr","uwr","uwrn",





        };
        for (int i = 0; i < A.length; i++) {
            calculateDSC(topic,A[i]);
        }

    }

    private static void calculateDSC(String[] topic, String s) throws IOException {
        String[] R={
                "CSIROmed_strRR","duoT5","tier1st","ebm","sibtm_run1","f_run0","CSIROmed_sRRa","damoespb1","pozadditional", "f_CTD_run2","r1st","monoT5","uwman","DA_DCU_IBM_4",
        };
        double sumR=0.0;
        for (int j = 0; j < R.length; j++) {
            double Ri=0.0;
            double realDsc=0.0;
            for (int i = 0; i < topic.length; i++) {
                LinkedHashSet<String> setA=new LinkedHashSet<>();
                BufferedReader brA=new BufferedReader(new FileReader("E:\\TREC 数据集\\2020MedicineTrackScientific\\standard-input-nor30-60\\"+s));
                String lineA="";
                while ((lineA=brA.readLine())!=null)
                {
                    String[] str=lineA.split("\\s+");
                    if(str[0].equals(topic[i]))
                    {
                        setA.add(str[2]);
                    }

                }
                //System.out.println(setA.size());


                int d=0;//代表重合文档个数
                double dsc=0.0;
                LinkedHashSet<String> setR=new LinkedHashSet<>();
                BufferedReader brR=new BufferedReader(new FileReader("E:\\TREC 数据集\\2020MedicineTrackScientific\\standard-input-nor30-60\\"+R[j]));
                String lineR="";
                while ((lineR=brR.readLine())!=null)
                {
                    String[] str=lineR.split("\\s+");
                    if(str[0].equals(topic[i]))
                    {
                        setR.add(str[2]);
                    }
                }
                //System.out.println(setR.size());
                for (String s1 : setR) {
                    if(!setA.add(s1))
                    {
                        d++;
                    }

                }
                //System.out.println(d);
                //System.out.println(setA.size());
                dsc=(d*2.0)/(setA.size()+setR.size());

                realDsc+=dsc;

            }
            Ri=realDsc/topic.length;

            sumR+=Ri;
        }
        double zuheDSC=sumR/R.length;
        System.out.println(zuheDSC);
    }
}
