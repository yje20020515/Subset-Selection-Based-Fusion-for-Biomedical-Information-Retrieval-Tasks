package sjh;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class C_ScoreMatrix {
    public static void main(String[] args) throws IOException {
        //输出每个主题中文档的对应分数矩阵
        //分数特征矩阵


        for (int topic = 41; topic <=50 ; topic++) {
            LinkedHashSet<String> lhs=new LinkedHashSet<>();
            String[] systemNames={/*"aCSIROmedAll","aCSIROmedMCB","aCSIROmedNEG"*/
                    /*"cbnuSA1","cbnuSA2","rbf", "two_stage"*/
                    /*"cubicmnzAbs","mnzAbs","sumAbs","sumEW"*/
                   /*"ECNU_S_Run2", "ECNU_S_Run3"*/
                    /*"ECNU_S_Run4", "ECNU_S_Run5"*/
                    /*"IKMLAB_1", "IKMLAB_2", "IKMLAB_3", "IKMLAB_4", "mayopubtator", "UNTIIA_DGEWU", "UNTIIA_WTU"*/
                    /*"MedIER_sa11",
                            "MedIER_sa12",
                            "MedIER_sa13",
                            "MedIER_sa14",
                            "MedIER_sa15",
                            "UCASSA1",
                            "UCASSA2",
                            "UCASSA3",
                            "UCASSA4",
                            "UCASSA5",
                            "UDInfoPMSA1",
                            "UDInfoPMSA2",
                            "UDInfoPMSA5"*/
                    /*"KL18AbsFuse", "KL18absHY", "KLPM18T2Bl"*/
                    /*"minfolabBA", "minfolabBC", "minfolabBD", "minfolabTH","UVAEXPBOOST", "UVAEXPBSTDIF", "UVAEXPBSTEXT","UVAEXPBSTNEG","UVAEXPBSTSHD",*/
                   /* "MSIIP_BASE", "MSIIP_PBAH","MSIIP_PBH","MSIIP_PBL","MSIIP_PBPK"*/
                    /*"RSA_DSC_LA_1","RSA_DSC_LA_2", "RSA_DSC_LA_3", "RSA_DSC_LA_4", "RSA_DSC_LA_5"*/
                    /*"UDInfoPMSA3","UDInfoPMSA4"*/
                    "UTDHLTRI_NL","UTDHLTRI_RF", "UTDHLTRI_SF", "UTDHLTRI_SS",};
            for (int i = 0; i < systemNames.length; i++) {
                BufferedReader br=new BufferedReader(new FileReader("E:\\TREC 数据集\\2018MedicineTrackScientific\\standard-input-nor1-60\\"+systemNames[i]));
                String line="";
                while ((line=br.readLine())!=null)
                {
                    String[] str=line.split("\\s+");
                    if(Integer.parseInt(str[0])==topic)
                    {
                        lhs.add(str[2]);
                    }
                }
                br.close();
            }
            //现在已经将三个成员系统topic为1的文档名不重复的存贮在lhs中，接下来就是将文档在三个成员系统内的分数找到
       /* for (String s : lhs) {
            sum++;
            System.out.println(s);
        }
        System.out.println(sum);*/
            selsectScore(systemNames,lhs,topic);
        }

    }

    private static void selsectScore(String[] systemNames, LinkedHashSet<String> lhs,int topic) throws IOException {

        for (int i = 0; i < systemNames.length; i++) {
            BufferedWriter bw=new BufferedWriter(new FileWriter("E:\\TREC 数据集\\2018MedicineTrackScientific\\ScoreMatrix\\cu21\\"+topic+"\\"+systemNames[i]));
            LinkedHashMap<String,Double> lhm=new LinkedHashMap<>();
            String line="";
            for (String s : lhs) {
                int n=0;
                BufferedReader br=new BufferedReader(new FileReader("E:\\TREC 数据集\\2018MedicineTrackScientific\\standard-input-nor1-60\\"+systemNames[i]));
                while ((line=br.readLine())!=null)
                {
                    String[] str=line.split("\\s+");
                    if(Integer.parseInt(str[0])==topic)
                    {
                        if(s.equals(str[2]))
                        {
                            lhm.put(s,Double.parseDouble(str[4]));
                            n=1;
                            //System.out.println(s);
                        }
                    }
                }
                br.close();
                if(n==0)
                {
                    lhm.put(s,0.0);
                }
            }

            for (Map.Entry<String, Double> entry : lhm.entrySet()) {
                System.out.println(entry.getKey()+"\t"+entry.getValue());
                bw.write(entry.getKey()+"\t"+entry.getValue());
                bw.write("\n");
            }
            bw.close();
        }
    }
}
