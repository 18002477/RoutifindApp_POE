package com.spartans.routifindapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spartans.routifindapp.Fragments.TripPlannerFragment;
import com.spartans.routifindapp.R;
import com.spartans.routifindapp.Utility.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

public class CreateTripActivity extends AppCompatActivity {

    // Variables Declarations
    private EditText mCreateTitleOfTrip,mCreateContentOfTrip;
    private FloatingActionButton mSaveTrip;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private Toolbar toolbar;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        mSaveTrip=findViewById(R.id.savetrip);
        mCreateContentOfTrip=findViewById(R.id.createcontentoftrip);
        mCreateTitleOfTrip=findViewById(R.id.createtitleoftrip);

        loadingDialog = new LoadingDialog(this);

        toolbar=findViewById(R.id.toolbarofcreatetrip);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        mSaveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=mCreateTitleOfTrip.getText().toString();
                String content=mCreateContentOfTrip.getText().toString();
                if(title.isEmpty() || content.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Both field are Require",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    loadingDialog.startLoading();

                    DocumentReference documentReference=firebaseFirestore.collection("trips").document(firebaseUser.getUid()).collection("myTrips").document();
                    Map<String ,Object> trip= new HashMap<>();
                    trip.put("title",title);
                    trip.put("content",content);

                    documentReference.set(trip).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Taking you back to your initial fragment
                            finish();
                            Fragment mFragment = null;
                            mFragment = new TripPlannerFragment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.load_tripPlannerPlaceHolder, mFragment).commit();



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed To Create trip",Toast.LENGTH_SHORT).show();
                            loadingDialog.stopLoading();
                            //startActivity(new Intent(CreateTripActivity.this,TripPlannerActivity.class));
                            // Back to trip Planner Fragment
                            finish();
                            Fragment mFragment = null;
                            mFragment = new TripPlannerFragment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.load_tripPlannerPlaceHolder, mFragment).commit();

                        }
                    });
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
                Fragment mFragment = null;
                mFragment = new TripPlannerFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.load_tripPlannerPlaceHolder, mFragment).commit();
            }
        });
    }

}