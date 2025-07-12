package com.algorithm.Microblog2015.DatasetStruct;

import java.util.ArrayList;

public class Results {
    private String date;
    private int topic;
    private ArrayList<Result> list;

    public Results() {
    }

    public Results(String date, int topic, ArrayList<Result> list) {
        this.date = date;
        this.topic = topic;
        this.list = list;
    }

    public Results(int topic) {
        this.topic = topic;
        this.list = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Results{" +
                "date='" + date + '\'' +
                ", topic=" + topic +
                ", list=" + list +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public ArrayList<Result> getList() {
        return list;
    }

    public void setList(ArrayList<Result> list) {
        this.list = list;
    }
}
