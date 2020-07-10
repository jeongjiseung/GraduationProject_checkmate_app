package com.example.jjscheckmate.login;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Session extends Application {
    private static String userEmail;
    private static String userName;
    private static Uri userImage;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setSession(String userEmail, String userName, Uri userImage){
        this.userEmail=userEmail;
        this.userName=userName;
        this.userImage=userImage;

        Log.d("mawang", "Session setSession - email : " + this.userEmail);
        Log.d("mawang", "Session setSession - name : " + this.userName);
        Log.d("mawang", "Session setSession - uri : " + this.userImage);
    }

    public static String getUserEmail() {
        return userEmail;
    }
    public static void setUserEmail(String userEmail) {
        Session.userEmail = userEmail;
    }

    public static String getUserName() {
        return userName;
    }
    public static void setUserName(String userName) {
        Session.userName = userName;
    }

    public static void setUserImage(Uri userImage) {
        Session.userImage = userImage;
    }
    public static Uri getUserImage() {
        return userImage;
    }

    public static String getTime(){
        long now=Long.valueOf(String.valueOf(System.currentTimeMillis()));
        Date date=new Date(now);
//        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy MM dd hh:mm:ss");
        SimpleDateFormat simpleDate = new SimpleDateFormat("a hh:mm");
        String time = simpleDate.format(date);
        return time;
    }

}
