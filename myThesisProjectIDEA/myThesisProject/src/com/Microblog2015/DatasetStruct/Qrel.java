package com.Microblog2015.DatasetStruct;

public class Qrel {
    private int topic;
    private String docID;
    private double rel;

    public Qrel() {
    }

    public Qrel(int topic, String docID, double rel) {
        this.topic = topic;
        this.docID = docID;
        this.rel = rel;
    }

    public Qrel(int topic, String docID) {
        this.topic = topic;
        this.docID = docID;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public double getRel() {
        return rel;
    }

    public void setRel(double rel) {
        this.rel = rel;
    }
}
