package com.example.myapplication.control;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import com.example.myapplication.config.RemoteAPIService;
import com.example.myapplication.config.RetroClient;
import com.example.myapplication.model.Menu;
import com.example.myapplication.model.User;

import retrofit2.Retrofit;

public class Control {
    public static User currentUser;
    public static final String Review_DishesID = "MenuId";

    public static  final String Restaurant_Scanned = "RestaurantId";
    public static String restID = "";

    public static String PHONE_ID = "\"userPhone\"";
    public static String convertStatus(String statusStage){
        if(statusStage.equals("0")){
            return "sent to kitchen";
        } else if(statusStage.equals("1")){
            return "cooking";
        }else {
            return "ready to serve";
        }

    }

    private static final String BASE_URL = "http://fcm.googleapis.com/";


    public static RemoteAPIService getCloudMessage(){
        return RetroClient.getClient(BASE_URL).create(RemoteAPIService.class);
    }



   // public static final String edit = "Edit";
    public static final String delete = "Delete";

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
