package com.beamotivator.beam.No_Internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.beamotivator.beam.DashboardActivity;
import com.beamotivator.beam.R;
import com.google.android.material.snackbar.Snackbar;


import static android.graphics.Color.BLACK;

public class Internet_off extends AppCompatActivity {
Button check;
Snackbar s;
    private BroadcastReceiver MyReceiverr = null;

FrameLayout vie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            Drawable background = this.getResources().getDrawable(R.drawable.main_gradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(android.R.color.transparent));
            // window.setNavigationBarColor(this.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);

        }

        setContentView(R.layout.activity_internet_off);

        MyReceiverr = new MyReceiver_Splash();
        broadcastIntent();
        check=findViewById(R.id.checkinternet);
        vie=findViewById(R.id.checkview);


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork=cm.getActiveNetworkInfo();

                boolean isConnected=activeNetwork !=null && activeNetwork.isConnected();
                if (isConnected){




                            Intent i = new Intent(Internet_off.this, DashboardActivity.class);
                            startActivity(i);
                            finish();
                            isConnected = false;
                }
                else{

                    s = Snackbar.make(vie, "Check your internet connection", Snackbar.LENGTH_SHORT);
                    View snackBarView = s.getView();
                    snackBarView.setBackgroundColor(BLACK);
                    s.show();
                    isConnected =false;




                }





            }
        });



    }

    private void broadcastIntent() {

        registerReceiver(MyReceiverr, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }
}
