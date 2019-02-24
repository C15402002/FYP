package com.example.appserver;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginbtn;

    FirebaseAuth mAuth;
    String email_input, password_input;
    public static final String TAG="LOGIN";
    public static final String userEmail="";


    FirebaseDatabase mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbtn = findViewById(R.id.buttonLogin);

        mDatabaseRef = FirebaseDatabase.getInstance();
        final DatabaseReference userTable = mDatabaseRef.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        password =  findViewById(R.id.editPassword);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_input = email.getText().toString().trim();
                password_input = password.getText().toString().trim();

                if (email_input.isEmpty()) {

                    Toast.makeText(LoginActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (password_input.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email_input, password_input).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //if login successful go to another activity
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra(userEmail,email_input);
                            startActivity(intent);

                        }else{
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Log.d(TAG,"AuthStateChanged:Logout");
                        }
                    }
                });
            }
        });
    }

}
