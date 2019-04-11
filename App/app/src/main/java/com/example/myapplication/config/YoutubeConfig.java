package com.example.myapplication.config;

public class YoutubeConfig {
    public YoutubeConfig() {

    }
    private static final String YOUTUBE_API_KEY = "AIzaSyABOvCR5YgMRJ_W23GR8RRPAHbcN_rjVZw";

    public static String getYoutubeApiKey(){
        return YOUTUBE_API_KEY;
    }
}
