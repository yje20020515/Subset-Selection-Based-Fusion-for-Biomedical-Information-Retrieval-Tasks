package com.algorithm.Gene;

import com.algorithm.evaluation.evaluationMain;
import com.algorithm.fusionAlgorithm.FastCombSUM;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;




public class GeneticAlgorithm {
    private int populationSize;     //搜索范围
    private double mutationRate;    //变异率
    private double crossoverRate;   //交叉率
    private int elitismCount;       //精英数量

    //构造函数
    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
    }

    //初始化群体
    public Population initPopulation(int chromosomeLength) {
        Population population = new Population(this.populationSize, chromosomeLength);
        return population;
    }

    //评估适应度
    public double calcFitness(Individual individual) throws Exception {
        //记录为1的染色体数量
        int correctGenes = 0;
        int[] systemIndex=new int[individual.getChromosomeLength()];
        //扫描所有染色体
        for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
            systemIndex[geneIndex]=individual.getGene(geneIndex);
        }
        String[] systemNames={
                //2017
                /*"aCSIROmedAll","aCSIROmedMCB","aCSIROmedMGB","aCSIROmedNEG","aCSIROmedPCB","Broad","cbnuSA1","cbnuSA2","cbnuSA3","DUTIR003",
                "ECNU_M_1","ECNU_M_2","ECNU_M_3","ECNU_M_4","ECNU_M_5","eth_a_gws","eth_a_luc","eth_a_nn","eth_a_ws","eth_a_ws_q",
                "Focused","GP14Medline","GP16Medline","Gwave","ielabRun1","ielabRun21","ielabRun22","ielabRun23","ielabRun3","KISTI01",
                "KISTI02","KISTI03","KISTI04","KISTI05","kkseabs1","kkseabs3","kkseabs4","mayonlppm1","mayonlppm2","mayonlppm3",
                "mayonlppm4","mayonlppm5","MedIER_sa1","MedIER_sa2","MedIER_sa3","MedIER_sa4","medline1","medline2","medline3","medline4",
                "medline5","mRun1Bsl","mRun2BslOth","mRun3MRF","mRun4MRFrf","mRun5MRFBow","mugpubbase","mugpubboost","mugpubdiseas",
                "mugpubshould","NOVAsa1","NOVAsa2","NOVAsa3","Ontological","pms_run1","pms_run2_abs","pms_run4_abs","pms_run5_abs",
                "POZabsBB2GRn","POZabsBB2sn","POZabsLGDGRn","SDSFU_Ensem","SDSFU_Jnal","SDSFU_Lambda","SDSFU_PF_SA","SDSFU_PU_SA","Semantic","SIBTMlit1",
                "SIBTMlit2","SIBTMlit3","SIBTMlit4","SIBTMlit5","Textual","UCASBASEa","UCASSEM1a","UCASSEM2a","UCASSEM3a","UCASSEMUMLSa",
                "udelT1Base","udelT1Comb","udelT1GeMeSH","udelT1Gene","udelT1PRF","UDInfoPMSA3","UDInfoPMSA5","UDInfoPMSA6","UDInfoPMSA7",
                "UD_GU_SA_1","UD_GU_SA_2","UD_GU_SA_3","UD_GU_SA_4","UKY_AGG","UKY_BASE","UKY_CJT","UKY_COM","UKY_MAN",
                "UNTIIADW","UNTIIAGA","UNTIIAIS","UNTIIALQ","UNTIIASY","UTDHLTAF","UTDHLTJQ","UTDHLTSF","UTDHLTSQ",
                "UWMSOIS5","UWMSOIS6","UWMSOIS7","UWMSOIS8","UWMSOIS9","UD_GU_SA_5","mugpubgene","UDInfoPMSA2","pms_run3_abs"*/

                //2018
                "aCSIROmedAll","aCSIROmedMCB","aCSIROmedNEG","bool51","cbnuSA1","cbnuSA2","cbnuSA3","cubicmnzAbs","cubicsumWAbs","doc2vec_run",
                "doc2vec_run2","ECNU_S_Run1","ECNU_S_Run2","ECNU_S_Run3","ECNU_S_Run4","ECNU_S_Run5","elastic_run","hpipubbase","hpipubclass",
                "hpipubcommon","hpipubnone","IKMLAB_1","IKMLAB_2","IKMLAB_3","IKMLAB_4","IKMLAB_5","imi_mug_abs1","imi_mug_abs2","imi_mug_abs3",
                "imi_mug_abs4","imi_mug_abs5","KL18AbsFuse","KL18absHY","KL18absWV","KLPM18T2Bl","mayomedcomp","mayomedcreat","mayomedsimp","mayopubtator",
                "MedIER_sa11","MedIER_sa12","MedIER_sa13","MedIER_sa14","MedIER_sa15","method_fu","minfolabBA","minfolabBC","minfolabBD","minfolabTH",
                "mnzAbs","MSIIP_BASE","MSIIP_PBAH","MSIIP_PBH","MSIIP_PBL","MSIIP_PBPK","para_fusion","PM_IBI_run1","PM_IBI_run2","PM_IBI_run3",
                "raw_medline","rbf","RSA_DSC_LA_1","RSA_DSC_LA_2","RSA_DSC_LA_3","RSA_DSC_LA_4","RSA_DSC_LA_5","SIBTMlit1","SIBTMlit2","SIBTMlit3",
                "SIBTMlit4","SIBTMlit5","SINAI_Base","SINAI_FU","SINAI_FUO","sumAbs","sumEW","two_stage","UCASSA1","UCASSA2",
                "UCASSA3","UCASSA4","UCASSA5","UDInfoPMSA1","UDInfoPMSA2","UDInfoPMSA3","UDInfoPMSA4","UDInfoPMSA5","UNTIIA_DGES","UNTIIA_DGEWS",
                "UNTIIA_DGEWU","UNTIIA_DGS","UNTIIA_WTU","UTDHLTRI_NL","UTDHLTRI_RA","UTDHLTRI_RF","UTDHLTRI_SF","UTDHLTRI_SS","UVAEXPBOOST","UVAEXPBSTDIF",
                "UVAEXPBSTEXT","UVAEXPBSTNEG","UVAEXPBSTSHD",
        };

        String[] s=new String[individual.getChromosomeLength()];
        for (int i = 0; i < systemIndex.length; i++) {
            s[i]=systemNames[systemIndex[i]];
            //System.out.println(s[i]);
        }
        int k1=0;
        for (int j = 0; j < s.length-1; j++) {
            // 创建数据
            Object[][] data = new Object[20][individual.getChromosomeLength()];
            data[0][0]="hpipubboost";
           /* data[1][0]="UD_GU_SA_5";
            data[2][0]="mugpubgene";
            data[3][0]="UDInfoPMSA2";
            data[4][0]="pms_run3_abs";*/
            for (int k = 1; k <=j+1 ; k++) {
                data[k][0]=s[k];
                //System.out.println(data[j][k]);
                k1=k;
        }

            // 创建工作簿
            Workbook workbook =new HSSFWorkbook();

            // 创建工作表
            Sheet sheet = workbook.createSheet("Sheet1");



            // 写入数据
            int rowNum = 0;
            System.out.println("Writing data into Excel...");

            for (Object[] rowData : data) {
                Row row = sheet.createRow(rowNum++);

                int colNum = 0;
                for (Object field : rowData) {
                    Cell cell = row.createCell(colNum++);
                    if (field instanceof String) {
                        cell.setCellValue((String) field);
                    } else if (field instanceof Integer) {
                        cell.setCellValue((Integer) field);
                    }
                }
            }

            // 保存工作簿到文件
            try (FileOutputStream outputStream = new FileOutputStream("E:\\TREC 数据集\\2018MedicineTrackScientific\\classGene\\"+(k1+1)+"\\Semi_K"+(k1+1)+"" + "_1.xls")) {
                workbook.write(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Data has been written successfully."+(j+2));
        }



        //计算适应值
        FastCombSUM.main(s);
        double fitness =evaluationMain.main(s);
        System.out.println(fitness);

        //存储适应度
        individual.setFitness(fitness);

        return fitness;
    }



    //评估群体适应度，其值就是每个个体适应度加和
    public void evalPopulation(Population population) throws Exception {
        double populationFitness = 0;   //群体的适应度
        for(Individual individual : population.getIndividuals()){
            populationFitness += calcFitness(individual);
        }
        population.setPopulationFitness(populationFitness);
    }

    //终止检查
    public boolean isTerminationConditionMet(Population population){
        //只要群体中有一个个体的适应度为1就终止检查
        for(Individual individual : population.getIndividuals()){
            if(individual.getFitness()>=0.452){
                return true;
            }
        }
        return false;
    }
    //选择方法
    //这个方法比较难理解，有点像概率里面的分布函数
    //我们使用轮盘赌的方式实现交叉，就是古典概型，适应度高的个体被选中染色体的概率就高
    public Individual selectParent(Population population){
        //获得个体
        Individual individuals[] = population.getIndividuals();

        double populationFitness = population.getPopulationFitness();
        double rouletteWheelPosition = Math.random()*populationFitness;
        //Math.random() 返回[0, 1)的随机数

        //找到亲代
        double spinWheel = 0;
        for(Individual individual: individuals){
            spinWheel += individual.getFitness();
            if(spinWheel >= rouletteWheelPosition){
                return individual;
            }
        }
        return individuals[population.size()-1];    //返回群体中的最后一个个体
    }
    //交叉方法
    public Population crossoverPopulation(Population population){
        //创建新种群
        Population newPopulation = new Population(population.size());

        //根据种群的适应度遍历种群
        for(int populationIndex=0; populationIndex<population.size(); populationIndex++){
            Individual parent1 = population.getFittest(populationIndex);

            //如果概率上达到了交叉的条件，并且不是精英就交叉（精英的性状直接保留至下一代）
            if(this.crossoverRate>Math.random() && populationIndex>this.elitismCount){
                //初始化后代
                Individual offspring = new Individual(parent1.getChromosomeLength());

                //找到第二个亲代
                //形状越好找被选中的概率越大
                Individual parent2 = selectParent(population);
                //开始交叉操作
                for(int geneIndex=0; geneIndex<parent1.getChromosomeLength(); geneIndex++){
                    //交叉的时候有一半的概率使用p1的基因，一半的概率使用p2的基因
                    if(Math.random() < 0.5){    //使用p1的基因
                        offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                    } else {                    //使用p2的基因
                        offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                    }
                }

                //将子代加入到种群中，实际上就是将原来的parent1替换成offspring了
                newPopulation.setIndividual(populationIndex, offspring);
            } else {
                //直接将个体添加到种群中
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }
        return newPopulation;
    }
    public Population mutatePopulation(Population population){
        //初始化新种群
        Population newPopulation = new Population(this.populationSize);

        //根据适应度遍历种群
        for(int populationIndex=0; populationIndex<population.size(); populationIndex++){
            Individual individual = population.getFittest(populationIndex);
            //遍历个体的基因
            for(int geneIndex=0; geneIndex<individual.getChromosomeLength(); geneIndex++){
                //精英就不用变异了（富人靠科技，穷人靠变异）
                if(populationIndex >= this.elitismCount){
                    if(this.mutationRate > Math.random()){  //达到了变异的条件
                        // 生成0到124之间的随机整数作为新基因值
                        int newGene = generateUniqueGene(individual); // 生成唯一的基因值

                        // 将变异后的基因插入
                        individual.setGene(geneIndex, newGene);
                    }
                }
            }
            //添加个体到群体中
            newPopulation.setIndividual(populationIndex, individual);
        }


    //返回变异后的群体
        return newPopulation;
    }

    private int generateUniqueGene(Individual individual) {
        int newGene;
        boolean isUnique = false;
        do {
            // 生成0到124之间的随机整数作为新基因值
            newGene = (int) (Math.random() * 102); // 125是上限值+1

            // 检查生成的基因值是否与个体中已有的基因值重复
            isUnique = true;
            for (int i = 0; i < individual.getChromosomeLength(); i++) {
                if (individual.getGene(i) == newGene) {
                    isUnique = false;
                    break;
                }
            }
        } while (!isUnique);

        return newGene;
    }


}
