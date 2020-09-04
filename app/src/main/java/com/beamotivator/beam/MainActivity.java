package com.beamotivator.beam;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView1, imageView2;
    private ValueAnimator valueAnimator;
    private  static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    FirebaseUser user1;
    FloatingActionButton signIn;
    ProgressBar progress_bar;
    TextView appversion;
    AuthCredential credential;
    FirebaseAuth firebaseAuth;
    ProgressBar prgresbarlogin;
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

        setContentView(R.layout.activity_main);


        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        prgresbarlogin = (ProgressBar) findViewById(R.id.prgresbarlogin);


        appversion= findViewById(R.id.appversion);
        String versionName = BuildConfig.VERSION_NAME;
        appversion.setText("BEAM"+versionName);


        signIn = findViewById(R.id.SignIn);
      GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("444026620385-63bcinfreu193nga04u936skv895obvo.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();



        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //searchAction();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });



    }

    private void firebaseAuthWithGoogle(final String idToken) {
        prgresbarlogin.setVisibility(View.VISIBLE);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            final String personName = Objects.requireNonNull(acct.getDisplayName()).trim();
            final String personEmail = acct.getEmail().trim();
            Uri personPhoto = acct.getPhotoUrl();
            final String proPic = Objects.requireNonNull(personPhoto).toString();
            final String name = personName.substring(0, personName.lastIndexOf(" "));
            if(personEmail.endsWith("christuniversity.in")) {
                credential = GoogleAuthProvider.getCredential(idToken, null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    prgresbarlogin.setVisibility(View.GONE);
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    //Get user email and id from auth
                                    String email = Objects.requireNonNull(user).getEmail();
                                    String uid = user.getUid();

                                    //When user registers store data in firebase database using hashmap
                                    HashMap<Object, String> hashMap = new HashMap<>();
                                    //put info to hashmap
                                    hashMap.putIfAbsent("email", email);
                                    hashMap.putIfAbsent("uid", uid);
                                    hashMap.putIfAbsent("name", name);
                                    hashMap.putIfAbsent("image", proPic);

                                    //Firebase database instance
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                                    //Add path to store data "Users"
                                    DatabaseReference reference = firebaseDatabase.getReference("Users");

                                    //put data within hashmap in database
                                    reference.child(uid).setValue(hashMap);

                                    //goto profile activity after logging in
                                    startActivity(new Intent(MainActivity.this, Splash.class));

                                }
                            }

//                        private void loaddilog() {
//
//
//
//
//                            final Dialog dialog = new Dialog(MainActivity.this);
//                             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
//                            dialog.setContentView(R.layout.dialog_dark);
//                            dialog.setCancelable(false);
//
//                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//                            lp.copyFrom(dialog.getWindow().getAttributes());
//                            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                            ImageView imageView=dialog.findViewById(R.id.imagenoentry);
//                            TextView textView=dialog.findViewById(R.id.titler);
//                            TextView contentr=dialog.findViewById(R.id.contentr);
//                            TextView statement=dialog.findViewById(R.id.statement);
//
//
//
//
//                            try {
//                Glide.with(MainActivity.this)
//                        .load(proPic)
//                        .centerInside()
//                        .into(imageView);
//            }
//            catch (Exception e) {
//                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//                            textView.setText(personName);
//                            contentr.setText(personEmail);
//                            statement.setText(personName+"\n"+"Sorry To Say Your Are Not Abel To Login ");
//
//                            ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                    GoogleSignIn.getClient(
//                                            getApplicationContext(),
//                                            new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
//                                    ).signOut();
//
//                                            //checkuserstatus();
//                                   dialog.dismiss();
//
//
//
//
//                                }
//                            });
//
//
//
//                            dialog.show();
//
//                            dialog.getWindow().setAttributes(lp);
//
//
//
//
//
//
//
//                        }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                prgresbarlogin.setVisibility(View.GONE);
                mGoogleSignInClient.signOut();
                Toast.makeText(this, "Not a valid Christ account"+personEmail, Toast.LENGTH_SHORT).show();
            }
        }
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN ) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        valueAnimator.end();

    }

    private void searchAction() {
        progress_bar.setVisibility(View.VISIBLE);
        signIn.setAlpha(0f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress_bar.setVisibility(View.GONE);
                signIn.setAlpha(1f);
                //  Snackbar.make(parent_view, "Login data submitted", Snackbar.LENGTH_SHORT).show();
            }
        }, 1000);
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
           //  finish();
        }
    }
}