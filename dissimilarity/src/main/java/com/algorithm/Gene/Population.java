package com.algorithm.Gene;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;


//群体类
public class Population {
    private Individual population[];
    private double populationFitness = -1;  //群体的适应度

    public Population(int populationSize){
        this.population = new Individual[populationSize];
    }

    public Population(int populationSize, int chromosomeLength){
        this.population = new Individual[populationSize];

        for(int individualCount = 0; individualCount < populationSize; individualCount++){
            Individual individual = new Individual(chromosomeLength);   //这通过长度创建个体
            this.population[individualCount] = individual;  //加入到群体中
        }
    }

    public Individual[] getIndividuals(){
        return this.population;
    }

    //将个体按照适应度排序，适应度越高索引值越低
    //排序后根据索引值获得个体
    public Individual getFittest(int offset){
        Arrays.sort(this.population, new Comparator<Individual>() {
            @Override
            public int compare(Individual o1, Individual o2) {
                if(o1.getFitness() > o2.getFitness()){
                    return -1;  //前面比后面高就返回-1
                } else if(o1.getFitness() < o2.getFitness()){
                    return 1;   //前天比后面第就返会1，此时交换两个元素
                } else {
                    return 0;   //相等返回0
                }
            }
        });
        return this.population[offset];
    }

    public void setPopulationFitness(double fitness){
        this.populationFitness = fitness;
    }

    public double getPopulationFitness(){
        return this.populationFitness;
    }

    //得到群体中的个体数
    public int size(){
        return this.population.length;
    }

    //将个体加入到群体中的某个位置
    public Individual setIndividual(int offset, Individual individual){
        return population[offset] = individual;
    }

    public Individual getIndividual(int offset){
        return this.population[offset];
    }

    //对于每一个个体，都将它与其他任意个体随机交换
    public void shuffle(){
        Random rnd = new Random();
        for(int i=population.length-1; i>0; i--){
            int index = rnd.nextInt(i+1);   //随机获得[0, i+1)的一个数
            Individual a = population[index];       //进行交换
            population[index] = population[i];
            population[i] = a;
        }
    }

}
