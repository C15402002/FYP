package com.example.appserver.control;


import com.example.appserver.config.RemoteAPIService;
import com.example.appserver.config.RetroClient;
import com.example.appserver.model.User;

public class Control {
    public static User currentUser;

    public static final String update = "Update";
    public static final String delete = "Delete";

    public static final int PICK_IMAGE_REQUEST = 71;

    public static final String BASE_URL = "http://fcm.googleapis.com/";

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

}
