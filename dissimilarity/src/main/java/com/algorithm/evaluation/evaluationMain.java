package com.algorithm.evaluation;



import java.io.File;

public class evaluationMain {
    /*
    public static void main(String[] args) throws Exception {
        args= new String[]{"-RR"};
        //String qrel = "qrels\\2004WEBqrels.txt";
        //String fusionResult = "2004LCOutputFile";
        //String fusionMap= "2004_fusion_WEB_MAP";
        //String qrel = "2020health\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful";//useful qrel file
        //String qrel = "2020health\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful-correct";//useful and correct  qrel file
        //String qrel = "2020health\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful-credible";//useful and credible  qrel file
        String qrel = "D:\\TREC数据集文件\\zhangzhen\\new_PMSA2018A\\2018qrel\\qrels.txt";

        //String qrel = "2020health\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful-correct-credible";//useful and correct and credible  qrel file
        //String fusionResult = "D:\\eclipse\\eclipse-workspace\\fusion2\\health2020\\kmeans_LC_fusion";
        for(int classification_num = 2;classification_num<=24;classification_num++) {
            String fusionResult = "D:\\TREC数据集文件\\zhangzhen\\new_PMSA2018A\\2018C_SF\\2018C_SFfinalououtput_fusion\\2018C_SFfinalou_LCfusion\\C_SFfinal_LCfusion_LCLRTest\\fusion_"+classification_num;
            String fusionTrecEvaloutput= "D:\\TREC数据集文件\\zhangzhen\\new_PMSA2018A\\2018C_SF\\2018C_SFfinalououtput_fusion\\2018C_SFfinalou_LCfusion\\C_SFfinal_LCfusion_RR";
            TrecEval eval = new TrecEval();
            File file = new File(fusionResult);
            File[] fusionRuns = file.listFiles();
            for(File run:fusionRuns)
            {
                eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\RR_2018C_SFLCou_"+run.getName().toString().replaceAll("input.", ""), args);
            }
            System.out.println("END");
        }


    }*/
	/*
	public static void main(String[] args) throws Exception {
		args= new String[]{"-RR"};
		//-MAP
		//-P
		//-Rprec
		//-RR

		String qrel = "D:\\TREC数据集文件\\zhangzhen\\new_PMSA2017\\2017qrel\\qrels.txt";
		for(int system_num=2;system_num<=20;system_num++) {
			String fusionResult = "D:\\TREC数据集文件\\zhangzhen\\new_PMSA2017\\2017random\\CombSUMfusion\\CombSUM_k"+system_num;
			String fusionTrecEvaloutput= "D:\\TREC数据集文件\\zhangzhen\\new_PMSA2017\\2017random\\CombSUMfusion\\CombSUM_RR_k"+system_num;
			TrecEval eval = new TrecEval();
			File file = new File(fusionResult);
			File[] fusionRuns = file.listFiles();
			for(File run:fusionRuns)
			{
				eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\RR_2017_Random_"+run.getName().toString().replaceAll("input.", ""), args);
			}
			System.out.println("END"+system_num);
		}
	}*/
    public static double output_MAP(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-MAP"};
        //-P
        //-Rprec
        //-RR
        //-NDCG

        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        //System.out.println("MAP");
        double s=0.0;
        double n=0;
        for(File run:fusionRuns)
        {
            s+=eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\MAP_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);

            n++;

        }

