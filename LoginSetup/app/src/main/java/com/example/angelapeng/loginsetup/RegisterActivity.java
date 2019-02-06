package com.example.angelapeng.loginsetup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    //wrapper
    EditText name, email, password;
    Button Registerbtn, Loginbtn;
    String name_input, email_input, password_input; //inputs

    //firebase authentication
    FirebaseAuth mAuth;
    DatabaseReference mdatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.editName);
        email =  findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        Registerbtn =  findViewById(R.id.buttonRegister);
        Loginbtn =  findViewById(R.id.buttonLogin);

        mAuth = FirebaseAuth.getInstance();
        mdatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        //login button onclick
        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);

            }
        });


        //register button onclick
        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name_input = name.getText().toString().trim();
                email_input = email.getText().toString().trim();
                password_input = password.getText().toString().trim();

                if (name_input.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (email_input.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password_input.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password_input.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password must be greater then 6 values", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email_input, password_input).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendEmailVerification();
                            OnAuth(task.getResult().getUser());
                            mAuth.signOut();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error! Please check your email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            //sending email verification
            private void sendEmailVerification() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //if user is registered send verification
                if (user != null) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //if successful return message
                                Toast.makeText(RegisterActivity.this, "Check your email for verification", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                        }
                    });
                }
            }

            //// send data to server /////
            //// checks if user exists ////

            private void OnAuth(FirebaseUser user) {
                createAnewUser(user.getUid());
            }

            private void createAnewUser(String uid) {
                User user = BuildNewuser();
                mdatabaseRef.child(uid).setValue(user);
            }


            private User BuildNewuser() {
                return new User(
                        getDisplayName(),
                        getUserEmail(),
                        new Date().getTime()
                );
            }

            public String getDisplayName() {
                return name_input;
            }

            public String getUserEmail() {
                return email_input;
            }
        });
    }
}

