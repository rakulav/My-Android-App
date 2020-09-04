package com.beamotivator.beam.models;


public class ModelPost {
    //use same name as given while uploading post
    String pId, pDescr,pLikes,group, pComments, pTime, uid, uEmail,uDp, uName, pImage,stamp;

    public ModelPost() {
    }

    public ModelPost(String pId, String pDescr, String pLikes, String group, String pComments, String pTime, String uid, String uEmail, String uDp, String uName, String pImage, String stamp) {
        this.pId = pId;
        this.pDescr = pDescr;
        this.pLikes = pLikes;
        this.group = group;
        this.pComments = pComments;
        this.pTime = pTime;
        this.uid = uid;
        this.uEmail = uEmail;
        this.uDp = uDp;
        this.uName = uName;
        this.pImage = pImage;
        this.stamp = stamp;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
//
//    public String getpTitle() {
//        return pTitle;
//    }
//
//    public void setpTitle(String pTitle) {
//        this.pTitle = pTitle;
//    }

    public String getpDescr() {
        return pDescr;
    }

    public void setpDescr(String pDescr) {
        this.pDescr = pDescr;
    }

    public String getpLikes() {
        return pLikes;
    }

    public void setpLikes(String pLikes) {
        this.pLikes = pLikes;
    }

    public String getpComments() {
        return pComments;
    }

    public void setpComments(String pComments) {
        this.pComments = pComments;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }



    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }
}
