package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.control.Control;
import com.example.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText phone_input, password_input;
    Button loginbtn;
    TextView registerbtn;

    FirebaseAuth mAuth;
    FirebaseDatabase mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbtn = findViewById(R.id.buttonLogin);
        registerbtn = findViewById(R.id.signUp);

        mDatabaseRef = FirebaseDatabase.getInstance();
        final DatabaseReference userTable = mDatabaseRef.getReference("Users");
        mAuth = FirebaseAuth.getInstance();
        phone_input =  findViewById(R.id.editPhone);
        password_input =  findViewById(R.id.editPassword);


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(Control.checkConnectivity(getBaseContext())) {
                    userTable.addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child(phone_input.getText().toString()).exists()) {

                                User user = dataSnapshot.child(phone_input.getText().toString()).getValue(User.class);
                                user.setPhone(phone_input.getText().toString());
                                if (!Boolean.parseBoolean(user.getIsStaff())) {
                                    if (user.getPassword().equals(password_input.getText().toString())) {

                                        Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MultiRestActivity.class);
                                        Control.currentUser = user;
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        Toast.makeText(LoginActivity.this, "unsuccessful", Toast.LENGTH_SHORT).show();

                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Cannot Login ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "user does not exist", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

}
