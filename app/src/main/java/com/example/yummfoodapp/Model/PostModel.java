package com.example.yummfoodapp.Model;

public class PostModel {
    String pImage, pTitle, pDescripition,pComments, pId, uDp, uName, uid, uEmail, pTime, pLikes;


    public PostModel(){


    }

    public PostModel(String pId, String uDp, String uName, String uid, String uEmail,String pImage, String pTitle, String pDescripition,String pTime, String pLikes, String pComments) {
        this.pId = pId;
        this.uDp = uDp;
        this.uName = uName;
        this.uid = uid;
        this.uEmail = uEmail;
        this.pTime = pTime;
        this.pImage = pImage;
        this.pTitle = pTitle;
        this.pDescripition = pDescripition;
        this.pLikes=pLikes;
        this.pComments= pComments;

    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpDescripition() {
        return pDescripition;
    }

    public void setpDescripition(String pDescripition) {
        this.pDescripition = pDescripition;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
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

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
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
}
