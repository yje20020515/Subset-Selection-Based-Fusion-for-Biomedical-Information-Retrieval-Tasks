package com.fusion_qrel_012_Main;

import com.LCfusion.Main.StardardFusionMain.FusionMain;
import com.fusionAlgorithm.CombSUM;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class fusion3_Main {
    /**
     * LC融合实验（分奇偶）
     * 通过xls文件得到要融合的系统路径
     * 按照奇偶topic进行分组得到Generatefile文件
     * 按照Generatefile文件获得奇偶各自的权重文件
     * 按照权重文件交叉进行LC数据融合
     * 将得到的结果再生成为为一个文件
     */
    private static String[] readXls(String xls_path,int Num) throws IOException {
        String[] fusionNames = new String[Num];
        FileInputStream fip = new FileInputStream(xls_path);
        HSSFWorkbook rb = new HSSFWorkbook(fip);
        Sheet sheet = rb.getSheetAt(0);
        Row row;

        for (int rowNum = 0; rowNum < sheet.getPhysicalNumberOfRows(); rowNum++) {
            row = sheet.getRow(rowNum);
            System.out.println(rowNum);
            String fusionName = row.getCell(0).getStringCellValue().replaceAll("###","");
            fusionNames[rowNum] = fusionName;
        }
        fip.close();
        return fusionNames;
    }
    private static void createDirectory(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()){
            file.mkdirs();
        }
    }
    private static void RunProgram(String runsPath,String classificationPath,String fusionPath,int[] topicsInt,int[] all_topic_ji,int[] all_topic_ou,int StartNum,int EndNum,int startExperimentNum,int EndExperimentNum
    ,String Generatefile_output,String weights_output,String qrelsPath) throws Exception {
        FusionMain.setAll_topic(topicsInt);
        FusionMain.setAll_topic_ji(all_topic_ji);
        FusionMain.setAll_topic_ou(all_topic_ou);
        for (int Num = StartNum; Num <= EndNum ; Num++) {
            String outputPath =fusionPath + Num+"\\";
            createDirectory(outputPath);
            for (int ExperimentNum = startExperimentNum; ExperimentNum <= EndExperimentNum ; ExperimentNum++) {
                System.err.println(Num+"\t"+ExperimentNum);
                String xls_path = classificationPath + Num+"\\Semi_K"+Num+"_"+ExperimentNum+".xls";//组合为xls路径
                String[] fusionNames = readXls(xls_path,Num);
                for (int NameNum = 0; NameNum < fusionNames.length; NameNum++) {
                    System.out.println(fusionNames[NameNum]);
                    fusionNames[NameNum] = runsPath + fusionNames[NameNum];
                }

                String FusionFileName = "LCfusion_"+Num+"_"+ExperimentNum;

                String outputFusionPath = outputPath + FusionFileName;
                //CombSUM.RunProgram(fusionNames,FusionFileName,outputFusionPath,topicsInt);
                FusionMain.RunProgram(fusionNames.length,1,fusionNames,runsPath,qrelsPath,ExperimentNum,Generatefile_output,
                        weights_output,outputFusionPath);
            }
        }

    }

    public static void main(String[] args) throws Exception {
        //for(int i1=21;i1<=40;i1++)
        //{
            //String runsPath = "F:\\TREC 数据集\\2019MedicineTrackScientific\\standard-input-nor1-60\\";
            String runsPath = "E:\\TREC 数据集\\2022DeepLearningPassageRanking\\standard-input-nor1-60\\";
            int[] topicsInt ={
                    /*42255, 47210, 67316, 135802, 156498, 169208,
                    174463, 258062, 324585,*/
                    //2020topic
                    /*23849, 42255, 47210, 67316,
                    118440, 121171, 135802, 141630,
                    156498, 169208, 174463, 258062,
                    324585, 330975, 332593, 336901,
                    390360, 405163, 555530, 583468,
                    640502, 673670, 701453, 730539,
                    768208, 877809, 911232, 914916,
                    938400, 940547, 997622, 1030303,
                    1037496, 1043135, 1051399, 1064670,
                    1071750, 1105792, 1106979, 1108651,
                    1109707, 1110678, 1113256, 1115210,
                    1116380, 1121353, 1122767, 1127540,
                    1131069, 1132532, 1133579, 1136043,
                    1136047, 1136962*/
                    //2021
                    /*2082, 23287, 30611, 112700,
                    168329, 190623, 226975, 237669,
                    253263, 300025, 300986, 337656,
                    364210, 395948, 421946, 493490,
                    505390, 508292, 540006, 596569,
                    615176, 629937, 632075, 646091,
                    647362, 661905, 681645, 688007,
                    707882, 764738, 806694, 818583,
                    832573, 835760, 845121, 935353,
                    935964, 952262, 952284, 975079,
                    1006728, 1040198, 1103547, 1104300,
                    1104447, 1107704, 1107821, 1109840,
                    1110996, 1111577, 1113361, 1117243,
                    1117298, 1118716, 1121909, 1128632,
                    1129560,*/
                    /*1, 2, 3, 4,
                    5, 6, 7, 8,
                    9, 10, 11, 12,
                    13, 14, 15, 16,
                    17, 18, 19, 20,
                    21, 22, 23, 24,
                    25, 26, 27, 28,
                    29, 30, 31, 32,
                    33, 34, 35, 36,
                    37, 38, 39, 40,*/
                   /* 41,42,43,
                    44,45,46,47,48,
                    49,50*/
                    /*2082,	23287,	30611,	112700,	168329,	190623,
                    226975,	237669,	253263,	300025,	300986,	337656,
                    364210,	395948,	421946,	493490,	505390,	508292,
                    540006,	596569,	629937,	646091,	647362,	661905,
                    681645,	688007,	707882,	764738,	806694,	818583,
                    832573,	835760,	845121,	935353,	935964,	952262,
                    952284,	975079,	1006728,1040198,1104300,1104447,
                    1107704,1107821,1109840,1110996,1111577,	1113361,
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
            int[] all_topic_ji = {
                    /*42255,67316,156498,174463,324585*/
                    //2020topic
                   /* 23849,47210,
                    118440,135802,
                    156498,174463,
                    324585,332593,
                    390360,555530,
                    640502,701453,
                    768208,911232,
                    938400,997622,
                    1037496,1051399,
                    1071750,1106979,
                    1109707,1113256,
                    1116380,1122767,
                    1131069,1133579,
                    1136047,*/
                    //2021
                    /*2082, 30611,
                    168329,226975,
                    253263,300986,
                    364210,421946,
                    505390,540006,
                    615176,632075,
                    647362,681645,
                    707882,806694,
                    832573,845121,
                    935964,952284,
                    1006728,1103547,
                    1104447,1107821,
                    1110996,1113361,
                    1117298,1121909,
                    1129560*/
                   /* 1,3,
                    5,7,
                    9,11,
                    13, 15,
                    17, 19,
                    21, 23,
                    25, 27,
                    29, 31,
                    33, 35,
                    37, 39,*/
                    /*41,43,
                    45,47,
                    49*/
                   /* 2082,   30611,	168329,
                    226975, 253263,	300986,
                    364210, 421946,	505390,
                    540006, 629937,	647362,
                    681645, 707882,	806694,
                    832573, 845121,	935964,
                    952284, 1006728,1104300,
                    1107704, 1109840,1111577,
                    1117243, 1121909,1129560,*/
                    2000511,2001532,2001975,
                    2002269,2002798,2003322,
                    2004237,2005810,2006211,
                    2006394,2007055,2008871,
                    2009871,2012536,2016333,
                    2025747,2026558,2027497,
                    2029260,2030608,2032090,
                    2032956,2033396,2034205,
                    2035009,2035565,2036968,
                    2037609,2038466,2039908,
                    2040352,2043895,2045272,
                    2049417,2053884,2055211,
                    2055634,2056158,
            };
            int[] all_topic_ou = {
                    //47210,135802,169208,258062
                    //2020topic
                    /*42255, 67316,
                    121171, 141630,
                    169208, 258062,
                    330975, 336901,
                    405163, 583468,
                    673670, 730539,
                    877809, 914916,
                    940547, 1030303,
                    1043135, 1064670,
                    1105792, 1108651,
                    1110678, 1115210,
                    1121353, 1127540,
                    1132532, 1136043,
                    1136962*/
                    //2021
                   /* 23287, 112700,
                    190623,237669,
                    300025,337656,
                    395948,493490,
                    508292,596569,
                    629937,646091,
                    661905,688007,
                    764738,818583,
                    835760,935353,
                    952262,975079,
                    1040198,1104300,
                    1107704,1109840,
                    1111577,1117243,
                    1118716,1128632,*/
                   /* 2, 4,
                    6, 8,
                    10,12,
                    14, 16,
                    18, 20,
                    22, 24,
                    26, 28,
                    30, 32,
                    34, 36,
                    38, 40,*/
                    /*42,44,
                    46,48,
                    50*/
                   /* 23287,112700,190623,
                    237669,300025,337656,
                    395948,493490,508292,
                    596569,646091,661905,
                    688007,764738,818583,
                    835760,935353,952262,
                    975079,1040198,1104447,
                    1107821,1110996,1113361,
                    1118716,1128632,*/
                    2000719,2001908,2002146,
                    2002533,2003157,2003976,
                    2004253,2005861,2006375,
                    2006627,2007419,2009553,
                    2012431,2013306,2017299,
                    2026150,2027130,2028378,
                    2030323,2031726,2032949,
                    2033232,2033470,2034676,
                    2035447,2036182,2037251,
                    2037924,2038890,2040287,
                    2040613,2044423,2046371,
                    2049687,2054355,2055480,
                    2055795,2056323
            };
        /*2021deeeplearning document
        int[] topicsInt ={
                2082,23287,30611,112700,168329,
                190623,226975,237669,253263,300025,
                300986,337656,364210,395948,421946,
                493490,505390,508292,540006,596569,
                615176,629937,632075,646091,647362,
                661905,681645,688007,707882,764738,
                806694,818583,832573,835760,845121,
                935353,935964,952262,952284,975079,
                1006728,1040198,1103547,1104300,1104447,
                1107704,1107821,1109840,1110996,1111577,
                1113361,1117243,1117298,1118716,1121909,
                1128632,1129560};
        int[] all_topic_ji = {
                2082,30611,168329,226975,253263,
                300986,364210,421946,505390,540006,
                615176,632075,647362,681645,707882,
                806694,832573,845121,935964,952284,
                1006728,1103547,1104447,1107821,1110996,
                1113361,1117298,1121909,1129560
        };
        int[] all_topic_ou = {
                23287,112700,190623,237669,300025,
                337656,395948,493490,508292,596569,
                629937,646091,661905,688007,764738,
                818583,835760,935353,952262,975079,
                1040198,1104300,1107704,1109840,1111577,
                1117243,1118716,1128632
        };
        2020deeplearning document
                42255, 47210, 67316, 135802, 156498, 169208,
                174463, 258062, 324585, 330975, 332593, 336901,
                673670, 701453, 730539, 768208, 877809, 911232,
                938400, 940547, 997622, 1030303, 1037496, 1043135,
                1049519, 1051399, 1056416, 1064670, 1071750, 1103153,
                1105792, 1108729, 1109707, 1113256, 1115210, 1116380,
                1119543, 1122767, 1127540, 1131069, 1132532, 1136043,
                1136047, 1136769, 1136962
        int[] all_topic_ji = {
                42255,67316,156498,174463,324585,332593,
                673670,324585,332593,673670, 730539,877809,
                938400, 997622,1037496,1049519, 1056416, 1071750,
                1105792,1109707,1115210, 1119543,1127540, 1132532,
                1136047, 1136962
        };
        int[] all
        _topic_ou = {
                47210,135802,169208,258062,330975, 336901,
                701453, 768208, 911232,940547,1030303, 1043135,
                1051399, 1064670,1103153, 1108729, 1113256,1116380,
                1122767,1131069,1136043,1136769

        };*/
            int StartNum = 2;
            int EndNum = 10;
            int startExperimentNum = 1;
            int EndExperimentNum =1;
        /*String classificationPath = "F:\\TREC 数据集\\2019MedicineTrackScientific\\classification\\";//原版代码
        String fusionPath = "F:\\TREC 数据集\\2019MedicineTrackScientific\\fusionTest\\fusion\\";
        String Generatefile_output="F:\\TREC 数据集\\2019MedicineTrackScientific\\fusionTest\\LCfuison\\GenerateFile\\";
        String weights_output="F:\\TREC 数据集\\2019MedicineTrackScientific\\fusionTest\\LCfuison\\weightsFile\\";
        String qrelsPath="F:\\TREC 数据集\\2019MedicineTrackScientific\\judgement\\judgmentFakeReal";*/
            String classificationPath = "E:\\TREC 数据集\\2022DeepLearningPassageRanking\\classification\\";
            String fusionPath = "E:\\TREC 数据集\\2022DeepLearningPassageRanking\\fusionTest\\fusion\\";
            String Generatefile_output="E:\\TREC 数据集\\2022DeepLearningPassageRanking\\fusionTest\\LCfuison\\GenerateFileFQ\\";
            String weights_output="E:\\TREC 数据集\\2022DeepLearningPassageRanking\\fusionTest\\LCfuison\\weightsFileFQ\\";
            //String qrelsPath="F:\\TREC 数据集\\2019MedicineTrackScientific\\judgement\\judgmentFakeReal";
            String qrelsPath="E:\\TREC 数据集\\2022DeepLearningPassageRanking\\judgement\\judgmentfile_01";
            RunProgram(runsPath,classificationPath,fusionPath,topicsInt,all_topic_ji,all_topic_ou,StartNum,EndNum,startExperimentNum,EndExperimentNum,
                    Generatefile_output,weights_output,qrelsPath);//
        }

    }


//}
