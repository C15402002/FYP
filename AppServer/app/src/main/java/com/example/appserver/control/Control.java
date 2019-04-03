package com.example.appserver.control;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.appserver.config.RemoteAPIService;
import com.example.appserver.config.RetroClient;
import com.example.appserver.model.MakeOrder;
import com.example.appserver.model.User;


import java.util.Calendar;
import java.util.Locale;

public class Control {
    public static User currentUser;
    public static MakeOrder currentOrder;

    public static final String update = "Update";
    public static final String delete = "Delete";

    public static final int PICK_IMAGE_REQUEST = 71;

    public static final String BASE_URL = "http://fcm.googleapis.com/";
    public static String PHONE_ID = "userPhone";

    public static RemoteAPIService getCloudMessage(){
        return RetroClient.getClient(BASE_URL).create(RemoteAPIService.class);
    }

    public static String convertStatus(String statusStage){
        if(statusStage.equals("0")){
            return "sent to kitchen";
        } else if(statusStage.equals("1")){
            return "cooking";
        }else {
            return "ready to serve";
        }

    }

    public static String orderDate(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date = new StringBuilder(android.text.format.DateFormat.format("dd-MM-yyyy HH:MM",calendar).toString());
        return date.toString();
    }

    public static boolean checkConnectivity(Context context){
        ConnectivityManager connectivityManager =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){

            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();

            if(infos != null){
                for(int i =0; i < infos.length; i++){
                    if(infos[i].getState() == NetworkInfo.State.CONNECTED);
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
