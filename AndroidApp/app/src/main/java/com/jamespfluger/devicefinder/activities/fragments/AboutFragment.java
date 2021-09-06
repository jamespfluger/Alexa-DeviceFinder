package com.jamespfluger.devicefinder.activities.fragments;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jamespfluger.devicefinder.R;

public class AboutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.clearDisappearingChildren();
        }

        if (getActivity() != null) {
            getActivity().getFragmentManager().popBackStack();
        }

        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView issuesView = view.findViewById(R.id.about_issues_content);
        TextView contributingView = view.findViewById(R.id.about_contributing_content);

        issuesView.setMovementMethod(LinkMovementMethod.getInstance());
        contributingView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
