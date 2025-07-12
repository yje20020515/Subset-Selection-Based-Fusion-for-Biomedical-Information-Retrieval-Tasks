//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.Semi_SupervisedMethod.Generate;

import com.DataStruct.Doc;
import com.DataStruct.Docs;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class GetPercentageSemi_Judgment {
    private static int[] topics = new int[]{
           /* 1, 2, 3, 4, 5,
            6, 7, 8, 9, 10,
            11, 12, 13, 14,
            15, 16, 17, 18,
            19, 20, 21, 22,
            23, 24, 25, 26,
            27, 28, 29, 30,
            31, 32, 33, 34,
            35, 36, 37, 38,
            39, 40,*//*41,42,43,
            44,45,46,47,48,
            49,50*/
            /*2082,23287,30611,112700,168329,190623,
            226975,237669,253263,300025,300986,337656,
            364210,395948,421946,493490,505390,508292,
            540006,596569,629937,646091,647362,661905,
            681645,688007,707882,764738,806694,818583,
            832573,835760,845121,935353,935964,952262,
            952284,975079,1006728,1040198,1104300,1104447,
            1107704,1107821,1109840,1110996,1111577,1113361,
            1117243,1118716,1121909,1128632,1129560,*/
            //2022DLpassage
            2000511,2000719,2001532,2001908,2001975,2002146,
            2002269,2002533,2002798,2003157,2003322,2003976,
            2004237,2004253,2005810,2005861,2006211,2006375,
            2006394,2006627,2007055,2007419,2008871,2009553,
            2009871,2012431,2012536,2013306,2016333,2017299,
            2025747,2026150,2026558,2027130,2027497,2028378,
            2029260,2030323,2030608,2031726,2032090,2032949,
            2032956,2033232,2033396,2033470,2034205,2034676,
            2035009,2035447,2035565,2036182,2036968,2037251,
            2037609,2037924,2038466,2038890,2039908,2040287,
            2040352,2040613,2043895,2044423,2045272,2046371,
            2049417,2049687,2053884,2054355,2055211,2055480,
            2055634,2055795,2056158,2056323
            };

    public GetPercentageSemi_Judgment() {
    }

    private static Docs getDocs(String JudgmentPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(JudgmentPath));
        String oneLine = "";
        Docs Judgment = new Docs(0);

        while((oneLine = br.readLine()) != null) {
            String[] str = oneLine.split("\\s+");
            int topic = Integer.parseInt(str[0]);
            String docID = str[2];
            double rel = Double.parseDouble(str[3]);
            if (rel > 0.0) {
                Judgment.getList().add(new Doc(docID, topic));
            }
        }

        br.close();
        return Judgment;
    }

    private static void calculatePercentage(String realJudgmentPath, String fakeJudgmentPath,String outputFakeRealPath) throws IOException {
        Docs realJudgmentList = getDocs(realJudgmentPath);
        Docs fakeJudgmentList = getDocs(fakeJudgmentPath);
         int fakeRealNum = 0;
        Iterator var5 = fakeJudgmentList.getList().iterator();

        while(var5.hasNext()) {
            Doc doc = (Doc)var5.next();
            if (realJudgmentList.getList().contains(doc)) {
                ++fakeRealNum;
            }
        }

        System.out.println(realJudgmentList.getList().size());
        double percentage = (double)fakeRealNum * 1.0 / (double)realJudgmentList.getList().size();
        System.out.print(fakeRealNum + "\t");
        System.out.println(percentage);
        CountTopicJudgmentNum(fakeJudgmentList, realJudgmentList);
        CountTopicJudgmentNum(realJudgmentList, realJudgmentList);

        writeFake_Real(fakeJudgmentList,realJudgmentList,outputFakeRealPath);
    }

    private static void writeFake_Real(Docs fakeJudgmentList, Docs realJudgmentList,String outputFakeRealPath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFakeRealPath));
        String line = "";
        for (Doc doc : fakeJudgmentList.getList()) {
            if (realJudgmentList.getList().contains(doc)){
                line = doc.getTopic() + "\tQ0\t"+ doc.getDocID() +"\t"+ "1\n";
                bw.write(line);
            }
        }
        bw.close();
    }

    private static void CountTopicJudgmentNum(Docs JudgmentList, Docs realJudgmentList) {
        ArrayList<TopicsNUM> topicsNUMS = new ArrayList();
        int[] var3 = topics;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            int topic = var3[var5];
            topicsNUMS.add(new TopicsNUM(topic, 0));
        }

        Iterator var7 = JudgmentList.getList().iterator();

        while(var7.hasNext()) {
            Doc doc = (Doc)var7.next();
            Iterator var10 = topicsNUMS.iterator();



            while(var10.hasNext()) {
                TopicsNUM topicsNUM = (TopicsNUM)var10.next();
                if (doc.getTopic() == topicsNUM.getTopic() && realJudgmentList.getList().contains(doc)) {
                    topicsNUM.setCount(topicsNUM.getCount() + 1);
                }
            }
        }

        System.out.println("-----------------------------------------------");
        var7 = topicsNUMS.iterator();

        while(var7.hasNext()) {
            TopicsNUM topicsNUM = (TopicsNUM)var7.next();
            System.out.println(topicsNUM.getTopic() + "\t" + topicsNUM.getCount());
        }

        System.out.println("-----------------------------------------------");
    }

    public static void main(String[] args) throws IOException {
        String realJudgmentPath = "E:\\TREC 数据集\\2022DeepLearningPassageRanking\\judgement\\judgmentfile_01";
            String fakeJudgmentPath = "E:\\TREC 数据集\\2022DeepLearningPassageRanking\\judgement\\A";//记得改上面topic参数；
        String outputFakeRealPath = "E:\\TREC 数据集\\2022DeepLearningPassageRanking\\judgement\\1";//该文档里面是真的且只含有相关文档
        calculatePercentage(realJudgmentPath, fakeJudgmentPath,outputFakeRealPath);
    }
}
