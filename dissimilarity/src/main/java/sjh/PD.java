package sjh;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PD {
    public static void main(String[] args) throws IOException {
        //该方法实现方法三
        //首先确定最大MAP值的检索系统
        String[] R={"UTDHLTFF", "UD_GU_SA_5",};//该集合中存放已经摘取出来的检索系统
        String[] A={
                "aCSIROmedAll", "aCSIROmedMCB", "aCSIROmedMGB", "aCSIROmedNEG", "aCSIROmedPCB", "Broad", "cbnuSA1", "cbnuSA2",
                "cbnuSA3", "DUTIR003", "ECNU_M_1", "ECNU_M_2", "ECNU_M_3", "ECNU_M_4", "ECNU_M_5", "eth_a_gws", "eth_a_luc",
                "eth_a_nn", "eth_a_ws", "eth_a_ws_q", "Focused", "GP14Medline", "GP16Medline", "Gwave", "ielabRun1", "ielabRun21",
                "ielabRun22", "ielabRun23", "ielabRun3", "KISTI01", "KISTI02", "KISTI03", "KISTI04", "KISTI05", "kkseabs1",
                "kkseabs3", "kkseabs4", "mayonlppm1", "mayonlppm2", "mayonlppm3", "mayonlppm4", "mayonlppm5", "MedIER_sa1",
                "MedIER_sa2", "MedIER_sa3", "MedIER_sa4", "medline1", "medline2", "medline3", "medline4", "medline5", "mRun1Bsl",
                "mRun2BslOth", "mRun3MRF", "mRun4MRFrf", "mRun5MRFBow", "mugpubbase", "mugpubboost", "mugpubdiseas","mugpubgene",
                "mugpubshould", "NOVAsa1", "NOVAsa2", "NOVAsa3", "Ontological", "pms_run1", "pms_run2_abs", "pms_run3_abs", "pms_run4_abs",
                "pms_run5_abs", "POZabsBB2GRn", "POZabsBB2sn", "POZabsLGDGRn", "SDSFU_Ensem", "SDSFU_Jnal", "SDSFU_Lambda", "SDSFU_PF_SA",
                "SDSFU_PU_SA", "Semantic", "SIBTMlit1", "SIBTMlit2", "SIBTMlit3", "SIBTMlit4", "SIBTMlit5", "Textual", "UCASBASEa",
                "UCASSEM1a", "UCASSEM2a", "UCASSEM3a", "UCASSEMUMLSa", "UD_GU_SA_1", "UD_GU_SA_2", "UD_GU_SA_3", "UD_GU_SA_4",
                "udelT1Base", "udelT1Comb", "udelT1GeMeSH", "udelT1Gene", "udelT1PRF", "UDInfoPMSA2", "UDInfoPMSA3", "UDInfoPMSA5",
                "UDInfoPMSA6", "UDInfoPMSA7", "UKY_AGG", "UKY_BASE", "UKY_CJT", "UKY_COM", "UKY_MAN", "UNTIIADW", "UNTIIAGA", "UNTIIAIS",
                "UNTIIALQ", "UNTIIASY", "UTDHLTAF", "UTDHLTJQ", "UTDHLTSF", "UTDHLTSQ", "UWMSOIS5", "UWMSOIS6", "UWMSOIS7", "UWMSOIS8", "UWMSOIS9"



        };
        //确定两两组合对数
        int n=R.length+1;
        int count=n*(n-1)/2;

        HashMap<String,Double> hm=new HashMap<>();//用来存放文件中所有计算出来的组合的J或者DSC

        BufferedReader br=new BufferedReader(new FileReader("E:\\TREC 数据集\\2017MedicineTrackScientific\\RJ&RDSC2"));
        String line="";
        while ((line=br.readLine())!=null)
        {
            String[] str=line.split("\\s+");
            double J=Double.parseDouble(str[2]);
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
        Set<String> set=new HashSet<>();//用来存放每次组合的检索系统组合
        for (int i = 0; i < r.length; i++) {
            set.add(r[i]+"&"+s);
            set.add(s+"&"+r[i]);
        }
        for (int j = 0; j < r.length-1; j++) {
            set.add(r[j]+"&"+r[j+1]);
        }

        // 存储组合的列表
        List<String> combinations = new ArrayList<>();

        // 生成两两组合
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
        //此处的c代表的是需要累加的组合的个数，当c==count时，累加得到的结果才是正确的
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
