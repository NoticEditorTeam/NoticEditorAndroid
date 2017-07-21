package com.noticeditorteam.noticeditorandroid.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.noticeditorteam.noticeditorandroid.R;

public class RenameDialogFragment extends DialogFragment {

    private static final String ARG_NAME = "name";

    public interface RenameDialogListener {
        void onDialogPositiveClick(RenameDialogFragment dialog);
    }

    public EditText getNoticeName() {
        return noticeName;
    }

    private EditText noticeName;
    private RenameDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (RenameDialogListener) context;
        } catch(ClassCastException e) {
            throw new RuntimeException(context.toString() + " must implement RenameDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle args = getArguments();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View mView = inflater.inflate(R.layout.dialog_rename, null);
        noticeName = (EditText) mView.findViewById(R.id.newname);
        noticeName.setText(args.getString(ARG_NAME));
        builder.setView(mView)
            .setPositiveButton("OK", (dialog, which) -> mListener.onDialogPositiveClick(RenameDialogFragment.this))
            .setNegativeButton("Cancel", (dialog, which) -> RenameDialogFragment.this.getDialog().cancel());
        return builder.create();
    }
}
