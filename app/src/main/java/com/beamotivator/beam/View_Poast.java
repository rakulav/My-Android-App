package com.beamotivator.beam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.beamotivator.beam.adapters.AdapterPosts;
import com.beamotivator.beam.adapters.AdapterPosts1;
import com.beamotivator.beam.adapters.AdapterPostsview;
import com.beamotivator.beam.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class View_Poast extends AppCompatActivity {
    RecyclerView savedPostsRv;

    List<ModelPost> postList;
    AdapterPosts1 adapterPosts;

    FirebaseAuth firebaseAuth;
    String myId ;
    String ig = "";
    int count  = 0;
    SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(android.R.color.white));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.BLACK);
            }
        }
        setContentView(R.layout.activity_view__poast);
        firebaseAuth = FirebaseAuth.getInstance();
        myId = firebaseAuth.getCurrentUser().getUid();

        sh=  getSharedPreferences("posts",MODE_PRIVATE);



        savedPostsRv =  findViewById(R.id.viewpost);
         postList = new ArrayList<>();

        loadSavedPosts();
//


     }

    private void loadSavedPosts() {

        //linear layout for recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        //show newest posts, load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        savedPostsRv.setLayoutManager(layoutManager);

        //set this layout to recycler view




        myId = firebaseAuth.getCurrentUser().getUid();



//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(pI).child("Saved");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                postList.clear();
//                for(DataSnapshot ds:snapshot.getChildren()){
//
//                    String postId = ""+ds.getKey();

        //now check for the post details
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Posts");
        ref1.orderByChild("uid").equalTo(sh.getString("uid",null)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                for(DataSnapshot ds1:datasnapshot.getChildren()){


                    ModelPost modelPost = ds1.getValue(ModelPost.class);


                    //add post
                    postList.add(modelPost);



                    //adapter
                    adapterPosts = new AdapterPosts1(getApplicationContext(),postList);

                    //set adapter to recycler view
                    savedPostsRv.setAdapter(adapterPosts);
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void onBackPressed() {

        SharedPreferences.Editor e = sh.edit();
        e.clear();
        e.apply();
        finish();
    }
}

