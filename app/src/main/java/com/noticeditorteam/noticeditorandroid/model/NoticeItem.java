package com.noticeditorteam.noticeditorandroid.model;

import java.util.ArrayList;

public class NoticeItem {

    private String title;
    private String content;
    private ArrayList<NoticeItem> children;

    public NoticeItem(String title) {
        this(title, null);
    }

    public NoticeItem(String title, String content) {
        this.title = title;
        this.content = content;
        children = new ArrayList();
    }

    public void addChild(NoticeItem item) {
        children.add(item);
    }

    public boolean isLeaf() {
        return content != null;
    }

    public boolean isBranch() {
        return content == null;
    }

    public String getContent() {
        return content;
    }

    public void changeContent(String content) {
        if(isLeaf()) {
            this.content = content;
        }
    }

    @Override
    public String toString() {
        return title + "\n\n" + content;
    }

}
