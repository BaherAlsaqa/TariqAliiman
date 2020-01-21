package com.tariqaliiman.tariqaliiman.classes;

public class Muslim {
    public int id;
    public String name;
    public String content;
    public String reference;

    public Muslim(int id, String name, String content, String reference) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.reference = reference;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
