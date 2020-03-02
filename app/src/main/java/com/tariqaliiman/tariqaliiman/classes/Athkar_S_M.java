package com.tariqaliiman.tariqaliiman.classes;

public class Athkar_S_M {
    public int id;
    public String content;
    public String reference;
    public int number;

    public Athkar_S_M(int id, String content, String reference, int number) {
        this.id = id;
        this.content = content;
        this.reference = reference;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
