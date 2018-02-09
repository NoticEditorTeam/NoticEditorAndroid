package com.noticeditorteam.noticeditorandroid.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noticeditorteam.noticeditorandroid.NoticeListener;
import com.noticeditorteam.noticeditorandroid.R;
import com.noticeditorteam.noticeditorandroid.activities.NoticeWorkActivity;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.InternalStyleSheet;
import br.tiagohm.markdownview.css.styles.Github;

public class PreviewNoticeFragment extends Fragment implements NoticeListener {

    private MarkdownView mdView;

    public PreviewNoticeFragment() {
    }

    public static PreviewNoticeFragment newInstance() {
        return new PreviewNoticeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview_notice, container, false);
        mdView = view.findViewById(R.id.markdownView);
        InternalStyleSheet mStyle = new Github();
        mdView.addStyleSheet(mStyle);
        NoticeWorkActivity noticeActivity = (NoticeWorkActivity) getActivity();
        assert noticeActivity != null;
        mdView.loadMarkdown(noticeActivity.getNotice().getContent());
        noticeActivity.getNotice().addNoticeListener(this);
        return view;
    }

    @Override
    public void onDestroy() {
        NoticeWorkActivity noticeActivity = (NoticeWorkActivity) getActivity();
        assert noticeActivity != null;
        noticeActivity.getNotice().removeNoticeListener(this);
        super.onDestroy();
    }

    @Override
    public void onTitleChanged(String newTitle) {
    }

    @Override
    public void onContentChanged(String newContent) {
        mdView.loadMarkdown(newContent);
    }
}
