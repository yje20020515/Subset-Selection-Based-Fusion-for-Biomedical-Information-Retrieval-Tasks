package com.Microblog2015.DatasetStruct;

import java.util.Objects;

public class Result {
    private String Date;
    private int topic;
    private String docID;
    private int rank;
    private double score;
    private String systemName;

    public Result() {
    }

    public Result(String date, int topic, String docID, int rank, double score, String systemName) {
        Date = date;
        this.topic = topic;
        this.docID = docID;
        this.rank = rank;
        this.score = score;
        this.systemName = systemName;
    }

    public Result(int topic, String docID, int rank, double score, String systemName) {
        this.topic = topic;
        this.docID = docID;
        this.rank = rank;
        this.score = score;
        this.systemName = systemName;
    }

    public Result(int topic, String docID) {
        this.topic = topic;
        this.docID = docID;
    }

    public Result(int topic, String docID, int rank) {
        this.topic = topic;
        this.docID = docID;
        this.rank = rank;
    }

    public Result(int topic, String docID, int rank, double score) {
        this.topic = topic;
        this.docID = docID;
        this.rank = rank;
        this.score = score;
    }

    public Result(String date, int topic, String docID, int rank, double score) {
        Date = date;
        this.topic = topic;
        this.docID = docID;
        this.rank = rank;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return topic == result.topic && Objects.equals(docID, result.docID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, docID);
    }

    @Override
    public String toString() {
        return "Result{" +
                "Date='" + Date + '\'' +
                ", topic=" + topic +
                ", docID='" + docID + '\'' +
                ", rank=" + rank +
                ", score=" + score +
                ", systemName='" + systemName + '\'' +
                '}';
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}
