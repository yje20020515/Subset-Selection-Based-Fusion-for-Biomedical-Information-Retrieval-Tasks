package com.algorithm.Gene;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

//这是个体类
public class Individual {

    private int[] chromosome;       //染色体数组
    private double fitness = -1;    //适应度

    //含有染色体数组的构造函数
    public Individual(int[] chromosome){
        this.chromosome = chromosome;
    }

    //含有染色体长度的构造函数，随机初始化染色体
    public Individual(int chromosomeLength){
        this.chromosome = new int[chromosomeLength];
        Set<Integer> generatedNumbers = new HashSet<>();
        Random random = new Random();
        for (int gene = 0; gene < chromosomeLength; gene++) {
            int randomNumber;
            do {
                randomNumber = random.nextInt(102); // 生成0到125范围内的随机整数
            } while (generatedNumbers.contains(randomNumber)); // 检查是否已生成过这个数字
            this.setGene(gene, randomNumber);
            generatedNumbers.add(randomNumber); // 将生成的数字添加到集合中
        }
    }

    // 设置染色体中指定位置的基因值
    public void setGene(int geneIndex, int value) {
        this.chromosome[geneIndex] = value;
    }


    public int[] getChromosome(){
        return this.chromosome;
    }

    public int getChromosomeLength(){
        return this.chromosome.length;
    }


    public int getGene(int offset){
        return this.chromosome[offset];
    }

    public void setFitness(double fitness){
        this.fitness = fitness;
    }

    public double getFitness(){
        return this.fitness;
    }

    public String toString(){
        String output = "";
        for(int gene = 0; gene < this.chromosome.length; gene++){
            output += this.chromosome[gene]+",";
        }
        return output;
    }


}

