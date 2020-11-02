package com.jamespfluger.alexadevicefinder.activities.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.jamespfluger.alexadevicefinder.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        container.clearDisappearingChildren();
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }
}