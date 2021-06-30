package com.spartan.routifindapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spartan.routifindapp.Model.DistanceUnitsModel.UnitSettingsModel;
import com.spartan.routifindapp.Utility.LoadingDialog;
import com.spartan.routifindapp.databinding.FragmentDistanceUnitsBinding;
import com.spartan.routifindapp.databinding.FragmentHomeBinding;
import com.spartan.routifindapp.databinding.FragmentSettingsBinding;

import java.util.HashMap;
import java.util.Map;

public class DistanceUnitsFragment extends Fragment {

    private FragmentDistanceUnitsBinding binding;
    private LoadingDialog loadingDialog;
    private UnitSettingsModel unitSettingsModel;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

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
        loadingDialog = new LoadingDialog(requireActivity());

        unitSettingsModel = new UnitSettingsModel();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference documentReference=firebaseFirestore.collection("settings").document(firebaseUser.getUid()).collection("mySettings").document(firebaseUser.getUid());
        Map<String ,Object> setting= new HashMap<>();
        setting.put("unit",unitSettingsModel);



        // Retrieving the value for Firestore and setting the value of the radio button
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    loadingDialog.startLoading();

                    Map<String ,Object> objValue= documentSnapshot.getData();
                    String data = String.valueOf(objValue.get("unit"));

                    String km = "{unit=Km}";
                    String miles = "{unit=Miles}";

                    //Toast.makeText(getContext(), data, Toast.LENGTH_LONG).show();

                    if (data.equals(km))
                    {
                        binding.radKilometers.setChecked(true);
                    }
                    else if(data.equals(miles))
                    {
                        binding.radMiles.setChecked(true);
                    }
                    loadingDialog.stopLoading();
                }
                else
                {
                    Toast.makeText(getContext(), "Please select a setting", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        binding.btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loadingDialog.startLoading();
                String unitKm = binding.radKilometers.getText().toString();
                String unitM = binding.radMiles.getText().toString();

                if (binding.radKilometers.isChecked()) {
                    unitSettingsModel.setUnit(unitKm);
                    documentReference.set(setting);
                    Toast.makeText(getContext(), "Setting Saved !", Toast.LENGTH_SHORT).show();
                }
                else if(binding.radMiles.isChecked())
                {
                    unitSettingsModel.setUnit((unitM));
                    documentReference.set(setting);
                    Toast.makeText(getContext(), "Setting Saved !", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Please select an option", Toast.LENGTH_SHORT).show();
                }

                loadingDialog.stopLoading();

            }
        });
        return binding.getRoot();
    }
}