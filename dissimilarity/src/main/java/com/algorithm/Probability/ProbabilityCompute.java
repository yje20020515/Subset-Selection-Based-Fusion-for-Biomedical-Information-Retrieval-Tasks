package com.algorithm.Probability;

import java.io.*;
import java.util.*;
/**
 * 现在用于分析相关性概率,没有考虑子主题情况 ######Slidefuse
 */
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
public class ProbabilityCompute {

    public static HashMap<String, String> MapQ = new HashMap<String, String>();

    /**
     * 把input_subquery文件存入Map_rel中
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

        // 把input_subquery文件存入Map_rel中
        String preQueryId = null;
        fileReader = new FileReader(input);
        buffReader = new BufferedReader(fileReader);
        while ((tempLine = buffReader.readLine()) != null) {
            terms = tempLine.split(" |\t");
            // 若preQueryId为空,给preQueryId赋值,建立array_rel
            if (preQueryId == null) {
                preQueryId = terms[0];
                array_rel = new ArrayList<Rel>();
            }
            // 若preQueryId和terms[0]相同,则把tempLine信息存入array_rel中
            if (preQueryId.equals(terms[0])) {
                rel = new Rel(tempLine);
                array_rel.add(rel);
            }
            // 若preQueryId和terms[0]不相同时,把preQueryId对应的array_rel存入Map_rel中,
            if (!preQueryId.equals(terms[0])) {
                Map_rel.put(runId + "\t" + preQueryId, array_rel);
                // 更新preQueryId,建立array_rel,并分析tempLine信息
                preQueryId = terms[0];
                array_rel = new ArrayList<Rel>();
                rel = new Rel(tempLine);
                array_rel.add(rel);
            }
        }
        // 最后的preQueryId对应的array_rel还没有存入Map_rel中
        Map_rel.put(runId + "\t" + preQueryId, array_rel);
        buffReader.close();
        return Map_rel;
    }

    /**
     *
     * 把2009年的2009adhoc60文件夹下的文件存入map_rel中
     *
     * @throws IOException
     */
    public static void process_inputSubquery() throws IOException {
        // String input = null;
        // input = "2018top20\\input.ECNU_S_Run1";
        // store_inputSubquery("input.ECNU_S_Run1", input);
        String date = "2015072"+9;
        File inputpath = new File("D:\\TRECDateset\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\standard_input_ou\\");// input文件夹修改
        String outputpath = "D:\\TRECDateset\\methode_qrel012\\BigExperiment\\2011MB\\Adhoc\\ProbabilityfileSlide_ou\\";
        String qinput = "D:\\TRECDateset\\methode_qrel012\\BigExperiment\\2011MB\\Judgmentfile\\Judgment_file012";
        int k = 3;//更改输出结果文件夹,posfuse时k为1，slidefuse时k为3
        File[] files = inputpath.listFiles();
        for (File runs : files) {
            // System.out.println(runs.toString().split("\\.")[1]);
            System.out.println("runs:" + runs.toString().split("\\\\")[runs.toString().split("\\\\").length-1]);

            HashMap<String, ArrayList<Rel>> Map_rel = new HashMap<String, ArrayList<Rel>>();
            Map_rel = store_inputSubquery(runs.toString(), runs.toString(), Map_rel);
            // 把qrels文件存入MapQ中
            process_qrel(qinput);

            // 根据MapQ给Map_rel中rel对象的relevant赋值
            Map_rel = completeMap_rel(Map_rel);

            // 计算结果列表第i篇文档为相关文档的概率
            generate_probability(
                    outputpath
                            + runs.toString().split("\\\\")[runs.toString().split("\\\\").length-1], Map_rel,k);// 更改输出结果文件夹,posfuse时k为1，slidefuse时k为3

            System.out.println("概率计算,已完成!");
        }
    }

    /**
     * 把qrels文件存入MapQ中,MapQ中键值对的格式为(queryId=\tdocId= relevant=)。
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
            // 若relevant<=0时,忽略这一行信息
            if (Double.parseDouble(terms[3])<= 0.0)
                continue;

            double relevant = Double.parseDouble(terms[3]);
            // 若relevant<0,把relevant设为0,若relevant>0,则把relevant设为1
            if (relevant < 0.0)
                relevant = 0.0;
            if (relevant == 1.0)
                relevant = 1.0;
            if (relevant == 2.0)
                relevant = 2.0;
            mapKey = "queryId=" + terms[0] + "\tdocId=" + terms[2];
            mapValue = "relevant=" + String.valueOf(relevant);
            // mapKey,mapValue存入MapQ中
            MapQ.put(mapKey, mapValue);
        }
        buffReader.close();
    }

    /**
     * 把2009-2012年的qrels文件存入MapQ中
     *
     * @throws IOException
     */
    public static void process_qrel(String input) throws IOException {
        //String input = null;
        // 把2009年的qrels文件存入MapQ中
        // input = "./previousData/qrels-for-adhoc.2009.txt";
        //input = "E:\\shiyan\\2018\\qrels.txt";// 2017年
        store_qrel(input);

    }

