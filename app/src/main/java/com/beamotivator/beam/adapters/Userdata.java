package com.beamotivator.beam.adapters;

public class Userdata {

    private String uid ;

    public Userdata(String uid ) {
        this.uid = uid;



    }
    public Userdata(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }





    @Override
    public String toString() {
        return "Users{" +
                "uid='" + uid + '\'' +


                '}';
    }
}