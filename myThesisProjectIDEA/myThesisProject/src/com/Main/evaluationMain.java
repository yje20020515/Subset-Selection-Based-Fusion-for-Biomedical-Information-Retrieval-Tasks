//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.Main;

import com.evaluation.TrecEval;
import java.io.File;

public class evaluationMain {
	public evaluationMain() {
	}

	public static void output_MAP(String qrel, String fusionResult, String fusionTrecEvaloutput, String eval_filename) throws Exception {
		String[] args = new String[]{"-MAP"};
		TrecEval eval = new TrecEval();
		File file = new File(fusionResult);
		File[] fusionRuns = file.listFiles();
		System.out.println("MAP");
		File[] var8 = fusionRuns;
		int var9 = fusionRuns.length;

		for(int var10 = 0; var10 < var9; ++var10) {
			File run = var8[var10];
			eval.trec_eval1(qrel, run, fusionTrecEvaloutput + "\\MAP_U_" + eval_filename + "_" + run.getName().toString().replaceAll(".txt", ""), args);
		}

	}

	public static void output_ALL(String qrel, String fusionResult, String fusionTrecEvaloutput, String eval_filename) throws Exception {
		String[] args = new String[]{"-ALL"};
		TrecEval eval = new TrecEval();
		File file = new File(fusionResult);
		File[] fusionRuns = file.listFiles();
		File[] var8 = fusionRuns;
		int var9 = fusionRuns.length;

		for(int var10 = 0; var10 < var9; ++var10) {
			File run = var8[var10];
			eval.trec_eval1(qrel, run, fusionTrecEvaloutput + "\\ALL_U_" + eval_filename + "_" + run.getName().toString().replaceAll(".txt", ""), args);
		}

	}

	public static void output_AP(String qrel, String fusionResult, String fusionTrecEvaloutput, String eval_filename) throws Exception {
		String[] args = new String[]{"-AP"};
		TrecEval eval = new TrecEval();
		File file = new File(fusionResult);
		File[] fusionRuns = file.listFiles();
		System.out.println("AP");
		File[] var8 = fusionRuns;
		int var9 = fusionRuns.length;

		for(int var10 = 0; var10 < var9; ++var10) {
			File run = var8[var10];
			eval.trec_eval1(qrel, run, fusionTrecEvaloutput + "\\AP_U_" + eval_filename + "_" + run.getName().toString().replaceAll(".txt", ""), args);
		}

	}

	public static void output_ERR(String qrel, String fusionResult, String fusionTrecEvaloutput, String eval_filename) throws Exception {
		String[] args = new String[]{"-ERR"};
		TrecEval eval = new TrecEval();
		File file = new File(fusionResult);
		File[] fusionRuns = file.listFiles();
		File[] var8 = fusionRuns;
		int var9 = fusionRuns.length;

		for(int var10 = 0; var10 < var9; ++var10) {
			File run = var8[var10];
			eval.trec_eval1(qrel, run, fusionTrecEvaloutput + "\\ERR_U_" + eval_filename + "_" + run.getName().toString().replaceAll(".txt", ""), args);
		}

	}

	public static void output_PX(String qrel, String fusionResult, String fusionTrecEvaloutput, String eval_filename) throws Exception {
		String[] args = new String[]{"-P"};
		TrecEval eval = new TrecEval();
		File file = new File(fusionResult);
		File[] fusionRuns = file.listFiles();
		System.out.println("P");
		File[] var8 = fusionRuns;
		int var9 = fusionRuns.length;

		for(int var10 = 0; var10 < var9; ++var10) {
			File run = var8[var10];
			eval.trec_eval1(qrel, run, fusionTrecEvaloutput + "\\Px_U_" + eval_filename + "_" + run.getName().toString().replaceAll(".txt", ""), args);
		}

	}

	public static void output_RP(String qrel, String fusionResult, String fusionTrecEvaloutput, String eval_filename) throws Exception {
		String[] args = new String[]{"-Rprec"};
		TrecEval eval = new TrecEval();
		File file = new File(fusionResult);
		File[] fusionRuns = file.listFiles();
		System.out.println("RP");
		File[] var8 = fusionRuns;
		int var9 = fusionRuns.length;

		for(int var10 = 0; var10 < var9; ++var10) {
			File run = var8[var10];
			eval.trec_eval1(qrel, run, fusionTrecEvaloutput + "\\RP_U_" + eval_filename + "_" + run.getName().toString().replaceAll(".txt", ""), args);
		}

	}

