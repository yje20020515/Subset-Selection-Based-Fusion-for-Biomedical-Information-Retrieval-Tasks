package com.Probability;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 * �������ڷ�������Ը���,û�п������������ ######Slidefuse
 *
 * @author xuchunlin
 *
 */
public class ProbabilityCompute {

	public static HashMap<String, String> MapQ = new HashMap<String, String>();

	/**
	 * ��input_subquery�ļ�����Map_rel��
	 *
	 * @throws IOException
	 *
	 */
	public static HashMap<String, ArrayList<Rel>> store_inputSubquery(String runId, String input,
																	  HashMap<String, ArrayList<Rel>> Map_rel) throws IOException {
		ArrayList<Rel> array_rel = null;
		Rel rel = null;
		FileReader fileReader = null;
		BufferedReader buffReader = null;
		String tempLine = null;
		String[] terms = null;

		// ��input_subquery�ļ�����Map_rel��
		String preQueryId = null;
		fileReader = new FileReader(input);
		buffReader = new BufferedReader(fileReader);
		while ((tempLine = buffReader.readLine()) != null) {
			terms = tempLine.split(" |\t");
			// ��preQueryIdΪ��,��preQueryId��ֵ,����array_rel
			if (preQueryId == null) {
				preQueryId = terms[0];
				array_rel = new ArrayList<Rel>();
			}
			// ��preQueryId��terms[0]��ͬ,���tempLine��Ϣ����array_rel��
			if (preQueryId.equals(terms[0])) {
				rel = new Rel(tempLine);
				array_rel.add(rel);
			}
			// ��preQueryId��terms[0]����ͬʱ,��preQueryId��Ӧ��array_rel����Map_rel��,
			if (!preQueryId.equals(terms[0])) {
				Map_rel.put(runId + "\t" + preQueryId, array_rel);
				// ����preQueryId,����array_rel,������tempLine��Ϣ
				preQueryId = terms[0];
				array_rel = new ArrayList<Rel>();
				rel = new Rel(tempLine);
				array_rel.add(rel);
			}
		}
		// ����preQueryId��Ӧ��array_rel��û�д���Map_rel��
		Map_rel.put(runId + "\t" + preQueryId, array_rel);
		buffReader.close();
		return Map_rel;
	}

	/**
	 *
	 * ��2009���2009adhoc60�ļ����µ��ļ�����map_rel��
	 *
	 * @throws IOException
	 */
	public static void process_inputSubquery() throws IOException {
		// String input = null;
		// input = "2018top20\\input.ECNU_S_Run1";
		// store_inputSubquery("input.ECNU_S_Run1", input);
		String date = "2015072"+9;
		File inputpath = new File("F:\\TREC ���ݼ�\\2020deeplearning document\\standard-input-nor30-6-ji\\");// input�ļ����޸�
		String outputpath = "F:\\TREC ���ݼ�\\2020deeplearning document\\Deep Learning document\\posfuse\\ProbabilityfileSlide_ji\\";
		String qinput = "F:\\TREC ���ݼ�\\2020deeplearning document\\judgement\\Judgmentfile1";
		int k = 1;//�����������ļ���,posfuseʱkΪ1��slidefuseʱkΪ3
		File[] files = inputpath.listFiles();
		for (File runs : files) {
			// System.out.println(runs.toString().split("\\.")[1]);
			System.out.println("runs:" + runs.toString().split("\\\\")[runs.toString().split("\\\\").length-1]);

			HashMap<String, ArrayList<Rel>> Map_rel = new HashMap<String, ArrayList<Rel>>();
			Map_rel = store_inputSubquery(runs.toString(), runs.toString(), Map_rel);
			// ��qrels�ļ�����MapQ��
			process_qrel(qinput);

			// ����MapQ��Map_rel��rel�����relevant��ֵ
			Map_rel = completeMap_rel(Map_rel);

			// �������б��iƪ�ĵ�Ϊ����ĵ��ĸ���
			generate_probability(
					outputpath
							+ runs.toString().split("\\\\")[runs.toString().split("\\\\").length-1], Map_rel,k);// �����������ļ���,posfuseʱkΪ1��slidefuseʱkΪ3

			System.out.println("���ʼ���,�����!");
		}
	}

	/**
	 * ��qrels�ļ�����MapQ��,MapQ�м�ֵ�Եĸ�ʽΪ(queryId=\tdocId= relevant=)��
	 *
	 * @param input
	 * @throws IOException
	 */
	public static void store_qrel(String input) throws IOException {
		FileReader fileReader = null;
		BufferedReader buffReader = null;
		String tempLine = null;
		String[] terms = null;
		String mapKey = null;
		String mapValue = null;

		fileReader = new FileReader(input);
		buffReader = new BufferedReader(fileReader);
		while ((tempLine = buffReader.readLine()) != null) {
			terms = tempLine.split(" |\t");
			// ��relevant<=0ʱ,������һ����Ϣ
			if (Double.parseDouble(terms[3])<= 0.0)
				continue;

			double relevant = Double.parseDouble(terms[3]);
			// ��relevant<0,��relevant��Ϊ0,��relevant>0,���relevant��Ϊ1
			if (relevant < 0.0)
				relevant = 0.0;
			if (relevant == 1.0)
				relevant = 1.0;
			if (relevant == 2.0)
				relevant = 2.0;
			mapKey = "queryId=" + terms[0] + "\tdocId=" + terms[2];
			mapValue = "relevant=" + String.valueOf(relevant);
			// mapKey,mapValue����MapQ��
			MapQ.put(mapKey, mapValue);
		}
		buffReader.close();
	}