        //System.out.println(n);
        return  s/n;//返回平均MAP值。

    }
    public static void output_ALL(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-ALL"};
        //-P
        //-Rprec
        //-RR
        //-NDCG

        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        //System.out.println("ALL");
        for(File run:fusionRuns)
        {
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\ALL_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);

        }

    }
    public static void output_AP(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-AP"};
        //-P
        //-Rprec
        //-RR
        //-NDCG

        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        System.out.println("AP");
        for(File run:fusionRuns)
        {
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\AP_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
        }

    }
    public static void output_ERR(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-ERR"};
        //-P
        //-Rprec
        //-RR
        //-NDCG

        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        for(File run:fusionRuns)
        {
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\ERR_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
        }

    }
    /*
     * p10 p10
     */
    public static void output_PX(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {

        String[] args= new String[]{"-P"};
        //-P
        //-Rprec
        //-RR
        //-NDCG

        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        System.out.println("P");
        for(File run:fusionRuns)
        {
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\Px_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
        }

    }

    public static void output_RP(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-Rprec"};
        //-P
        //-Rprec
        //-RR
        //-NDCG
        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        System.out.println("RP");
        for(File run:fusionRuns)
        {
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\RP_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
        }


    }
    public static void output_RR(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {

        String[] args= new String[]{"-RR"};
        //-P
        //-Rprec
        //-RR
        //-NDCG
        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        for(File run:fusionRuns)
        {
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\RR_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
        }
    }
    public static void output_U_NDCG(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-NDCG"};
        //-P
        //-Rprec
        //-RR
        //-NDCG
        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        System.out.println("NDCG");
        for(File run:fusionRuns)
        {
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\NDCG_U_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
        }
    }
    public static void output_UC_NDCG(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-NDCG"};
        //-P
        //-Rprec
        //-RR
        //-NDCG
        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        for(File run:fusionRuns)
        {
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\NDCG_UC_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
        }
    }
    public static void output_U_C_NDCG(String qrel,String fusionResult,String fusionTrecEvaloutput,String eval_filename) throws Exception {
        String[] args= new String[]{"-NDCG"};
        //-P
        //-Rprec
        //-RR
        //-NDCG
        TrecEval eval = new TrecEval();
        File file = new File(fusionResult);
        File[] fusionRuns = file.listFiles();
        for(File run:fusionRuns)
        {
            eval.trec_eval1(qrel, run, fusionTrecEvaloutput+"\\NDCG_U_C_"+eval_filename+"_"+run.getName().toString().replaceAll(".txt", ""), args);
        }
    }
    private static void createDirectory(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()){
            file.mkdirs();
        }
    }
    public static double main(String[] args) throws Exception {
        /*
         * 2020health
         */
        //String U_qrel = "D:\\数据集\\2020health\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful";//useful qrel file is U	"CAM" use U UC U_C
        //String UC_qrel = "D:\\数据集\\2020health\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful-correct";//useful and correct  qrel file is UC 	"CAM" use U UC U_C
        //String U_C_qrel = "D:\\数据集\\2020health\\trec-misinfo-resources\\qrels\\2020-derived-qrels\\misinfo-qrels-binary.useful-credible";//useful and credible  qrel file is U_C	"CAM" use U UC U_C

        /*
         * 2019decision
         */
        //String U_qrel = "D:\\TREC数据集文件\\2019decision\\qrel\\file-containing-raw-U__-judgments";//useful qrel file is U	"CAM" use U UC U_C
        //String UC_qrel = "D:\\TREC数据集文件\\2019decision\\qrel\\file-containing-raw-_C_-judgments";//useful and correct  qrel file is UC 	"CAM" use U UC U_C
        //String U_C_qrel = "D:\\TREC数据集文件\\2019decision\\qrel\\file-containing-raw-__C-judgments";//useful and credible  qrel file is U_C	"CAM" use U UC U_C

		/*
		 2014MB
		 */
        //String U_qrel = "D:\\TREC数据集文件\\methode_qrel012\\BigExperiment\\2014MB\\Judgmentfile\\relevance judgments";
		/*
		 2015MB
		 */
        //String U_qrel = "D:\\TREC数据集文件\\2015Microblog\\Judgment_file\\Judgment_file012";
		/*
		 2013MB
		 */
        //String U_qrel = "E:\\TRECDataset\\methode_qrel012\\WISA\\2013Microblog\\Judgmentfile\\Judgment_file012";
		/*
		 2012MB
		 */
        //String U_qrel = "D:\\TREC数据集文件\\methode_qrel012\\BigExperiment\\2012MB\\Judgmentfile\\Judgment_file012";
		/*
		 2011MB
		 */
        //String U_qrel = "D:\\TREC数据集文件\\methode_qrel012\\BigExperiment\\2011MB\\Judgmentfile\\Judgment_file012";

        /*
         * 2014session
         * */
        //String U_qrel = "D:\\TREC数据集文件\\methode_qrelNegative\\2014Session\\Judgmentfile\\SessionJudgmentFile_201234";
        //String eval_filename = "2014Session";
        /*
         * 2018PrecisionLiterature
         * */
        //String U_qrel = "E:\\TRECDataset\\Semi-SupervisedMethodExperiment\\2018PrecisionLiterature\\judgmentFile\\judgmentfile_01";
        //String eval_filename = "2018PrecisionLiterature";
        /*
         * 2019PrecisionLiterature
         * */
        //String U_qrel = "E:\\TRECDataset\\Semi-SupervisedMethodExperiment\\2019PrecisionLiterature\\judgmentFile\\judgmentfile_01";
        //String eval_filename = "2019PrecisionLiterature";
        /*
         * 2020DeepLearning_Passage
         * */
        String U_qrel = "E:\\TREC 数据集\\2017MedicineTrackScientific\\judgement\\judgmentfile_01";
        String eval_filename = "2017MedicineTrackScientific";
        /*
         * 2021DeepLearning_Document
         * */
        //String U_qrel = "E:\\TRECDataset\\Semi-SupervisedMethodExperiment\\2021DeepLearning_Document\\judgmentfile\\judgmentfile_01";
        //String eval_filename = "2021DeepLearning_Document";
        //String U_qrel = "D:\\TREC数据集文件\\diversification_1_TREC\\TREC2009\\qrel\\qrels-Multi_rel";
        int StartNum =2;
        int EndNum =2;
        double result=0.0;
        for(int select_num = StartNum ;select_num <= EndNum;select_num++) {
            //for(int dateEnd = 0 ; dateEnd <=9;dateEnd++) {
            //String date = "2015072"+dateEnd;
            //String fusionName = "fusion"+22;
            //String jiou = "ou";
            //String fusionResult = "D:\\TREC数据集文件\\2015Microblog\\Microblog_TaskB\\split_standard_input\\"+date+"\\";
            //System.err.println(select_num);
            //String fusionResult = "F:\\TREC 数据集\\2019MedicineTrackScientific\\fusion\\Vfusion\\"+select_num+"\\";//这里
            //String fusionResult = "E:\\TREC 数据集\\2022DeepLearningPassageRanking\\fusionTest\\fusion\\"+select_num+"\\";//该语句针对2_10组融合
            //String fusionResult = "E:\\TREC 数据集\\2017MedicineTrackScientific\\ACombSUM";
            //String fusionResult = "E:\\TREC 数据集\\2022DeepLearningPassageRanking\\BCombMNZ";
            //String fusionResult = "E:\\TREC 数据集\\2018MedicineTrackScientific\\Borda\\";
            String fusionResult = "E:\\TREC 数据集\\2017MedicineTrackScientific\\standard-input-nor30-60\\";
            //String MAP_outputpath= "D:\\TREC数据集文件\\2013Microblog\\adhoc\\"+fusionName+"\\eval_"+jiou+"\\MAP";
            //output_MAP(U_qrel, fusionResult, MAP_outputpath,eval_filename);

            String ALL_outputpath= "E:\\TREC 数据集\\2017MedicineTrackScientific\\fusionTest\\eval\\";
            createDirectory(ALL_outputpath);
            //output_ALL(U_qrel, fusionResult, ALL_outputpath,eval_filename);
           result= output_MAP(U_qrel, fusionResult, ALL_outputpath,eval_filename);

            //String AP_outputpath= "D:\\TREC数据集文件\\2013Microblog\\adhoc\\"+fusionName+"\\eval_"+ iou+"\\AP";
            //output_AP(U_qrel, fusionResult, AP_outputpath,eval_filename);

            //String ERR_outputpath = "D:\\TREC数据集文件\\diversification_1_TREC\\TREC2009\\eval\\raw_inputeval\\ERR";
            //output_ERR(U_qrel, fusionResult, ERR_outputpath, eval_filename);

            //String Px_outputpath= "D:\\TREC数据集文件\\2013Microblog\\adhoc\\"+fusionName+"\\eval_"+jiou+"\\P10";
            //output_PX(U_qrel, fusionResult, Px_outputpath,eval_filename);

            //String RP_outputpath= "D:\\TREC数据集文件\\2013Microblog\\adhoc\\"+fusionName+"\\eval_"+jiou+"\\RP";
            //output_RP(U_qrel, fusionResult, RP_outputpath,eval_filename);

            //String RR_outputpath= "D:\\TREC数据集文件\\new_idea\\2020Health\\classification_fusion\\"+select_num+"\\eval\\RR";
            //output_RR(U_qrel, fusionResult, RR_outputpath,eval_filename);

            //String U_NDCG_outputpath= "D:\\TREC数据集文件\\2013Microblog\\adhoc\\"+fusionName+"\\eval_"+jiou+"\\NDCG10";
            //output_U_NDCG(U_qrel, fusionResult, U_NDCG_outputpath,eval_filename);

//				String UC_NDCG_outputpath= "D:\\数据集\\2020health\\correct_fusion\\fusion15\\eval\\UC_NDCG";
//				output_UC_NDCG(UC_qrel, fusionResult, UC_NDCG_outputpath,eval_filename);

//				String U_C_NDCG_outputpath= "D:\\数据集\\2020health\\correct_fusion\\fusion15\\eval\\U_CNDCG";
//				output_U_C_NDCG(U_C_qrel, fusionResult, U_C_NDCG_outputpath,eval_filename);

            //System.out.println("END");
            //}

        }
        System.out.println(result);
        return result;

    }
}
