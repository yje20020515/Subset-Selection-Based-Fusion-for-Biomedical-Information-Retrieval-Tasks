package com.DataStruct;

import java.util.Objects;

/**
 * 此bean仅仅表示一个文档，仅有docID和Topic
 */
public class Doc {
    private String docID;
    private int topic;

    public Doc() {
    }

    public Doc(String docID, int topic) {
        this.docID = docID;
        this.topic = topic;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "Doc{" +
                "docID='" + docID + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doc doc = (Doc) o;
        return Objects.equals(docID, doc.docID) && Objects.equals(topic, doc.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docID, topic);
    }
}
