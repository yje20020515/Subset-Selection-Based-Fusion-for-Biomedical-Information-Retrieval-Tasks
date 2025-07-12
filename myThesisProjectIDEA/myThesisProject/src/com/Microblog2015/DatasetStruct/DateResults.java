package com.Microblog2015.DatasetStruct;

import java.util.ArrayList;

public class DateResults {
    private String date;
    private ArrayList<Results> list;

    public DateResults() {
    }

    public DateResults(String date, ArrayList<Results> list) {
        this.date = date;
        this.list = list;
    }

    public DateResults(String date) {
        this.date = date;
        this.list = new ArrayList<>();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Results> getList() {
        return list;
    }

    public void setList(ArrayList<Results> list) {
        this.list = list;
    }
}
