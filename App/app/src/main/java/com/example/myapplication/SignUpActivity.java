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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
                if(Control.checkConnectivity(getBaseContext())) {
                    userTable.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child(phone.getText().toString()).exists()) {
                                Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();

                            } else {
                                User user = new User(name.getText().toString(), email.getText().toString(), password.getText().toString());
                                userTable.child(phone.getText().toString()).setValue(user);
                                sendEmailVerification();
                                Toast.makeText(SignUpActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                finish();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    Toast.makeText(SignUpActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
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
                        Toast.makeText(SignUpActivity.this, "Check your email for verification", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                }
            });
        }
    }
}
