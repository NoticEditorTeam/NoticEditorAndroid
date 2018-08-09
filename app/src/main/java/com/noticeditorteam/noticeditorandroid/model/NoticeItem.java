package com.noticeditorteam.noticeditorandroid.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.noticeditorteam.noticeditorandroid.NoticeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NoticeItem implements Parcelable {

    private String title;
    private String content;

    private Set<NoticeListener> listeners;

    protected NoticeItem(Parcel in) {
        title = in.readString();
        content = in.readString();
        path = in.readString();
        children = in.createTypedArrayList(NoticeItem.CREATOR);
        listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
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
        children = new ArrayList<>();
        listeners = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public void addChild(NoticeItem item) {
        children.add(item);
    }

    @SuppressWarnings("WeakerAccess")
    public boolean isLeaf() {
        return content != null;
    }

    public boolean isBranch() {
        return content == null;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
        sendTitleChanged(title);
    }

    public ArrayList<NoticeItem> getChildren() { return children; }

    public String getContent() {
        return content;
    }

    public void changeContent(String content) {
        if(isLeaf()) {
            this.content = content;
            sendContentChanged(content);
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

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (!NoticeItem.class.isAssignableFrom(other.getClass())) return false;
        final NoticeItem item = (NoticeItem) other;
        if (this.isBranch() && item.isLeaf()) return false;
        if (this.isLeaf() && item.isBranch()) return false;
        if (this.isLeaf() && item.isLeaf()) return (this.getContent().equals(item.getContent()));
        return this.isBranch() && item.isBranch() && (this.getChildren().equals(item.getChildren()));
    }

    public void addNoticeListener(NoticeListener listener) {
        listeners.add(listener);
    }

    public void removeNoticeListener(NoticeListener listener) {
        listeners.remove(listener);
    }

    private void sendTitleChanged(String newTitle) {
        for(NoticeListener listener : listeners) listener.onTitleChanged(newTitle);
    }

    private void sendContentChanged(String newContent) {
        for(NoticeListener listener : listeners) listener.onContentChanged(newContent);
    }
}
