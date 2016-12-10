package com.noticeditorteam.noticeditorandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class NoticeItem implements Parcelable {

    private String title;
    private String content;

    protected NoticeItem(Parcel in) {
        title = in.readString();
        content = in.readString();
        path = in.readString();
        children = in.createTypedArrayList(NoticeItem.CREATOR);
    }

    public static final Creator<NoticeItem> CREATOR = new Creator<NoticeItem>() {
        @Override
        public NoticeItem createFromParcel(Parcel in) {
            return new NoticeItem(in);
        }

        @Override
        public NoticeItem[] newArray(int size) {
            return new NoticeItem[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;

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

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public ArrayList<NoticeItem> getChildren() { return children; }

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
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(path);
        dest.writeTypedList(children);
    }
}
