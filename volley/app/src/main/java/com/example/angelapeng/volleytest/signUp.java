package com.example.angelapeng.volleytest;

import android.app.ProgressDialog;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class signUp extends AppCompatActivity {

    private static String S_URL ="http://147.252.136.168/android/signup.php";
    EditText signUpFName, signUpSName, signUpEmail,signUpPassword;
    TextView login;
    Button signupButton;
    Vibrator v;

//    private Snackbar snackbar;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        pd = new ProgressDialog(signUp.this);
        signupButton =findViewById(R.id.signupButton);
        signUpFName =findViewById(R.id.signUpFName);
        signUpSName = findViewById(R.id.signUpSName);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);

        signupButton = findViewById(R.id.signupButton);
        login = findViewById(R.id.login);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUserData();
                }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void validateUserData() {
        //find values
        final String fname = signUpFName.getText().toString();
        final String sname = signUpSName.getText().toString();
        final String email = signUpEmail.getText().toString();
        final String password = signUpPassword.getText().toString();

//       checking if signUpFName is empty
        if (TextUtils.isEmpty(fname)) {
            signUpFName.setError("Please enter first name");
            signUpFName.requestFocus();
            // Vibrate for 100 milliseconds
            v.vibrate(100);
            return;
        }
        if (TextUtils.isEmpty(sname)) {
            signUpSName.setError("Please enter last name");
            signUpSName.requestFocus();
            // Vibrate for 100 milliseconds
            v.vibrate(100);
            return;
        }
        //checking if signUpEmail is empty
        if (TextUtils.isEmpty(email)) {
            signUpEmail.setError("Please enter email");
            signUpEmail.requestFocus();
            // Vibrate for 100 milliseconds
            v.vibrate(100);
            return;
        }
        //checking if signUpPassword is empty
        if (TextUtils.isEmpty(password)) {
            signUpPassword.setError("Please enter password");
            signUpPassword.requestFocus();
            //Vibrate for 100 milliseconds
            v.vibrate(100);
            return;
        }
        //validating signUpEmail
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signUpEmail.setError("Enter a valid email");
            signUpEmail.requestFocus();
            //Vibrate for 100 milliseconds
            v.vibrate(100);
            return;
        }
        //After Validating we register User
        registerUser();
    }

    private void registerUser() {
        pd.setMessage("Signing Up . . .");
        pd.show();
        final String fname = signUpFName.getText().toString();
        final String sname = signUpSName.getText().toString();
        final String email = signUpEmail.getText().toString();
        final String password = signUpPassword.getText().toString();


        //Call our volley library
        StringRequest stringRequest = new StringRequest(Request.Method.POST, S_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                    try {
                        JSONObject obj = new JSONObject(response);
                        String success = obj.getString("success");

                        if (success.equals("1")) {
                            pd.hide();
                            Toast.makeText(signUp.this, "Register success!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        pd.hide();
                        Toast.makeText(signUp.this, "Register error!" + e.toString(), Toast.LENGTH_SHORT).show();
                    } }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error){
                        Toast.makeText(signUp.this, "Register Error" + error.toString(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                })
        {
            @Override
            protected Map < String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("signUpFName", fname);
                        params.put("signUpSName", sname);
                        params.put("signUpEmail", email);
                        params.put("signUpPassword", password);

                        return params;
                    }
                };
        RequestQueue RQ = Volley.newRequestQueue(this);
        RQ.add(stringRequest);
    }
}