	public static void output_RR(String qrel, String fusionResult, String fusionTrecEvaloutput, String eval_filename) throws Exception {
		String[] args = new String[]{"-RR"};
		TrecEval eval = new TrecEval();
		File file = new File(fusionResult);
		File[] fusionRuns = file.listFiles();
		File[] var8 = fusionRuns;
		int var9 = fusionRuns.length;

		for(int var10 = 0; var10 < var9; ++var10) {
			File run = var8[var10];
			eval.trec_eval1(qrel, run, fusionTrecEvaloutput + "\\RR_U_" + eval_filename + "_" + run.getName().toString().replaceAll(".txt", ""), args);
		}

	}

	public static void output_U_NDCG(String qrel, String fusionResult, String fusionTrecEvaloutput, String eval_filename) throws Exception {
		String[] args = new String[]{"-NDCG"};
		TrecEval eval = new TrecEval();
		File file = new File(fusionResult);
		File[] fusionRuns = file.listFiles();
		System.out.println("NDCG");
		File[] var8 = fusionRuns;
		int var9 = fusionRuns.length;

		for(int var10 = 0; var10 < var9; ++var10) {
			File run = var8[var10];
			eval.trec_eval1(qrel, run, fusionTrecEvaloutput + "\\NDCG_U_" + eval_filename + "_" + run.getName().toString().replaceAll(".txt", ""), args);
		}

	}

	public static void output_UC_NDCG(String qrel, String fusionResult, String fusionTrecEvaloutput, String eval_filename) throws Exception {
		String[] args = new String[]{"-NDCG"};
		TrecEval eval = new TrecEval();
		File file = new File(fusionResult);
		File[] fusionRuns = file.listFiles();
		File[] var8 = fusionRuns;
		int var9 = fusionRuns.length;

		for(int var10 = 0; var10 < var9; ++var10) {
			File run = var8[var10];
			eval.trec_eval1(qrel, run, fusionTrecEvaloutput + "\\NDCG_UC_" + eval_filename + "_" + run.getName().toString().replaceAll(".txt", ""), args);
		}

	}

	public static void output_U_C_NDCG(String qrel, String fusionResult, String fusionTrecEvaloutput, String eval_filename) throws Exception {
		String[] args = new String[]{"-NDCG"};
		TrecEval eval = new TrecEval();
		File file = new File(fusionResult);
		File[] fusionRuns = file.listFiles();
		File[] var8 = fusionRuns;
		int var9 = fusionRuns.length;

		for(int var10 = 0; var10 < var9; ++var10) {
			File run = var8[var10];
			eval.trec_eval1(qrel, run, fusionTrecEvaloutput + "\\NDCG_U_C_" + eval_filename + "_" + run.getName().toString().replaceAll(".txt", ""), args);
		}

	}

	private static void createDirectory(String outputPath) {
		File file = new File(outputPath);
		if (!file.exists()) {
			file.mkdirs();
		}

	}

	public static void main(String[] args) throws Exception {
		String U_qrel = "F:\\TREC 数据集\\2019MedicineTrackScientific\\judgement\\judgmentfile_01";
		String eval_filename = "2019MedicineTrackScientific";
		int StartNum = 2;
		int EndNum = 14;

		for(int select_num = StartNum; select_num <= EndNum; ++select_num) {
			//String fusionResult = "F:\\TREC 数据集\\2019MedicineTrackScientific\\50%fusion\\fusion\\" + select_num + "\\";
			String fusionResult = "F:\\TREC 数据集\\2019MedicineTrackScientific\\fusionTest\\fusion\\"+select_num+"\\";
			String ALL_outputpath = "F:\\TREC 数据集\\2019MedicineTrackScientific\\fusionTest\\eval\\fenQ\\";
			createDirectory(ALL_outputpath);
			output_ALL(U_qrel, fusionResult, ALL_outputpath, eval_filename);
		}

	}
}
