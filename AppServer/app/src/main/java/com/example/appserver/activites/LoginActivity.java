package com.example.appserver.activites;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appserver.R;
import com.example.appserver.control.Control;
import com.example.appserver.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText phone_input, password_input;
    Button loginbtn;

    FirebaseDatabase mDatabaseRef;
    DatabaseReference staff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginbtn = findViewById(R.id.buttonLogin);

        mDatabaseRef = FirebaseDatabase.getInstance();
        staff = mDatabaseRef.getReference("Users");

        phone_input =  findViewById(R.id.editPhone);
        password_input =  findViewById(R.id.editPassword);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn(phone_input.getText().toString(), password_input.getText().toString());
            }
        });
    }

    private void SignIn(final String phone, final String password) {
        final String thisphone = phone;
        final String thispassword = password;
        staff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(thisphone).exists()) {

                    User user = dataSnapshot.child(phone).getValue(User.class);
                    user.setPhone(phone);
                    if (Boolean.parseBoolean(user.getIsStaff())) {
                        if (user.getPassword().equals(thispassword)) {

                            Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        Control.currentUser = user;
                        startActivity(intent);
                        finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Login with staff number", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Staff does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
