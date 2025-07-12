package com.algorithm.Microblog2015.DatasetStruct;

import java.util.ArrayList;

public class Qrels {
    private int topic;
    private ArrayList<Qrel> list;

    public Qrels() {
    }

    public Qrels(int topic, ArrayList<Qrel> list) {
        this.topic = topic;
        this.list = list;
    }

    public Qrels(int topic) {
        this.topic = topic;
        this.list = new ArrayList<>();
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public ArrayList<Qrel> getList() {
        return list;
    }

    public void setList(ArrayList<Qrel> list) {
        this.list = list;
    }
}
