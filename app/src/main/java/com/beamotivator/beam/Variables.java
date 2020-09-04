package com.beamotivator.beam;

import android.content.SharedPreferences;
import android.os.Environment;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Variables {



    public static final String device="android";

    public static int screen_width;
    public static int screen_height;


    public static final String root= Environment.getExternalStorageDirectory().toString();
    public static final String app_hided_folder =root+"/.Beam/";
    public static final String app_showing_folder =root +"/.Beam/";








}