	/**
	 * ��2009-2012���qrels�ļ�����MapQ��
	 *
	 * @throws IOException
	 */
	public static void process_qrel(String input) throws IOException {
		//String input = null;
		// ��2009���qrels�ļ�����MapQ��
		// input = "./previousData/qrels-for-adhoc.2009.txt";
		//input = "E:\\shiyan\\2018\\qrels.txt";// 2017��
		store_qrel(input);

	}

	/**
	 * ����MapQ��Map_rel��rel�����relevant��ֵ
	 */
	public static HashMap<String, ArrayList<Rel>> completeMap_rel(HashMap<String, ArrayList<Rel>> Map_rel) {
		Set<Entry<String, ArrayList<Rel>>> set = null;
		Iterator<Entry<String, ArrayList<Rel>>> it = null;
		Entry<String, ArrayList<Rel>> entry = null;
		ArrayList<Rel> array_rel = null;
		String queryId = null;

		// ����Map_rel,����MapQ��Map_rel��rel�����relevant��ֵ
		String mapKey = null;
		String mapValue = null;
		Rel rel = null;
		set = Map_rel.entrySet();
		it = set.iterator();
		while (it.hasNext()) {
			entry = it.next();
			// key�ĸ�ʽΪrunId\tqueryId
			queryId = entry.getKey().split("\t")[1];
			array_rel = entry.getValue();
			// ����array_rel
			for (int i = 0; i < array_rel.size(); i++) {
				rel = array_rel.get(i);
				mapKey = "queryId=" + queryId + "\tdocId=" + rel.getDocId();
				mapValue = MapQ.get(mapKey);
				if (mapValue != null) {
					rel.setRelevant(Double.parseDouble(mapValue.split("=")[1]));
				}
			}
		}
		System.out.println("����MapQ��Map_rel��rel�����relevant��ֵ,�����..");
		return Map_rel;

	}

	/**
	 * ����Map_rel�е���Ϣ��ĳ��ѯ��Ӧ�ļ�������б�, ����λ��i���ĵ�,������ĵ�Ϊ����ĵ��ĸ��ʡ�
	 *
	 * @throws IOException
	 */
	public static void generate_probability(String input, HashMap<String, ArrayList<Rel>> Map_rel, int k) throws IOException {
		Set<Entry<String, ArrayList<Rel>>> set = null;
		Iterator<Entry<String, ArrayList<Rel>>> it = null;
		Entry<String, ArrayList<Rel>> entry = null;
		ArrayList<Rel> array_rel = null;

		int k_analy = 1000;// �������������ǰ500ƪ�ĵ�
		double[] prob = new double[k_analy];// ����ĳλ�ô��ĵ�Ϊ��صĸ���
		double sum_rel = 0;// ��ʱ�洢ĳλ�ô����ĵ�Ϊ����ĵ��ķ�����
		int sum_doc = 0;// ��ʱ�洢ĳλ�ô��ĵ�������
		//
		for (int i = 0; i < k_analy; i++) {
			set = Map_rel.entrySet();
			it = set.iterator();
			while (it.hasNext()) {
				entry = it.next();
				array_rel = entry.getValue();
				// ��array_rel������i��
				if (array_rel.size() > i) {
					sum_rel = sum_rel + array_rel.get(i).getRelevant();
					sum_doc++;
				}
			}
			// ��sum_doc>0
			// System.out.println(sum_rel);
			if (sum_doc > 0)
				prob[i] = Double.valueOf(sum_rel) / sum_doc;
			// ��sum_rel,sum_doc��Ϊ0
			//System.out.println(sum_rel+"\t"+sum_doc);
			sum_rel = 0;
			sum_doc = 0;
		}
		// ��prob����������ļ���
		FileWriter fileWriter = null;
		BufferedWriter buffWriter = null;
		String tempLine = null;

		fileWriter = new FileWriter(input);
		buffWriter = new BufferedWriter(fileWriter);
		// tempLine = "rel_prob\t1.0/(60+rank)\n";
		tempLine = "rel_prob\n";
		buffWriter.write(tempLine);
		prob = k_window(k, prob);// ���ô����prob��������޸�,kΪ1ʱ����posfuse
		for (int i = 0; i < prob.length; i++) {
			// tempLine = String.format("%.6f", prob[i]) + "\t" +
			// String.format("%.6f", 1.0 / (60 + i + 1)) + "\n";
			tempLine = String.format("%.6f", prob[i]) + "\n";
			buffWriter.write(tempLine);
		}
		buffWriter.close();

		System.out.println("�������б��iƪ�ĵ�Ϊ����ĵ��ĸ���,�����..");
	}

	// kֻ��Ϊ����3,5,7...
	public static double[] k_window(int k, double[] temp) {
		double[] fin = new double[1000];
		int count = 0;
		double temp_a = 0.0;
		for (int i = 0; i < temp.length; i++) {
			for (int j = i - (k - 1) / 2; j <= i + (k - 1) / 2; j++) {
				// System.out.print(" "+j+" ");
				if (j < 0 || j >= temp.length) {
					continue;
				}
				temp_a += temp[j];
				count++;

			}
			fin[i] = temp_a / count;
			// System.out.println(" "+count+" "+fin[i]+" "+i);
			count = 0;
			temp_a = 0.0;
		}
		return fin;
	}

	/**
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// ��input_subquery�ļ�����Map_rel��
		process_inputSubquery();

	}

}

class Rel {
	String docId;
	double relevant;

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public double getRelevant() {
		return relevant;
	}

	public void setRelevant(double relevant) {
		this.relevant = relevant;
	}

	public Rel(String tempLine) {
		String[] terms = null;
		terms = tempLine.split(" |\t");
		docId = terms[2];
		relevant = 0;
	}

}
