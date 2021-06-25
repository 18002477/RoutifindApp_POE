package com.spartans.routifindapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spartans.routifindapp.R;
import com.spartans.routifindapp.Utility.LoadingDialog;
import com.spartans.routifindapp.databinding.FragmentDistanceUnitsBinding;
import com.spartans.routifindapp.databinding.FragmentHomeBinding;
import com.spartans.routifindapp.databinding.FragmentSettingsBinding;

public class DistanceUnitsFragment extends Fragment {

    private FragmentDistanceUnitsBinding binding;
    private LoadingDialog loadingDialog;

    public DistanceUnitsFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        binding = FragmentDistanceUnitsBinding.inflate(inflater, container, false);
        loadingDialog = new LoadingDialog(getActivity());


        return binding.getRoot();
    }
}