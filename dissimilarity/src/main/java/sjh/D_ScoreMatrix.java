package sjh;


import java.io.*;

public class D_ScoreMatrix {
    public static void main(String[] args) throws IOException {

        for (int topic = 1; topic <=50 ; topic++) {
            String[] systemNames={/*"aCSIROmedAll","aCSIROmedMCB","aCSIROmedNEG"*/
                    /*"cbnuSA1","cbnuSA2","rbf","two_stage"*/
                    /*"cubicmnzAbs","mnzAbs","sumAbs","sumEW"*/
                   /* "ECNU_S_Run2", "ECNU_S_Run3"*/
                   /* "IKMLAB_1", "IKMLAB_2", "IKMLAB_3", "IKMLAB_4", "mayopubtator", "UNTIIA_DGEWU", "UNTIIA_WTU"*/
                    /*"MedIER_sa11", "MedIER_sa12", "MedIER_sa13", "MedIER_sa14", "MedIER_sa15", "UCASSA1", "UCASSA2", "UCASSA3", "UCASSA4", "UCASSA5", "UDInfoPMSA1", "UDInfoPMSA2", "UDInfoPMSA5"*/
                    /*"KL18AbsFuse", "KL18absHY", "KLPM18T2Bl"*/
                   /* "minfolabBA", "minfolabBC", "minfolabBD", "minfolabTH","UVAEXPBOOST", "UVAEXPBSTDIF", "UVAEXPBSTEXT","UVAEXPBSTNEG","UVAEXPBSTSHD",*/
                    /*"MSIIP_BASE", "MSIIP_PBAH","MSIIP_PBH","MSIIP_PBL","MSIIP_PBPK"*/
                    /*"RSA_DSC_LA_1","RSA_DSC_LA_2", "RSA_DSC_LA_3", "RSA_DSC_LA_4", "RSA_DSC_LA_5"*/
                    "UTDHLTRI_NL","UTDHLTRI_RF", "UTDHLTRI_SF", "UTDHLTRI_SS"};
            BufferedReader br1=new BufferedReader(new FileReader("E:\\TREC 数据集\\2018MedicineTrackScientific\\ScoreMatrix\\cu21\\"+topic+"\\"+systemNames[0]));
            String line1="";
            int n=0;
            while ((line1=br1.readLine())!=null)
            {
                String[] str=line1.split("\\s+");
                n++;
            }
            System.out.println(n);
            br1.close();

            double[][] ScoreMatrix=new double[n][4];

            for (int i = 0; i < systemNames.length; i++) {
                BufferedReader br=new BufferedReader(new FileReader("E:\\TREC 数据集\\2018MedicineTrackScientific\\ScoreMatrix\\cu21\\"+topic+"\\"+systemNames[i]));
                String line="";
                int k=0;
                while ((line=br.readLine())!=null)
                {
                    String[] str=line.split("\\s+");
                    ScoreMatrix[k][i]=Double.parseDouble(str[1]);
                    k++;
                }
                br.close();
            }
            BufferedWriter bw=new BufferedWriter(new FileWriter("E:\\TREC 数据集\\2018MedicineTrackScientific\\ScoreMatrix\\cu21\\"+topic+"\\ScoreMatrixs"));
            for (int i = 0; i < ScoreMatrix.length; i++) {
                System.out.print("[");
//            bw.write("[");
                for (int i1 = 0; i1 < ScoreMatrix[i].length; i1++) {
                    System.out.print(ScoreMatrix[i][i1]+",");
                    bw.write(ScoreMatrix[i][i1]+" ");
                }
                System.out.print("],");
//            bw.write("],");
                System.out.println("");
                bw.write('\n');
            }
            bw.close();
        }
    }
}
