package com.tariqaliiman.tariqaliiman.classes;

import java.util.Comparator;

public class Sereen {
    public String title;
    public String content;

    public Sereen(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static final Comparator<Sereen> BY_NAME_ALPHABETICAL = new Comparator<Sereen>() {
        @Override
        public int compare(Sereen sereen1, Sereen sereen2) {
            return sereen1.title.compareTo(sereen2.title);
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
