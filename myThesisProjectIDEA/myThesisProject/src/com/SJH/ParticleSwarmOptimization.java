package com.SJH;

import javax.xml.transform.sax.SAXSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ParticleSwarmOptimization {

    private static final int NUM_MEMBERS = 10; // 成员系统数量
    private static final int NUM_TOPICS = 20; // 主题数量
    private static final int NUM_ITERATIONS = 15; // 迭代次数
    private static final int NUM_PARTICLES = 30; // 粒子数量
    private static final double INERTIA_WEIGHT = 0.39; // 惯性权重
    private static final double COGNITIVE_WEIGHT = 0.89; // 认知因子权重
    private static final double SOCIAL_WEIGHT = 0.83; // 社会因子权重
    private static final double RANDOM_WEIGHT_LOWER_BOUND = -0.1; // 随机权重下界
    private static final double RANDOM_WEIGHT_UPPER_BOUND = 0.2; // 随机权重上界

    // 数据结构存储成员系统信息
    private static Map<String, List<Document>> memberSystems = new HashMap<>();

    public static void main(String[] args) {
        String[] memberS = {"CSIROmed_strRR", "baseline", "rrf", "sibtm_run1", "duoT5", "nnrun1", "r1st", "bm25", "uog_ufmg_sb_df5", "monoT5",};
        int[] topic={
                1,2,3,4,5,6,7,8,9,10,
                11,12,13,14,15,16,17,18,19,20,
                21,22,23,24,25,26,27,28,29,30,
                31,32,33,34,35,36,37,38,39,40,
        };
        int[] ji={
                1,3,
                5,7,
                9,11,
                13, 15,
                17, 19,
                21, 23,
                25, 27,
                29, 31,
                33, 35,
                37, 39,
        };
        int[] ou={
                2, 4,
                6, 8,
                10,12,
                14, 16,
                18, 20,
                22, 24,
                26, 28,
                30, 32,
                34, 36,
                38, 40,
        };
        // 读取成员系统信息
        readMemberSystems();

        // 读取判定信息
        readJudgement();

        // 计算每个成员系统的AP和MAP值
        Map<String, Double> apValues = new HashMap<>();
        Map<String, Double> fusionMapArray = new HashMap<>();
        for (String memberSystem : memberSystems.keySet()) {
            List<Document> documents = memberSystems.get(memberSystem);
            double[] apArray = new double[NUM_TOPICS];
            for (int i = 0; i < NUM_TOPICS; i++) {
                List<Document> topicDocuments = getTopicDocuments(documents, ji[i]);//记得改里面的ji,ou,topic参数
                int[] relevance = new int[topicDocuments.size()];
                double[] scores = new double[topicDocuments.size()];
                for (int j = 0; j < topicDocuments.size(); j++) {
                    Document document = topicDocuments.get(j);
                    relevance[j] = document.relevance;
                    scores[j] = document.score;
                }
                apArray[i] = EvaluationMetrics.averagePrecision(relevance);//这里是每个成员系统中每个Q的AP
                //System.out.println(i+"\t"+apArray[i]);
            }
            double ap = EvaluationMetrics.meanAveragePrecision(apArray);//这里是每个成员系统的MAP
            apValues.put(memberSystem, ap);
            System.out.println(memberSystem + " AP: " + ap);
        }
        double[] weight=new double[NUM_MEMBERS];
        double sumweight=0;
        for (int i = 0; i < NUM_MEMBERS; i++) {
            weight[i]=getRandomWeight();
            sumweight+=weight[i];
        }
        double[] Weights=new double[NUM_MEMBERS];
        double fusionMap=0;
        for (int i = 0; i < NUM_MEMBERS; i++) {
            Weights[i]=weight[i]/sumweight;
            //System.out.println("weight"+i+":   "+Weights[i]);
            fusionMap+=Weights[i]*apValues.get(memberS[i]);
        }
        //System.out.println(fusionMap);

        for (int i = 0; i <= NUM_PARTICLES; i++) {
            fusionMapArray.put("粒子"+i,fusionMap);
        }
        // 使用粒子群优化算法训练权重，使数据融合的MAP值最大
        double[] weights = particleSwarmOptimization(fusionMapArray);
        // 打印训练得到的权重值
        for (int i = 0; i < NUM_MEMBERS; i++) {
            //System.out.println(memberS[i]+ "\t" + weights[i]);
            System.out.print(weights[i]+",");
        }
    }

    // 读取成员系统信息
    private static void readMemberSystems() {
        String[] memberSystems = {"CSIROmed_strRR", "baseline", "rrf", "sibtm_run1", "duoT5", "nnrun1", "r1st", "bm25", "uog_ufmg_sb_df5", "monoT5",
        };

        for (String memberSystem : memberSystems) {
            List<Document> documents = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("E:\\TREC 数据集\\2020MedicineTrackScientific\\ji\\"+memberSystem))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\t");
                    String topic = parts[0];
                    String documentName = parts[2];
                    int rank = Integer.parseInt(parts[3]);
                    double score = Double.parseDouble(parts[4]);
                    documents.add(new Document(topic, documentName, rank, score));
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ParticleSwarmOptimization.memberSystems.put(memberSystem, documents);

        }
    }

    // 读取判定信息
    private static void readJudgement() {
        try (BufferedReader reader = new BufferedReader(new FileReader("E:\\TREC 数据集\\2020MedicineTrackScientific\\judgement\\testfun868"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                String topic = parts[0];
                String documentName = parts[2];
                int relevance = Integer.parseInt(parts[3]);
                for (List<Document> documents : memberSystems.values()) {
                    for (Document document : documents) {
                        if (document.topic.equals(topic) && document.documentName.equals(documentName)) {
                            document.relevance = relevance;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取特定主题的文档列表
    private static List<Document> getTopicDocuments(List<Document> documents, int topicNumber) {
        List<Document> topicDocuments = new ArrayList<>();
        for (Document document : documents) {
            if (Integer.parseInt(document.topic)==topicNumber) {
                topicDocuments.add(document);
            }
        }
        return topicDocuments;
    }

    // 粒子群优化算法训练权重
    private static double[] particleSwarmOptimization(Map<String, Double> fusionMapArray) {
        String[] memberS = {"CSIROmed_strRR", "baseline", "rrf", "sibtm_run1", "duoT5", "nnrun1", "r1st", "bm25", "uog_ufmg_sb_df5", "monoT5",};

        double globalBestValue=0;
        // 初始化粒子的位置和速度
        //一个粒子代表一套10个成员系统的权重数组
        double[][] position = new double[NUM_PARTICLES][NUM_MEMBERS];
        double[][] positions = new double[NUM_PARTICLES][NUM_MEMBERS];
        double[][] velocities = new double[NUM_PARTICLES][NUM_MEMBERS];
        double[] sum=new double[NUM_PARTICLES];
        for (int i = 0; i < NUM_PARTICLES; i++) {
            for (int j = 0; j < NUM_MEMBERS; j++) {
                position[i][j] = getRandomWeight();
                sum[i]+=position[i][j];
            }
        }
        for (int i = 0; i < NUM_PARTICLES; i++) {
            for (int j = 0; j < NUM_MEMBERS; j++) {
                positions[i][j] = position[i][j]/sum[i];//这里是初始化时对每个系统进行的权重随机赋值，确保10个成员系统的权重和为1；
                //System.out.println(positions[i][j]);
                velocities[i][j] = 0.0;
            }
        }

        // 初始化全局最优位置和个体最优位置
        double[] globalBestPosition = new double[NUM_MEMBERS];
        double[] individualBestPositions = new double[NUM_PARTICLES];
        for (int i = 0; i < NUM_MEMBERS; i++) {
            globalBestPosition[i] = getRandomWeight();
        }
        for (int i = 0; i < NUM_PARTICLES; i++) {
            individualBestPositions[i] = getRandomWeight();
        }

        // 迭代训练
        for (int iter = 0; iter < NUM_ITERATIONS; iter++) {
            // 更新速度和位置
            for (int i = 0; i < NUM_PARTICLES; i++) {
                for (int j = 0; j < NUM_MEMBERS; j++) {
                    velocities[i][j] = INERTIA_WEIGHT * velocities[i][j]
                            + COGNITIVE_WEIGHT * Math.random() * (individualBestPositions[i] - positions[i][j])
                            + SOCIAL_WEIGHT * Math.random() * (globalBestPosition[j] - positions[i][j]);
                    positions[i][j] += velocities[i][j];
                }
            }

            // 对于每个粒子，计算fusionMAP 值，并更新个体最优位置
                for (int i = 0; i < NUM_PARTICLES; i++) {
                    double[] apArray = new double[NUM_TOPICS];
                    double[] MapArray = new double[NUM_MEMBERS];
                    double fusionMapValue=0;
                    for (int l = 0; l < NUM_MEMBERS; l++) {
                        for (int j = 0; j < NUM_TOPICS; j++) {
                            List<Document> documents = memberSystems.get(memberS[l]);
                            //System.out.println( "!!!!!!!!!!"+memberSystems.get(memberS[l]));
                            List<Document> topicDocuments = getTopicDocuments(documents, j + 1);
                            int[] relevance = new int[topicDocuments.size()];
                            for (int k = 0; k < topicDocuments.size(); k++) {
                                Document document = topicDocuments.get(k);
                                relevance[k] = document.relevance;
                            }
                            apArray[j] = EvaluationMetrics.averagePrecision(relevance);
                        }
                        double mapValue = EvaluationMetrics.meanAveragePrecision(apArray);
                        MapArray[l] = mapValue;
                    }

                    for (int m = 0; m < MapArray.length; m++) {
                        fusionMapValue+=MapArray[m]*(positions[i][m]);
                    }
                    //System.out.println("粒子"+i+"\t"+fusionMapValue);
                    fusionMapArray.put("粒子"+i,fusionMapValue);

                    //每个粒子要找到最佳位置
                    //再从粒子群中找到全局最佳位置
                        if (fusionMapValue>fusionMapArray.get("粒子"+(i+1))) {
                            System.out.println(fusionMapArray.get("粒子"+(i+1)));
                            fusionMapArray.put("粒子"+(i+1), fusionMapValue);
                            individualBestPositions[i] = positions[i][0];
                            for (int j = 1; j < NUM_MEMBERS; j++) {
                                if (positions[i][j] > individualBestPositions[i]) {
                                    individualBestPositions[i] = positions[i][j];
                                }
                            }
                        }

            }
            /*for (int i = 0; i < NUM_PARTICLES; i++) {
                if (fusionMapArray.get("粒子"+(i))>fusionMapArray.get("粒子"+(i+1))) {
                            //System.out.println(fusionMapArray.get("粒子"+(i+1)));
                            fusionMapArray.put("粒子"+(i+1),fusionMapArray.get("粒子"+(i)));
                            individualBestPositions[i] = positions[i][0];
                            for (int j = 1; j < NUM_MEMBERS; j++) {
                                if (positions[i][j] > individualBestPositions[i]) {
                                    individualBestPositions[i] = positions[i][j];
                                }
                            }
                        }
                if(i==18)
                {
                    break;
                }
            }*/

            // 更新全局最优位置
            globalBestValue = fusionMapArray.get("粒子1");
            for (int i = 2; i < NUM_MEMBERS; i++) {
                if (fusionMapArray.get("粒子"+i) > globalBestValue) {
                    globalBestValue = fusionMapArray.get("粒子"+i);
                    for (int j = 0; j < NUM_MEMBERS; j++) {
                        globalBestPosition[j] = positions[i - 1][j];
                    }
                }
            }

        }
        System.out.println("Best Value:"+globalBestValue);


        // 返回训练得到的权重数组
        return globalBestPosition;
    }
    // 获取随机权重
    private static double getRandomWeight() {
        Random random = new Random();
        return RANDOM_WEIGHT_LOWER_BOUND + (RANDOM_WEIGHT_UPPER_BOUND - RANDOM_WEIGHT_LOWER_BOUND) * random.nextDouble();
    }

    // 文档类
    static class Document {
        String topic;
        String documentName;
        int rank;
        double score;
        int relevance;

        public Document(String topic, String documentName, int rank, double score) {
            this.topic = topic;
            this.documentName = documentName;
            this.rank = rank;
            this.score = score;
        }
    }

    private static class EvaluationMetrics {
        // 计算单个查询的平均精确率（AP）
        public static double averagePrecision(int[] relevance) {
            int numRelevant = 0;
            double sumPrecision = 0.0;

            for (int i = 0; i < relevance.length; i++) {
                if (relevance[i] == 1) {
                    numRelevant++;
                    double precision = (double) numRelevant / (i + 1);
                    sumPrecision += precision;
                }
            }

            if (numRelevant == 0) {
                return 0.0;
            } else {
                return sumPrecision / numRelevant;
            }
        }

        // 计算整个检索系统的平均精确率（MAP）
        public static double meanAveragePrecision(double[] apArray) {
            int numQueries = apArray.length;
            //System.out.println(numQueries);
            double sumAP = 0.0;

            for (double ap : apArray) {
                sumAP += ap;
            }

            return sumAP / numQueries;
        }

    }
}