package com.example.appserver;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    //wrapper
    EditText name, phone, password;
    Button Registerbtn;
    TextView Loginbtn;
    String name_input, phone_input, password_input; //inputs

    //firebase authentication
    FirebaseAuth mAuth;
    DatabaseReference mdatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.editName);
        phone = findViewById(R.id.editPhone)
        password = findViewById(R.id.editPassword);
        Registerbtn =  findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();
        mdatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        //login button onclick
        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);

            }
        });


        //register button onclick
        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name_input = name.getText().toString().trim();
                phone_input = phone.getText().toString().trim();
                password_input = password.getText().toString().trim();

                if (name_input.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                } else if (phone_input.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password_input.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (password_input.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Password must be greater then 6 values", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }
}
