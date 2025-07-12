package com.SJH.NewSimilarity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;

public class RJ {
    public static void main(String[] args) throws IOException {
        String[] topic={
                "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "10",
                "11","12", "13", "14",
                "15","16", "17", "18",
                "19","20", "21", "22",
                "23","24", "25", "26",
                "27","28", "29", "30",
                /*"31","32", "33", "34",
                "35","36", "37", "38",
                "39","40", "41", "42",
                "43","44", "45", "46",
                "47","48", "49", "50"*/
        };
        String[] A={
                "aCSIROmedAll","aCSIROmedMCB","aCSIROmedMGB","aCSIROmedNEG","aCSIROmedPCB","Broad","cbnuSA1","cbnuSA2","cbnuSA3","DUTIR003",
                "ECNU_M_1","ECNU_M_2","ECNU_M_3","ECNU_M_4","ECNU_M_5","eth_a_gws","eth_a_luc","eth_a_nn","eth_a_ws","eth_a_ws_q",
                "Focused","GP14Medline","GP16Medline","Gwave","ielabRun1","ielabRun21","ielabRun22","ielabRun23","ielabRun3","KISTI01",
                "KISTI02","KISTI03","KISTI04","KISTI05","kkseabs1","kkseabs3","kkseabs4","mayonlppm1","mayonlppm2","mayonlppm3",
                "mayonlppm4","mayonlppm5","MedIER_sa1","MedIER_sa2","MedIER_sa3","MedIER_sa4","medline1","medline2","medline3","medline4",
                "medline5","mRun1Bsl","mRun2BslOth","mRun3MRF","mRun4MRFrf","mRun5MRFBow","mugpubboost","mugpubdiseas","mugpubgene",
                "mugpubshould","NOVAsa1","NOVAsa2","NOVAsa3","Ontological","pms_run1","pms_run2_abs","pms_run4_abs","pms_run5_abs",
                "POZabsBB2GRn","POZabsBB2sn","POZabsLGDGRn","SDSFU_Ensem","SDSFU_Jnal","SDSFU_Lambda","SDSFU_PF_SA","SDSFU_PU_SA","Semantic","SIBTMlit1",
                "SIBTMlit2","SIBTMlit3","SIBTMlit4","SIBTMlit5","Textual","UCASBASEa","UCASSEM1a","UCASSEM2a","UCASSEM3a","UCASSEMUMLSa",
                "udelT1Comb","udelT1GeMeSH",
                "UD_GU_SA_1","UD_GU_SA_2","UD_GU_SA_3","UD_GU_SA_4","UD_GU_SA_5","UKY_AGG","UKY_BASE","UKY_CJT","UKY_COM","UKY_MAN",
                "UNTIIADW","UNTIIAGA","UNTIIAIS","UNTIIALQ","UNTIIASY","UTDHLTAF","UTDHLTJQ","UTDHLTSF","UTDHLTSQ",
                "UWMSOIS5","UWMSOIS6","UWMSOIS7","UWMSOIS8","UWMSOIS9",


        };
        for (int i = 0; i < A.length; i++) {
            calculateRJ(topic,A[i]);
        }
    }

    private static void calculateRJ(String[] topic, String s) throws IOException {
        String[] R={
                "UTDHLTFF","mugpubbase","pms_run3_abs","UDInfoPMSA7","UDInfoPMSA6","UDInfoPMSA5","udelT1Base","UDInfoPMSA3","UDInfoPMSA2","udelT1PRF","udelT1Gene",

        };


        double sumR=0.0;
        for (int j = 0; j < R.length; j++) {
            double Ri=0.0;
            double realJ=0.0;
            for (int i = 0; i < topic.length; i++) {
                int num=getMinNum(s,R[j],topic[i]);
                //System.out.println(num);
                double rankJ=0.0;

                for (int l = 1; l <= num; l++) {
                    LinkedHashSet<String> setA=new LinkedHashSet<>();
                    BufferedReader brA=new BufferedReader(new FileReader("E:\\TREC 数据集\\2017MedicineTrackScientific\\standard-input-nor30-60\\"+s));
                    String lineA="";
                    while ((lineA=brA.readLine())!=null)
                    {
                        String[] str=lineA.split("\\s+");
                        if(str[0].equals(topic[i]))
                        {
                            setA.add(str[2]);
                            if(setA.size()==l)
                            {
                                break;
                            }
                        }

                    }
                    //System.out.println(setA.size());


                    int d=0;//代表重合文档个数
                    int notd=0;
                    double J=0.0;
                    LinkedHashSet<String> setR=new LinkedHashSet<>();
                    BufferedReader brR=new BufferedReader(new FileReader("E:\\TREC 数据集\\2017MedicineTrackScientific\\standard-input-nor30-60\\"+R[j]));
                    String lineR="";
                    while ((lineR=brR.readLine())!=null)
                    {
                        String[] str=lineR.split("\\s+");
                        if(str[0].equals(topic[i]))
                        {
                            setR.add(str[2]);
                            if(setR.size()==l)
                            {
                                break;
                            }
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

                    notd=setA.size();
                    J=(d*1.0)/notd;
                    rankJ+=J;
                }
                realJ+=(rankJ/num);

            }
            Ri=realJ/topic.length;

            sumR+=Ri;
        }
        double zuheJ=sumR/R.length;
        System.out.println(zuheJ);
    }

    private static int getMinNum(String s, String s1,String topicName) throws IOException {
        LinkedHashSet<String> setA=new LinkedHashSet<>();
        BufferedReader brA=new BufferedReader(new FileReader("E:\\TREC 数据集\\2017MedicineTrackScientific\\standard-input-nor30-60\\"+s));
        String lineA="";
        while ((lineA=brA.readLine())!=null)
        {
            String[] str=lineA.split("\\s+");
            if(str[0].equals(topicName))
            {
                setA.add(str[2]);
            }

        }
        //System.out.println(setA.size());



        LinkedHashSet<String> setR=new LinkedHashSet<>();
        BufferedReader brR=new BufferedReader(new FileReader("E:\\TREC 数据集\\2017MedicineTrackScientific\\standard-input-nor30-60\\"+s1));
        String lineR="";
        while ((lineR=brR.readLine())!=null)
        {
            String[] str=lineR.split("\\s+");
            if(str[0].equals(topicName))
            {
                setR.add(str[2]);
            }
        }
        //System.out.println(setR.size());
        return Math.min(setA.size(), setR.size());//防止出现topic数量不相同的情况，选择topic文档数量最少的作为标准
    }


}
