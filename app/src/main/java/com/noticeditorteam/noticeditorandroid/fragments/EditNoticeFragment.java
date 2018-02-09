package com.noticeditorteam.noticeditorandroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.noticeditorteam.noticeditorandroid.R;
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;

public class EditNoticeFragment extends Fragment {

    public EditNoticeFragment() {
    }

    public static EditNoticeFragment newInstance(NoticeItem tree) {
        EditNoticeFragment fragment = new EditNoticeFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_TREE, tree);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            notice = getArguments().getParcelable(ARG_PARAM_TREE);
        }
        if(savedInstanceState != null) {
            notice = savedInstanceState.getParcelable(SAVE_PARAM_TREE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_notice, container, false);
        EditText editText = (EditText) view.findViewById(R.id.editNotice);
        editText.setText(notice.getContent());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                notice.changeContent(editable.toString());
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_PARAM_TREE, notice);
    }
}
