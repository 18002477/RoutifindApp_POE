package com.spartans.routifindapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spartans.routifindapp.R;
import com.spartans.routifindapp.databinding.ActivityDirectionBinding;
import com.spartans.routifindapp.databinding.FragmentSettingsBinding;
import com.spartans.routifindapp.databinding.FragmentTripPlannerBinding;

public class TripPlannerFragment extends Fragment {

    private FragmentTripPlannerBinding binding;

    public TripPlannerFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        binding = FragmentTripPlannerBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }
}