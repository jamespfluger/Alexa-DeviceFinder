package com.jamespfluger.devicefinder.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.jamespfluger.devicefinder.R;

public class BlankFragment extends Fragment {
    public BlankFragment() {
        // Required empty public constructor
    }

    public static BlankFragment newInstance() {
        BlankFragment fragment = new BlankFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }
}