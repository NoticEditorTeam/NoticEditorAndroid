package com.noticeditorteam.noticeditorandroid.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.noticeditorteam.noticeditorandroid.R;
import com.noticeditorteam.noticeditorandroid.activities.NoticeWorkActivity;

public class EditNoticeFragment extends Fragment {

    public EditNoticeFragment() {
    }

    public static EditNoticeFragment newInstance() {
        return new EditNoticeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_notice, container, false);
        EditText editText = view.findViewById(R.id.editNotice);
        NoticeWorkActivity noticeActivity = (NoticeWorkActivity) getActivity();
        assert noticeActivity != null;
        editText.setText(noticeActivity.getNotice().getContent());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                noticeActivity.getNotice().changeContent(editable.toString());
            }
        });
        return view;
    }
}
