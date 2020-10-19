package com.jamespfluger.alexadevicefinder.activities.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jamespfluger.alexadevicefinder.R;

public class AboutFragment extends Fragment {

    private AboutViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = ViewModelProviders.of(this).get(AboutViewModel.class);
        container.clearDisappearingChildren();
        getActivity().getFragmentManager().popBackStack();
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        return root;
    }
}