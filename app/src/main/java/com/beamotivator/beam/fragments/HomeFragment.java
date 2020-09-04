package com.beamotivator.beam.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beamotivator.beam.AboutActivity;
import com.beamotivator.beam.Attendance;
import com.beamotivator.beam.DashboardActivity;
import com.beamotivator.beam.MainActivity;
import com.beamotivator.beam.R;
import com.beamotivator.beam.SavedPost;
import com.beamotivator.beam.SuggestionsActivity;
import com.beamotivator.beam.ThierProfile;
import com.beamotivator.beam.TodoMain;
import com.beamotivator.beam.adapters.AdapterPosts;
import com.beamotivator.beam.adapters.Userdata;
import com.beamotivator.beam.models.ModelPost;
import com.beamotivator.beam.models.ModelUser;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment  implements View.OnClickListener {

    //firebase auth
    FirebaseAuth firebaseAuth;

    RelativeLayout empty;
    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;
    ShimmerFrameLayout mShimmerViewContainer;
    private SwipeRefreshLayout mSwipeRefreshLayout;
ImageView navbtn,imagenavigation;
    //init views
    CircleImageView wokImage;
    TextView homeEmpty,wokPoints,wokname, greetName,homeTitle;

     ImageView homeimg;
    ConstraintLayout wokDisplay;
    DrawerLayout drawer;

    GoogleSignInClient mGoogleSignInClient;
TextView navigationname,navigationemail;
    CardView wokCard;
LinearLayout menuLogout,setgoals,bunkcheck,saved_Posts,personalInfo,menuSuggestions,aboutus;
//Toolbar toolbar;
    //To get resources text
    Resources resources;
ImageView getHomeimg;
String Uid;
    String myUid;
    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            Drawable background = getActivity().getResources().getDrawable(R.drawable.main_gradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getActivity().getResources().getColor(android.R.color.transparent));
            // window.setNavigationBarColor(getActivity().getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);

        }

        final View view =  inflater.inflate(R.layout.fragment_home, container, false);
load();
        mShimmerViewContainer = view.findViewById(R.id.postshimmer);
        mShimmerViewContainer.startShimmer();
        homeimg = view.findViewById(R.id.homeimgfc);
        drawer = view.findViewById(R.id.drawer_layout);

        navbtn = view.findViewById(R.id.menubt);
        navbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view_home);
        View headerView = navigationView.getHeaderView(0);

        homeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(getActivity(), ThierProfile.class);
                intent.putExtra("uid",Uid);
                startActivity(intent);

            }
        });



        imagenavigation = view.findViewById(R.id.imagenavigation);
        navigationname = view.findViewById(R.id.navigationname);
        navigationemail = view.findViewById(R.id.navigationemail);

        menuLogout = view.findViewById(R.id.menuLogout);
        setgoals = view.findViewById(R.id.setgoals);
        bunkcheck = view.findViewById(R.id.bunkcheck);
//         saved_Posts = view.findViewById(R.id.saved_Posts);
        personalInfo = view.findViewById(R.id.personalInfo);
        menuSuggestions = view.findViewById(R.id.menuSuggestions);
        aboutus = view.findViewById(R.id.aboutus);


        menuLogout.setOnClickListener(this);
        setgoals.setOnClickListener(this);
        bunkcheck.setOnClickListener(this);
