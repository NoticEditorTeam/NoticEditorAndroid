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
import com.noticeditorteam.noticeditorandroid.model.NoticeItem;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditNoticeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditNoticeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_TREE = "tree";
    private static final String SAVE_PARAM_TREE = "tree";

    // TODO: Rename and change types of parameters
    private NoticeItem notice;

    public EditNoticeFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tree Parameter 1.
     * @return A new instance of fragment EditNoticeFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVE_PARAM_TREE, notice);
    }
}
