package com.algorithm.cluster.kmeans.kmeans2;

public class pointbean {

    public String name;
    public int id;
    public boolean iscentre = false;//是否中心点
    public double cost=0;
    public String becentre = null;//属于哪个中心点
    public pointbean() {}
    public pointbean(String name,int id,boolean iscentre,double cost,String becentre) {
        this.name=name;
        this.id=id;
        this.iscentre=iscentre;
        this.cost=cost;
        this.becentre=becentre;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isIscentre() {
        return iscentre;
    }
    public void setIscentre(boolean iscentre) {
        this.iscentre = iscentre;
    }
    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getBecentre() {
        return becentre;
    }
    public void setBecentre(String becentre) {
        this.becentre = becentre;
    }
}
