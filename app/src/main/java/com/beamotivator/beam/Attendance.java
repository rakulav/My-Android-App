
package com.beamotivator.beam;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class Attendance extends AppCompatActivity {

    private EditText present,total,percentage;
    private Button attendancecheck;
    private TextView attendanceresult,attendanceresult2;
    float presentc,totalc,percentagec,currentpercentage,perc,newperc;
    int resultc;

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
        setContentView(R.layout.activity_attendance);

        present=findViewById(R.id.checkpresent);
        total=findViewById(R.id.checktotal);
        percentage=findViewById(R.id.checkgoal);
        attendancecheck=findViewById(R.id.acheck);
        attendanceresult=findViewById(R.id.aresult);
        attendanceresult2=findViewById(R.id.bresult);

        attendancecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(present.getText().toString().trim()))
                {
                    if(!TextUtils.isEmpty(total.getText().toString().trim()))
                    {
                        if(!TextUtils.isEmpty(percentage.getText().toString().trim()))
                        {
                            checkAttendance();
                        }
                        else
                        {
                            Toast.makeText(Attendance.this, "Input field empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(Attendance.this, "Input field empty", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {

                    Toast.makeText(Attendance.this, "Input field empty", Toast.LENGTH_SHORT).show();


                }
            }
        });


    }

    public void checkAttendance(){

        presentc = Float.parseFloat(present.getText().toString().trim());
        totalc = Float.parseFloat(total.getText().toString().trim());
        percentagec = Float.parseFloat(percentage.getText().toString().trim());
        perc=percentagec/100;
        currentpercentage=(presentc/totalc)*100;
        attendanceresult2.setVisibility(View.INVISIBLE);
        attendanceresult.setVisibility(View.INVISIBLE);
        if (presentc!=0.0f && totalc!=0.0f && percentagec!=0.0f) {
            if(currentpercentage>percentagec){
                resultc = (int)Math.ceil((presentc-( perc* totalc))/(perc));
                resultc=resultc-1;
                newperc=(presentc/(totalc+resultc))*100;
                attendanceresult.setText("Your current percentage is "+String.format("%.2f", currentpercentage)+"%");
                attendanceresult.setVisibility(View.VISIBLE);
                if(resultc>0){
                    attendanceresult2.setText("You can bunk "+resultc+" more classes while keeping your attendance at "+String.format("%.2f", newperc)+"% !");
                    attendanceresult2.setVisibility(View.VISIBLE);}
                else if(resultc==0) {
                    attendanceresult2.setText("Don't bunk right now!");
                    attendanceresult2.setVisibility(View.VISIBLE);
                }

            }
            else if(currentpercentage<percentagec){
                resultc = (int)Math.ceil((( perc* totalc)-presentc)/(1-perc));
                resultc=resultc-1;
                newperc=((presentc+resultc)/(totalc+resultc))*100;

                attendanceresult.setText("Your current percentage is "+String.format("%.2f", currentpercentage)+"%");
                attendanceresult.setVisibility(View.VISIBLE);
                if(resultc>0){
                    attendanceresult2.setText(" Attend "+resultc+" more classes to keep your attendance at "+String.format("%.2f", newperc)+"% !");
                    attendanceresult2.setVisibility(View.VISIBLE);}
                else if(resultc==0) {
                    attendanceresult2.setText("Don't bunk right now!");
                    attendanceresult2.setVisibility(View.VISIBLE);
                }

            }
            else if(currentpercentage==percentagec){


                attendanceresult.setText("Current Percentage is "+String.format("%.2f", currentpercentage)+"%. Don't bunk right now!");
                attendanceresult2.setVisibility(View.GONE);

            }
        }
        else{
            Toast.makeText(Attendance.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
        }
    }
}

