package com.spartans.routifindapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.spartans.routifindapp.R;
import com.spartans.routifindapp.SavedLocationInterface;
import com.spartans.routifindapp.SavedPlaceModel;
import com.spartans.routifindapp.Utility.LoadingDialog;
import com.spartans.routifindapp.databinding.FragmentSavedPlacesBinding;
import com.spartans.routifindapp.databinding.SavedItemLayoutBinding;

import java.util.ArrayList;

public class SavedPlacesFragment extends Fragment implements SavedLocationInterface{

    private FragmentSavedPlacesBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<SavedPlaceModel> savedPlaceModelArrayList;
    private LoadingDialog loadingDialog;
    private FirebaseRecyclerAdapter<String, ViewHolder> firebaseRecyclerAdapter;
    private SavedLocationInterface savedLocationInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentSavedPlacesBinding.inflate(inflater, container, false);
        savedLocationInterface = this;
        firebaseAuth = FirebaseAuth.getInstance();
        savedPlaceModelArrayList = new ArrayList<>();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Saved Places");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new LoadingDialog(requireActivity());
        binding.savedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.savedRecyclerView);
        getSavedPlaces();
    }

    private void getSavedPlaces()
    {
        loadingDialog.startLoading();
        Query query = FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseAuth.getUid()).child("Saved Locations");

        FirebaseRecyclerOptions<String> options = new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(query, String.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<String, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull String savePlaceId) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Places").child(savePlaceId);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            SavedPlaceModel savedPlaceModel = snapshot.getValue(SavedPlaceModel.class);
                            holder.binding.setSavedPlaceModel(savedPlaceModel);
                            holder.binding.setListener(savedLocationInterface);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                SavedItemLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()),
                        R.layout.saved_item_layout, parent, false);
                return new ViewHolder(binding);
            }
        };
        binding.savedRecyclerView.setAdapter(firebaseRecyclerAdapter);
        loadingDialog.stopLoading();
    }

    @Override
    public void onResume() {
        super.onResume();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        firebaseRecyclerAdapter.stopListening();
    }

    @Override
    public void onLocationClick(SavedPlaceModel savedPlaceModel) {
        Toast.makeText(getContext(), "Place Clicked", Toast.LENGTH_SHORT).show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SavedItemLayoutBinding binding;
        public ViewHolder(@NonNull SavedItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}