//        saved_Posts.setOnClickListener(this);
        personalInfo.setOnClickListener(this);
        menuSuggestions.setOnClickListener(this);
        aboutus.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        myUid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        wokDisplay = view.findViewById(R.id.wokDisplay);

        wokCard = view.findViewById(R.id.wokCard);
        wokCard.setVisibility(View.GONE);

        wokCard = view.findViewById(R.id.wokCard);
        wokCard.setVisibility(View.GONE);

        empty = view.findViewById(R.id.emptyLayout);
        wokImage = view.findViewById(R.id.wokImage);
        wokPoints = view.findViewById(R.id.wokPoints);
        wokname=view.findViewById(R.id.wokName);
        //greetName=view.findViewById(R.id.home_username_greet);
        homeEmpty = view.findViewById(R.id.homeMessage);
         homeTitle = view.findViewById(R.id.homeTitle);

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
        homeTitle.startAnimation(animation);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);



        //set home navigation




        //init the drawer layout

        //set navigation for drawer

        //set values of menu drawer
        DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference("Users");
        menuRef.child(myUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String mName =""+snapshot.child("name").getValue();
                        String mImage = ""+snapshot.child("image").getValue();
                        String email = ""+snapshot.child("email").getValue();
                         navigationname.setText(mName);
//                        greetName.setText(mName);
                        navigationemail.setText(email);

                        try {
                            Glide.with(getActivity())
                                    .load(mImage)
                                    .centerInside()
                                    .into(imagenavigation);
                        }
                        catch (Exception e) {
//                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                        try {
                            Glide.with(getActivity())
                                    .load(mImage)
                                    .centerInside()
                                    .into(homeimg);
                        }
                        catch (Exception e) {
//                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }




                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        resources = getResources();

        //recycler view and its properties
        recyclerView = view.findViewById(R.id.postsRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        //show newest post, for this load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        //set layout to recycelr view
        recyclerView.setLayoutManager(layoutManager);

        //init post list
        postList = new ArrayList<>();


        loadPosts();



        return view;
    }





    private void load() {

        FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");
        Query query = users.orderByChild("email").equalTo(userr.getEmail());
        final List<Userdata> user = new ArrayList<>();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    user.add(dataSnapshot.getValue(Userdata.class));
                    Userdata userlogin = dataSnapshot.getValue(Userdata.class);
                    Uid = userlogin.getUid().toString();



                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {




            }
        });
    }

    private void logout() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //ask confirmation
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure to logout?");
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth.signOut();
                mGoogleSignInClient.signOut();

                checkuserstatus();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }








    private void loadPosts() {
        final int len = 0;
        //linear layout for recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        //show newest posts, load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        //set this layout to recycler view
        recyclerView.setLayoutManager(layoutManager);

        //path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        final DatabaseReference gRef = FirebaseDatabase.getInstance().getReference("Groups");

        //get all data from this ref
        ref.orderByChild("group").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    final ModelPost modelPost = ds.getValue(ModelPost.class);
                    final String group = Objects.requireNonNull(modelPost).getGroup();
                    if(group != null)
                    {
                        gRef.child(group).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                if(snapshot.child("Participants").hasChild(myUid))
                                {
                                    postList.add(modelPost);

                                    //adapter posts
                                    adapterPosts = new AdapterPosts(getActivity(),postList);

                                    //set adapter to recyclerview
                                    recyclerView.setAdapter(adapterPosts);
                                }

                                if(postList.size() == 0)
                                {
                                    empty.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                    homeEmpty.setVisibility(View.VISIBLE);
                                }
                                else {
                                    empty.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    homeEmpty.setVisibility(View.GONE);
                                }
                                mShimmerViewContainer.stopShimmer();

                            }






                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

            //  Toast.makeText(getContext(), ""+postList.size(), Toast.LENGTH_SHORT).show();

                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case of error
            }
        });
    }


    @Override
    public void onStart() {

        super.onStart();
        checkuserstatus();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); //to show options menu in fragment
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkuserstatus();
    }

    @Override
    public void onPause() {
        super.onPause();
    }





    public void checkuserstatus()
    {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            //stay signed in
            //set email of user
            //protxt.setText(user.getEmail());
        }
        else
        {
            mGoogleSignInClient.signOut();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }




    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.setgoals:
                Intent i = new Intent(getActivity(), TodoMain.class);
                startActivity(i);
                getActivity();
                break;
            case R.id.bunkcheck:
                Intent xi = new Intent(getActivity(), Attendance.class);
                startActivity(xi);
                getActivity();
                break;

//            case R.id.saved_Posts:
//                startActivity(new Intent(getActivity(), SavedPost.class));
//                break;

            case R.id.personalInfo:
                Intent prof = new Intent(getActivity(), ThierProfile.class);
                prof.putExtra("uid",myUid);
                startActivity(prof);
                break;
            case R.id.menuSuggestions:
                startActivity(new Intent(getActivity(), SuggestionsActivity.class));
                break;

            case R.id.menuLogout:
                logout();
                break;
            case R.id.aboutus:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;

        }





        drawer.closeDrawer(Gravity.LEFT);

    }



}