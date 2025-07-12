package com.SJH.NewSimilarity;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class newRJandRDsc {
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
                //2017
                /*"aCSIROmedAll","aCSIROmedMCB","aCSIROmedMGB","aCSIROmedNEG","aCSIROmedPCB","Broad","cbnuSA1","cbnuSA2","cbnuSA3","DUTIR003",
                "ECNU_M_1","ECNU_M_2","ECNU_M_3","ECNU_M_4","ECNU_M_5","eth_a_gws","eth_a_luc","eth_a_nn","eth_a_ws","eth_a_ws_q",
                "Focused","GP14Medline","GP16Medline","Gwave","ielabRun1","ielabRun21","ielabRun22","ielabRun23","ielabRun3","KISTI01",
                "KISTI02","KISTI03","KISTI04","KISTI05","kkseabs1","kkseabs3","kkseabs4","mayonlppm1","mayonlppm2","mayonlppm3",
                "mayonlppm4","mayonlppm5","MedIER_sa1","MedIER_sa2","MedIER_sa3","MedIER_sa4","medline1","medline2","medline3","medline4",
                "medline5","mRun1Bsl","mRun2BslOth","mRun3MRF","mRun4MRFrf","mRun5MRFBow","mugpubbase","mugpubboost","mugpubdiseas","mugpubgene",
                "mugpubshould","NOVAsa1","NOVAsa2","NOVAsa3","Ontological","pms_run1","pms_run2_abs","pms_run3_abs","pms_run4_abs","pms_run5_abs",
                "POZabsBB2GRn","POZabsBB2sn","POZabsLGDGRn","SDSFU_Ensem","SDSFU_Jnal","SDSFU_Lambda","SDSFU_PF_SA","SDSFU_PU_SA","Semantic","SIBTMlit1",
                "SIBTMlit2","SIBTMlit3","SIBTMlit4","SIBTMlit5","Textual","UCASBASEa","UCASSEM1a","UCASSEM2a","UCASSEM3a","UCASSEMUMLSa",
                "udelT1Base","udelT1Comb","udelT1GeMeSH","udelT1Gene","udelT1PRF","UDInfoPMSA2","UDInfoPMSA3","UDInfoPMSA5","UDInfoPMSA6","UDInfoPMSA7",
                "UD_GU_SA_1","UD_GU_SA_2","UD_GU_SA_3","UD_GU_SA_4","UD_GU_SA_5","UKY_AGG","UKY_BASE","UKY_CJT","UKY_COM","UKY_MAN",
                "UNTIIADW","UNTIIAGA","UNTIIAIS","UNTIIALQ","UNTIIASY","UTDHLTAF","UTDHLTFF","UTDHLTJQ","UTDHLTSF","UTDHLTSQ",
                "UWMSOIS5","UWMSOIS6","UWMSOIS7","UWMSOIS8","UWMSOIS9",*/
                //2018
               /* "aCSIROmedAll", "aCSIROmedMCB", "aCSIROmedNEG", "bool51", "cbnuSA1", "cbnuSA2", "cbnuSA3", "cubicmnzAbs", "cubicsumWAbs",
                "doc2vec_run", "doc2vec_run2", "ECNU_S_Run1", "ECNU_S_Run2", "ECNU_S_Run3", "ECNU_S_Run4", "ECNU_S_Run5", "elastic_run",
                "hpipubbase", "hpipubboost", "hpipubclass", "hpipubcommon", "hpipubnone", "IKMLAB_1", "IKMLAB_2", "IKMLAB_3", "IKMLAB_4",
                "IKMLAB_5", "imi_mug_abs1", "imi_mug_abs2", "imi_mug_abs3", "imi_mug_abs4", "imi_mug_abs5", "KL18AbsFuse", "KL18absHY",
                "KL18absWV", "KLPM18T2Bl", "mayomedcomp", "mayomedcreat", "mayomedsimp", "mayopubtator", "MedIER_sa11", "MedIER_sa12",
                "MedIER_sa13", "MedIER_sa14", "MedIER_sa15", "method_fu", "minfolabBA", "minfolabBC", "minfolabBD", "minfolabTH", "mnzAbs",
                "MSIIP_BASE", "MSIIP_PBAH", "MSIIP_PBH", "MSIIP_PBL", "MSIIP_PBPK", "para_fusion", "PM_IBI_run1", "PM_IBI_run2", "PM_IBI_run3",
                "raw_medline", "rbf", "RSA_DSC_LA_1", "RSA_DSC_LA_2", "RSA_DSC_LA_3", "RSA_DSC_LA_4", "RSA_DSC_LA_5", "SIBTMlit1", "SIBTMlit2",
                "SIBTMlit3", "SIBTMlit4", "SIBTMlit5", "SINAI_Base", "SINAI_FU", "SINAI_FUO", "sumAbs", "sumEW", "two_stage", "UCASSA1",
                "UCASSA2", "UCASSA3", "UCASSA4", "UCASSA5", "UDInfoPMSA1", "UDInfoPMSA2", "UDInfoPMSA3", "UDInfoPMSA4", "UDInfoPMSA5",
                "UNTIIA_DGES", "UNTIIA_DGEWS", "UNTIIA_DGEWU", "UNTIIA_DGS", "UNTIIA_WTU", "UTDHLTRI_NL", "UTDHLTRI_RA", "UTDHLTRI_RF",
                "UTDHLTRI_SF", "UTDHLTRI_SS", "UVAEXPBOOST", "UVAEXPBSTDIF", "UVAEXPBSTEXT", "UVAEXPBSTNEG", "UVAEXPBSTSHD",*/
                //2019
               /* "absrun1", "absrun2", "BM25", "BM25neop01", "BM25neopcomd", "BM25neopgngm", "bm25_6801", "cbnuSA1", "cbnuSA2", "cbnuSA3",
                "cbnuSA4", "ccnl_sa1", "ccnl_sa2", "ccnl_sa3", "ccnl_sa4", "ccnl_sa5", "default100k", "default1m", "dfr_9464", "DutirRun1",
                "DutirRun2", "DutirRun3", "DutirRun4", "DutirRun5", "et_8435", "imi_mug1", "imi_mug2", "imi_mug2_t", "imi_mug3", "imi_mug3_t",
                "jlpmcommon2", "jlpmletor", "jlpmltrin", "jlpmtrboost", "jlpmtrcommon", "MedIR1", "MedIR2", "MedIR3", "MedIR4", "MedIR5",
                "run3", "run4", "run5", "SAsimpleLGD", "sa_base", "sa_base_rr", "SA_bc", "SA_DPH_letor", "sa_ft", "sa_ft_rr", "SA_LGD_letor",
                "SIBTMlit1", "SIBTMlit2", "SIBTMlit3", "SIBTMlit4", "SIBTMlit5", "sils_run1", "sils_run2", "sils_run3", "sils_run4", "top4fusion", "xgb_5113"*/
                //2020
                               "baseline", "bm25", "bm25_p10", "bm25_synonyms", "CincyMedIR_20", "CincyMedIR_28", "CincyMedIR_28_t",
                "CincyMedIR_dgt", "CincyMedIR28dgt", "CornellTech1", "CornellTech2", "CSIROmed_rlxRR", "CSIROmed_rRRa", "CSIROmed_sRRa",
                "CSIROmed_strDFR", "CSIROmed_strRR", "DA_DCU_IBM_1", "DA_DCU_IBM_2", "DA_DCU_IBM_3", "DA_DCU_IBM_4", "damoespb1", "damoespb2",
                "damoespcbh1", "damoespcbh2", "damoespcbh3", "duoT5", "duoT5rct", "ebm", "ens", "f_CTD_run1", "f_CTD_run2", "f_run0",
                "f_run1", "monoT5", "monoT5e1", "monoT5rct", "nnrun1", "nnrun2", "nnrun3", "PA1run", "pozadditional", "pozbaseline",
                "pozreranked", "r1st", "rrf", "rrf_p10", "rrf_prf_infndcg", "rrf_prf_p10", "rrf_prf_rprec", "run_bm25", "sibtm_run1",
                "sibtm_run2", "sibtm_run3", "sibtm_run4", "sibtm_run5", "tier1st", "uog_ufmg_DFRee", "uog_ufmg_s_dfr0", "uog_ufmg_s_dfr5",
                "uog_ufmg_sb_df5", "uog_ufmg_secL2R", "uwbm25", "uwman", "uwpr", "uwr", "uwrn"


        };
        for (int j = 0; j < A.length-1 ; j++) {
            for (int i = j; i < A.length-1; i++) {

                calculate(A[j],A[i+1],topic);
            }
        }

    }


    private static void calculate(String s1, String s2,String[] topic) throws IOException {
        double J=0.0;
        double DSC=0.0;

        double sumTopicJ=0.0;
        double sumTopicDSC=0.0;

        for (int i = 0; i < topic.length; i++) {
            int a=getMin(s1,topic[i]);
            int b=getMin(s2,topic[i]);
            int min=0;//用来确定每个查询拉手对的最小对数
            if(a<b)
            {
                min=a;
            }else
            {
                min=b;
            }
            //System.out.println(min);
            //定义用来存放元素的集合
            LinkedHashSet<String> setA=new LinkedHashSet<>();
            LinkedHashSet<String> setB=new LinkedHashSet<>();
            double sumMinJ=0.0;
            double sumMinDSC=0.0;

            for (int j1 = 1; j1 <=min; j1++) {//按照拉手对的数量进行摘取，分别对逐渐增大的拉手集合进行计算
                double j=0.0;
                double dsc=0.0;

                cunfangyuansu(setA,j1,s1);
                cunfangyuansu(setB,j1,s2);
                //接下来计算A,B集合的交集个数和并集元素个数
                // 计算交集
                Set<String> intersection = new LinkedHashSet(setA);
                intersection.retainAll(setB);
                int intersectionCount = intersection.size();

                // 计算并集
                Set<String> union = new LinkedHashSet(setA);
                union.addAll(setB);
                int unionSize = union.size();

                //System.out.println(intersectionCount);
                //System.out.println(unionSize);

                //System.out.println(setA.size());
                j=intersectionCount*1.0/unionSize;
                dsc=2.0*intersectionCount/(setA.size()+setB.size());

                sumMinJ+=j;
                sumMinDSC+=dsc;

            }
            sumTopicJ+=(sumMinJ/min);
            sumTopicDSC+=(sumMinDSC/min);

        }
        J=sumTopicJ/topic.length;
        DSC=sumTopicDSC/topic.length;
        BufferedWriter bw=new BufferedWriter(new FileWriter("E:\\TREC 数据集\\2020MedicineTrackScientific\\RJ&RDSC",true));
        bw.write(s1+"&"+s2+"\t"+"J："+"\t"+J+"\t"+"DSC:"+"\t"+DSC);
        bw.write("\n");
        bw.close();
        System.out.println(s1+"&"+s2+"\t"+"J："+"\t"+J+"\t"+"DSC:"+"\t"+DSC);



    }

    private static void cunfangyuansu(LinkedHashSet<String> set, int j,String s) throws IOException {//其中的s代表检索系统的名字
        BufferedReader br=new BufferedReader(new FileReader("E:\\TREC 数据集\\2020MedicineTrackScientific\\standard-input-nor30-60\\"+s));
        String line="";
        int count=0;
        while ((line=br.readLine())!=null)
        {
            String[] str=line.split("\\s+");
            count++;
            set.add(str[0]+"\t"+str[2]);
            if(count==j)
            {
                break;
            }
        }
        br.close();
    }

    private static int getMin(String s,String topic) throws IOException {
        BufferedReader br=new BufferedReader(new FileReader("E:\\TREC 数据集\\2020MedicineTrackScientific\\standard-input-nor30-60\\"+s));
        String line="";
        int count=0;
        while ((line=br.readLine())!=null)
        {
            String[] str=line.split("\\s+");
            if(str[0].equals(topic))
            {
                count++;
            }
            if(count!=0 && !(str[0].equals(topic)))
            {
                break;
            }
        }
        br.close();
        return count;
    }
}
