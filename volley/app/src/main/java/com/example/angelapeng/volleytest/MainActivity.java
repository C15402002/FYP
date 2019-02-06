package com.example.angelapeng.volleytest;


import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText loginEmail,loginPassword;
    Button loginButton, signUp;
    Vibrator v;

    //change this to match your url
    final String loginURL = "http://localhost/android/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        signUp = findViewById(R.id.signup);
        loginButton = findViewById(R.id.loginButton);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUserData();
            }
        });

        //when someone clicks on login
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), signUp.class);
                startActivity(intent);
            }
        });

    }
    private void validateUserData() {

        //first getting the values
        final String email = loginEmail.getText().toString();
        final String password = loginPassword.getText().toString();

        //checking if email is empty
        if (TextUtils.isEmpty(email)) {
            loginEmail.setError("Please enter your email");
            loginEmail.requestFocus();
            // Vibrate for 100 milliseconds
            v.vibrate(100);
            loginButton.setEnabled(true);
            return;
        }
        //checking if password is empty
        if (TextUtils.isEmpty(password)) {
            loginPassword.setError("Please enter your password");
            loginPassword.requestFocus();
            //Vibrate for 100 milliseconds
            v.vibrate(100);
            loginButton.setEnabled(true);
            return;
        }
        //validating email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Enter a valid email");
            loginEmail.requestFocus();
            //Vibrate for 100 milliseconds
            v.vibrate(100);
            loginButton.setEnabled(true);
            return;
        }


        //Login User if everything is fine
        loginUser();


    }

    private void loginUser() {

        //first getting the values
        final String email = loginEmail.getText().toString();
        final String password = loginPassword.getText().toString();



        //Call our volley library
        StringRequest stringRequest = new StringRequest(Request.Method.POST,loginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
//                            Toast.makeText(getApplicationContext(),response.toString(), Toast.LENGTH_SHORT).show();

                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {

                                //getting user name
                                String Username = obj.getString("username");
                                Toast.makeText(getApplicationContext(),Username, Toast.LENGTH_SHORT).show();

                                //storing the user in shared preferences
                                //SharedPref.getInstance(getApplicationContext()).storeUserName(Username);
                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), signUp.class));

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Connection Error"+error, Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
    }



}