    /**
     * 根据MapQ给Map_rel中rel对象的relevant赋值
     */
    public static HashMap<String, ArrayList<Rel>> completeMap_rel(HashMap<String, ArrayList<Rel>> Map_rel) {
        Set<Map.Entry<String, ArrayList<Rel>>> set = null;
        Iterator<Map.Entry<String, ArrayList<Rel>>> it = null;
        Map.Entry<String, ArrayList<Rel>> entry = null;
        ArrayList<Rel> array_rel = null;
        String queryId = null;

        // 遍历Map_rel,根据MapQ给Map_rel中rel对象的relevant赋值
        String mapKey = null;
        String mapValue = null;
        Rel rel = null;
        set = Map_rel.entrySet();
        it = set.iterator();
        while (it.hasNext()) {
            entry = it.next();
            // key的格式为runId\tqueryId
            queryId = entry.getKey().split("\t")[1];
            array_rel = entry.getValue();
            // 遍历array_rel
            for (int i = 0; i < array_rel.size(); i++) {
                rel = array_rel.get(i);
                mapKey = "queryId=" + queryId + "\tdocId=" + rel.getDocId();
                mapValue = MapQ.get(mapKey);
                if (mapValue != null) {
                    rel.setRelevant(Double.parseDouble(mapValue.split("=")[1]));
                }
            }
        }
        System.out.println("根据MapQ给Map_rel中rel对象的relevant赋值,已完成..");
        return Map_rel;

    }

    /**
     * 分析Map_rel中的信息。某查询对应的检索结果列表, 对于位置i处文档,计算此文档为相关文档的概率。
     *
     * @throws IOException
     */
    public static void generate_probability(String input, HashMap<String, ArrayList<Rel>> Map_rel, int k) throws IOException {
        Set<Map.Entry<String, ArrayList<Rel>>> set = null;
        Iterator<Map.Entry<String, ArrayList<Rel>>> it = null;
        Map.Entry<String, ArrayList<Rel>> entry = null;
        ArrayList<Rel> array_rel = null;

        int k_analy = 1000;// 分析检索结果的前500篇文档
        double[] prob = new double[k_analy];// 计算某位置处文档为相关的概率
        double sum_rel = 0;// 临时存储某位置处的文档为相关文档的分数，
        int sum_doc = 0;// 临时存储某位置处文档的总数
        //
        for (int i = 0; i < k_analy; i++) {
            set = Map_rel.entrySet();
            it = set.iterator();
            while (it.hasNext()) {
                entry = it.next();
                array_rel = entry.getValue();
                // 若array_rel包含第i项
                if (array_rel.size() > i) {
                    sum_rel = sum_rel + array_rel.get(i).getRelevant();
                    sum_doc++;
                }
            }
            // 若sum_doc>0
            // System.out.println(sum_rel);
            if (sum_doc > 0)
                prob[i] = Double.valueOf(sum_rel) / sum_doc;
            // 把sum_rel,sum_doc置为0
            //System.out.println(sum_rel+"\t"+sum_doc);
            sum_rel = 0;
            sum_doc = 0;
        }
        // 把prob数组输出到文件中
        FileWriter fileWriter = null;
        BufferedWriter buffWriter = null;
        String tempLine = null;

        fileWriter = new FileWriter(input);
        buffWriter = new BufferedWriter(fileWriter);
        // tempLine = "rel_prob\t1.0/(60+rank)\n";
        tempLine = "rel_prob\n";
        buffWriter.write(tempLine);
        prob = k_window(k, prob);// 利用窗体对prob数组进行修改,k为1时就是posfuse
        for (int i = 0; i < prob.length; i++) {
            // tempLine = String.format("%.6f", prob[i]) + "\t" +
            // String.format("%.6f", 1.0 / (60 + i + 1)) + "\n";
            tempLine = String.format("%.6f", prob[i]) + "\n";
            buffWriter.write(tempLine);
        }
        buffWriter.close();

        System.out.println("计算结果列表第i篇文档为相关文档的概率,已完成..");
    }

    // k只能为奇数3,5,7...
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
        // 把input_subquery文件存入Map_rel中
        process_inputSubquery();

    }

}
