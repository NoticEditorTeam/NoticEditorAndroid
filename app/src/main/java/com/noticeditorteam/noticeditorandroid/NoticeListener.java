package com.noticeditorteam.noticeditorandroid;

/**
 * Created by smakarov on 7/11/17.
 */

public interface NoticeListener {
    void onTitleChanged(String newTitle);
    void onContentChanged(String newContent);
}
