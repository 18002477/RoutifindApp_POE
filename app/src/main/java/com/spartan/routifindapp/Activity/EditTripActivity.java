package com.spartan.routifindapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spartan.routifindapp.Fragments.TripPlannerFragment;
import com.spartan.routifindapp.R;

import java.util.HashMap;
import java.util.Map;

public class EditTripActivity extends AppCompatActivity {

    private Intent data;
    private EditText mEditTitleOfTrip,mEditContentOfTrip;
    private FloatingActionButton mSaveEditTrip;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        mEditTitleOfTrip=findViewById(R.id.edittitleoftrip);
        mEditContentOfTrip=findViewById(R.id.editcontentoftrip);
        mSaveEditTrip=findViewById(R.id.saveedittrip);

        data=getIntent();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        toolbar=findViewById(R.id.toolbarofedittrip);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSaveEditTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle=mEditTitleOfTrip.getText().toString();
                String newContent=mEditContentOfTrip.getText().toString();

                if(newTitle.isEmpty()||newContent.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Field cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    DocumentReference documentReference=firebaseFirestore.collection("trips").document(firebaseUser.getUid()).collection("myTrips").document(data.getStringExtra("tripId"));
                    Map<String,Object> trip=new HashMap<>();
                    trip.put("title",newTitle);
                    trip.put("content",newContent);
                    documentReference.set(trip).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Note is updated",Toast.LENGTH_SHORT).show();
                            // Taking you back to your initial fragment
                            finish();
                            Fragment mFragment = null;
                            mFragment = new TripPlannerFragment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.loadFromEdit_PlaceHolder, mFragment).commit();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed To update",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        String tripTitle=data.getStringExtra("title");
        String tripContent=data.getStringExtra("content");
        mEditContentOfTrip.setText(tripContent);
        mEditTitleOfTrip.setText(tripTitle);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
                Fragment mFragment = null;
                mFragment = new TripPlannerFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.loadFromEdit_PlaceHolder, mFragment).commit();
            }
        });
    }
}