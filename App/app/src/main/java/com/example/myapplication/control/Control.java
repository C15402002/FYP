package com.example.myapplication.control;


import com.example.myapplication.config.RemoteAPIService;
import com.example.myapplication.config.RetroClient;
import com.example.myapplication.model.Menu;
import com.example.myapplication.model.User;

import retrofit2.Retrofit;

public class Control {
    public static User currentUser;

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
}
