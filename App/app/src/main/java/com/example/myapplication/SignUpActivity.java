package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    //wrapper
    EditText name, phone, email, password;
    Button Registerbtn;
    TextView Loginbtn;

    //firebase authentication
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.editName);
        phone = findViewById(R.id.editPhone);
        email =  findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        Registerbtn =  findViewById(R.id.buttonRegister);
        Loginbtn =  findViewById(R.id.login);

        mDatabaseRef = FirebaseDatabase.getInstance();
        final DatabaseReference userTable = mDatabaseRef.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

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
                userTable.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(phone.getText().toString()).exists()) {
                            Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();

                        } else {
                            User user = new User(name.getText().toString(), email.getText().toString(), password.getText().toString());
                            userTable.child(phone.getText().toString()).setValue(user);
                            Toast.makeText(SignUpActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            finish();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

        });
    }
}