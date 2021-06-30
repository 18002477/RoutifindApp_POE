package com.spartan.routifindapp.Adapter;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.android.SphericalUtil;
import com.spartan.routifindapp.databinding.InfoWindowLayoutBinding;

import java.text.DecimalFormat;
import java.util.Map;

public class InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private InfoWindowLayoutBinding binding;
    private Location location;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    DecimalFormat df = new DecimalFormat("#.#");


    public InfoWindowAdapter(Location location, Context context) {
        this.location = location;
        this.context = context;

        binding = InfoWindowLayoutBinding.inflate(LayoutInflater.from(context), null, false);
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference documentReference=firebaseFirestore.collection("settings").document(firebaseUser.getUid()).collection("mySettings").document(firebaseUser.getUid());
        binding.txtLocationName.setText(marker.getTitle());

        // Retrieving the measurement units value from Firestore database
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                double distance = SphericalUtil.computeDistanceBetween(new LatLng(location.getLatitude(), location.getLongitude()),
                        marker.getPosition());

                if(documentSnapshot.exists())
                {
                    Map<String ,Object> objValue= documentSnapshot.getData();

                    String data = String.valueOf(objValue.get("unit"));
                    String km = "{unit=Km}";
                    String miles = "{unit=Miles}";

                    if (data.equals(km))
                    {
                        double kilometers = distance / 1000;
                        binding.txtLocationDistance.setText(df.format(kilometers) + " KM");

                        float speed = location.getSpeed();

                        if (speed > 0) {
                            double time = distance / speed;
                            binding.txtLocationTime.setText(time + " sec");
                        } else {
                            binding.txtLocationTime.setText("N/A");
                        }
                    }
                    else if(data.equals(miles))
                    {
                        double dMiles  = distance / 1609.34;
                        binding.txtLocationDistance.setText(df.format(dMiles) + " Miles");

                        float speed = location.getSpeed();

                        if (speed > 0) {
                            double time = distance / speed;
                            binding.txtLocationTime.setText(time + " sec");
                        } else {
                            binding.txtLocationTime.setText("N/A");
                        }
                    }

                }
                else if (!documentSnapshot.exists())
                {
                    double kilometers = distance / 1000;
                    binding.txtLocationDistance.setText(df.format(kilometers) + " KM");

                    float speed = location.getSpeed();

                    if (speed > 0) {
                        double time = distance / speed;
                        binding.txtLocationTime.setText(time + " sec");
                    } else {
                        binding.txtLocationTime.setText("N/A");
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.e("Error",e.getMessage());
            }
        });
        return binding.getRoot();
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference documentReference=firebaseFirestore.collection("settings").document(firebaseUser.getUid()).collection("mySettings").document(firebaseUser.getUid());
        binding.txtLocationName.setText(marker.getTitle());


        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                double distance = SphericalUtil.computeDistanceBetween(new LatLng(location.getLatitude(), location.getLongitude()),
                        marker.getPosition());

                if(documentSnapshot.exists())
                {
                    Map<String ,Object> objValue= documentSnapshot.getData();

                    String data = String.valueOf(objValue.get("unit"));
                    String km = "{unit=Km}";
                    String miles = "{unit=Miles}";

                    if (data.equals(km))
                    {
                        double kilometers = distance / 1000;
                        binding.txtLocationDistance.setText(df.format(kilometers) + " KM");

                        float speed = location.getSpeed();

                        if (speed > 0) {
                            double time = distance / speed;
                            binding.txtLocationTime.setText(time + " sec");
                        } else {
                            binding.txtLocationTime.setText("N/A");
                        }
                    }
                    else if(data.equals(miles))
                    {
                        double dMiles  = distance / 1609.34;
                        binding.txtLocationDistance.setText(df.format(dMiles) + " Miles");

                        float speed = location.getSpeed();

                        if (speed > 0) {
                            double time = distance / speed;
                            binding.txtLocationTime.setText(time + " sec");
                        } else {
                            binding.txtLocationTime.setText("N/A");
                        }
                    }
                }
                else if (!documentSnapshot.exists())
                {
                    double kilometers = distance / 1000;
                    binding.txtLocationDistance.setText(df.format(kilometers) + " KM");

                    float speed = location.getSpeed();

                    if (speed > 0) {
                        double time = distance / speed;
                        binding.txtLocationTime.setText(time + " sec");
                    } else {
                        binding.txtLocationTime.setText("N/A");
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.e("Error",e.getMessage());
            }
        });
        return binding.getRoot();
    }

}
