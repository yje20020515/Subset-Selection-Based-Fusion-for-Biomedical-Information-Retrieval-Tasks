package com.Microblog2015.Evaluation;

/**
 * 用于评价Microblog2015年taskB的数据集 nDCG
 * @author HYD
 * @date 20220427
 */
public class Evaluation {
    /**
     1.获取qrel文件
        1.1 将qrel文件按照topic分别加载到Qrels对象中，并将该系列Qrels对象加载到一个Arraylist对象中
     2.获取result文件
        2.1 将result文件按照日期加载到DateResults对象中，并封装为一个Arraylist对象中
        2.2 将DateResults对象中的数据按照topic分别加载到该对象的list列表中
     3.计算nDCG值
        3.1 计算dcg的值
        3.2 计算idcg的值
        3.3 ndcg = dcg / idcg
     4.输出结果
     */

}
