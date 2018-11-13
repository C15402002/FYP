package com.example.angelapeng.login;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by angelapeng on 13/11/2018.
 */

public class BackgroundWorker extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog message;

    BackgroundWorker (Context c){
        context = c;
    }


    @Override
    protected String doInBackground(String... params){

        String type = params[0];
        String login_url = "http://10.0.2.2/userinfo/login.php";
        if(type.equals("login")){
            try{
                String email = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                OutputStream opStream = urlConnection.getOutputStream();
                BufferedWriter bfWriter = new BufferedWriter(new OutputStreamWriter(opStream, "UTF-8"));
                String PostData = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bfWriter.write(PostData);
                bfWriter.flush();
                bfWriter.close();
                opStream.close();
                InputStream ipStream = urlConnection.getInputStream();
                BufferedReader bfReader = new BufferedReader(new InputStreamReader(ipStream,"iso-8859-1"));
                String result = "";
                String line ;
                while((line = bfReader.readLine())!= null ){
                    result += line;
                }
                bfReader.close();
                ipStream.close();
                urlConnection.disconnect();
                return result;
            }catch (java.io.IOException e){
                e.printStackTrace();
            }


        }
        return null;

    }

    @Override
    protected void onPreExecute(){
         message = new AlertDialog.Builder(context).create();
        message.setTitle("Login status");
    }

    @Override
    protected void onPostExecute(String result){
       message.setMessage(result);
        message.show();

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
