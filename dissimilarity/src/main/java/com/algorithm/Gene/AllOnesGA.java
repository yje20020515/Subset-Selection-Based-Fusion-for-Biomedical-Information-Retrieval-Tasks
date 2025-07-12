package com.algorithm.Gene;


import java.io.BufferedWriter;
import java.io.FileWriter;

//引导类，用于初始化遗传算法
public class AllOnesGA {

    public static void main(String[] args) throws Exception {
        //实例化一个遗传算法对象
        GeneticAlgorithm ga = new GeneticAlgorithm(500, 0.1, 0.95, 2);

        //初始化群体，染色体长度为50
        Population population = ga.initPopulation(15);

        //下面进行进化计算
        ga.evalPopulation(population);
        int generation = 1; //进化的代数

        while(ga.isTerminationConditionMet(population) == false){
            //打印群体中适应度最高的个体
            System.out.println("Best solution:======================= "+population.getFittest(0) +" generation:============================= "+generation);
            BufferedWriter bw=new BufferedWriter(new FileWriter("E:\\TREC 数据集\\2018MedicineTrackScientific\\testeval\\result",true));
            bw.write("Best solution: "+population.getFittest(0) +"meanMAP:  "+population.getIndividual(0).getFitness()+" generation: "+generation);
            bw.write("\n");
            bw.close();

            //交叉
            //更新种群
            population = ga.crossoverPopulation(population);

            //变异
            population = ga.mutatePopulation(population);

            //评估群体
            ga.evalPopulation(population);

            //代数加一
            generation++;

        }


        //打印最终结果
        System.out.println("Found solution in " + generation + "generations");
        System.out.println("Best solution: "+population.getFittest(0).toString());
    }
}