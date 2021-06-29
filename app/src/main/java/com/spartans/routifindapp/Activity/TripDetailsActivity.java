package com.spartans.routifindapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spartans.routifindapp.Fragments.TripPlannerFragment;
import com.spartans.routifindapp.R;

public class TripDetailsActivity extends AppCompatActivity {

    private TextView mTitleOfTripDetail,mContentOfTripDetail;
    private FloatingActionButton mGoToEditTrip;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        mTitleOfTripDetail = findViewById(R.id.titleoftripdetail);
        mContentOfTripDetail = findViewById(R.id.contentoftripdetail);
        mGoToEditTrip = findViewById(R.id.gotoedittrip);

        toolbar= findViewById(R.id.toolbaroftripdetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data=getIntent();

        mGoToEditTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),EditTripActivity.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("tripId",data.getStringExtra("tripId"));
                v.getContext().startActivity(intent);
                finish();
            }
        });

        mContentOfTripDetail.setText(data.getStringExtra("content"));
        mTitleOfTripDetail.setText(data.getStringExtra("title"));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
                Fragment mFragment = null;
                mFragment = new TripPlannerFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.loadFromDetails_PlaceHolder, mFragment).commit();
            }
        });
    }
}