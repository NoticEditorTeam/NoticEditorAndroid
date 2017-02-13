package com.noticeditorteam.noticeditorandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class RenameDialogFragment extends DialogFragment {

    public interface RenameDialogListener {
        void onDialogPositiveClick(RenameDialogFragment dialog);
    }

    public EditText getNoticeName() {
        return noticeName;
    }

    private EditText noticeName;
    private RenameDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (RenameDialogListener) activity;
        } catch(ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement RenameDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle args = getArguments();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_rename, null);
        noticeName = (EditText) mView.findViewById(R.id.newname);
        noticeName.setText(args.getString("name"));
        builder.setView(mView)
            .setPositiveButton("OK", (dialog, which) -> {
                mListener.onDialogPositiveClick(RenameDialogFragment.this);
            })
            .setNegativeButton("Cancel", (dialog, which) -> {
                RenameDialogFragment.this.getDialog().cancel();
            });
        return builder.create();
    }
}
