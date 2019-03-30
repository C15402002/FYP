package com.example.myapplication.config;

import com.example.myapplication.model.Response;
import com.example.myapplication.model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface RemoteAPIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAZd_f7b0:APA91bGriF-XKzzLN04lyznsuAmPTyvpZPlS80K3h4CalfLiuMgoV9ddEwwkW-rBlJKGEScBHlvp899KmvExKQikhWogb4RurcmY05gN481cxj1HN_7nOZGPbUGxzHXzBqC_U8eTLvpI"
            }
    )
    @POST("fcm/send")
    Call<Response> sendNotice(@Body Sender body);


}


