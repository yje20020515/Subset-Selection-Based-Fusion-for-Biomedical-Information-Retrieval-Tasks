package com.SJH.NewSimilarity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PD {
    public static void main(String[] args) throws IOException {
        //�÷���ʵ�ַ�����
        //����ȷ�����MAPֵ�ļ���ϵͳ
        String[] R={ "CSIROmed_strRR","sibtm_run2","pozadditional","uwman","CornellTech1","sibtm_run4","f_run0", "DA_DCU_IBM_4","tier1st","CornellTech2", "ens",  "ebm",  "damoespcbh1", "f_CTD_run2",};//�ü����д���Ѿ�ժȡ�����ļ���ϵͳ
        String[] A={
                "baseline", "bm25", "bm25_p10", "bm25_synonyms", "CincyMedIR_20", "CincyMedIR_28", "CincyMedIR_28_t",
                "CincyMedIR_dgt", "CincyMedIR28dgt",   "CSIROmed_rlxRR", "CSIROmed_rRRa",
                "CSIROmed_sRRa", "CSIROmed_strDFR",  "DA_DCU_IBM_1", "DA_DCU_IBM_2", "DA_DCU_IBM_3",
                 "damoespb1", "damoespb2","damoespcbh2", "damoespcbh3", "duoT5", "duoT5rct",
               "f_CTD_run1",  "f_run1", "monoT5", "monoT5e1", "monoT5rct", "nnrun1",
                "nnrun2", "nnrun3", "PA1run",  "pozbaseline", "pozreranked", "r1st", "rrf", "rrf_p10",
                "rrf_prf_infndcg", "rrf_prf_p10", "rrf_prf_rprec", "run_bm25", "sibtm_run1",  "sibtm_run3",
                 "sibtm_run5", "uog_ufmg_DFRee", "uog_ufmg_s_dfr0", "uog_ufmg_s_dfr5", "uog_ufmg_sb_df5",
                "uog_ufmg_secL2R", "uwbm25",  "uwpr", "uwr", "uwrn",
        };
        //ȷ��������϶���
        int n=R.length+1;
        int count=n*(n-1)/2;

        HashMap<String,Double> hm=new HashMap<>();//��������ļ������м����������ϵ�J����DSC

        BufferedReader br=new BufferedReader(new FileReader("E:\\TREC ���ݼ�\\2020MedicineTrackScientific\\RJ&RDSC"));
        String line="";
        while ((line=br.readLine())!=null)
        {
            String[] str=line.split("\\s+");
            double J=Double.parseDouble(str[2]);///////////////////////////////////////////////////////////////
            double dsc=Double.parseDouble(str[4]);
            hm.put(str[0],J);
        }
        br.close();
        /*for (Map.Entry<String, Double> entry : hm.entrySet()) {
            System.out.println(entry.getKey()+"\t"+entry.getValue());
        }*/

        for (int i = 0; i < A.length; i++) {
            chaxunFunction(R,A[i],count,hm);
        }
    }

    private static void chaxunFunction(String[] r, String s, int count,HashMap<String,Double> hm) throws IOException {
        Set<String> set=new HashSet<>();//�������ÿ����ϵļ���ϵͳ���
        for (int i = 0; i < r.length; i++) {
            set.add(r[i]+"&"+s);
            set.add(s+"&"+r[i]);
        }
        for (int j = 0; j < r.length-1; j++) {
            set.add(r[j]+"&"+r[j+1]);
        }

        // �洢��ϵ��б�
        List<String> combinations = new ArrayList<>();

        // �����������
        for (int i = 0; i < r.length; i++) {
            for (int j = i + 1; j < r.length; j++) {
                set.add(r[i]+"&"+r[j]);
                set.add(r[j]+"&"+r[i]);
            }
        }
        /*for (String s1 : set) {
            System.out.println(s1);
        }*/
        double sum=0.0;
        int c=0;
        for (String s1 : set) {
            if(hm.keySet().contains(s1))
            {
                c++;
                //System.out.println(c);
                //System.out.println(s1+"\t"+hm.get(s1));
                sum+=hm.get(s1);
                //System.out.println(sum);

            }
        }
        //�˴���c���������Ҫ�ۼӵ���ϵĸ�������c==countʱ���ۼӵõ��Ľ��������ȷ��
        //System.out.println(c);

        //System.out.println(count);
        if(count==1)
        {
            System.out.println(sum);
        }else
        {

            System.out.println(sum/count);

        }

    }
}
