package com.beamotivator.beam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.beamotivator.beam.adapters.AdapterPosts;
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
import java.util.Objects;

public class View_Post extends AppCompatActivity {
    RecyclerView savedPostsRv;

    List<ModelPost> postList;
    AdapterPosts adapterPosts;

    FirebaseAuth firebaseAuth;
    String myId ;
    int pos = 0;
    SharedPreferences sh;
    SharedPreferences pC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = this.getWindow();
            Drawable background = this.getResources().getDrawable(R.drawable.main_gradient);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(this.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.BLACK);
            }
        }
        setContentView(R.layout.activity_view__post);
        firebaseAuth = FirebaseAuth.getInstance();
        myId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        sh=  getSharedPreferences("posts",MODE_PRIVATE);
        pC = getSharedPreferences("count",MODE_PRIVATE);

  //      Intent intent = getIntent();
//        int position = Integer.parseInt(Objects.requireNonNull(intent.getStringExtra("position")));

        savedPostsRv =  findViewById(R.id.viewpost);
//        int total = Objects.requireNonNull(savedPostsRv.getAdapter()).getItemCount();
        postList = new ArrayList<>();

        Intent pintent = getIntent();

        int choice = sh.getInt("choice",3);
        int position = sh.getInt("position",0);
        int length = pC.getInt("size",0);
        int plength = pC.getInt("pSize",0);

        int total = length - position;
       // Toast.makeText(this, ""+choice, Toast.LENGTH_SHORT).show();

       // Toast.makeText(this, ""+total, Toast.LENGTH_SHORT).show();

        switch (choice)
        {
            case 1:
                loadMyPosts(choice);
                break;
            case 2:
                loadSavedPosts(total);
                break;

        }

    }

    private void loadSavedPosts(final int element) {

      //  Toast.makeText(this, ""+sh.getInt("position",0), Toast.LENGTH_SHORT).show();
        //linear layout for recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(View_Post.this);

        //show newest posts, load from last
       // Objects.requireNonNull(savedPostsRv.getLayoutManager()).scrollToPosition(pos);

//        layoutManager.setStackFromEnd(false);
        //layoutManager.setReverseLayout(true);

        //set this layout to recycler view
        savedPostsRv.setLayoutManager(layoutManager);

        myId = firebaseAuth.getCurrentUser().getUid();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Extras").child(myId).child("Saved");
        ref.limitToLast(element).addValueEventListener(new ValueEventListener() {
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
                                        adapterPosts = new AdapterPosts(View_Post.this,postList);

                                        //set adapter to recycler view
                                        savedPostsRv.setAdapter(adapterPosts);

                                         }




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

        }

    private void loadMyPosts(int choice) {

        //linear layout for recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        //show newest posts, load from last
        layoutManager.setStackFromEnd(false);
        layoutManager.setReverseLayout(false);
        savedPostsRv.setLayoutManager(layoutManager);

        Toast.makeText(this, ""+3, Toast.LENGTH_LONG).show();

        //set this layout to recycler view

        //Toast.makeText(this, ""+postList.size(), Toast.LENGTH_SHORT).show();

        myId = firebaseAuth.getCurrentUser().getUid();

        //now check for the post details
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Posts");
        ref1.orderByChild("uid")
//                .limitToLast(ele)
                .equalTo(sh.getString("uid",null))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                for(DataSnapshot ds1:datasnapshot.getChildren()){


                    ModelPost modelPost = ds1.getValue(ModelPost.class);


                    //add post
                    postList.add(modelPost);



                    //adapter
                    adapterPosts = new AdapterPosts(getApplicationContext(),postList);

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

