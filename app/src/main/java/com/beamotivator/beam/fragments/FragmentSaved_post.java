package com.beamotivator.beam.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
;

import com.beamotivator.beam.R;
import com.beamotivator.beam.adapters.AdapterPosts;
import com.beamotivator.beam.adapters.AdapterPostsview;
import com.beamotivator.beam.adapters.AdapterSaved;
import com.beamotivator.beam.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class FragmentSaved_post extends Fragment {
    RecyclerView savedPostsRv;

    List<ModelPost> postList;
    AdapterSaved adapterPosts;

    FirebaseAuth firebaseAuth;
    String myId ;
    String ig = "";
    int count  = 0;
    public FragmentSaved_post() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_saved, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        myId = firebaseAuth.getCurrentUser().getUid();



        ;

        savedPostsRv =root. findViewById(R.id.mysave);

        postList = new ArrayList<>();

       // loadSavedPosts();



        return  root;
    }

    private void loadSavedPosts() {

        //linear layout for recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        //show newest posts, load from last
        layoutManager.setStackFromEnd(false);
        layoutManager.setReverseLayout(false);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(),3);
        savedPostsRv.setLayoutManager(layoutManager1);
        //set this layout to recycler view

        myId = firebaseAuth.getCurrentUser().getUid();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Extras").child(myId).child("Saved");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){

                    String postId = ""+ds.getKey();

                    //now check for the post details
                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Posts");
                    ref1.orderByChild("pId")
                            .equalTo(postId)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                                    for(DataSnapshot ds1:datasnapshot.getChildren()){


                                        ModelPost modelPost = ds1.getValue(ModelPost.class);


                                        //add post
                                        postList.add(modelPost);



                                        //adapter
                                        adapterPosts = new AdapterSaved(getActivity(),postList);

                                        //set adapter to recycler view

                                        savedPostsRv.setAdapter(adapterPosts);
                                        }
                                    SharedPreferences pC = requireContext().getSharedPreferences("count",MODE_PRIVATE);
                                    SharedPreferences.Editor pcc = pC.edit();
                                    pcc.putInt("size",postList.size());
                                    pcc.apply();
                                    //Toast.makeText(getContext(), ""+postList.size(), Toast.LENGTH_SHORT).show();




                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//
//        int count = 0;
//        if(savedPostsRv.getAdapter().getItemCount() != 0)
//        {
//            count = savedPostsRv.getAdapter().getItemCount();
//            Toast.makeText(getContext(), ""+count, Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toast.makeText(getContext(), "Poda", Toast.LENGTH_SHORT).show();
//        }
//        Toast.makeText(getContext(), ""+ Objects.requireNonNull(savedPostsRv.getAdapter()).getItemCount(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadSavedPosts();
    }
}

