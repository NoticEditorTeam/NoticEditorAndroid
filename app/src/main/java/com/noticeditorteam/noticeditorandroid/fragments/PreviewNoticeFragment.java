package com.noticeditorteam.noticeditorandroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noticeditorteam.noticeditorandroid.NoticeListener;
import com.noticeditorteam.noticeditorandroid.R;
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.InternalStyleSheet;
import br.tiagohm.markdownview.css.styles.Github;

public class PreviewNoticeFragment extends Fragment implements NoticeListener {
    private static final String ARG_PARAM_TREE = "tree";
    private static final String SAVE_PARAM_TREE = "tree";

    private static NoticeItem notice;

    private MarkdownView mdView;

    public PreviewNoticeFragment() {
        // Required empty public constructor
    }

    public static PreviewNoticeFragment newInstance(NoticeItem notice) {
        PreviewNoticeFragment fragment = new PreviewNoticeFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_TREE, notice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            notice = getArguments().getParcelable(ARG_PARAM_TREE);
            assert notice != null;
        }
        if(savedInstanceState != null) {
            notice = savedInstanceState.getParcelable(SAVE_PARAM_TREE);
            assert notice != null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview_notice, container, false);
        mdView = (MarkdownView) view.findViewById(R.id.markdownView);
        InternalStyleSheet mStyle = new Github();
        mdView.addStyleSheet(mStyle);
        mdView.loadMarkdown(notice.getContent());
        notice.addNoticeListener(this);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        notice.removeNoticeListener(this);
        outState.putParcelable(SAVE_PARAM_TREE, notice);
    }

    @Override
    public void onTitleChanged(String newTitle) {
    }

    @Override
    public void onContentChanged(String newContent) {
        mdView.loadMarkdown(newContent);
    }
}
