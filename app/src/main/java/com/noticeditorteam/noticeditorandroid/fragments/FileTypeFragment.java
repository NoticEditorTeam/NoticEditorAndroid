package com.noticeditorteam.noticeditorandroid.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.noticeditorteam.noticeditorandroid.R;
import com.noticeditorteam.noticeditorandroid.io.exportstrategies.ExportStrategy;
import com.noticeditorteam.noticeditorandroid.io.exportstrategies.ExportStrategyHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FileTypeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FileTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileTypeFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PATH = "path";

    // TODO: Rename and change types of parameters
    private String mPath;
    private int index;

    private OnFragmentInteractionListener mListener;

    public FileTypeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param path Parameter 1.
     * @return A new instance of fragment FileTypeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FileTypeFragment newInstance(String path) {
        FileTypeFragment fragment = new FileTypeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PATH, path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPath = getArguments().getString(ARG_PATH);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View mView = inflater.inflate(R.layout.fragment_file_type, null);
        Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mView.getContext(),
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ExportStrategy[] strategies = ExportStrategyHolder.values();
        for(ExportStrategy strategy : strategies) {
            adapter.add(strategy.getFormatName());
        }
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                index = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        builder.setView(mView)
                .setPositiveButton("OK", (dialog, which) -> {
                    ExportStrategy strategy = strategies[index];
                    String ext = strategy.getFileExtension();
                    if(!mPath.endsWith(ext)) mPath += ext;
                    mListener.onFragmentInteraction(mPath, strategy);
                })
                .setNegativeButton("Cancel", (dialog, which) -> FileTypeFragment.this.getDialog().cancel());
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String path, ExportStrategy strategy);
    }
}
