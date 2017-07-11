package com.noticeditorteam.noticeditorandroid;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

import us.feras.mdv.MarkdownView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreviewNoticeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviewNoticeFragment extends Fragment implements NoticeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_TREE = "tree";
    private static final String SAVE_PARAM_TREE = "tree";

    // TODO: Rename and change types of parameters
    private static NoticeItem notice;

    private MarkdownView mdView;

    public PreviewNoticeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param notice notice.
     * @return A new instance of fragment PreviewNoticeFragment.
     */
    // TODO: Rename and change types and number of parameters
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
            notice.addNoticeListener(this);
        }
        if(savedInstanceState != null) {
            notice.removeNoticeListener(this);
            notice = savedInstanceState.getParcelable(SAVE_PARAM_TREE);
            notice.addNoticeListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_preview_notice, container, false);
        mdView = (MarkdownView) view.findViewById(R.id.markdownView);
        mdView.loadMarkdown(notice.getContent());
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_PARAM_TREE, notice);
        notice.removeNoticeListener(this);
    }

    @Override
    public void onTitleChanged(String newTitle) {
    }

    @Override
    public void onContentChanged(String newContent) {
        mdView.loadMarkdown(newContent);
    }
}
