package com.example.angelapeng.loginsetup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout emailwrapper, passwrapper;
    EditText user_email, user_password;
    Button loginbtn;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailwrapper = findViewById(R.id.email_wrapper);
        passwrapper = findViewById(R.id.password_wrapper);

        user_email = findViewById(R.id.userEmail);
        user_password = findViewById(R.id.userPassword);

        loginbtn = findViewById(R.id.btnUserLogin);

        auth = FirebaseAuth.getInstance();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_email.getText().toString().trim();
                String password = user_password.getText().toString().trim();

                if (email.isEmpty()) {

                    emailwrapper.setError("email is error");
                    emailwrapper.requestFocus();
                    return;

                }
                if (password.isEmpty()) {
                    passwrapper.setError("password error");
                    passwrapper.requestFocus();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //if login successful go to another activity
                            Intent intent = new Intent(LoginActivity.this, ScannerActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }else{
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });
    }
}
