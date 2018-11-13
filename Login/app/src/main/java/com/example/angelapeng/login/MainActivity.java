package com.example.angelapeng.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText emailet, passwordet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailet = (EditText)findViewById(R.id.email);
        passwordet = (EditText)findViewById(R.id.password);
    }

    public void onLogin(View v){
        String email = emailet.getText().toString();
        String password = passwordet.getText().toString();
        String type = "login";

        BackgroundWorker worker = new BackgroundWorker(this);
        worker.execute(type, email, password);

    }
}
