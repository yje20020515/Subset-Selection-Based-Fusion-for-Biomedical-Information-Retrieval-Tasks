package sjh;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        //计算每个检索系统每个topic有多少文档
        int rank=1000;
        int topics=30;
        for (int i = 1; i <= rank; i++) {
            countFun(i,topics);
        }
    }

    private static void countFun(int ranks,int topics) throws IOException {
        int sum=0;
        String[] systemNames={
                "SIBTMlit4",
                "mugpubbase",
                "UTDHLTFF",
                "pms_run3_abs",
                "UDInfoPMSA2",
                "UTDHLTSF",
                "SIBTMlit3",
                "mugpubboost",
                "UD_GU_SA_5",
                "UCASBASEa",
                "pms_run5_abs",
                "UTDHLTJQ",
                "UKY_AGG",
                "SIBTMlit2",
                "UDInfoPMSA7",
                "mugpubgene",
                "aCSIROmedPCB",
                "UNTIIADW",
                "UD_GU_SA_4",
                "mRun3MRF",
                "pms_run1",
        };
        for (int i = 0; i < systemNames.length; i++) {
            for (int i1 = 1; i1 <= topics; i1++) {
                int n=0;
                BufferedReader br=new BufferedReader(new FileReader("E:\\TREC 数据集\\2017MedicineTrackScientific\\standard-input-nor1-60\\"+systemNames[i]));
                String line="";
                while ((line=br.readLine())!=null)
                {
                    String[] str=line.split("\\s+");
                    int topic=Integer.parseInt(str[0]);
                    if(topic==i1)
                    {
                        n++;
                        if(n==ranks)
                        {
                            sum++;
                            break;
                        }

                    }
                }
                br.close();
            }
        }
        System.out.println("rank"+ranks+":"+"\t"+sum);

    }
}
