package sjh;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class DREP {
    public static void main(String[] args) throws IOException {
        //计算每个成员系统的P、R、F
        //求出每个成员系统的F值（前20）

        for (int p = 1; p <124; p++) {
            int topN=100;//前多少
            int sumTopic=30;
            double sumW=0;
            double W;
            double w;
            double DI=0;
            double a=0;
            double sumDI=0;
            double memberSystemSumDistance;
            //Wi = α * DI(Si) + (1 - α) * AC(Si)，
            String[] systemNames={
                    "UTDHLTFF","UTDHLTAF","UTDHLTJQ","mugpubboost","SIBTMlit3","SIBTMlit2","UDInfoPMSA2",
                    "UD_GU_SA_5","SIBTMlit1","UD_GU_SA_4","UD_GU_SA_3","mRun1Bsl","UDInfoPMSA7","UKY_AGG","UCASBASEa",
                    "pms_run5_abs","SIBTMlit5","mugpubdiseas","Broad","medline3","UDInfoPMSA3","mRun3MRF","UKY_BASE","mRun2BslOth","UKY_CJT",
                    "UKY_MAN","UCASSEM1a","UCASSEM2a","UCASSEM3a","mRun4MRFrf","cbnuSA1","udelT1Base","UCASSEMUMLSa","udelT1PRF",
                    "UNTIIADW","ECNU_M_1","udelT1Comb","UWMSOIS5","UNTIIASY","Textual","ECNU_M_5","udelT1GeMeSH",
                    "ECNU_M_2","UWMSOIS7","UWMSOIS6","UWMSOIS9","UWMSOIS8","Semantic","medline4","udelT1Gene","KISTI04","medline2",
                    "ECNU_M_3","KISTI02","eth_a_ws","mugpubshould","eth_a_luc","KISTI03","aCSIROmedNEG",
                    "UDInfoPMSA5","Ontological","mayonlppm3","mayonlppm5","eth_a_ws_q","SDSFU_PF_SA","POZabsBB2sn","eth_a_nn","aCSIROmedAll","NOVAsa2",
                    "mRun5MRFBow","pms_run1","SDSFU_Ensem","aCSIROmedMCB","SDSFU_PU_SA","medline1","NOVAsa3","pms_run4_abs","SDSFU_Jnal",
                    "SDSFU_Lambda","mayonlppm1","NOVAsa1","UTDHLTSQ","mayonlppm4","mayonlppm2","Focused","MedIER_sa2","POZabsBB2GRn","UDInfoPMSA6",
                    "ECNU_M_4","POZabsLGDGRn","MedIER_sa1","eth_a_gws","MedIER_sa3","cbnuSA3","MedIER_sa4","cbnuSA2","kkseabs1",
                    "UNTIIAIS","UNTIIAGA","aCSIROmedMGB","DUTIR003","kkseabs4","kkseabs3","GP16Medline","GP14Medline","ielabRun22","ielabRun23",
                    "Gwave","ielabRun3","ielabRun21","ielabRun1",

            };
            String[] systemName={
                    "UTDHLTFF","mugpubbase","pms_run3_abs","UD_GU_SA_1","SIBTMlit4","UNTIIALQ","UKY_COM","mugpubgene","KISTI05","UD_GU_SA_2","UTDHLTSF","aCSIROmedPCB","pms_run2_abs","medline5",systemNames[p]
            };

           /* for (int i = 0; i < systemName.length; i++) {KISTI05

                double F=0;
                double sumf=0;
                double sump=0;
                double sumr=0;
                for (int topic = 1; topic <=sumTopic ; topic++) {
                    double P=0;//precision
                    double R=0;//recall
                    int n=0;
                    int pN=0;
                    double sumRel=0;
                    double f=0;

                    LinkedHashMap<String,Double> lhm=new LinkedHashMap();
                    BufferedReader br=new BufferedReader(new FileReader("E:\\TREC 数据集\\2017MedicineTrackScientific\\judgement\\judgmentfile_01"));
                    String line="";
                    while ((line=br.readLine())!=null)
                    {
                        String[] str=line.split("\\s+");
                        if(Integer.parseInt(str[0])==topic)
                        {
                            lhm.put(str[2],Double.parseDouble(str[3]));
                            sumRel+=Double.parseDouble(str[3]);
                        }

                    }
                    br.close();
                    //System.out.println(sumRel);

                    BufferedReader br1=new BufferedReader(new FileReader("E:\\TREC 数据集\\2017MedicineTrackScientific\\standard-input-nor1-60\\"+systemName[i]));
                    String line1="";
                    while ((line1=br1.readLine())!=null)
                    {
                        String[] str= line1.split("\\s+");
                        if(Integer.parseInt(str[0])==topic)
                        {
                            if(lhm.keySet().contains(str[2]))
                            {
                                pN+=lhm.get(str[2]);
                            }else
                            {
                                pN+=0;
                            }
                            n++;
                            if(n==topN)
                            {
                                break;
                            }
                        }
                    }
                    br1.close();
                    //System.out.println(pN);

                    P=pN*1.0/topN;
                    R=pN/sumRel;
                *//*System.out.println(P);
                System.out.println(R);*//*
                    if((2*P*R)==0)
                    {
                        f=0;
                    }else
                    {
                        f=(2*P*R)/(P+R);
                    }
                    //System.out.println(systemName[i]+"\t"+topic+"\t"+f);
                    sumf+=f;
                    sump+=P;
                    sumr+=R;
                }

                //System.out.println(systemName[i]+"\t"+"F"+"\t"+sumf/sumTopic+"\t"+"P"+"\t"+sump/sumTopic+"\t"+"R"+"\t"+sumr/sumTopic);
                //计算每个成员系统的相对权重Wi
                W=((sumf/sumTopic)*0.8+(sump/sumTopic)*0.1+(sumr/sumTopic)*0.1);
                sumW+=W;
                //System.out.println(systemName[i]+"\t"+"W"+"\t"+W);//每个系统的相对权重
                w=W*(1-a);
                //System.out.println(systemName[i]+"\t"+"AC"+"\t"+(w));//每个系统综合准确度指标AC(Si)
                //System.out.println(sumW);//每个系统综合准确度指标AC(Si)

            }*/
            //求成员系统之间的多样性，利用成员系统之间的欧氏距离
            for (int run1 = 0; run1 < systemName.length; run1++) {//遍历存放成员系统姓名的数组，以此计算当前成员系统与其他成员系统之间的欧氏距离
                memberSystemSumDistance=0;//存放当前成员系统与其他成员系统之间的欧氏距离；
                for (int run = 0; run <systemName.length ; run++) {
                    if(run1!=run)
                    {
                        double Sum=0;
                        for (int topic = 1; topic <=sumTopic ; topic++) {
                            LinkedHashMap<String,Double> d1=new LinkedHashMap<>();
                            LinkedHashMap<String,Double> d2=new LinkedHashMap<>();
                            LinkedHashSet<String> lhs=new LinkedHashSet<>();
                            String system1="E:\\TREC 数据集\\2017MedicineTrackScientific\\standard-input-nor1-60\\"+systemName[run1];
                            String system2="E:\\TREC 数据集\\2017MedicineTrackScientific\\standard-input-nor1-60\\"+systemName[run];

                            mergeDocument(system1,system2,lhs,topic);
       /* System.out.println("两个成员系统中的所有不重复的文档的名字：");
        for (String s : lhs) {
            System.out.println(s);
        }*/
                            //将两个成员系统对应的相同查询的文档名和文档分数记录下来
                            d1=readScores(system1,topic);
                            d2=readScores(system2,topic);

                            //将两组分数的求差
                            Sum+=scoreSubtraction(d1,d2,lhs,run,run1);
                        }
                        //System.out.println(Sum/sumTopic);//这里输出的成员系统A与成员系统B之间的欧氏距离
                        memberSystemSumDistance+=Sum/sumTopic;
                    }
                }
                /*
                * 多样性指标DI
                （1）基于距离度量的方法：可以使用成员系统之间的距离来度量多样性，例如可以使用欧几里得距离或余弦距离等。
                DI(Si) = 1 / ∑j≠i d(Si,Sj)
                其中，d(Si,Sj)为成员系统Si和Sj之间的距离。
                * */
                //DI=(1/memberSystemSumDistance)*a;
                DI=memberSystemSumDistance/(systemName.length-1);
                //System.out.println("DI:"+"\t"+(1/memberSystemSumDistance));
                // System.out.println(systemName[run1]+"\t"+"DI"+"\t"+DI);
                sumDI+=DI;
            }
            System.out.println("检索系统组合的DI："+sumDI/systemName.length);


        }
    }

    private static double scoreSubtraction(LinkedHashMap<String, Double> d1, LinkedHashMap<String, Double> d2, LinkedHashSet<String> lhs, int run, int run1) {
        LinkedHashMap<String,Double> sub1=new LinkedHashMap<>();
        LinkedHashMap<String,Double> sub2=new LinkedHashMap<>();
        LinkedHashMap<String,Double> sub=new LinkedHashMap<>();
        double sum=0;
        for (String s : lhs) {
            sub1.put(s,0.0);
            sub2.put(s,0.0);
        }
        for (String s : lhs) {
            for (Map.Entry<String, Double> entry : d1.entrySet()) {
                if(s.equals(entry.getKey()))
                {
                    sub1.put(s,d1.get(s));
                }
            }
        }
        for (String s : lhs) {
            for (Map.Entry<String, Double> entry : d2.entrySet()) {
                if(s.equals(entry.getKey()))
                {
                    sub2.put(s,d2.get(s));
                }
            }
        }
        for (String s : lhs) {
            double result=Math.pow((sub1.get(s)-sub2.get(s)),2);
            sum+=Math.sqrt(result);
        }
        //System.out.println(sum);
        return sum;
    }

    private static LinkedHashMap<String, Double> readScores(String system, int topic) throws IOException {
        LinkedHashMap<String,Double> d=new LinkedHashMap<>();
        BufferedReader br=new BufferedReader(new FileReader(system));
        String line="";
        while ((line=br.readLine())!=null)
        {
            String[] str=line.split("\\s+");
            if(Integer.parseInt(str[0])==topic)
            {
                d.put(str[2],Double.parseDouble(str[4]));
            }

        }
        br.close();
        return d;
    }

    private static void mergeDocument(String system1, String system2, LinkedHashSet<String> lhs, int topic) throws IOException {
        BufferedReader br1=new BufferedReader(new FileReader(system1));
        String line1="";
        while ((line1=br1.readLine())!=null)
        {
            String[] str=line1.split("\\s+");
            if(Integer.parseInt(str[0])==topic)
            {
                lhs.add(str[2]);
            }

        }
        br1.close();
        BufferedReader br2=new BufferedReader(new FileReader(system2));
        String line2="";
        while ((line2=br2.readLine())!=null)
        {
            String[] str=line2.split("\\s+");
            if(Integer.parseInt(str[0])==topic)
            {
                lhs.add(str[2]);
            }

        }
        br2.close();
    }
}


