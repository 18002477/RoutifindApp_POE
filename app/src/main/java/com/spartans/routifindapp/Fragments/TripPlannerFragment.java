package com.spartans.routifindapp.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.spartans.routifindapp.Activity.CreateTripActivity;
import com.spartans.routifindapp.Activity.EditTripActivity;
import com.spartans.routifindapp.Activity.TripDetailsActivity;
import com.spartans.routifindapp.Model.TripModel.TripPlannerModel;
import com.spartans.routifindapp.R;
import com.spartans.routifindapp.databinding.ActivityDirectionBinding;
import com.spartans.routifindapp.databinding.FragmentSettingsBinding;
import com.spartans.routifindapp.databinding.FragmentTripPlannerBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TripPlannerFragment extends Fragment {

    //Declaration of global variables
    private FloatingActionButton mCreateTripFab;
    private FirebaseAuth firebaseAuth;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter<TripPlannerModel,TripViewHolder> tripAdapter;

    public TripPlannerFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_planner, container, false);

        mCreateTripFab = view.findViewById(R.id.createtripfab);
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("All Trips");

        mCreateTripFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateTripActivity.class);
                startActivity(intent);

            }
        });

        Query query=firebaseFirestore.collection("trips").document(firebaseUser.getUid()).collection("myTrips").orderBy("title",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<TripPlannerModel> allUserTrips= new FirestoreRecyclerOptions.Builder<TripPlannerModel>().setQuery(query,TripPlannerModel.class).build();

        tripAdapter= new FirestoreRecyclerAdapter<TripPlannerModel, TripViewHolder>(allUserTrips) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull TripViewHolder tripViewHolder, int i, @NonNull TripPlannerModel tripPlannerModel) {


                ImageView popUpButton=tripViewHolder.itemView.findViewById(R.id.menupopbutton);

                int colourCode=getRandomColor();
                tripViewHolder.mTrip.setBackgroundColor(tripViewHolder.itemView.getResources().getColor(colourCode,null));

                tripViewHolder.txvTripTitle.setText(tripPlannerModel.getTitle());
                tripViewHolder.txtTripContent.setText(tripPlannerModel.getContent());

                String docId=tripAdapter.getSnapshots().getSnapshot(i).getId();

                tripViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //we have to open trip detail activity


                        Intent intent=new Intent(v.getContext(), TripDetailsActivity.class);
                        intent.putExtra("title",tripPlannerModel.getTitle());
                        intent.putExtra("content",tripPlannerModel.getContent());
                        intent.putExtra("tripId",docId);

                        v.getContext().startActivity(intent);

                    }
                });

                popUpButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PopupMenu popupMenu=new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Intent intent=new Intent(v.getContext(), EditTripActivity.class);
                                intent.putExtra("title",tripPlannerModel.getTitle());
                                intent.putExtra("content",tripPlannerModel.getContent());
                                intent.putExtra("tripId",docId);
                                v.getContext().startActivity(intent);
                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                //Toast.makeText(v.getContext(),"This note is deleted",Toast.LENGTH_SHORT).show();
                                DocumentReference documentReference=firebaseFirestore.collection("trips").document(firebaseUser.getUid()).collection("myTrips").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(v.getContext(),"This trip is deleted",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(v.getContext(),"Failed To Delete",Toast.LENGTH_SHORT).show();
                                    }
                                });


                                return false;
                            }
                        });

                        popupMenu.show();
                    }
                });

            }
            @NonNull
            @Override
            public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trips_layout,parent,false);
                return new TripViewHolder(view);
            }
        };

        mRecyclerView= view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(tripAdapter);

        return view;
    }

    public class TripViewHolder extends RecyclerView.ViewHolder
    {
        private TextView txvTripTitle;
        private TextView txtTripContent;
        LinearLayout mTrip;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            txvTripTitle=itemView.findViewById(R.id.triptitle);
            txtTripContent=itemView.findViewById(R.id.tripcontent);
            mTrip=itemView.findViewById(R.id.trip);

        }
    }

    @Override
    public void onStart ()
    {
        super.onStart();
        tripAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(tripAdapter!=null)
        {
            tripAdapter.stopListening();
        }
    }

    private int getRandomColor()
    {
        List<Integer> colorCode=new ArrayList<>();
        colorCode.add(R.color.gray);
        colorCode.add(R.color.pink);
        colorCode.add(R.color.lightgreen);
        colorCode.add(R.color.skyblue);
        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);

        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);
        colorCode.add(R.color.green);

        Random random=new Random();
        int number=random.nextInt(colorCode.size());
        return colorCode.get(number);
    }
}